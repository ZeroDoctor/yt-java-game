package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.Enemy;
import com.zerulus.game.entity.Player;
import com.zerulus.game.graphics.Font;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.tiles.TileManager;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.Camera;
import com.zerulus.game.util.AABB;
import java.awt.Graphics2D;

public class PlayState extends GameState {

	private Font font;
	private Player player;
	private Enemy enemy;
	private TileManager tm;
	private Camera cam;

	public static Vector2f map;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		map = new Vector2f();
		Vector2f.setWorldVar(map.x, map.y);
		
		cam = new Camera(new AABB(new Vector2f(0, 0), GamePanel.width + 64, GamePanel.height + 64));

		tm = new TileManager("tile/tilemap.xml", cam);

		enemy = new Enemy(cam, new Sprite("entity/littlegirl.png", 48, 48), new Vector2f(0 + (GamePanel.width / 2) - 32 + 150, 0 + (GamePanel.height / 2) - 32 + 150), 64);
		player = new Player(cam, new Sprite("entity/linkFormatted.png"), new Vector2f(0 + (GamePanel.width / 2) - 32, 0 + (GamePanel.height / 2) - 32), 64);
		cam.target(player);
	}

	public void update(double time) {
		Vector2f.setWorldVar(map.x, map.y);
		if(!gsm.getState(GameStateManager.PAUSE)) {
			player.update(enemy, time);
			enemy.update(player);
			cam.update();
		}
		
	}

	public void input(MouseHandler mouse, KeyHandler key) {
		key.escape.tick();

		if(!gsm.getState(GameStateManager.PAUSE)) {
			player.input(mouse, key);
			cam.input(mouse, key);
		}
		if (key.escape.clicked) {
			if(gsm.getState(GameStateManager.PAUSE)) {
				gsm.pop(GameStateManager.PAUSE);
			} else {
				gsm.add(GameStateManager.PAUSE);
			}
        }
	}

	public void render(Graphics2D g) {
		tm.render(g);
		String fps = GamePanel.oldFrameCount + " FPS";
		Sprite.drawArray(g, fps, new Vector2f(GamePanel.width - fps.length() * 32, 32), 32, 24);

		String tps = GamePanel.oldTickCount + " TPS";
		Sprite.drawArray(g, tps, new Vector2f(GamePanel.width - tps.length() * 32, 64), 32, 24);
		
		player.render(g);
		enemy.render(g);
		cam.render(g);
	}
}
