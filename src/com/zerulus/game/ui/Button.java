package com.zerulus.game.ui;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.states.PlayState;
import com.zerulus.game.GamePanel;

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

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight, Vector2f pos,
            boolean centered) {
        this.label = label;
        this.lbWidth = lbWidth;
        this.lbHeight = lbHeight;
        this.image = image;
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        this.pos = pos;

        if (centered) {
            iPos = new Vector2f(GamePanel.width / 2 - iWidth / 2 + pos.x, GamePanel.height / 2 - iHeight / 2 + pos.y);
            lbPos = new Vector2f((GamePanel.width / 2 - ((label.length() - 1) * lbWidth) / 2),
                    (GamePanel.height / 2 - (lbHeight + 7) / 2) + pos.y);
        }
    }

    public void update() {

    }

    public void input(MouseHandler mouse, KeyHandler key) {

    }

    public void render(Graphics2D g) {
        Sprite.drawArray(g, label, lbPos, lbWidth, lbHeight);
        g.drawImage(image, (int) iPos.x, (int) iPos.y, iWidth, iHeight, null);
    }
}