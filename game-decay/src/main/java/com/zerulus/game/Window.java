package com.zerulus.game;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame {
    public static final long serialVersionUID = 1L;

    private BufferStrategy bs;
    private GamePanel gp;

    public Window() {
        setTitle("Decay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void addNotify() {
        super.addNotify();

        createBufferStrategy(2);
        bs = getBufferStrategy();

        gp = new GamePanel(bs, 1280, 720);
        //add(gp);
        setContentPane(gp);
        
    }

}
