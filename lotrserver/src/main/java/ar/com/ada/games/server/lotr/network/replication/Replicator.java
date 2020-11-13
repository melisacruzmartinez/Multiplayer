package ar.com.ada.games.server.lotr.network.replication;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.ada.games.server.lotr.game.GameState;
import ar.com.ada.games.server.lotr.network.replication.*;


public class Replicator {
    
	
	public static ServerMessage unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		return (ServerMessage) unmarshaller.unmarshal(sr);
	}


	public static String marshall(GameState gameState) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(GameState.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(gameState, sw);
        
		return sw.toString();
	}
	
	public static String marshall(PlayerReplicationInfo pri) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(GameState.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(pri, sw);
        
		return sw.toString();
	}
	
}
