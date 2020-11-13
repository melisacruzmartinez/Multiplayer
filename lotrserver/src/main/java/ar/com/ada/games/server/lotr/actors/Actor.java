package ar.com.ada.games.server.lotr.actors;

import ar.com.ada.games.server.lotr.hitbox.HitBox;

public class Actor {
    public int id;
    public int xVel;
    public int yVel;
    public HitBox hitBox;
    public boolean isPlayerControlled = false;
}
