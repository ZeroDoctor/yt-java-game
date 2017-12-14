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
import java.awt.Graphics2D;

public class PlayState extends GameState {

	private Font font;
	private Player player;
	private Enemy enemy;
	private TileManager tm;

	public static Vector2f map;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		map = new Vector2f();
		Vector2f.setWorldVar(map.x, map.y);

		tm = new TileManager("tile/tilemap.xml");
		font = new Font("font/font.png", 10, 10);

		enemy = new Enemy(new Sprite("entity/littlegirl.png", 48, 48), new Vector2f(0 + (GamePanel.width / 2) - 32 + 150, 0 + (GamePanel.height / 2) - 32 + 150), 64);
		player = new Player(new Sprite("entity/linkFormatted.png"), new Vector2f(0 + (GamePanel.width / 2) - 32, 0 + (GamePanel.height / 2) - 32), 64);
	}

	public void update() {
		Vector2f.setWorldVar(map.x, map.y);
		player.update(enemy);
		enemy.update(player);
	}

	public void input(MouseHandler mouse, KeyHandler key) {
		player.input(mouse, key);
	}

	public void render(Graphics2D g) {
		tm.render(g);
		String fps = GamePanel.oldFrameCount + " FPS";
		Sprite.drawArray(g, font, fps, new Vector2f(GamePanel.width - fps.length() * 32, 32), 32, 24);

		String tps = GamePanel.oldTickCount + " TPS";
		Sprite.drawArray(g, tps, new Vector2f(GamePanel.width - tps.length() * 32, 64), 32, 24);
		
		player.render(g);
		enemy.render(g);
	}
}
