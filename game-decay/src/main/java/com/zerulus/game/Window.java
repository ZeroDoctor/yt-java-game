package com.zerulus.game;

import java.awt.BorderLayout;
import java.awt.image.BufferStrategy;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Window extends JFrame implements ComponentListener {
    public static final long serialVersionUID = 1L;

    private BufferStrategy bs;
    private GamePanel gp;

    public Window() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        getContentPane().addComponentListener(this);
    }

    public void addNotify() {
        super.addNotify();

        createBufferStrategy(2);
        bs = getBufferStrategy();

        setLayout(new BorderLayout());
        gp = new GamePanel(bs, 1280, 720);
        add(gp);
        
    }


    public void componentHidden(ComponentEvent ce) {}
    public void componentShown(ComponentEvent ce) {}
    public void componentMoved(ComponentEvent ce) {}
    public void componentResized(ComponentEvent ce) {
        GamePanel.width = this.getWidth();
        GamePanel.height = this.getHeight();
    }

}
