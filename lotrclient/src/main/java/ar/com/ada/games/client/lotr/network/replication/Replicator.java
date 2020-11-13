package ar.com.ada.games.client.lotr.network.replication;

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

import ar.com.ada.games.client.lotr.game.GameState;
import ar.com.ada.games.client.lotr.network.replication.*;


public class Replicator {
    
	
	/**
	 * Marshalls ServerMessage class to a string.
	 * 
	 * @param sm ServerMessage class
	 * @return an XML string 
	 * @throws JAXBException
	 */
	
	public static String marshall(ServerMessage sm) throws JAXBException {
		
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(sm, sw);
        
		return sw.toString();
	}

	/**
	 * Unmarshall a list of Boxes in XML to a actual object
	 * 
	 * @param data
	 * @return A list of boxes
	 * @throws JAXBException
	 */
	
	public static GameState unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(GameState.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		GameState gameState = (GameState) unmarshaller.unmarshal(sr);
		return gameState;
	}

	public static PlayerReplicationInfo unmarshallPRI(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(PlayerReplicationInfo.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		PlayerReplicationInfo pri = (PlayerReplicationInfo) unmarshaller.unmarshal(sr);
		return pri;
	}
}
