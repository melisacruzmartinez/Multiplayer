package ar.com.ada.games.server.lotr.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.actors.projectiles.Projectile;
import ar.com.ada.games.server.lotr.game.GameServer;
import ar.com.ada.games.server.lotr.hitbox.HitBox;
import ar.com.ada.games.server.lotr.network.connections.Client;
import ar.com.ada.games.server.lotr.network.replication.PlayerReplicationInfo;

public class PlayerController {

    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public int playerId = -1;
    public String playerName;
    public Pawn pawn;
    public GameServer server;
    public Client client;

    //Thread safe list because bullets can be updated while 
	//iterating them which would resolve in an error.
	private List<FiredProjectile> firedProjectiles;

    public PlayerController(GameServer server, Client client) {
        this.server = server;
        this.client = client;
        this.playerId = client.clientId;
        this.client.playerController = this;
        this.firedProjectiles = Collections.synchronizedList(new ArrayList<FiredProjectile>());
        
    }
    
    
    public PlayerController() {
        
        
    }

    
    
    public void updateState(PlayerReplicationInfo pri) {
        pri.fillTo(this);
        addProjectiles(pri.weapon.projectiles);
    }

    private void addProjectiles(List<Projectile> newBullets){
		if (newBullets == null)	return;
		synchronized (firedProjectiles) {
			firedProjectiles.clear();
			for (Projectile sb : newBullets) {
				float r = this.pawn.hitBox.r;
				float g = this.pawn.hitBox.g;
				float b = this.pawn.hitBox.b;
				firedProjectiles.add(new FiredProjectile(sb.x, sb.y, sb.k, sb.c, sb.pn, r, g, b));
			}
		}
	}

    public PlayerReplicationInfo gReplicationInfo() {

        return PlayerReplicationInfo.From(this);
    }

    public void update(List<PlayerController> fullCharacters) {
		
		
		
		//updating bullets
		synchronized (firedProjectiles) {
            synchronized (this.pawn.actualWeapon.projectiles) {
                Iterator<FiredProjectile> itr = firedProjectiles.listIterator();
                this.pawn.actualWeapon.projectiles.clear(); //Limpio porque reseteo los projectiles.(hoy q hay que sincronizar)
                while (itr.hasNext()) {

                    FiredProjectile projectile = itr.next();

                    //Si el proyectil colisiona en algun momento
                    if (projectile.update(fullCharacters, this.playerId)) {
                        itr.remove();
                    } else {
                        Projectile clientProjectile = new Projectile(projectile.x, projectile.y, projectile.k, projectile.c, projectile.direc);
                        clientProjectile.hitBox = new HitBox(projectile.x, projectile.y, projectile.width,
                                projectile.height, projectile.r, projectile.g, projectile.b, -1L, -1);

                        this.pawn.actualWeapon.projectiles.add(clientProjectile);
                    }
                }
            }
            


        }
        if (this.pawn.state.equals("firing"))
            this.pawn.state = "fired";
		//updating character
        float x = this.pawn.hitBox.x;
        x += this.pawn.xVel;
		if (x < 0 || x + this.pawn.hitBox.w > GameServer.MAP_WIDTH) {
			x -= this.pawn.xVel;
        }
        
        float y = this.pawn.hitBox.y;

		y += this.pawn.yVel;
		if (y < 0 || y + this.pawn.hitBox.h > GameServer.MAP_HEIGTH) {
			y -= this.pawn.yVel;
		}
		
		
		//if xp is below 1 we reset player to its initial position
		if (this.pawn.health < 1){
			x = y = 0;
			this.pawn.health = 100; //Revive y vuelve al principio
        }
        this.pawn.hitBox.x = x;
        this.pawn.hitBox.y = y;
		
		/*boxes.add(new Box(x, y, width, height, r, g, b, id, xp));
		return boxes;*/
	}

	@Override
    public String toString() {
		String tmp = "PC# " + this.playerId;
		tmp += " Name "+this.playerName + " pawn: "+ this.pawn.name;
		tmp +=  " State " + this.pawn.state + " health " + this.pawn.health;
        if (this.pawn.actualWeapon != null)
			tmp += " projectiles: " + this.pawn.actualWeapon.projectiles.size();
		if (this.firedProjectiles != null)
            tmp += " firedProjectiles: " + this.firedProjectiles.size();

        return tmp;
    }

    /**
	 * ServerBullet class represents bullets of main character
	 */
	private class FiredProjectile {

		private float d, 	// distance between old and new bullet position
				direc; 		// y=kx+c going up or down
		private float k, c, x, y; 	// y=kx+c
		private int width, height;
		private float r, g,b;
		
		public FiredProjectile(float x, float y, float k, float c, float direc, float r, float g, float b){
			
			this.x = x;
			this.y = y;
			this.c = c;
			this.k = k;
			this.direc = direc;
			this.r = r;
			this.g = g;
			this.b = b;
			
			d = 8f;
			width = height = 10;
		}
		
		/**
		 * Updates bullets state.
		 * @param obstacles		Simple square obstacles .
		 * @param fullCharacters	All characters.
		 * @param id	Id of this character so we don't check collision with itself.
		 * @return If there was a collision returns true otherwise false.
		 */
		
		private boolean update(List<PlayerController> fullCharacters, long id) {
			
			
			//collision with enemies
			for (PlayerController mc : fullCharacters){
				if (mc.playerId == id)
					continue;
				if (CollisionController.Collision(x, y, x + width, y + height,
						mc.pawn.hitBox.x, mc.pawn.hitBox.y, mc.pawn.hitBox.x + mc.pawn.hitBox.w, mc.pawn.hitBox.y + mc.pawn.hitBox.h)){
					mc.pawn.health -= 30; //Le saca 30 harcoded
					return true;
				}
			}

			//collision with map
			if (x < 0 || x > server.MAP_WIDTH || y < 0 || y > server.MAP_HEIGTH) {
				return true;
			}

			/* 
			 * Super cool formula to find next x that is d (distance) away from
			 * starting point.
			 * Used distance formula and wolfram alpha to express next x position 
			 */
			x = (float) (-c * k + x + k * y - direc
					* Math.sqrt(-c * c + d * d + d * d * k * k - 2 * c * k * x
							- k * k * x * x + 2 * c * y + 2 * k * x * y - y * y))
					/ (1 + k * k);
			y = k * x + c;
			
			return false;
		}

	}
}
