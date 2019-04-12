package com.zerulus.game;

import java.awt.BorderLayout;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame {
    public static final long serialVersionUID = 1L;

    private BufferStrategy bs;

    public Window() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addNotify() {
        super.addNotify();

        createBufferStrategy(3);
        bs = getBufferStrategy();

        setLayout(new BorderLayout());
        add(new GamePanel(bs, 1664, 936));
        
    }


}
