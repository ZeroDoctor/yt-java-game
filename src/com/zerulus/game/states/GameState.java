package com.zerulus.game.states;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;

import java.awt.Graphics2D;

public abstract class GameState {

    public GameState(GameStateManager gsm) {
        
    }

    public abstract void update();

    public abstract void input(MouseHandler mouse, KeyHandler key);

    public abstract void render(Graphics2D g);
}
