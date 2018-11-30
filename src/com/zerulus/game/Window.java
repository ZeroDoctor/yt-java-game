package com.zerulus.game;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new GamePanel(1280, 720));
        setIgnoreRepaint(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
