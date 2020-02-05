package com.zerulus.game.entity;

import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.util.Camera;
import com.zerulus.game.math.AABB;
import com.zerulus.game.math.Vector2f;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;

public abstract class Enemy extends Entity {

    protected AABB sense;
    protected int r_sense;

    protected AABB attackrange;
    protected int r_attackrange;

    private Camera cam;

    protected int xOffset;
    protected int yOffset;

    protected ArrayList<GameObject> collisions;

    public Enemy(Camera cam, SpriteSheet sprite, Vector2f origin, int size) {
        super(sprite, origin, size);
        this.cam = cam;

        bounds.setWidth(size / 2);
        bounds.setHeight(size / 2 - yOffset);
        bounds.setXOffset(size / 2 - xOffset);
        bounds.setYOffset(size / 2 + yOffset);

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

                bounds.setWidth(size / 2);
                bounds.setHeight(size / 2 - yOffset);
                bounds.setXOffset(size / 2 - xOffset);
                bounds.setYOffset(size / 2 + yOffset);

                hitBounds = new AABB(pos, size, size);
                hitBounds.setXOffset(size / 2);

                sense = new AABB(new Vector2f(pos.x + size / 2 - r_sense / 2, pos.y + size / 2 - r_sense / 2), r_sense);
                attackrange = new AABB(new Vector2f(pos.x + bounds.getXOffset() + bounds.getWidth() / 2 - r_attackrange / 2 , pos.y + bounds.getYOffset() + bounds.getHeight() / 2 - r_attackrange / 2 ), r_attackrange);
            }

            if(attackrange.colCircleBox(player.getBounds()) && !isInvincible) {
                attack = true;
                player.setHealth(player.getHealth() - damage, 5f * getDirection(), currentDirection == UP || currentDirection == DOWN);
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

            //if(isInvincible) 
            if(useRight && left) {
                g.drawImage(ani.getImage().image, (int) (pos.getWorldVar().x) + size, (int) (pos.getWorldVar().y), -size, size, null);
            } else {
                g.drawImage(ani.getImage().image, (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
            }
            
			
            // Health Bar UI
            g.setColor(Color.red);
			g.fillRect((int) (pos.getWorldVar().x + bounds.getXOffset()), (int) (pos.getWorldVar().y - 5), 24, 5);
			
			g.setColor(Color.green);
            g.fillRect((int) (pos.getWorldVar().x + bounds.getXOffset()), (int) (pos.getWorldVar().y - 5), (int) (24 * healthpercent), 5);
            
        }
    }
}