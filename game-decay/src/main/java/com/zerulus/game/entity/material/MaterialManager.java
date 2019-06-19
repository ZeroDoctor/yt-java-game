package com.zerulus.game.entity.material;

import java.util.ArrayList;

import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.util.GameObjectKey;

public class MaterialManager {

    public ArrayList<GameObjectKey> list;
    public static enum TYPE {
        TREE(0), STONE(1), IRON(2), WHEAT(3);

        private final int value;

        TYPE(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    };

    private TemplateMaterial[] tempMaterial;

    private int tileSize;
    private int chuckSize;
    private Vector2f playerStart = new Vector2f(0 + (GamePanel.width / 2) - 32, 0 + (GamePanel.height / 2) - 32);

    public MaterialManager(int tileSize, int chuckSize) {
        this.tileSize = tileSize;
        this.chuckSize = chuckSize;
        list = new ArrayList<GameObjectKey>();
        tempMaterial = new TemplateMaterial[4];
        
    }

    public void setMaterial(TYPE material, Sprite sprite, int size) {
        if(tempMaterial[material.getValue()] == null) {
            tempMaterial[material.getValue()] = new TemplateMaterial(size);
        }

        tempMaterial[material.getValue()].images.add(sprite);
    }

    public void add(TYPE material, int position) {

        int size = Math.max(tileSize, tempMaterial[material.getValue()].size);
        int length = (tempMaterial[material.getValue()].images.size() / 10) + 1;
        int index = (int) (Math.random() * (10 * length)) % (tempMaterial[material.getValue()].images.size()); 

        Vector2f pos = new Vector2f((position % chuckSize) * size, (position / chuckSize) * size);

        Material mat = new Material(tempMaterial[material.getValue()].images.get(index), pos, size, material.getValue()); 
        float distance = mat.getBounds().distance(playerStart);
        list.add(new GameObjectKey(distance, mat));
    }

}

class TemplateMaterial {

    public int size;
    public ArrayList<Sprite> images;

    public TemplateMaterial(int size) {
        this.size = size;
        this.images = new ArrayList<Sprite>();
    }
}