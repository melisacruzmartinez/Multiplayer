package ar.com.ada.games.client.lotr.actors.projectile;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.ada.games.client.lotr.actors.Actor;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Projectile extends Actor {
	
	public float x;
	public float y;
	public float k;
	public float c;
	public float pn;
	
	public Projectile(){}
	
	public Projectile(float x, float y, float k, float c, float pn){
		this.x = x;
		this.y = y;
		this.k = k;
		this.c = c;
		this.pn = pn;
	}
}
