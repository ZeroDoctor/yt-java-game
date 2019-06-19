package com.zerulus.game.entity;

import com.zerulus.game.graphics.Animation;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.math.AABB;
import com.zerulus.game.util.TileCollision;


import java.awt.Graphics2D;

public abstract class Entity extends GameObject {

    public int IDLE = 6;
    public int ATTACK = 5;
    public int FALLEN = 4;
    public int UP = 3;
    public int DOWN = 2;
    public int LEFT = 1;
    public int RIGHT = 0;

    protected boolean hasIdle = false;

    protected int currentAnimation;
    protected int currentDirection = RIGHT;

    public boolean useRight = false;

    protected Animation ani;
    protected int hitsize;

    protected boolean up = false;
    protected boolean down = false;
    protected boolean right = false;
    protected boolean left = false;
    protected boolean attack = false;
    protected boolean fallen = false;

    public boolean xCol = false;
    public boolean yCol = false;

    protected int invincible = 500;
    protected double invincibletime;
    protected boolean isInvincible = false;
    protected boolean die = false;

    protected int attackSpeed = 1050; // in milliseconds
    protected int attackDuration = 650; // in milliseconds
    protected double attacktime;
    protected boolean canAttack = true;
    protected boolean attacking = false;
	
	protected int maxHealth = 100;
    protected int health = 100;
    protected float healthpercent = 1;
    protected int defense = 100;
    protected int damage = 25;

    protected AABB hitBounds;

    public Entity(SpriteSheet sprite, Vector2f origin, int size) {
        super(sprite, origin, 0, 0, size);
        this.hitsize = size;

        hitBounds = new AABB(origin, size, size);
        hitBounds.setXOffset(size / 2);

        ani = new Animation();
        setAnimation(RIGHT, sprite.getSpriteArray(RIGHT), 10);

        tc = new TileCollision(this);
    }

    public void setFallen(boolean b) { fallen = b; }

    public void setHealth(int i, float f, boolean dir) {
        if(!isInvincible) {
            health = i;
            isInvincible = true;
            invincibletime = System.nanoTime();
            if(health <= 0) {
                die = true;
            }

            addForce(f, dir);

            healthpercent = (float) health / (float) maxHealth;
        }
    }

    public boolean getDeath() { return die; }
    public int getHealth() { return health; }
    public float getHealthPercent() { return healthpercent; }
    public int getDefense() { return defense; }
    public AABB getHitBounds() { return hitBounds; }
    public int getDirection() {
        if(currentDirection == UP || currentDirection == LEFT) {
            return 1;
        }
        return -1;
    }
    public Animation getAnimation() { return ani; }

    public void setAnimation(int i, Sprite[] frames, int delay) {
        currentAnimation = i;
        ani.setFrames(i, frames);
        ani.setDelay(delay);
    }

    public void animate() {

        if(attacking) {
            if(currentAnimation < 5) {
                setAnimation(currentAnimation + ATTACK, sprite.getSpriteArray(currentAnimation + ATTACK), attackDuration / 100);
            }
        } else if (up) {
            if ((currentAnimation != UP || ani.getDelay() == -1)) {
                setAnimation(UP, sprite.getSpriteArray(UP), 5);
            }
        } else if (down) {
            if ((currentAnimation != DOWN || ani.getDelay() == -1)) {
                setAnimation(DOWN, sprite.getSpriteArray(DOWN), 5);
            }
        } else if (left) {
            if ((currentAnimation != LEFT || ani.getDelay() == -1)) {
                setAnimation(LEFT, sprite.getSpriteArray(LEFT), 5);
            }
        } else if (right) {
            if ((currentAnimation != RIGHT || ani.getDelay() == -1)) {
                setAnimation(RIGHT, sprite.getSpriteArray(RIGHT), 5);
            }
        } else if (fallen) {
            if (currentAnimation != FALLEN || ani.getDelay() == -1) {
                setAnimation(FALLEN, sprite.getSpriteArray(FALLEN), 15);
            }
        }
        else {
            if(!attacking && currentAnimation > 4) {
                setAnimation(currentAnimation - ATTACK, sprite.getSpriteArray(currentAnimation - ATTACK), -1);
            } else if(!attacking) {
                if(hasIdle && currentAnimation != IDLE) {
                    setAnimation(IDLE, sprite.getSpriteArray(IDLE), 10);
                } else if(!hasIdle) {
                    setAnimation(currentAnimation, sprite.getSpriteArray(currentAnimation), -1);
                }
            }
        }
    }

    private void setHitBoxDirection() {
        if (up && !attacking) {
            hitBounds.setXOffset((size - hitBounds.getWidth()) / 2);
            hitBounds.setYOffset(-hitBounds.getHeight() / 2 + hitBounds.getXOffset());
        } else if (down && !attacking) {
            hitBounds.setXOffset((size - hitBounds.getWidth()) / 2);
            hitBounds.setYOffset(hitBounds.getHeight() / 2 + hitBounds.getXOffset());
        } else if (left && !attacking) {
            hitBounds.setYOffset((size - hitBounds.getHeight()) / 2);
            hitBounds.setXOffset(-hitBounds.getWidth() / 2 + hitBounds.getYOffset()); 
        } else if (right && !attacking) {
            hitBounds.setYOffset((size - hitBounds.getHeight()) / 2);
            hitBounds.setXOffset(hitBounds.getWidth() / 2 + hitBounds.getYOffset());
        }
    }

    protected boolean isAttacking(double time) {

        if((attacktime / 1000000) > ((time / 1000000) - attackSpeed)) {
            canAttack = false;
        } else {
            canAttack = true;
        }

        if((attacktime / 1000000) + attackDuration > (time / 1000000)) {
            return true;
        }

        return false;
    }

    public void move() {
        if(up) {
            currentDirection = UP;
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
            currentDirection = DOWN;
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
            currentDirection = LEFT;
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
            currentDirection = RIGHT;
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

    public void update(double time) {
        if(isInvincible) {
            if((invincibletime / 1000000) + invincible < (time / 1000000) ) {
                isInvincible = false;
            }
        }
        animate();
        setHitBoxDirection();
        ani.update();
    }

    public abstract void render(Graphics2D g);

}
