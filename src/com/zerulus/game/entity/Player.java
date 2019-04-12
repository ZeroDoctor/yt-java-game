package com.zerulus.game.entity;


import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.graphics.Screen;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.states.PlayState;
import com.zerulus.game.tiles.TileManager;
import com.zerulus.game.tiles.blocks.NormBlock;
import com.zerulus.game.util.Camera;

import java.util.ArrayList;
import java.awt.Color;

public class Player extends Entity {

    private Camera cam;
    private ArrayList<Enemy> enemy;
    private TileManager tm;

    public Player(Camera cam, SpriteSheet sprite, Vector2f origin, int size, TileManager tm) {
        super(sprite, origin, size);
        this.cam = cam;
        this.tm = tm;

        bounds.setWidth(42);
        bounds.setHeight(20);
        bounds.setXOffset(12);
        bounds.setYOffset(40);

        hitBounds.setWidth(42);
        hitBounds.setHeight(42);

        ani.setNumFrames(4, UP);
        ani.setNumFrames(4, DOWN);
        ani.setNumFrames(4, ATTACK + RIGHT);
        ani.setNumFrames(4, ATTACK + LEFT);
        ani.setNumFrames(4, ATTACK + UP);
        ani.setNumFrames(4, ATTACK + DOWN);

        enemy = new ArrayList<Enemy>();

        for(int i = 0; i < sprite.getSpriteArray2().length; i++) {
            for(int j = 0; j < sprite.getSpriteArray2()[i].length; j++) {
                sprite.getSpriteArray2()[i][j].setEffect(Sprite.effect.NEGATIVE);
                sprite.getSpriteArray2()[i][j].saveColors();
            }
        }

        hasIdle = false;
    }

    public void setTargetEnemy(Enemy enemy) { 
        this.enemy.add(enemy);
    }

    private void resetPosition() {
        System.out.println("Reseting Player... ");
        pos.x = GamePanel.width / 2 - 32;
        PlayState.map.x = 0;
        cam.getBounds().getPos().x = 0;

        pos.y = GamePanel.height /2 - 32;
        PlayState.map.y = 0;
        cam.getBounds().getPos().y = 0;

        setAnimation(RIGHT, sprite.getSpriteArray(RIGHT), 10);

    }

    public void update(double time) {
        super.update(time);

        attacking = isAttacking(time);
        for(int i = 0; i < enemy.size(); i++) {
            if(attacking) {
                enemy.get(i).setHealth(enemy.get(i).getHealth() - damage, force * getDirection(), currentDirection == UP || currentDirection == DOWN);
                enemy.remove(i);
            }
        }

        if(!fallen) {
            move();
            if(!tc.collisionTile(dx, 0)) {
                //PlayState.map.x += dx;
                pos.x += dx;
                xCol = false;
            } else {
                xCol = true;
            }
            if(!tc.collisionTile(0, dy)) {
                //PlayState.map.y += dy;
                pos.y += dy;
                yCol = false;
            } else {
                yCol = true;
            }
        } else {
            xCol = true;
            yCol = true;
            if(ani.hasPlayedOnce()) {
                resetPosition();
                dx = 0;
                dy = 0;
                fallen = false;
            }
        }

        NormBlock block = tm.getNormalTile(tc.getTile());
        if(block != null) {
            block.getImage().setEffect(Sprite.effect.NEGATIVE);
        }
    }

    @Override
    public void render(Screen s) {
        /* g.setColor(Color.green);
        g.drawRect((int) (pos.getWorldVar().x + bounds.getXOffset()), (int) (pos.getWorldVar().y + bounds.getYOffset()), (int) bounds.getWidth(), (int) bounds.getHeight()); */

        if(attack) {
            s.drawRect((int) (hitBounds.getPos().getWorldVar().x + hitBounds.getXOffset()), (int) (hitBounds.getPos().getWorldVar().y + hitBounds.getYOffset()), (int) hitBounds.getWidth(), (int) hitBounds.getHeight(), Color.red.getRGB());
        }

        if(isInvincible) {
            if(GamePanel.tickCount % 30 >= 15) {
                ani.getImage().setEffect(Sprite.effect.REDISH);
            } else {
                ani.getImage().restoreColors();
            }
        } else {
            ani.getImage().restoreColors();
        }

        if(useRight && left) {
            s.drawImage(ani.getImage(), (int) (pos.getWorldVar().x) + size, (int) (pos.getWorldVar().y), -size, size, null);
        } else {
            s.drawImage(ani.getImage(), (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
        }
    }

    public void input(MouseHandler mouse, KeyHandler key) {

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

            if(key.attack.down && canAttack) {
                attack = true;
                attacktime = System.nanoTime();
            } else {
                if(!attacking) {
                    attack = false;
                }
            }

            if(key.shift.down) {
                maxSpeed = 8;
                cam.setMaxSpeed(7);
            } else {
                maxSpeed = 4;
                cam.setMaxSpeed(4);
            }

            if(up && down) {
                up = false;
                down = false;
            }

            if(right && left) {
                right = false;
                left = false;
            }
        } else {
            up = false;
            down = false;
            right = false;
            left = false;
        }
    }
}
