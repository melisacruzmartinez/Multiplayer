package ar.com.ada.games.server.lotr.game;

import java.util.Random;

import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.actors.weapons.Weapon;
import ar.com.ada.games.server.lotr.hitbox.HitBox;

public class GameCatalog {
    
    public static Pawn GetRandomCharater() {
        Pawn p = new Pawn();

        Random randomFloat = new Random(System.currentTimeMillis());
        int r = randomFloat.nextInt();
        
        p.name = "El Gandalf " + r;
        
        p.actualWeapon = new Weapon();
        p.actualWeapon.name ="weapon";
        p.characterId = 1;
        p.health = 100;
        p.state = "idle";
        p.hitBox = new HitBox();
        p.hitBox.h = 50;
        p.hitBox.w = 50;

        //Random hitbox color
        
		p.hitBox.r = randomFloat.nextFloat();
		p.hitBox.g = randomFloat.nextFloat();
		p.hitBox.b = randomFloat.nextFloat();

        return p;

    }
}
