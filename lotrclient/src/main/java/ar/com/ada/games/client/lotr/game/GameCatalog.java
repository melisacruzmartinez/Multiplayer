package ar.com.ada.games.client.lotr.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.actors.weapons.Weapon;
import ar.com.ada.games.client.lotr.hitbox.HitBox;

public class GameCatalog {

    public static List<Pawn> Characters = new ArrayList<>();
    public static List<Weapon> Weapons = new ArrayList<>();
    
    public static Weapon FindWeapon(Integer id) {
        //Debe retornar una nueva weapon en base a la misma clase
        return new Weapon();
    }
    
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
