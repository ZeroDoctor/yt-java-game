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
    private Vector2f offset;

    private Vector2f iPos;
    private Vector2f lbPos;

    private AABB bounds;
    private boolean hovering = false;
    private int hoverSize;
    private ArrayList<ClickEvent> events;
    private boolean clicked = false;
    private boolean canHover = true;

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight,
            Vector2f offset, boolean centered) {
        this.label = label;
        this.lbWidth = lbWidth;
        this.lbHeight = lbHeight;
        this.image = image;
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        this.offset = offset;
        this.hoverSize = 20;

        if(centered) {
            iPos = new Vector2f((GamePanel.width / 2 - iWidth / 2 + offset.x) , (GamePanel.height / 2 - iHeight / 2 + offset.y));
            lbPos = new Vector2f((GamePanel.width / 2 - ((label.length() - 1) * lbWidth) / 2), (GamePanel.height / 2 - (lbHeight + 5) / 2) + offset.y);
        }
        this.bounds = new AABB(iPos, iWidth, iHeight);
        events = new ArrayList<ClickEvent>();
    }

    public void setHoverSize(int size) {
        this.hoverSize = size;
    }

    public void setHover(boolean b) {
        this.canHover = b;
    }

    public void addEvent(ClickEvent e) {
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