package br.com.astradev.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.astradev.game.Game;

public class Tile {
	public static BufferedImage GRASS1_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage STONE1_FLOOR = Game.spritesheet.getSprite(0, 16, 16, 16);
	
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);		
	}

}
