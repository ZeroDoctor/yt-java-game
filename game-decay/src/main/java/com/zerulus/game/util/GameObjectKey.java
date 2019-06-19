package com.zerulus.game.util;

import com.zerulus.game.entity.GameObject;
import com.zerulus.game.math.AABB;

public class GameObjectKey {

    public float value;
    public GameObject go;

    public GameObjectKey(float value, GameObject go) {
        this.value = value;
        this.go = go;
    }

    public AABB getBounds() { return go.getBounds(); }
}