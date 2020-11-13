package ar.com.ada.games.client.lotr.controllers;

import ar.com.ada.games.client.lotr.GameClient;
import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.network.replication.PlayerReplicationInfo;

public class PlayerController {
    public int playerId;
    public String playerName;
    public Pawn pawn;
    public GameClient client;

    public PlayerController() {
        
        
    }

    public PlayerController(GameClient client) {
        this.client = client;
        
        this.client.playerController = this;
        
    }
    
    public void updateState(PlayerReplicationInfo pri) {
        pri.fillTo(this);
    }

    public PlayerReplicationInfo gReplicationInfo() {

        return PlayerReplicationInfo.From(this);
    }
    @Override
    public String toString() {
		String tmp = "PC# " + this.playerId;
		tmp += " Name "+this.playerName + " pawn: "+ this.pawn.name;
		tmp +=  " State " + this.pawn.state + " health " + this.pawn.health;
        if (this.pawn.actualWeapon != null)
			tmp += " projectiles: " + this.pawn.actualWeapon.projectiles.size();
		

        return tmp;
    }
    
    /**
     * OFFLINE METHOD ONLY MOVEMENT!
     */
    public void updateOfflineMode() {
		
		
		/* MAKE OFFLINE CALCS for local projectiles
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
            


        }*/
        if (this.pawn.state.equals("firing"))
            this.pawn.state = "fired";
		//updating character
        float x = this.pawn.hitBox.x;
        x += this.pawn.xVel;
		if (x < 0 || x + this.pawn.hitBox.w > GameClient.MAP_WIDTH) {
			x -= this.pawn.xVel;
        }
        
        float y = this.pawn.hitBox.y;

		y += this.pawn.yVel;
		if (y < 0 || y + this.pawn.hitBox.h > GameClient.MAP_HEIGTH) {
			y -= this.pawn.yVel;
		}
		
		
		//if xp is below 1 we reset player to its initial position
		if (this.pawn.health < 1){
			x = y = 0;
			this.pawn.health = 100; //Revive y vuelve al principio
        }
        this.pawn.hitBox.x = x;
        this.pawn.hitBox.y = y;

	}
}
