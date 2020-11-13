package ar.com.ada.games.server.lotr;



import ar.com.ada.games.server.lotr.game.GameServer;


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
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXBException;


public class Main {

	public static void main(String[] args) {
		

		GameServer server = new GameServer(42581);//Integer.parseInt(args[0]));
		server.start();
	}
	
}