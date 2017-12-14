package com.zerulus.game.entity;


import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.states.PlayState;

import java.awt.*;

public class Player extends Entity {


    public Player(Sprite sprite, Vector2f orgin, int size) {
        super(sprite, orgin, size);
        acc = 2f;
        maxSpeed = 4f;
        bounds.setWidth(42);
        bounds.setHeight(20);
        bounds.setXOffset(12);
        bounds.setYOffset(40);
    }

    private void move() {
        if(up) {
            dy -= acc;
            if(dy < -maxSpeed) {
                dy = -maxSpeed;
            }
        } else {
            if(dy < 0) {
                dy += deacc;
                if(dy > 0) {
                    dy = 0;
                }
            }
        }
        if(down) {
            dy += acc;
            if(dy > maxSpeed) {
                dy = maxSpeed;
            }
        } else {
            if(dy > 0) {
                dy -= deacc;
                if(dy < 0) {
                    dy = 0;
                }
            }
        }
        if(left) {
            dx -= acc;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else {
            if(dx < 0) {
                dx += deacc;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }
        if(right) {
            dx += acc;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if(dx > 0) {
                dx -= deacc;
                if(dx < 0) {
                    dx = 0;
                }
            }
        }
    }

    private void resetPosition() {
        System.out.println("Reseting Player... ");
        pos.x = GamePanel.width / 2 - 32;
        PlayState.map.x = 0;

        pos.y = GamePanel.height /2 - 32;
        PlayState.map.y = 0;

        setAnimation(RIGHT, sprite.getSpriteArray(RIGHT), 10);

    }

    public void update(Enemy enemy) {
        super.update();

        if(attack && hitBounds.collides(enemy.getBounds())) {
            System.out.println("I've been hit!");
        }

        if(!fallen) {
            move();
            if(!tc.collisionTile(dx, 0)) {
                PlayState.map.x += dx;
                pos.x += dx;
            }
            if(!tc.collisionTile(0, dy)) {
                PlayState.map.y += dy;
                pos.y += dy;
            }
        } else {
            if(ani.hasPlayedOnce()) {
                resetPosition();
                dx = 0;
                dy = 0;
                fallen = false;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.green);
        g.drawRect((int) (pos.getWorldVar().x + bounds.getXOffset()), (int) (pos.getWorldVar().y + bounds.getYOffset()), (int) bounds.getWidth(), (int) bounds.getHeight());

        if(attack) {
            g.setColor(Color.red);
            g.drawRect((int) (hitBounds.getPos().getWorldVar().x + hitBounds.getXOffset()), (int) (hitBounds.getPos().getWorldVar().y + hitBounds.getYOffset()), (int) hitBounds.getWidth(), (int) hitBounds.getHeight());
        }

        g.drawImage(ani.getImage(), (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
    }

    public void input(MouseHandler mouse, KeyHandler key) {

        if(mouse.getButton() == 1) {
            System.out.println("Player: " + pos.x + ", " + pos.y);
        }
        if(!fallen) {
            if(key.up.down) {
                up = true;
            } else {
                up = false;
            }
            if(key.down.down) {
                down = true;
            } else {
                down = false;
            }
            if(key.left.down) {
                left = true;
            } else {
                left = false;
            }
            if(key.right.down) {
                right = true;
            } else {
                right = false;
            }

            if(key.attack.down) {
                attack = true;
            } else {
                attack = false;
            }
        } else {
            up = false;
            down = false;
            right = false;
            left = false;
        }
    }
}
