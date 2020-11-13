package ar.com.ada.games.server.lotr.network.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.controllers.PlayerController;
import ar.com.ada.games.server.lotr.game.GameCatalog;
import ar.com.ada.games.server.lotr.game.GameServer;
import ar.com.ada.games.server.lotr.network.replication.Replicator;
import ar.com.ada.games.server.lotr.network.replication.ServerMessage;
import ar.com.ada.games.server.lotr.network.replication.*;
/**
 * This class establishes TCP connection and listens to client side
 * for tasks to do.
 */
public class TCPConn implements Runnable{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int GET_ID = 0;
	private static final int GET_MAP = 1;
	private static final int SEND_MAIN_CHARACTER = 2;
	private static final int GET_ID_IP_PORT = 3;
	private static final int REMOVE_CHARACTER = 4;
	private static final int GET_RANDOM_CHARACTER = 5;

	private GameServer main;
	private Client client;
	private Socket socket;
	
	public TCPConn(GameServer main, Client client, Socket socket) {
		this.client = client;
		this.client.tcpConn = this;
		this.main = main;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
			
			while(true){
				String msg = (String)ois.readObject();
				ServerMessage sm;
				try {
					sm = Replicator.unmarshall(msg);
				} catch (JAXBException e) {
					e.printStackTrace();
					continue;
				}
				switch(sm.messageType){
					case GET_ID:
						oos.writeLong(this.client.clientId);
						break;
					
					case SEND_MAIN_CHARACTER:
						main.updatePlayer(sm.characterData);
						break;
					case GET_ID_IP_PORT: 
						String ipString = socket.getInetAddress().getHostName();
						InetAddress clientIp = InetAddress.getByName(ipString);
						System.err.println(ipString + " " + clientIp);
						//Seteo el udp port del cliente
						this.client.udpConn.address = clientIp;
						this.client.udpConn.port = sm.port;
						break;
					case REMOVE_CHARACTER:
						main.removePlayer(sm.id);
						break;
					case GET_RANDOM_CHARACTER:
						try {
							Pawn randomPawn = GameCatalog.GetRandomCharater();
							this.client.playerController.pawn = randomPawn;
							PlayerReplicationInfo pri = PlayerReplicationInfo.From(randomPawn);
							String data = Replicator.marshall(pri);
							oos.writeObject(data);
						} catch (JAXBException e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
				}
				oos.flush();
				
			}
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
			System.out.println("Player leaves");
		}
	}

}

