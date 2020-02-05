package com.zerulus.game.states;

import com.zerulus.game.GamePanel;
import com.zerulus.game.entity.Enemy;
import com.zerulus.game.entity.Player;
import com.zerulus.game.entity.material.Material;
import com.zerulus.game.entity.material.MaterialManager;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.tiles.TileManager;

import com.zerulus.game.util.KeyHandler;
import com.zerulus.game.util.MouseHandler;
import com.zerulus.game.util.Camera;
import com.zerulus.game.util.GameObjectHeap;
import com.zerulus.game.util.AABBTree;

import com.zerulus.game.ui.PlayerUI;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayState extends GameState {

	public Player player;
	private GameObjectHeap gameObject;
	private AABBTree aabbTree;
	private TileManager tm;
	private MaterialManager mm;
	private Camera cam;
	private PlayerUI pui;

	public static Vector2f map;
	private double heaptime;

	public PlayState(GameStateManager gsm, Camera cam) {
		super(gsm);
		
		map = new Vector2f();
		Vector2f.setWorldVar(map.x, map.y);
		this.cam = cam;

		tm = new TileManager("tile/tilemap.xml", cam);

		/*SpriteSheet tileset = new SpriteSheet("tile/overworldOP.png", 32, 32);
		SpriteSheet treeset = new SpriteSheet("material/trees.png", 64, 96);

		mm = new MaterialManager(64, 150);
		mm.setMaterial(MaterialManager.TYPE.TREE, treeset.getSprite(1, 0), 64);
		mm.setMaterial(MaterialManager.TYPE.TREE, treeset.getSprite(3, 0), 64);

		tm = new TileManager(tileset, 150, cam, mm);*/

		gameObject = new GameObjectHeap();
		//gameObject.addAll(mm.list);
		aabbTree = new AABBTree();

		player = new Player(cam, new SpriteSheet("entity/wizardPlayer.png", 64, 64), new Vector2f(0 + (GamePanel.width / 2) - 32, 0 + (GamePanel.height / 2) - 32), 64, tm);
		pui = new PlayerUI(player);
		aabbTree.insert(player);

		cam.target(player);

	}

	public GameObjectHeap getGameObjects() { return gameObject; }
	public AABBTree getAABBObjects() { return aabbTree; }
	public Vector2f getPlayerPos() { return player.getPos(); }

	private boolean canBuildHeap(int offset, int si, double time) {

		if(gameObject.size() > 3 && (heaptime / si) + offset < (time / si)) {
			return true;
		}

		return false;
	}

	public void update(double time) {
		Vector2f.setWorldVar(map.x, map.y);

		if(!gsm.isStateActive(GameStateManager.PAUSE)) {
			if(!gsm.isStateActive(GameStateManager.EDIT)) {


				//aabbTree.update(player);

				if(player.getDeath()) {
					gsm.add(GameStateManager.GAMEOVER);
					gsm.pop(GameStateManager.PLAY);
				}

				for(int i = 0; i < gameObject.size(); i++) {
					if(gameObject.get(i).go instanceof Enemy) {
						Enemy enemy = ((Enemy) gameObject.get(i).go);
						if(player.getHitBounds().collides(enemy.getBounds())) {
							player.setTargetEnemy(enemy);
						}

						if(enemy.getDeath()) {
							gameObject.remove(enemy);
						} else {
							enemy.update(player, time);
						}

						if(canBuildHeap(2500, 1000000, time)) {
							gameObject.get(i).value = enemy.getBounds().distance(player.getPos());
						}

						continue;
					}

					if(gameObject.get(i).go instanceof Material) {
						Material mat = ((Material) gameObject.get(i).go);
						if(player.getHitBounds().collides(mat.getBounds())) {
							player.setTargetGameObject(mat);
						}
					}
				}

				if(canBuildHeap(3, 1000000000, time)) {
					heaptime = System.nanoTime();
					gameObject.buildHeap();
					//System.out.println(gameObject);
				}
				
				player.update(time);
				pui.update(time);
			}
			cam.update();
		}
	}

	public void input(MouseHandler mouse, KeyHandler key) {
		key.escape.tick();
		key.f1.tick();
		key.enter.tick();

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

			if(key.enter.clicked) {
				System.out.println(aabbTree.toString());
				System.out.println(gameObject.toString());
			}

			pui.input(mouse, key);
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

		player.render(g);
		for(int i = 0; i < gameObject.size(); i++) {
			if(cam.getBounds().collides(gameObject.get(i).getBounds())) {
				gameObject.get(i).go.render(g);
			}
		}

		g.setColor(Color.white);

		String fps = GamePanel.oldFrameCount + " FPS";
		g.drawString(fps, GamePanel.width - 6 * 32, 32);

		String tps = GamePanel.oldTickCount + " TPS";
		g.drawString(tps, GamePanel.width - 6 * 32, 64);

		pui.render(g);

		cam.render(g);
	}
}
