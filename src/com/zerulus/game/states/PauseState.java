package com.zerulus.game.states;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.ui.Button;
import com.zerulus.game.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PauseState extends GameState {

    private BufferedImage imgButton;
    private Button btnExit;
    private Button btnPlay;

    public PauseState(GameStateManager gsm) {
        super(gsm);

        imgButton = GameStateManager.ui.getSprite(0, 0, 128, 64);

        btnPlay = new Button("RESUME", 32, 24, imgButton, 200, 75, new Vector2f(0, 0), true);
        btnExit = new Button("EXIT", 32, 24, imgButton, 200, 75, new Vector2f(0, 100), true);
        
    }

    @Override
    public void update() {

    }

    @Override
    public void input(MouseHandler mouse, KeyHandler key) {

    }

    @Override
    public void render(Graphics2D g) {
        btnExit.render(g);
        btnPlay.render(g);
    }
}
