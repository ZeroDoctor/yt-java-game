package com.zerulus.game.entity.material;

import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.entity.GameObject;

import java.awt.Graphics2D;

public class Material extends GameObject {

    protected int maxHealth = 100;
    protected int health = 100;
    protected int damage = 10;
    protected int material;

    public Material(Sprite image, Vector2f origin, int size, int material) {
        super(image, origin, size);
        this.material = material;

        bounds.setXOffset(16);
        bounds.setYOffset(48);
        bounds.setWidth(32);
        bounds.setHeight(16);

        image.setEffect(Sprite.effect.DECAY);
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        g.drawImage(image.image, (int) (pos.getWorldVar().x), (int) (pos.getWorldVar().y), size, size, null);
    }
}