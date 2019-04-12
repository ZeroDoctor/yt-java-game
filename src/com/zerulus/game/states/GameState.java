package com.zerulus.game.states;

import com.zerulus.game.graphics.Screen;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;

public abstract class GameState {

    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public abstract void update(double time);
    public abstract void input(MouseHandler mouse, KeyHandler key);
    public abstract void render(Screen s);
}
