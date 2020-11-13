package ar.com.ada.games.server.lotr.actors.characters;

import ar.com.ada.games.server.lotr.actors.Actor;
import ar.com.ada.games.server.lotr.actors.weapons.Weapon;
import ar.com.ada.games.server.lotr.hitbox.HitBox;
import ar.com.ada.games.server.lotr.network.replication.PlayerReplicationInfo;

public class Pawn extends Actor {

    public String name;
    public int characterId;
    public int health;
    public Weapon actualWeapon;
    public String state;//WeaponFired, Dead, Alive, Hit
    public String damageTakenLegend;


}
