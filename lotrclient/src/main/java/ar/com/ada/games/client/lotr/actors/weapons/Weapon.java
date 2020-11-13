package ar.com.ada.games.client.lotr.actors.weapons;

import java.util.*;

import ar.com.ada.games.client.lotr.actors.Actor;
import ar.com.ada.games.client.lotr.actors.projectile.Projectile;

public class Weapon extends Actor {
    
    public int id;
    public String name;

    public List<Projectile> projectiles = new ArrayList<>();

    public void registerProjectile(Projectile p) {
        projectiles.add(p);
    }

}