package com.zerulus.game.entity.enemy;

import com.zerulus.game.entity.Enemy;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.util.Camera;
import com.zerulus.game.math.Vector2f;

public class TinyMon extends Enemy {

    public TinyMon(Camera cam, SpriteSheet sprite, Vector2f origin, int size) {
        super(cam, sprite, origin, size);
        xOffset = size / 4;
        yOffset = size / 4;

        damage = 10;
        acc = 1f;
        deacc = 2f;
        maxSpeed = 2f;
        r_sense = 350;
        r_attackrange = 32;

        ATTACK = 0;
        IDLE = 0;
        FALLEN = 1;
        UP = 1;
        DOWN = 1;
        LEFT = 1;
        RIGHT = 1;

        hasIdle = true;
        useRight = true;

        ani.setNumFrames(3, 0);
        ani.setNumFrames(5, 1);

       currentAnimation = IDLE;
       right = true;
    }

}