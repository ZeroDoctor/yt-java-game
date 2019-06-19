package com.zerulus.game.graphics;

import com.zerulus.game.graphics.Sprite;

public class Animation {

    private Sprite[] frames;
    private int[] states;
    private int currentFrame;
    private int numFrames;

    private int count;
    private int delay;

    private int timesPlayed;

    public Animation(Sprite[] frames) {
        setFrames(0, frames);
        timesPlayed = 0;
        states = new int[10];
    }

    public Animation() {
        timesPlayed = 0;
        states = new int[10];
    }

    public void setFrames(int state, Sprite[] frames) {
        this.frames = frames;
        currentFrame = 0;
        count = 0;
        timesPlayed = 0;
        delay = 2;
        if(states[state] == 0) {
            numFrames = frames.length;
        } else {
            numFrames = states[state];
        }
    }

    public void setDelay(int i) { delay = i; }
    public void setFrame(int i) { currentFrame = i; }
    public void setNumFrames(int i, int state) { states[state] = i; }

    public void update() {
        if(delay == -1) return;
        
        count++;

        if(count == delay) {
            currentFrame++;
            count = 0;
        }
        if(currentFrame == numFrames) {
            currentFrame = 0;
            timesPlayed++;
        }
    }

    public int getDelay() { return delay; }
    public int getFrame() { return currentFrame; }
    public int getCount() { return count; }
    public Sprite getImage() { return frames[currentFrame]; }
    public boolean hasPlayedOnce() { return timesPlayed > 0; }
    public boolean hasPlayed(int i) { return timesPlayed == i; }

}
