package ar.com.ada.games.server.lotr.network.connections;

import java.net.InetAddress;

import ar.com.ada.games.server.lotr.controllers.PlayerController;

public class Client {


    public int clientId;
    public PlayerController playerController;
    public UDPConn udpConn = new UDPConn();

    public TCPConn tcpConn;
}
