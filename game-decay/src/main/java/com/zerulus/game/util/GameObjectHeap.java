package com.zerulus.game.util;

import java.util.ArrayList;

import com.zerulus.game.entity.GameObject;

// Maybe Quicksort? 
public class GameObjectHeap extends ArrayList<GameObjectKey> {

    private static final long serialVersionUID = 1L;

	public void buildHeap() {
        for(int i = size() / 2; i >= 0; i--) {
            minHeapify(i);
        }
    }

    public void add(float value, GameObject go) {
        GameObjectKey gok = new GameObjectKey(value, go);
        super.add(gok);

        int i = size() - 1;
        int parent = parent(i);

        if(i > 3) {
            while(parent != i && get(i).value < get(parent).value) {
                swap(i, parent);
                i = parent;
                parent = parent(i);
            }
        }
    }

    public boolean contains(GameObject go) {
        for(int i = 0; i < size(); i++) {
            if(go.equals(get(i).go) ) {
                return true;
            }
        }

        return false;
    }

    public void remove(GameObject go) {
        for(int i = 0; i < size(); i++) {
            if(go.equals(get(i).go)) {
                super.remove(i);
            }
        }
    }

    private void minHeapify(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = -1;

        if(left < size() - 1 && get(left).value < get(i).value) {
            smallest = left;
        } else {
            smallest = i;
        }

        if(right < size() - 1 && get(right).value < get(smallest).value) {
            smallest = right;
        }

        if(smallest != i) {
            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    private int parent(int i) {
        if(i % 2 == 1) {
            return i / 2;
        }

        return (i - 1) / 2;
    }

    private void swap(int i, int parent) {
        GameObjectKey temp = get(parent);
        set(parent, get(i));
        set(i, temp);
    }

    public String toString() {
        String string = "[";

        for(int i = 0; i < size(); i++) {
            string += get(i).value + ", ";
        }

        string += "]";

        return string;
    }
}
