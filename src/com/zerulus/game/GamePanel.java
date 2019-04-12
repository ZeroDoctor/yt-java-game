package com.zerulus.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

import com.zerulus.game.graphics.Screen;
import com.zerulus.game.states.GameStateManager;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.KeyHandler;


public class GamePanel extends JPanel implements Runnable {

    public static final long serialVersionUID = 1L;

    public static int width;
    public static int height;
    public static int oldFrameCount;
    public static int oldTickCount;
    public static int tickCount;

    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private BufferedImage img;

    private MouseHandler mouse;
    private KeyHandler key;

    private GameStateManager gsm;
    
    private Screen screen;
    private int[] pixels;

    public GamePanel(BufferStrategy bs, int width, int height) {
        GamePanel.width = width;
        GamePanel.height = height;
        this.bs = bs;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void init() {
        running = true;

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        //g = (Graphics2D) img.getGraphics();

        mouse = new MouseHandler(this);
        key = new KeyHandler(this);

        screen = new Screen(width, height);
        gsm = new GameStateManager();
        
    }

    public void run() {
        init();

        final double GAME_HERTZ = 64.0;
        final double TBU = 1000000000 / GAME_HERTZ; // Time Before Update

        final int MUBR = 3; // Must Update before render

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 1000;
        final double TTBR = 1000000000 / TARGET_FPS; // Total time before render

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        oldFrameCount = 0;

        tickCount = 0;
        oldTickCount = 0;

        while (running) {

            double now = System.nanoTime();
            int updateCount = 0;
            while (((now - lastUpdateTime) > TBU) && (updateCount < MUBR)) {
                update(now);
                input(mouse, key);
                lastUpdateTime += TBU;
                updateCount++;
                tickCount++;
                // (^^^^) We use this varible for the soul purpose of displaying it
            }

            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            input(mouse, key);
            render();
            draw();
            
            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }

                if (tickCount != oldTickCount) {
                    System.out.println("NEW SECOND (T) " + thisSecond + " " + tickCount);
                    oldTickCount = tickCount;
                }
                tickCount = 0;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TTBR && now - lastUpdateTime < TBU) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println("ERROR: yielding thread");
                }

                now = System.nanoTime();
            }

        }
    }

    public void update(double time) {
        gsm.update(time);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        gsm.input(mouse, key);
    }

    public void render() {
            screen.clear();
            //g.setColor(new Color(33, 30, 39));
            //g.fillRect(0, 0, width, height);

            gsm.render(screen);

            for(int i = 0; i < pixels.length; i++) {
                pixels[i] = screen.pixels[i];
            }
        
    }

    public void draw() {
        do {
            Graphics g2 = (Graphics) bs.getDrawGraphics();
            g2.drawImage(img, 8, 31, width, height, null);
            g2.dispose();
            bs.show();
        } while(bs.contentsLost());
        
    }

}
