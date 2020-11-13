package ar.com.ada.games.server.lotr.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.actors.projectiles.Projectile;
import ar.com.ada.games.server.lotr.controllers.PlayerController;
import ar.com.ada.games.server.lotr.network.replication.PlayerReplicationInfo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GameState {
    public String gameMode;
    public boolean started;
    public boolean finished;
    public String winnerId;
    public List<PlayerReplicationInfo> playersInfo;

    public void update(List<PlayerController> players) {

        playersInfo = new ArrayList<>();
        for (PlayerController playerController : players) {
			
			//Updateo el gameplay de todos
			playerController.update(players);

			//Agrego nuevos 
            playersInfo.add(PlayerReplicationInfo.From(playerController));

			
        }
        
    }

    
}

