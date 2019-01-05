package com.zerulus.game.entity;

import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.AABB;
import com.zerulus.game.util.Camera;
import com.zerulus.game.util.Vector2f;

import java.awt.*;

public class Enemy extends Entity {

    private AABB sense;
    private int r_sense;

    private AABB attackrange;
    private int r_attackrange;

    private Camera cam;
    public Enemy(Camera cam, Sprite sprite, Vector2f origin, int size) {
        super(sprite, origin, size);
        deacc = 2f;
        this.cam = cam;

        acc = 1f;
        maxSpeed = 2f;
        r_sense = 350;
        r_attackrange = 32;

        bounds.setWidth(42);
        bounds.setHeight(20);
        bounds.setXOffset(12);
        bounds.setYOffset(40);

        sense = new AABB(new Vector2f(origin.x + size / 2 - r_sense / 2, origin.y + size / 2 - r_sense / 2), r_sense);
        attackrange = new AABB(new Vector2f(origin.x + bounds.getXOffset() + bounds.getWidth() / 2 - r_attackrange / 2 , origin.y + bounds.getYOffset() + bounds.getHeight() / 2 - r_attackrange / 2 ), r_attackrange);
    }

    public void chase(Player player) {
        AABB playerBounds = player.getBounds();
        if (sense.colCircleBox(playerBounds) && !attackrange.colCircleBox(playerBounds)) {
            if (pos.y > player.pos.y + 1) {
                up = true;
            } else {
                up = false;
            }
            if (pos.y < player.pos.y - 1) {
                down = true;
            } else {
                down = false;
            }

            if (pos.x > player.pos.x + 1) {
                left = true;
            } else {
                left = false;
            } 
            if (pos.x < player.pos.x - 1) {
                right = true;
            } else {
                right = false;
            }
        } else {
            up = false;
            down = false;
            left = false;
            right = false;
        }
    }

    public void update(Player player, double time) {
        if(cam.getBounds().collides(this.bounds)) {
            super.update(time);
            chase(player);
            move();

            if(teleported) {
                teleported = false;
                bounds.setWidth(42);
                bounds.setHeight(20);
                bounds.setXOffset(12);
                bounds.setYOffset(40);

                hitBounds = new AABB(pos, size, size);
                hitBounds.setXOffset(size / 2);

                sense = new AABB(new Vector2f(pos.x + size / 2 - r_sense / 2, pos.y + size / 2 - r_sense / 2), r_sense);
                attackrange = new AABB(new Vector2f(pos.x + bounds.getXOffset() + bounds.getWidth() / 2 - r_attackrange / 2 , pos.y + bounds.getYOffset() + bounds.getHeight() / 2 - r_attackrange / 2 ), r_attackrange);
            }

            if(attackrange.colCircleBox(player.getBounds())) {
                attack = true;
                player.setHealth(player.getHealth() - damage);
            } else {
                attack = false;
            }

            if (!fallen) {
                if (!tc.collisionTile(dx, 0)) {
                    sense.getPos().x += dx;
                    attackrange.getPos().x += dx;
                    pos.x += dx;
                }
                if (!tc.collisionTile(0, dy)) {
                    sense.getPos().y += dy;
                    attackrange.getPos().y += dy;
                    pos.y += dy;
                }
            } else {
                if(ani.hasPlayedOnce()) { 
                    die = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if(cam.getBounds().collides(this.bounds)) { 
            /* g.setColor(Color.green);
            g.drawRect((int) (pos.getWorldVar().x + bounds.getXOffset()), (int) (pos.getWorldVar().y + bounds.getYOffset()), (int) bounds.getWidth(), (int) bounds.getHeight()); */

            g.setColor(Color.blue);
            g.drawOval((int) (sense.getPos().getWorldVar().x), (int) (sense.getPos().getWorldVar().y), r_sense, r_sense);

            g.setColor(Color.red);
            g.drawOval((int) (attackrange.getPos().getWorldVar().x), (int) (attackrange.getPos().getWorldVar().y), r_attackrange, r_attackrange);

            g.drawImage(ani.getImage(), (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
        }
    }
}