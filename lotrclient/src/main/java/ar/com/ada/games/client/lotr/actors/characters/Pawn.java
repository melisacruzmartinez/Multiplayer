package ar.com.ada.games.client.lotr.actors.characters;

import ar.com.ada.games.client.lotr.actors.Actor;
import ar.com.ada.games.client.lotr.actors.weapons.Weapon;
import ar.com.ada.games.client.lotr.hitbox.HitBox;

public class Pawn extends Actor {

    public String name;
    public int characterId;
    public int health;
    public Weapon actualWeapon;
    public String state;//WeaponFired, Dead, Alive, Hit
    public String damageTakenLegend;

    
}
