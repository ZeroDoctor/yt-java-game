package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.GameObject;
import com.zerulus.game.entity.Enemy;
import com.zerulus.game.entity.Player;
import com.zerulus.game.graphics.Font;
import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.tiles.TileManager;
import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.util.Camera;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<GameObject> gameObject;
	private TileManager tm;
	private Camera cam;

	public static Vector2f map;

	public PlayState(GameStateManager gsm, Camera cam) {
		super(gsm);
		
		map = new Vector2f();
		Vector2f.setWorldVar(map.x, map.y);
		this.cam = cam;

		tm = new TileManager("tile/tilemap.xml", cam);

		gameObject = new ArrayList<GameObject>();
		player = new Player(cam, new Sprite("entity/wizardPlayer.png", 64, 64), new Vector2f(0 + (GamePanel.width / 2) - 32, 0 + (GamePanel.height / 2) - 32), 64);
		
		cam.target(player);
	}

	public ArrayList<GameObject> getGameObjects() { return gameObject; }

	public void update(double time) {
		Vector2f.setWorldVar(map.x, map.y);

		if(!gsm.isStateActive(GameStateManager.PAUSE)) {
			if(!gsm.isStateActive(GameStateManager.EDIT)) {
				for(int i = 0; i < gameObject.size(); i++) {
					if(gameObject.get(i) instanceof Enemy) {
						Enemy enemy = ((Enemy) gameObject.get(i));
						if(player.getHitBounds().collides(enemy.getBounds())) {
							player.setTargetEnemy(enemy);
						}

						if(enemy.getDeath()) {
							gameObject.remove(enemy);
						} else {
							enemy.update(player, time);
						}
					}

				}

				if(player.getDeath()) {
					gsm.add(GameStateManager.GAMEOVER);
					gsm.pop(GameStateManager.PLAY);
				}

				player.update(time);
			}

			cam.update();
		}
		
	}

	public void input(MouseHandler mouse, KeyHandler key) {
		key.escape.tick();
		key.f1.tick();

		if(!gsm.isStateActive(GameStateManager.PAUSE)) {
			if(cam.getTarget() == player) {
				player.input(mouse, key);
			}
			cam.input(mouse, key);

			if(key.f1.clicked) {
				if(gsm.isStateActive(GameStateManager.EDIT)) {
					gsm.pop(GameStateManager.EDIT);
					cam.target(player);
				} else {
					gsm.add(GameStateManager.EDIT);
					cam.target(null);
				}
			}
		} else if(gsm.isStateActive(GameStateManager.EDIT)) {
			gsm.pop(GameStateManager.EDIT);
			cam.target(player);
		}

		if (key.escape.clicked) {
			if(gsm.isStateActive(GameStateManager.PAUSE)) {
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
		for(int i = 0; i < gameObject.size(); i++) {
			gameObject.get(i).render(g);
		}
		cam.render(g);
	}
}
