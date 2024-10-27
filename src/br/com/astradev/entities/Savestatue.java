package br.com.astradev.entities;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Savestatue {	

	public static BufferedImage SAVESTATUE_EN = Game.spritesheet.getSprite(0, 16*3, 16, 16);
	
	int x;
	int y;
	int z = 0;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;	
	private BufferedImage[] sprites;
	private int maskx, masky, maskw, maskh;
	
	public Savestatue(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		sprites = new BufferedImage[5];
		sprites[0] = Game.spritesheet.getSprite(0, 16*3, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16, 16*3, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(0, 16*4, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(16, 16*4, 16, 16);
		sprites[4] = Game.spritesheet.getSprite(0, 16*5, 16, 16);
	}		

	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;		
	}
	
	public static boolean isColidding(Savestatue e1, Player e2) {
		Rectangle e1Mask = new Rectangle(e1.x + e1.maskx, e1.y + e1.masky, e1.maskw, e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), 16, 16);
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) return true;
		return false;		
	}
	
	public void tick(){
		if(isColidding(null, null)) {
			
		}
	}


	public void render(Graphics g) {
		g.drawImage(sprite, this.x - Camera.x, this.y - Camera.y, null);
		
		//g.setColor(Color.CYAN);
		//g.fillRect(this.getX()- Camera.x + maskx, this.getY() - Camera.y + masky, maskw, maskh);
	}
}
