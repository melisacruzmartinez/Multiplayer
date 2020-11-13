package ar.com.ada.games.client.lotr;


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.actors.projectile.Projectile;
import ar.com.ada.games.client.lotr.controllers.PlayerController;
import ar.com.ada.games.client.lotr.game.GameMenu;
import ar.com.ada.games.client.lotr.game.GameState;
import ar.com.ada.games.client.lotr.game.render.ADAEngine;
import ar.com.ada.games.client.lotr.game.render.IEngine;
import ar.com.ada.games.client.lotr.hitbox.HitBox;
import ar.com.ada.games.client.lotr.network.connections.TCPConn;
import ar.com.ada.games.client.lotr.network.connections.UDPConn;
import ar.com.ada.games.client.lotr.network.replication.PlayerReplicationInfo;

public class GameClient {

	private static final int DISPLAY_WIDTH = 700;
	private static final int DISPLAY_HEIGTH = 500;

	public static final int MAP_WIDTH = 1500;
	public static final int MAP_HEIGTH = 900;



	static long ID = -1; // we get ID from the server side

	private TCPConn connections; // establishing TCP connection

	public PlayerController playerController; // data about the main character
	
	//private List<Box> obstacles;
	private List<PlayerController> players = new ArrayList<>(); // all players and bullets. We get this from server
	private List<Pawn> actors = new ArrayList<>();
	//private Box updatedCharacter; // clients character that we get from server

	private Camera camera;
	
	private String server_ip;
	private int server_port_tcp;
	private int client_port_udp;
	
	private IEngine engine;
	private boolean offlineMode = false;
	
	public GameClient(String ip, int portTcp, int portUdp){
		server_ip = ip;
		server_port_tcp = portTcp;
		client_port_udp = portUdp;
		engine = new ADAEngine(); //WII
	}
	public void letsGO() {
		
		this.init();
		this.start();
	}
	

	/** Setting up screen, establishing connections (TCP, UPD) with server, etc. */
	private void init() {


		this.playerController = new PlayerController(this);
		
		GameMenu.PlayerSetup(this.playerController);

		this.players.add(this.playerController);
		
		connections = new TCPConn(this, server_ip, server_port_tcp);

		if ((ID = connections.getIdFromServer()) == -1) {
			System.err.println("cant get id for char. Switching to Offline");
			this.offlineMode = true;
			ID = 667; //Fixed number
		}
		this.playerController.playerId = (int)ID;

		this.engine.init(DISPLAY_WIDTH,DISPLAY_HEIGTH);
		
		//Actualizo el personaje
		if (!offlineMode)
			this.connections.sendUpdatedVersion(this.playerController.gReplicationInfo());
			
		
		camera = new Camera(0, 0);

		if (!offlineMode)
			new Thread(new UDPConn(this, connections, client_port_udp)).start();
	}

