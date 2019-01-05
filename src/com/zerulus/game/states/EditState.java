package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.GameObject;
import com.zerulus.game.entity.Enemy;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.Camera;
import com.zerulus.game.ui.Button;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class EditState extends GameState {

    private BufferedImage imgButton;
    private Button enemy1;
    private boolean clicked = false;

    private GameObject gameObject;
    private PlayState ps;
    private Camera cam;

    public EditState(GameStateManager gsm, Camera cam) {
        super(gsm);
        imgButton = GameStateManager.ui.getSprite(0, 0, 128, 64);
        this.ps = (PlayState) gsm.getState(GameStateManager.PLAY);
        this.cam = cam;

        enemy1 = new Button("enemy1", new Vector2f(64 + 24, 64 + 24), 32, 24, imgButton, new Vector2f(64, 64), 200, 75);
        enemy1.addEvent(e -> {
            gameObject = new Enemy(cam, new Sprite("entity/enemy/littlegirl.png", 48, 48), new Vector2f(0 + (GamePanel.width / 2) - 32 + 150, 0 + (GamePanel.height / 2) - 32 + 150), 64);
        });
    }

    @Override
    public void update(double time) {

    }

    @Override
    public void input(MouseHandler mouse, KeyHandler key) {
        enemy1.input(mouse, key);
        if(mouse.getButton() == 1 && !clicked && gameObject != null) {

            gameObject.setPos(new Vector2f(mouse.getX() - gameObject.getSize() / 2 + cam.getBounds().getPos().x, mouse.getY() - gameObject.getSize() / 2 + cam.getBounds().getPos().y));
            if(!ps.getGameObjects().contains(gameObject)) {
                ps.getGameObjects().add(gameObject);
            }
            
            clicked = true;
        } else if(mouse.getButton() == -1) {
            clicked = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        enemy1.render(g);
    }
}