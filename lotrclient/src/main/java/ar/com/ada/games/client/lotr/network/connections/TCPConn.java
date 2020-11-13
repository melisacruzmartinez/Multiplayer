package ar.com.ada.games.client.lotr.network.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

import javax.xml.bind.JAXBException;

import ar.com.ada.games.client.lotr.GameClient;
import ar.com.ada.games.client.lotr.network.replication.PlayerReplicationInfo;
import ar.com.ada.games.client.lotr.network.replication.Replicator;
import ar.com.ada.games.client.lotr.network.replication.ServerMessage;


public class TCPConn {

	private static final int GET_ID = 0;
	private static final int GET_MAP = 1;
	private static final int SEND_MAIN_CHARACTER = 2;
	private static final int GET_ID_IP_PORT = 3;
	private static final int REMOVE_CHARACTER = 4;
	private static final int GET_RANDOM_CHARACTER = 5;
	
	private final int SERVER_PORT_TCP;
	
	private final String SERVER_IP;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Socket socket;

	public TCPConn(GameClient main, String ip, int port) {
		
		SERVER_PORT_TCP = port;
		SERVER_IP = ip;
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT_TCP);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Gets unique ID for player **/
	public long getIdFromServer() {
		if (socket == null)
			return -1; //Offline mode
		
		try {
			ServerMessage sm = new ServerMessage(GET_ID);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
			
			return ois.readLong();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	/** Downloads player **/
	public PlayerReplicationInfo getRandomCharacter() {
		
		try {
			ServerMessage sm = new ServerMessage(GET_RANDOM_CHARACTER);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
			
			String response = (String) ois.readObject();
			return Replicator.unmarshallPRI(response);
			
		} catch (IOException | ClassNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Downloads map from server **/
	/*List<Box> getMapDetails() {
		
		try {
			ServerMessage sm = new ServerMessage(GET_MAP);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
			
			String response = (String) ois.readObject();
			return Replicator.unmarshall(response);
			
		} catch (IOException | ClassNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/** Sends data about the main character to server. Velocity, etc. */
	public void sendUpdatedVersion(PlayerReplicationInfo character) {
		try {
			//System.err.println("Sending--> " + character.toString());
			ServerMessage sm = new ServerMessage(SEND_MAIN_CHARACTER);
			sm.setCharacterData(character);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
			oos.reset();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Sends IP and port of Udp connection **/
	public void sendIpIdPort(int port) {
		
		try {
			ServerMessage sm = new ServerMessage(GET_ID_IP_PORT);
			sm.setPort(port);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Sends id of player to the server to inform that a player has left the game **/
	public void removeCharacter(long id) {
		
		try {
			ServerMessage sm = new ServerMessage(REMOVE_CHARACTER);
			sm.setId(id);
			String data = Replicator.marshall(sm);
			oos.writeObject(data);
			//oos.reset();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}

}
