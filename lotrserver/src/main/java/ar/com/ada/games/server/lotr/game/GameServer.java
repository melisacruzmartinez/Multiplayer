package ar.com.ada.games.server.lotr.game;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.ada.games.server.lotr.network.replication.*;
import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.controllers.PlayerController;
import ar.com.ada.games.server.lotr.game.GameLoop;
import ar.com.ada.games.server.lotr.game.GameState;
import ar.com.ada.games.server.lotr.network.connections.Client;
import ar.com.ada.games.server.lotr.network.connections.TCPConn;
import ar.com.ada.games.server.lotr.network.replication.PlayerReplicationInfo;

public class GameServer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TILES_FILE = "\\gameInfo.txt";

    // refreshing game state and sending data to clients every x ms
    private static final long RESHRESH_GAP = 30;

    private static int SERVER_PORT_TCP;

    private static long IDs = 0L;

    public static final int MAP_WIDTH = 1500;
	public static final int MAP_HEIGTH = 900;

    //thread safe array because while one thread is reading another
    //might add delete some entries
    //private CopyOnWriteArrayList<IpPort> activeClients;
    public CopyOnWriteArrayList<PlayerController> players;

    private static final long MIN_PLAYERS = 2; //2

    public GameState gameState;
    private GameLoop gameLoop;

    private UdpConnectionsSend udpSend;

    public GameServer(int tcpPort) {

        SERVER_PORT_TCP = tcpPort;
        gameState = new GameState();
        udpSend = new UdpConnectionsSend();
        //thread-safe
        players = new CopyOnWriteArrayList<PlayerController>();

        gameLoop = new GameLoop(this);

    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_TCP)) {

            logger.info("Waiting For Players");
            Socket clientSocket;
            while ((clientSocket = serverSocket.accept()) != null) {

                //Nuevo player controller
                Client client = new Client();
                client.clientId = (int)this.getId();
                PlayerController pc = new PlayerController(this,client);
                TCPConn tcpConn = new TCPConn(this, client, clientSocket);

                this.players.add(pc);

                new Thread(tcpConn).start();

                logger.info("New player");

                if (this.players.stream().filter(p -> p.playerId != -1).count() >= MIN_PLAYERS) {
                    //Dont accept more players

                    break;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (PlayerController playerController : players) {

            //Esperamos a que ya tenga el pawn para empezar
            //y ademas tenga seteado los puertos UDPs
            while (playerController.pawn == null || playerController.client.udpConn.address == null ) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }; //Que loopee mientras no se haya recibido el pawn elegido

            logger.info("Player "+ playerController.playerId);
            logger.info("Player name "+ playerController.playerName);
            logger.info("Player pawn "+ playerController.pawn.name);
        }

        logger.info("Begining game");
        this.gameLoop.run();

    }

    /*private void gameStateRefresher(){
       
       Timer timer = new Timer();
       timer.scheduleAtFixedRate(new TimerTask() {
           
           @Override
           public void run() {
               updateGamePlay();
               udpSend.sendGamePlay();
           }
    
           private void updateGamePlay() {
               gameState.clear();
               for (MainCharacter mc : fullCharacters){
                   gameState.addAll(mc.update(tiles.realList, fullCharacters));
               }
           }
           
       },0, RESHRESH_GAP);
    }*/

    public void updateGamePlay() {

        gameState.update(players);

    }

    public synchronized long getId() {
        return IDs++;
    }

    public void updatePlayer(PlayerReplicationInfo data) {

        long specId = data.playerId;
        PlayerController pc = this.players.stream().filter(p -> p.playerId == data.playerId).findFirst().get();

        //logger.info(data.toString());
        pc.updateState(data);
        //logger.info(pc.toString());

    }

    public void removePlayer(long id) {

        this.players.remove(this.players.stream().filter(p -> p.playerId == id).findFirst().get());

    }

    protected void sendGamePlay() {
        this.udpSend.sendGamePlay();
        ;
    }

    public class UdpConnectionsSend {

        DatagramSocket gamePlaySocket;

        public UdpConnectionsSend() {

            try {
                gamePlaySocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        public void sendGamePlay() {

            try {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(Replicator.marshall(gameState));
                byte[] bytes = baos.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                for (PlayerController dest : players) {

                    if (dest.client.udpConn.address == null)
                        logger.warn("UDP nulo para cliente " + dest.playerId);
                    packet.setAddress(dest.client.udpConn.address);
                    packet.setPort(dest.client.udpConn.port);
                    gamePlaySocket.send(packet);
                    packet.setData(bytes);
                    packet.setLength(bytes.length);
                }

            } catch (IOException | JAXBException e) {

            }
        }
    }

}
