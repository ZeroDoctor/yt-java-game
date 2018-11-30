package com.zerulus.game.util;

import com.zerulus.game.GamePanel;
import java.awt.Graphics2D;
import com.zerulus.game.entity.Entity;
import com.zerulus.game.util.AABB;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.states.PlayState;
import com.zerulus.game.util.Vector2f;
import java.awt.Color;

public class Camera {

    private AABB collisionCam;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private float dx;
    private float dy;
    private float maxSpeed = 4f;
    private float acc = 2f;
    private float deacc = 0.3f;

	private int widthLimit;
	private int heightLimit;

	private Entity e;

    public Camera(AABB collisionCam) {
        this.collisionCam = collisionCam;
    }

	public void setLimit(int widthLimit, int heightLimit) {
		this.widthLimit = widthLimit;
		this.heightLimit = heightLimit;
	}
    public AABB getBounds() { return collisionCam; }

    public void update() {
        move();
		if(e != null) {

			// checks if it collides with a solid tile. if not than it binds the camera to the map
			if(!e.xCol) {
                if ((e.getBounds().getPos().getWorldVar().x + e.getSpeedx()) < Vector2f.getWorldVarX(widthLimit - GamePanel.width / 2) - 64 &&
                    (e.getBounds().getPos().getWorldVar().x + e.getSpeedx()) > Vector2f.getWorldVarX(GamePanel.width / 2 - 64)) {
                    PlayState.map.x += dx;
                }
            }
            
			if(!e.yCol) {
                if ((e.getBounds().getPos().getWorldVar().y + e.getSpeedy()) < Vector2f.getWorldVarY(heightLimit - collisionCam.getHeight() / 2) - 64 &&
                    (e.getBounds().getPos().getWorldVar().y + e.getSpeedy()) > Vector2f.getWorldVarY(GamePanel.height / 2 - 64)) {
                    PlayState.map.y += dy;
                }
			}
		}
    }

    public void render(Graphics2D g) {
        g.setColor(Color.blue);
        g.drawRect((int) collisionCam.getPos().x, (int) collisionCam.getPos().y, (int) collisionCam.getWidth(), (int) collisionCam.getHeight());
    }

	public void target(Entity e) {
        this.e = e;
        acc = e.getAcc();
        deacc = e.getDeacc();
        maxSpeed = e.getMaxSpeed();
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

    public void input(MouseHandler mouse, KeyHandler key) {
		if(e == null) {
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
		} else {
            if(!e.yCol) {
                if(PlayState.map.y + GamePanel.height / 2 - e.getSize() / 2 + dy > e.getBounds().getPos().y + e.getSpeedy() + 2) {
                    up = true;
                    down = false;
                } else if (PlayState.map.y + GamePanel.height / 2 - e.getSize() / 2 + dy < e.getBounds().getPos().y + e.getSpeedy() - 2) {
                    down = true;
                    up = false;
                } else {
                    dy = 0;
                    up = false;
                    down = false;
                }
            }

            if(!e.xCol) {
                if(PlayState.map.x + GamePanel.width / 2 - e.getSize() / 2 + dx > e.getBounds().getPos().x + e.getSpeedx() + 2) {
                    left = true;
                    right = false;
                } else if (PlayState.map.x + GamePanel.width / 2 - e.getSize() / 2 + dx < e.getBounds().getPos().x + e.getSpeedx() - 2) {
                    right = true;
                    left = false;
                } else {
                    dx = 0;
                    right = false;
                    left = false;
                }
            }
		}
    }
}
