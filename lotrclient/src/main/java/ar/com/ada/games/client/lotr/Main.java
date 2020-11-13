package ar.com.ada.games.client.lotr;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


/**
 * 
 * @author Titas Skrebe
 *
 * This is the main class of a client side for an online multiplayer game.
 * 
 * Go to www.tskrebe.me for more info 
 * 
 */
public class Main {

	
	
	public static void main(String[] args) {
		
		/*if (args.length != 3){
			throw new IllegalArgumentException("Bad input. You need [REMOTE IP] [REMOTE TCP PORT] [LOCAL UDP PORT or -1 for random port]");
		}*/
		String remoteIp = "localhost";
		int remoteTCPPort = 42581;
		int localUdpPort = -1; //Random
		
		
		GameClient main = new GameClient(remoteIp, remoteTCPPort, localUdpPort);

		main.letsGO();
		
		
	}
	
}
