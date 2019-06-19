package com.zerulus.game.states;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.ui.Button;
import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.SpriteSheet;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GameOverState extends GameState {

    private String gameover = "GAME OVER";

    private BufferedImage imgButton;
    private BufferedImage imgHover;
    private Button btnReset;
    private Button btnQuit;
    private Font font;


    public GameOverState(GameStateManager gsm) {
        super(gsm);

        imgButton = GameStateManager.button.getSubimage(0, 0, 121, 26);
        imgHover = GameStateManager.button.getSubimage(0, 29, 122, 28);

        font = new Font("MeatMadness", Font.PLAIN, 48);
        btnReset = new Button("RESTART", imgButton, font, new Vector2f(GamePanel.width / 2, GamePanel.height / 2 - 48), 32, 16);
        btnQuit = new Button("QUIT", imgButton, font, new Vector2f(GamePanel.width / 2, GamePanel.height / 2 + 48), 32, 16);

        btnReset.addHoverImage(btnReset.createButton("RESTART", imgHover, font, btnReset.getWidth(), btnReset.getHeight(), 32, 20));
        btnQuit.addHoverImage(btnQuit.createButton("QUIT", imgHover, font, btnQuit.getWidth(), btnQuit.getHeight(), 32, 20));
        
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
    public void render(Graphics2D g) {
        SpriteSheet.drawArray(g, gameover, new Vector2f(GamePanel.width / 2 - gameover.length() * (32 / 2), GamePanel.height / 2 - 32 / 2), 32, 32, 32);
        btnReset.render(g);
        btnQuit.render(g);
    }
}
