package com.zerulus.game.tiles.blocks;

import com.zerulus.game.graphics.Screen;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.AABB;

public abstract class Block {
    protected int w;
    protected int h;

    public Sprite img;
    public Vector2f pos;

    public Block(Sprite img, Vector2f pos, int w, int h) {
        this.img = img;
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    public int getWidth() { return w; }
    public int getHeight() { return h; }

    public abstract boolean update(AABB p);
    public abstract boolean isInside(AABB p);

    public abstract Sprite getImage();
    public Vector2f getPos() { return pos; }

    public void render(Screen s) {
        s.drawImage(img, (int) pos.getWorldVar().x, (int) pos.getWorldVar().y, w, h, null);
    }
}
