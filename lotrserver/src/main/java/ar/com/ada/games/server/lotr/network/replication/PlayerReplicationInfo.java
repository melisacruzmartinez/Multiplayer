package ar.com.ada.games.server.lotr.network.replication;

import ar.com.ada.games.server.lotr.actors.characters.Pawn;
import ar.com.ada.games.server.lotr.actors.weapons.Weapon;
import ar.com.ada.games.server.lotr.controllers.PlayerController;
import ar.com.ada.games.server.lotr.hitbox.HitBox;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerReplicationInfo extends ReplicationInfo {
    public int playerId;
    public String name;
    public int characterId;
    public int health;
    public Weapon weapon;
    public String state;
    public String damageTakenLegend;
    public int xVel;
	public int yVel; 
    public HitBox hitBox;


    private void fillFrom(Pawn pawn) {
        this.health = pawn.health;
        this.weapon = pawn.actualWeapon;
        this.state = pawn.state;
        this.characterId = pawn.characterId;
        this.damageTakenLegend = pawn.damageTakenLegend;
        this.xVel = pawn.xVel;
        this.yVel = pawn.yVel;
        this.hitBox = pawn.hitBox;
    }

    public void fillTo(Pawn pawn) {
        pawn.health = this.health;
        pawn.name = String.valueOf(this.characterId);
        //pawn.name = ;
        pawn.actualWeapon = this.weapon;
        pawn.state = this.state;
        pawn.characterId = this.characterId;
        pawn.damageTakenLegend = this.damageTakenLegend;
        pawn.xVel = this.xVel;
        pawn.yVel = this.yVel;
        pawn.hitBox = this.hitBox;
    }

    public void fillTo(PlayerController pc) {
        pc.playerId = this.playerId;
        pc.playerName = this.name;
        pc.pawn = new Pawn();
        fillTo(pc.pawn);
        pc.pawn.isPlayerControlled = true;
    }

    public static PlayerReplicationInfo From(Pawn pawn) {
        PlayerReplicationInfo pri = new PlayerReplicationInfo();
        pri.fillFrom(pawn);
        return pri;
    }

    public static PlayerReplicationInfo From(PlayerController pc) {
        PlayerReplicationInfo pri = new PlayerReplicationInfo();
        pri.playerId = pc.playerId;
        pri.name = pc.playerName;
        pri.fillFrom(pc.pawn);
        pri.name = pc.playerName;
        return pri;
    }

    @Override
    public String toString() {
        String tmp = "PRI Player " + this.playerId + " State " + this.state + " health " + this.health;
        if (this.weapon != null)
            tmp += " projectiles: " + this.weapon.projectiles.size();

        return tmp;
    }
}

