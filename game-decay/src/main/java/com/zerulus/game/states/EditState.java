package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.GameObject;
import com.zerulus.game.entity.enemy.TinyMon;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.util.Camera;
import com.zerulus.game.ui.Button;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class EditState extends GameState {

    private BufferedImage imgButton;
    private Button btnEnemy1;
    private Button btnEnemy2;
    private boolean clicked = false;

    private GameObject gameObject = null;
    private PlayState ps;
    private Camera cam;

    private int selection = 0;
    private GameObject e_enemy1;
    private GameObject e_enemy2;
    private GameObject[] entityList = {gameObject, e_enemy1, e_enemy2};


    public EditState(GameStateManager gsm, Camera cam) {
        super(gsm);
        imgButton = GameStateManager.ui.getSprite(0, 0, 128, 64).image;
        this.ps = (PlayState) gsm.getState(GameStateManager.PLAY);
        this.cam = cam;

        SpriteSheet enemySheet = new SpriteSheet("entity/enemy/minimonsters.png", 16, 16);

        btnEnemy1 = new Button("TinyMon", new Vector2f(64 + 24, 64 + 24), 32, 24, imgButton, new Vector2f(64, 64), 220, 75);
        btnEnemy1.addEvent(e -> {
            selection = 1;
            entityList[1] = new TinyMon(cam, new SpriteSheet(enemySheet.getSprite(0, 0, 128, 32), "tiny monster", 16, 16), 
							new Vector2f((GamePanel.width / 2) - 32 + 150, 0 + (GamePanel.height / 2) - 32 + 150), 48);
        });

        btnEnemy2 = new Button("TinyBoar", new Vector2f(64 + 24, (64 + 24) * 2), 32, 24, imgButton, new Vector2f(64, 64 + 85), 235, 75);
        btnEnemy2.addEvent(e -> {
            selection = 2;
            entityList[2] = new TinyMon(cam, new SpriteSheet(enemySheet.getSprite(0, 1, 128, 32), "tiny boar", 16, 16), 
							new Vector2f((GamePanel.width / 2) - 32 + 150, 0 + (GamePanel.height / 2) - 32 + 150), 48);
        });
    }

    @Override
    public void update(double time) {

    }

    @Override
    public void input(MouseHandler mouse, KeyHandler key) {
        btnEnemy1.input(mouse, key);
        btnEnemy2.input(mouse, key);
		
        if(mouse.getButton() == 1 && !clicked && entityList[selection] != null && !btnEnemy1.getHovering() && !btnEnemy2.getHovering()) {
            GameObject go = entityList[selection];
            go.setPos(new Vector2f(mouse.getX() - go.getSize() / 2 + cam.getPos().x + 64, 
                                    mouse.getY() - go.getSize() / 2 + cam.getPos().y + 64));

            if(!ps.getGameObjects().contains(go)) {
				ps.getGameObjects().add(go.getBounds().distance(ps.getPlayerPos()), go);
				ps.getAABBObjects().insert(go);
            }

            clicked = true;
        } else if(mouse.getButton() == -1) {
            clicked = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        btnEnemy1.render(g);
        btnEnemy2.render(g);
    }
}