	/** Game loop */
	private void start() {

		while (!Display.isCloseRequested()) {

			this.engine.clear();

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				closingOperations();
			}
			
			handlingEvents();
			sendCharacter();
			update();
			render();

			engine.updateDisplay();
		}
		closingOperations();
	}

	
	/** Updating camera's position */
	private void update() {

		if (playerController.pawn != null) {
			camera.update(playerController.pawn.hitBox);
		}
	}

	
	/** Rendering obstacles, players and bullets */
	private void render() {

		engine.updateCamera(-camera.xmov, -camera.ymov, 0);	//camera's position

		for (PlayerController player: players) {
			engine.drawPawn(player.pawn);
		}

		for (Pawn pawn: actors) {
			//Que dibuje otros actores
			if (!pawn.isPlayerControlled)
				engine.drawPawn(pawn);
		}

		//projectiles!
		for (PlayerController player: players) {
			if (player.pawn.actualWeapon.projectiles != null) {
				for (Projectile projectile : player.pawn.actualWeapon.projectiles) {
					engine.drawActor(projectile);
				}
			}
		}
	}
	

	/** Function to send main characters data to server */
	private void sendCharacter() {

		if (playerController.pawn.state.equals("firing"))
		{
			int lala = 0;
			lala = 1;
			System.err.println("projectiles: "+playerController.pawn.actualWeapon.projectiles.size());
		}
		if (!offlineMode)
			connections.sendUpdatedVersion(playerController.gReplicationInfo());
		else
			playerController.updateOfflineMode();
	}

	/** Closing game */
	private void closingOperations() {

		connections.removeCharacter(ID);
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Getting info about game play
	 * 
	 * @param objects Object can be either bullet or player
	 */
	public void updateListOfObjects(GameState gameState) {
		if (gameState == null )	return;
		if (!gameState.started)	return;

		actors = new ArrayList<>();
		players = new ArrayList<>();
		for (PlayerReplicationInfo pri : gameState.playersInfo) {

			if (pri.playerId == -1) //Non player 
			{
				Pawn p = new Pawn();
				pri.fillTo(p);

				actors.add(p);
			}
			else {

				PlayerController pc = new PlayerController();
				pri.fillTo(pc);
				
				if (this.playerController.playerId == pri.playerId){
					this.playerController.pawn = pc.pawn;
					if (this.playerController.pawn.state.equals("fired"))
						this.playerController.pawn.state = "idle";
					this.players.add(this.playerController);
				}
				else {
					this.players.add(pc);
				}
			}
			
		}

		for (PlayerController pc : this.players) {
			System.err.println(pc.toString());
		}
        /*movingObjects = objects;
		for (Box box : objects) {
			if (box.id == ID) {
				updatedCharacter = box;
				break;
			}
		}*/
	}
	

	
	private boolean up = false;
	private boolean down = false;
	private boolean right = false;
	private boolean left = false;

	private void handlingEvents() {

		//Handle Input Events. This should be in a GameInput class

		Pawn updatedCharacter = this.playerController.pawn;

		if (Display.isActive()) { // if display is focused events are handled
			
			// new bullets shot
			while (Mouse.next()) {
				System.err.println("Mouse event");
				System.err.println("Pawn " + (this.playerController.pawn != null));
				//System.err.println("Pawn Info " + this.playerController.pawn.toString());
				if (Mouse.getEventButtonState() && updatedCharacter != null) {	

					updatedCharacter.state = "firing"; //state to firing
					float xmouse = Mouse.getX() + camera.x;
					float ymouse = DISPLAY_HEIGTH - Mouse.getY() + camera.y;
					float pnx = 1;
					float xmain = updatedCharacter.hitBox.x + updatedCharacter.hitBox.w / 2;
					float ymain = updatedCharacter.hitBox.y + updatedCharacter.hitBox.h / 2;
					float k = (ymain - ymouse) / (xmain - xmouse);
					float c = ymain - k * xmain;

					if (xmouse > xmain) {
						pnx = -1;
					}
					//Registro el projectil nuevo
					Projectile newProjectile = new Projectile(xmain, ymain, k, c, pnx);
                        newProjectile.hitBox = new HitBox(newProjectile.x, newProjectile.y, 10,
                                10, updatedCharacter.hitBox.r, updatedCharacter.hitBox.g, updatedCharacter.hitBox.b, -1L, -1);
					updatedCharacter.actualWeapon.registerProjectile(newProjectile);
				}
			}
			
			// character's moves
			while (Keyboard.next()) {
				System.err.println("Keyboard event "+ Keyboard.getEventKey());
				if (Keyboard.getEventKey() == Keyboard.KEY_W
						|| Keyboard.getEventKey() == Keyboard.KEY_UP) {
					if (Keyboard.getEventKeyState()) {
						playerController.pawn.yVel = -5;
						up = true;
					} else {
						up = false;
						if (!down) {
							playerController.pawn.yVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S
						|| Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					if (Keyboard.getEventKeyState()) {
						playerController.pawn.yVel = 5;
						down = true;
					} else {
						down = false;
						if (!up) {
							playerController.pawn.yVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D
						|| Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					if (Keyboard.getEventKeyState()) {
						playerController.pawn.xVel = 5;
						right = true;
					} else {
						right = false;
						if (!left) {
							playerController.pawn.xVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A
						|| Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					if (Keyboard.getEventKeyState()) {
						playerController.pawn.xVel = -5;
						left = true;
					} else {
						left = false;
						if (!right) {
							playerController.pawn.xVel = 0;
						}
					}
				}
				playerController.pawn.state = "moving";
			}
		} else {
			playerController.pawn.xVel = 0;
			playerController.pawn.yVel = 0;
			playerController.pawn.state = "idle";
		}
	}

	/**
	 * Camera shows map regarding main character's position
	 */
	private class Camera {

		private float x;
		private float y;

		private float xmov;
		private float ymov;

		Camera(float x, float y) {

			this.x = x;
			this.y = y;
			xmov = 0;
			ymov = 0;
		}

		private void update(HitBox character) {

			float xnew = character.x, ynew = character.y;
			float xCam = Math.min(Math.max(0, (xnew + character.w / 2) - DISPLAY_WIDTH / 2),
					MAP_WIDTH - DISPLAY_WIDTH);
			float yCam = Math.min(Math.max(0, (ynew + character.h / 2) - DISPLAY_HEIGTH / 2),
					MAP_HEIGTH - DISPLAY_HEIGTH);

			xmov = xCam - x;
			x = xCam;

			ymov = yCam - y;
			y = yCam;
		}
	}
}
