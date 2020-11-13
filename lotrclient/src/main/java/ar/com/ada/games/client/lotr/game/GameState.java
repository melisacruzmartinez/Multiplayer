package ar.com.ada.games.client.lotr.game;

import java.util.List;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


import ar.com.ada.games.client.lotr.network.replication.PlayerReplicationInfo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GameState {
    public String gameMode;
    public boolean started;
    public boolean finished;
    public String winnerId;
    public List<PlayerReplicationInfo> playersInfo;

    
}

