package com.zerulus.game.states;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.ui.Button;
import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.Screen;
import com.zerulus.game.graphics.SpriteSheet;

import java.awt.image.BufferedImage;

public class GameOverState extends GameState {

    private String gameover = "GAME OVER";

    private BufferedImage imgButton;
    private Button btnReset;
    private Button btnQuit;

    public GameOverState(GameStateManager gsm) {
        super(gsm);

        imgButton = GameStateManager.ui.getSprite(0, 0, 128, 64).image;
        btnReset = new Button("Restart", 32, 24, imgButton, 200, 75 , new Vector2f(0, 60));
        btnQuit = new Button("Quit", 32, 24, imgButton, 200, 75, new Vector2f(0, 165));
        
        btnReset.addEvent(e -> {
            gsm.add(GameStateManager.PLAY);
            gsm.pop(GameStateManager.GAMEOVER);
        });

        btnQuit.addEvent(e -> {
            System.exit(0);
        });
    }

    @Override
    public void update(double time) {

    }

    @Override
    public void input(MouseHandler mouse, KeyHandler key) {
        key.escape.tick();

        btnReset.input(mouse, key);
        btnQuit.input(mouse, key);

        if (key.escape.clicked) {
			System.exit(0);
		}
    }

    @Override
    public void render(Screen s) {
        SpriteSheet.drawArray(s, gameover, new Vector2f(GamePanel.width / 2 - gameover.length() * (32 / 2), GamePanel.height / 2 - 32 / 2), 32, 32, 32);
        btnReset.render(s);
        btnQuit.render(s);
    }
}
