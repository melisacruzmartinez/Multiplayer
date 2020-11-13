package ar.com.ada.games.server.lotr.controllers;

public class CollisionController {
    public static boolean Collision(float f, float h, float i, float j, float left,
    float top, float right, float buttom) {
return f < right && i > left && h < buttom && j > top;
}
}
