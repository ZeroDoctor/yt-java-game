package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.math.AABB;
import com.zerulus.game.util.Camera;
import com.zerulus.game.graphics.Font;
import com.zerulus.game.graphics.Fontf;
import com.zerulus.game.graphics.SpriteSheet;

import java.awt.Graphics2D;

public class GameStateManager {

    private GameState states[];

    public static Vector2f map;

    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int GAMEOVER = 3;
    public static final int EDIT = 4;

    public static Font font;
    public static Fontf fontf;
    public static SpriteSheet ui;
    public static SpriteSheet button;
    public static Camera cam;
    public static Graphics2D g;

    public GameStateManager(Graphics2D g) {
        GameStateManager.g = g;
        map = new Vector2f(GamePanel.width, GamePanel.height);
        Vector2f.setWorldVar(map.x, map.y);

        states = new GameState[5];

        font = new Font("font/font.png", 10, 10);
        fontf = new Fontf();
        fontf.loadFont("font/Stackedpixel.ttf", "MeatMadness");
        fontf.loadFont("font/GravityBold8.ttf", "GravityBold8");
        SpriteSheet.currentFont = font;

        ui = new SpriteSheet("ui/ui.png", 64, 64);
        button = new SpriteSheet("ui/buttons.png", 122, 57);
        

        cam = new Camera(new AABB(new Vector2f(-64, -64), GamePanel.width + 128, GamePanel.height + 128));

        states[PLAY] = new PlayState(this, cam);
    }

    public boolean isStateActive(int state) {
        return states[state] != null;
    }

    public GameState getState(int state) {
        return states[state];
    }

    public void pop(int state) {
        states[state] = null;
    }

    public void add(int state) {
        if (states[state] != null)
            return;

        if (state == PLAY) {
            cam = new Camera(new AABB(new Vector2f(0, 0), GamePanel.width + 64, GamePanel.height + 64));
            states[PLAY] = new PlayState(this, cam);
        }
        else if (state == MENU) {
            states[MENU] = new MenuState(this);
        }
        else if (state == PAUSE) {
            states[PAUSE] = new PauseState(this);
        }
        else if (state == GAMEOVER) {
            states[GAMEOVER] = new GameOverState(this);
        }
        else if (state == EDIT) {
            if(states[PLAY] != null) {
                states[EDIT] = new EditState(this, cam);
            }
        }
    }

    public void addAndpop(int state) {
        addAndpop(state, 0);
    }

    public void addAndpop(int state, int remove) {
        pop(state);
        add(state);
    }

    public void update(double time) {
        for (int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].update(time);
            }
        }
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        
        for (int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].input(mouse, key);
            }
        }        
    }

    public void render(Graphics2D g) {
        g.setFont(GameStateManager.fontf.getFont("MeatMadness"));
        for (int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].render(g);
            }
        }
    }

}
