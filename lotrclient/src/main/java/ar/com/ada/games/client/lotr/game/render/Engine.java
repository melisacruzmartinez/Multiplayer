package ar.com.ada.games.client.lotr.game.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import ar.com.ada.games.client.lotr.actors.Actor;
import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.hitbox.HitBox;

public class Engine implements IEngine {
    

    private static final int FRAMES_PER_SECOND = 30;

    private int displayWidth;
    private int displayHeigth;
    
    public void init(int displayWidth, int displayHeigth){
        this.displayWidth = displayWidth;
        this.displayHeigth = displayHeigth;
        this.initOpenGl();
    }
    /** Initializing OpenGL functions */
	protected void initOpenGl() {

		try {
			DisplayMode dp = new DisplayMode(displayWidth, displayHeigth);
			
			Display.setDisplayMode(dp);
			Display.setResizable(true);
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, this.displayWidth, this.displayHeigth, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }
    public void updateDisplay() {

        Display.update();
        Display.sync(FRAMES_PER_SECOND);
	}
	public void updateCamera(float x, float y, float z){
		glTranslatef(x, y, z);
	}

    /** Function to draw square */
	public void drawSquare(HitBox box) {

		if (box == null){
			int lala = 0;
			lala = 1;
		}
		glColor3f(box.r, box.g, box.b);
		glBegin(GL_QUADS);
			glVertex2f(box.x, box.y);
			glVertex2f(box.x + box.w, box.y);
			glVertex2f(box.x + box.w, box.y + box.h);
			glVertex2f(box.x, box.y + box.h);
		glEnd();
	}

	public void drawPawn(Pawn pawn){

        this.drawSquare(pawn.hitBox);
    }

	public void drawActor(Actor actor){
        this.drawSquare(actor.hitBox);
    }

    public void drawPawnAnnimation(Pawn pawn, String animation){
        
    }    
}
