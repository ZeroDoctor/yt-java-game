package com.zerulus.game.ui;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.Player;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class PlayerUI {

    private FillBars healthbar;
    private Slots[] buildingslots;

    public PlayerUI(Player p) {

        SpriteSheet bars = new SpriteSheet("ui/fillbars.png");
        BufferedImage[] barSprites = { 
            bars.getSubimage(12, 2, 7, 16), 
            bars.getSubimage(39, 0, 7, 14), // red health bar
            bars.getSubimage(0, 0, 12, 20) };
        
        Vector2f pos = new Vector2f(32, GamePanel.height - 52);
        this.healthbar = new FillBars(p, barSprites, pos, 16, 16);
        
        BuildOptionUI boUI = new BuildOptionUI();
        buildingslots = boUI.getSlots();
    }

    
    public void update(double time) {
        for(int i = 0; i < buildingslots.length; i++) {
            buildingslots[i].update(time);
        }
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        for(int i = 0; i < buildingslots.length; i++) {
            buildingslots[i].input(mouse, key);
        }
    }

    public void render(Graphics2D g) {
        healthbar.render(g);

        for(int i = 0; i < buildingslots.length; i++) {
            buildingslots[i].render(g);
        }
    }

}