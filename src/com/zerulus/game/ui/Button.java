package com.zerulus.game.ui;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.AABB;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.states.PlayState;
import com.zerulus.game.GamePanel;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Button {

    private String label;
    private int lbWidth;
    private int lbHeight;
    private BufferedImage image;
    private int iWidth;
    private int iHeight;
    private Vector2f pos;

    private Vector2f iPos;
    private Vector2f lbPos;

    private AABB bounds;
    private boolean hovering = false;
    private int hoverSize;
    private ArrayList<ClickedEvent> events;
    private boolean clicked = false;


    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight, Vector2f pos,
            boolean centered) {
        this.label = label;
        this.lbWidth = lbWidth;
        this.lbHeight = lbHeight;
        this.image = image;
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        this.pos = pos;
        this.hoverSize = 20;
        
        if (centered) {
            iPos = new Vector2f(GamePanel.width / 2 - iWidth / 2 + pos.x, GamePanel.height / 2 - iHeight / 2 + pos.y);
            lbPos = new Vector2f((GamePanel.width / 2 - ((label.length() - 1) * lbWidth) / 2),
                    (GamePanel.height / 2 - (lbHeight + 7) / 2) + pos.y);
        }
        this.bounds = new AABB(iPos, iWidth, iHeight);
        events = new ArrayList<ClickedEvent>();
    }

    public void setHoverSize(int size) {
        this.hoverSize = size;
    }

    public void addEvent(ClickedEvent e) {
        events.add(e);
        System.out.println("Button: " + events.size());
    }

    public void update() {
        
    }

    private void hover(int value) {
        iPos.x -= value / 2;
        iPos.y -= value / 2;
        iWidth += value;
        iHeight += value;
        this.bounds = new AABB(iPos, iWidth, iHeight);
        hovering = true;
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if(bounds.inside(mouse.getX(), mouse.getY())) {
            if(!hovering)
                hover(hoverSize);
        } else if(hovering) {
            hover(-hoverSize);
            hovering = false;
        }

        if(mouse.getButton() == 1 && !clicked) {
            clicked = true;
            for(int i = 0; i < events.size(); i++) {
                events.get(i).action(1);
            }
        } else if(mouse.getButton() == -1){
            clicked = false;
        }
    }

    public void render(Graphics2D g) {
        Sprite.drawArray(g, label, lbPos, lbWidth, lbHeight);
        g.drawImage(image, (int) iPos.x, (int) iPos.y, iWidth, iHeight, null);
    }
}