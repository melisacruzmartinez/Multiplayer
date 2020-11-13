package ar.com.ada.games.client.lotr.game.render;

import ar.com.ada.games.client.lotr.actors.Actor;
import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.hitbox.HitBox;

public interface IEngine {
    public void init(int displayWidth, int displayHeigth);
    public void clear();
    public void updateDisplay();
    public void updateCamera(float x, float y, float z);
    public void drawSquare(HitBox box);
    public void drawPawn(Pawn pawn);
    public void drawActor(Actor actor);
    public void drawPawnAnnimation(Pawn pawn, String animation);
}
