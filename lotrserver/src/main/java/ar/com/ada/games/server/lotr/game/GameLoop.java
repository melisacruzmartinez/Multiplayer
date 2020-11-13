package ar.com.ada.games.server.lotr.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.ada.games.server.lotr.controllers.PlayerController;
import ar.com.ada.games.server.lotr.game.GameServer;

public class GameLoop {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected volatile GameStatus status;

    private Thread gameThread;
    private GameServer server;

    public GameLoop(GameServer server) {
        this.server = server;
        status = GameStatus.STOPPED;
    }

    public void run() {
        status = GameStatus.RUNNING;
        gameThread = new Thread(this::processGameLoop);
        gameThread.start();
    }

    public void stop() {
        status = GameStatus.STOPPED;
    }

    public boolean isGameRunning() {
        return status == GameStatus.RUNNING;
    }

    protected void processInput() {

    }

    protected void render() {
        //      logger.info("Current bullet position: " + position);
    }

    protected void processGameLoop() {

        /*
        Basico
        while (isGameRunning()) {
            processInput();
            update();
            render();
        }*/
        for (PlayerController pc : this.server.players) {
            logger.info(pc.toString());
        }
        
        //Con fps limited
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isGameRunning()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                updateGamePlay();
                //updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
                //updates = 0;
            }
        }
        stop();

    }

    protected void updateGamePlay() {
        //mandar updates a los clientes

        server.gameState = new GameState();
        server.gameState.started = this.status == GameStatus.RUNNING;
        server.gameState.finished = this.status == GameStatus.STOPPED;
        
        //server.gameState.update(server.players);

        server.updateGamePlay();
        server.sendGamePlay();
    }

    
    public enum GameStatus {

        RUNNING, STOPPED
    }
}
