package com.zerulus.game;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void testApp() {

        int data = 0;

        int tile = 0;
        int material = 0;

        int tilesetSize = 50;   
        int numMaterials = 5;

        for(int i = 0; i < tilesetSize; i++) {
            for(int j = 0; j < numMaterials; j++) {
                data = i + (tilesetSize * (numMaterials + j));
                material = (int) ((data / tilesetSize) % numMaterials);
                tile = data - (tilesetSize * (numMaterials + material));

                assertFalse("tile: " + tile + " i: " + i, tile != i);
                assertFalse("material: " + material + " j: " + j, material != j);
            }
        }
        
        assertTrue(true);
    }
}
