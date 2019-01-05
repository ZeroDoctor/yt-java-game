package com.zerulus.game.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zerulus.game.GamePanel;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.AABB;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.graphics.Sprite;

public class Button {

    private String label;
    private int lbWidth;
    private int lbHeight;
    private BufferedImage image;
    private int iWidth;
    private int iHeight;

    private Vector2f iPos;
    private Vector2f lbPos;

    private AABB bounds;
    private boolean hovering = false;
    private int hoverSize;
    private ArrayList<ClickedEvent> events;
    private boolean clicked = false;
    private boolean canHover = true;

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight) {
        this.label = label;
        this.lbWidth = lbWidth;
        this.lbHeight = lbHeight;
        this.image = image;
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        this.hoverSize = 20;

        iPos = new Vector2f((GamePanel.width / 2 - iWidth / 2) , (GamePanel.height / 2 - iHeight / 2));
        lbPos = new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4);
    
        this.bounds = new AABB(iPos, iWidth, iHeight);

        events = new ArrayList<ClickedEvent>();
    }

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight, Vector2f offset) {
        this(label, lbWidth, lbHeight, image, iWidth, iHeight);

        iPos = new Vector2f((GamePanel.width / 2 - iWidth / 2 + offset.x) , (GamePanel.height / 2 - iHeight / 2 + offset.y));
        lbPos = new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4);
    
        this.bounds = new AABB(iPos, iWidth, iHeight);
    }

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, Vector2f iPos, int iWidth, int iHeight) {
        this(label, new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4), lbWidth, lbHeight, image, iPos, iWidth, iHeight);
    }

    public Button(String label, Vector2f lbPos, int lbWidth, int lbHeight, BufferedImage image, Vector2f iPos, int iWidth, int iHeight) {
        this(label, lbWidth, lbHeight, image, iWidth, iHeight);

        this.iPos = iPos;
        this.lbPos = lbPos;

        this.bounds = new AABB(iPos, iWidth, iHeight);
    }

    public void setHoverSize(int size) {
        this.hoverSize = size;
    }

    public void setHover(boolean b) {
        this.canHover = b;
    }

    public void addEvent(ClickedEvent e) {
        events.add(e);
    }

    public void update() {

    }

    private void hover(int value) {
        iPos.x -= value / 2;
        iPos.y -= value / 2;
        iWidth += value;
        iHeight += value;
        this.bounds = new AABB(iPos, iWidth, iHeight);

        lbPos.x -= value / 2;
        lbPos.y -= value / 2;
        lbWidth += value / 3;
        lbHeight += value / 3;

        hovering = true;
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if(bounds.inside(mouse.getX(), mouse.getY())) {
            if(canHover && !hovering) {
                hover(hoverSize);
            }
            if(mouse.getButton() == 1 && !clicked) {
                clicked = true;
                for(int i = 0; i < events.size(); i++) {
                    events.get(i).action(1);
                }
            } else if(mouse.getButton() == -1) {
                clicked = false;
            }
        } else if(canHover && hovering) {
            hover(-hoverSize);
            hovering = false;
        }
    }

    public void render(Graphics2D g) {
        Sprite.drawArray(g, label, lbPos, lbWidth, lbHeight);
        g.drawImage(image, (int) iPos.x, (int) iPos.y, iWidth, iHeight, null);
    }

}