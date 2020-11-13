package ar.com.ada.games.client.lotr.hitbox;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HitBox{
	
	public float x;
	public float y;
	public int w;
	public int h;
	
	public int xp;
	
	public float r;
	public float g;
	public float b;
	
	public long id;
	
	public HitBox(){}

	public HitBox(float x, float y, int width, int height, float r, float g, float b, long id, int xp) {
		this.r = r;
		this.b = b;
		this.g = g;

		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		
		this.id= id;
		this.xp = xp;
	}
	
	
}