package ar.com.ada.games.server.lotr.actors.weapons;

import java.util.*;

import ar.com.ada.games.server.lotr.actors.projectiles.Projectile;

public class Weapon {

    public int id;
    public String name;

    public List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<Projectile>());

    public void registerProjectile(Projectile p) {
        projectiles.add(p);
    }
}
