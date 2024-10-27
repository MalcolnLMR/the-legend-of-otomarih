package br.com.astradev.entities;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entities {		

	public static BufferedImage RED_FLOWER_EN = Game.spritesheet.getSprite(16*2, 16*2, 16, 16);
	public static BufferedImage RED_FLOWER_FEEDBACK = Game.spritesheet.getSprite(16*2, 16*4, 16, 16);
	public static BufferedImage YELLOW_FLOWER_EN = Game.spritesheet.getSprite(16*5, 16*2, 16, 16);
	public static BufferedImage POISON_TREE_EN = Game.spritesheet.getSprite(16*2, 16*3, 16, 16);
	public static BufferedImage PURPLE_PLANT_EN = Game.spritesheet.getSprite(16*5, 16*3, 16, 16);
	public static BufferedImage TREE_EN = Game.spritesheet.getSprite(16, 0, 16, 16);	
	public static BufferedImage ARROWSHOOT_EN = Game.spritesheet.getSprite(16*8, 16, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected int z;
	
	private BufferedImage sprite;	
	private int maskx, masky, maskw, maskh;
	
	public Entities(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;		
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	public int getY() {
		return (int)this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isColidding(Entities e1, Entities e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw, e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw, e2.maskh);
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) return true;
		return false;		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX()- Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.CYAN);
		//g.fillRect(this.getX()- Camera.x + maskx, this.getY() - Camera.y + masky, maskw, maskh);
	}
}
