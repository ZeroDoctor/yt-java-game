package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.graphics.Font;
import com.zerulus.game.graphics.Sprite;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {

    private GameState states[];

    public static Vector2f map;

    public static final int PLAY = 0;
    public static final int MENU = 1;
    public static final int PAUSE = 2;
    public static final int GAMEOVER = 3;

    public int onTopState = 0;

    public static Font font;
    public static Sprite ui;

    public GameStateManager() {
        map = new Vector2f(GamePanel.width, GamePanel.height);
        Vector2f.setWorldVar(map.x, map.y);

        states = new GameState[4];

        font = new Font("font/font.png",10, 10);
        ui = new Sprite("ui/ui.png", 64, 64);

        states[PLAY] = new PlayState(this);
    }

    public void pop(int state) {
        states[state] = null;
    }

    public void add(int state) {
        if (states[state] != null)
            return;

        if (state == PLAY) {
            states[PLAY] = new PlayState(this);
        }
        if (state == MENU) {
            states[MENU] = new MenuState(this);
        }
        if (state == PAUSE) {
            states[PAUSE] = new PauseState(this);
        }
        if (state == GAMEOVER) {
            states[GAMEOVER] =  new GameOverState(this);
        }

        onTopState = state;
    }

    public void addAndpop(int state) {
        addAndpop(state, 0);
    }

    public void addAndpop(int state, int remove) {
        pop(state);
        add(state);
    }

    public void update() {
        Vector2f.setWorldVar(map.x, map.y);

        for (int i = 0; i < states.length; i++) {
            if(states[i] != null)
                states[i].update();
        }
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        key.escape.tick();
        
        for (int i = 0; i < states.length; i++) {
            if(states[i] != null)
                states[i].input(mouse, key);
        }
        
        if (key.escape.clicked) {
            if(states[PAUSE] != null) {
                pop(GameStateManager.PAUSE);
            } else {
                add(GameStateManager.PAUSE);
            }
        }
    }

    public void render(Graphics2D g) {
        for (int i = 0; i < states.length; i++) {
            if(states[i] != null)
                states[i].render(g);
        }
    }

}
