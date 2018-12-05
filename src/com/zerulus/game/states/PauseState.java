package com.zerulus.game.states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.ui.Button;

public class PauseState extends GameState {
    
    private BufferedImage imgButton;
    private Button btnResume;
    private Button btnExit;

    public PauseState(GameStateManager gsm) {
        super(gsm);
        imgButton = GameStateManager.ui.getSprite(0, 0, 128, 64);


        btnResume = new Button("RESUME", 32, 24, imgButton, 200, 75, new Vector2f(0, -50), true);
        btnExit = new Button("EXIT", 32, 24, imgButton, 200, 75, new Vector2f(0, 50), true);
        
        btnResume.addEvent(e -> {
            gsm.pop(GameStateManager.PAUSE);
        });

        btnExit.addEvent(e -> {
            System.exit(0);
        });
    }

    @Override
    public void update(double time) {
        btnResume.update();
        btnExit.update();
    }

    @Override
    public void input(MouseHandler mouse, KeyHandler key) {
        btnResume.input(mouse, key);
        btnExit.input(mouse, key);
    }

    @Override
    public void render(Graphics2D g) {
        btnResume.render(g);
        btnExit.render(g);
    }
}
