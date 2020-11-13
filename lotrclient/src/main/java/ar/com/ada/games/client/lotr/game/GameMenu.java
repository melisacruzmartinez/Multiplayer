package ar.com.ada.games.client.lotr.game;

import java.util.Scanner;

import ar.com.ada.games.client.lotr.actors.characters.Pawn;
import ar.com.ada.games.client.lotr.controllers.PlayerController;
import net.java.games.input.Keyboard;

public class GameMenu {

    public static Scanner Keyboard = new Scanner(System.in);

    public static void PlayerSetup(PlayerController pc) {
        //
        System.out.println("Pick your name:");

        pc.playerName = Keyboard.nextLine();

        pc.pawn = DoCaracterSelection();
    }

    public static Pawn DoCaracterSelection() {
        //Aca en base a los personajes hacer la selecion. en este caso sale algo random
        return GameCatalog.GetRandomCharater();
    }
}
