package com.zerulus.game.ui;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zerulus.game.GamePanel;
import com.zerulus.game.states.GameStateManager;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.math.AABB;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.graphics.SpriteSheet;

public class Button {

    private String label;
    private int lbWidth;
    private int lbHeight;

    private BufferedImage image;
    private BufferedImage hoverImage;
    private BufferedImage pressedImage;

    private Vector2f iPos;
    private Vector2f lbPos;

    private AABB bounds;
    private boolean hovering = false;
    private int hoverSize;
    private ArrayList<ClickedEvent> events;
    private boolean clicked = false;
    private boolean pressed = false;
    private boolean canHover = true;
    private boolean drawString = true;

    private float pressedtime;

    // ******************************************** ICON CUSTOM POS *******************************************

    public Button(BufferedImage icon, BufferedImage image, Vector2f pos, int width, int height, int iconsize) {
        this.image = createIconButton(icon, image, width + iconsize, height + iconsize, iconsize);
        this.iPos = pos;
        this.bounds = new AABB(iPos, this.image.getWidth(), this.image.getHeight());

        events = new ArrayList<ClickedEvent>();
        this.canHover = false;
        this.drawString = false;
    }

    private BufferedImage createIconButton(BufferedImage icon, BufferedImage image, int width, int height, int iconsize) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        if(image.getWidth() != width || image.getHeight() != height) {
            image = resizeImage(image, width, height);
        }

        if(icon.getWidth() != width - iconsize || icon.getHeight() != height - iconsize) {
            icon = resizeImage(icon, width - iconsize, height - iconsize);
        }

        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, width, height, null);

        g.drawImage(icon, 
            image.getWidth() / 2 - icon.getWidth() / 2, 
            image.getHeight() / 2 - icon.getHeight() / 2,
            icon.getWidth(), icon.getHeight(), null);

        g.dispose();

        return result;
    }

    // ******************************************** LABEL TTF CUSTOM MIDDLE POS *******************************************

    public Button(String label, BufferedImage image, Font font, Vector2f pos, int buttonSize) {
        this(label, image, font, pos, buttonSize, -1);
    }

    public Button(String label, BufferedImage image, Font font, Vector2f pos, int buttonWidth, int buttonHeight) {
        GameStateManager.g.setFont(font);
        FontMetrics met = GameStateManager.g.getFontMetrics(font);
        int height = met.getHeight();
        int width = met.stringWidth(label);

        if(buttonWidth == -1) buttonWidth = buttonHeight;

        this.label = label;

        this.image = createButton(label, image, font, width + buttonWidth, height + buttonHeight, buttonWidth, buttonHeight);
        this.iPos = new Vector2f(pos.x - this.image.getWidth() / 2, pos.y - this.image.getHeight() / 2);
        this.bounds = new AABB(iPos, this.image.getWidth(), this.image.getHeight());
        

        events = new ArrayList<ClickedEvent>();
        this.canHover = false;
        this.drawString = false;
    }

    public BufferedImage createButton(String label, BufferedImage image, Font font, int width, int height, int buttonWidth, int buttonHeight) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        if(image.getWidth() != width || image.getHeight() != height) {
            image = resizeImage(image, width, height);
        }

        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, width, height, null);

        g.setFont(font);
        g.drawString(label, buttonWidth / 2, (height - buttonHeight));

        g.dispose();

        return result;
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        g.drawImage(temp, 0, 0, null);
        g.dispose();

        return result;
    }

    // ******************************************** LABEL PNG GAMEPANEL POS *******************************************

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight, Vector2f offset) {
        this(label, lbWidth, lbHeight, image, iWidth, iHeight);

        iPos = new Vector2f((GamePanel.width / 2 - iWidth / 2 + offset.x) , (GamePanel.height / 2 - iHeight / 2 + offset.y));
        lbPos = new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4);
    
        this.bounds = new AABB(iPos, iWidth, iHeight);
    }

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, int iWidth, int iHeight) {
        this.label = label;
        this.lbWidth = lbWidth;
        this.lbHeight = lbHeight;
        this.image = image;
        this.hoverSize = 20;

        iPos = new Vector2f((GamePanel.width / 2 - iWidth / 2) , (GamePanel.height / 2 - iHeight / 2));
        lbPos = new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4);
    
        this.bounds = new AABB(iPos, iWidth, iHeight);

        events = new ArrayList<ClickedEvent>();
    }

    // ******************************************** LABEL PNG CUSTOM POS *******************************************

    public Button(String label, int lbWidth, int lbHeight, BufferedImage image, Vector2f iPos, int iWidth, int iHeight) {
        this(label, new Vector2f((iPos.x + iWidth / 2 + lbWidth / 2) - ((label.length()) * lbWidth / 2), iPos.y + iHeight / 2 - lbHeight / 2 - 4), lbWidth, lbHeight, image, iPos, iWidth, iHeight);
    }

    public Button(String label, Vector2f lbPos, int lbWidth, int lbHeight, BufferedImage image, Vector2f iPos, int iWidth, int iHeight) {
        this(label, lbWidth, lbHeight, image, iWidth, iHeight);

        this.iPos = iPos;
        this.lbPos = lbPos;

        this.bounds = new AABB(iPos, iWidth, iHeight);
    }

    public void addHoverImage(BufferedImage image) {
        this.hoverImage = image;
        this.canHover = true;
    }

    public void addPressedImage(BufferedImage image) {
        this.pressedImage = image;
    }

    public void setHoverSize(int size) { this.hoverSize = size; }
	public boolean getHovering() { return hovering; }
    public void setHover(boolean b) { this.canHover = b; }
    public void addEvent(ClickedEvent e) { events.add(e);}

    public int getWidth() { return (int) bounds.getWidth(); }
    public int getHeight() { return (int) bounds.getHeight(); }
    public Vector2f getPos() { return bounds.getPos(); }

    public void update(double time) {
        if(pressedImage != null && pressed && pressedtime + 300 < time / 1000000) {
            pressed = false;
        }
    }

    private void hover(int value) {
        if(hoverImage == null) {
            iPos.x -= value / 2;
            iPos.y -= value / 2;
            float iWidth = value + bounds.getWidth();
            float iHeight = value + bounds.getHeight();
            this.bounds = new AABB(iPos, (int) iWidth, (int) iHeight);

            lbPos.x -= value / 2;
            lbPos.y -= value / 2;
            lbWidth += value / 3;
            lbHeight += value / 3;
            
        }
        
        hovering = true;
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if(bounds.inside(mouse.getX(), mouse.getY())) {
            if(canHover && !hovering) {
                hover(hoverSize);
            }
            if(mouse.getButton() == 1 && !clicked) {
                clicked = true;
                pressed = true;

                pressedtime = System.nanoTime() / 1000000;

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
        if(drawString) {
            SpriteSheet.drawArray(g, label, lbPos, lbWidth, lbHeight);
        }

        if(canHover && hoverImage != null && hovering) {
            g.drawImage(hoverImage, (int) iPos.x, (int) iPos.y, (int) bounds.getWidth(), (int) bounds.getHeight(), null);
        } else if(pressedImage != null && pressed) {
            g.drawImage(pressedImage, (int) iPos.x, (int) iPos.y, (int) bounds.getWidth(), (int) bounds.getHeight(), null);
        } else {
            g.drawImage(image, (int) iPos.x, (int) iPos.y, (int) bounds.getWidth(), (int) bounds.getHeight(), null);
        }
        
    }

}