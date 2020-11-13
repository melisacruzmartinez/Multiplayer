package ar.com.ada.games.client.lotr.network.replication;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerMessage implements Serializable{
	
	public PlayerReplicationInfo characterData;
	
	public int messageType;
	public long id;
	public int port;
	
	public ServerMessage(){}
	
	public ServerMessage(int msgType){
		messageType = msgType;
	}
	
	public void setCharacterData(PlayerReplicationInfo data){
		characterData = data;
	}
	

	public void setPort(int port) {
		this.port = port;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}

