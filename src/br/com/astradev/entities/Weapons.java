package br.com.astradev.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;

public class Weapons {		

	public static BufferedImage BOW_EN = Game.spritesheet.getSprite(16*9, 0, 16, 16);
	public static BufferedImage BOW_RIGHT = Game.spritesheet.getSprite(16*9, 0, 16, 16);
	public static BufferedImage BOW_LEFT = Game.spritesheet.getSprite(16*9, 16, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;	
	private int maskx, masky, maskw, maskh;
	
	public Weapons(int x, int y, int width, int height, BufferedImage sprite) {
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
	
	public static boolean isColidding(Entities e1, Weapons w2) {
		Rectangle eMask = new Rectangle(e1.getX() + Game.player.mask_playerx, e1.getY() + Game.player.mask_playery,
				Game.player.mask_playerw, Game.player.mask_playerh);
		Rectangle wMask = new Rectangle(w2.getX() + w2.maskx, w2.getY() + w2.masky, w2.maskw, w2.maskh);
		return eMask.intersects(wMask);
	}
	
	public void render(Graphics g) {		
		g.drawImage(sprite, this.getX()- Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.CYAN);
		//g.fillRect(this.getX()- Camera.x + maskx, this.getY() - Camera.y + masky, maskw, maskh);
	}
}