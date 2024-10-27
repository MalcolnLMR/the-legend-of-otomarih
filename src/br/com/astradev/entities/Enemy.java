package br.com.astradev.entities;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;
import br.com.astradev.world.World;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Entities{
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;	
	private double speed = 1;
	private int maskx = 4, masky = 3;
	private int maskw = 8, maskh = 11;
	private boolean moved = false;
	private int hitRate = 80, critRate = 1;
	private int hitDamageCap = 2, critDamageCap = 4;
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	//damage 1 ~ 2 //crit 2 ~ 4 
	
	private BufferedImage[] sprites;
	private BufferedImage[] spritesDamaged;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprite(16*2, 16*2, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16*3, 16*2, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(16*4, 16*2, 16, 16);
		spritesDamaged = new BufferedImage[3];
		spritesDamaged[0] = Game.spritesheet.getSprite(16*2, 16*4, 16, 16);
		spritesDamaged[1] = Game.spritesheet.getSprite(16*3, 16*4, 16, 16);
		spritesDamaged[2] = Game.spritesheet.getSprite(16*4, 16*4, 16, 16);
	}
	
	public void tick() {
		//if(Game.rand.nextInt(100) < 25) {}	
		this.moved = false;
		if(this.isColiddingWithPlayer() == false) {
			if(x < Game.player.getX() && World.isFree_EN((int)(x+speed), (int)(y)) && !isColidding((int)(x+speed), (int)(y))
					&& World.isStaticEntityFree((int)(x+speed), (int)(y))) {
				x+=speed;
				this.moved = true;
			}else if(x > Game.player.getX() && World.isFree_EN((int)(x-speed), (int)(y)) && !isColidding((int)(x-speed), (int)(y))
					&& World.isStaticEntityFree((int)(x-speed), (int)(y))) {
			x-=speed;
			this.moved = true;
			}
			if(y < Game.player.getY() && World.isFree_EN((int)(x), (int)(y+speed)) && !isColidding((int)(x), (int)(y+speed))
					&& World.isStaticEntityFree((int)(x), (int)(y+speed))) {
				y+=speed;
				this.moved = true;
			}else if(y > Game.player.getY() && World.isFree_EN((int)(x), (int)(y-speed)) && !isColidding((int)(x), (int)(y-speed)) 
					&& World.isStaticEntityFree((int)(x), (int)(y-speed))) {
				y-=speed;
				this.moved = true;
			}	
			if(this.moved) {
				frames++;
				if(frames == maxFrames) {
					frames = 0;
					index++;
					if (index == maxIndex) {
						index = 0;
					}
				}
			}
		}
		else {
			if(Game.rand.nextInt(100) > 100 - this.hitRate) {
				Player.life -= Game.rand.nextInt(this.hitDamageCap);
				Game.player.isDamaged = true;
				if(Game.rand.nextInt(100) > 100 - this.critRate) {
					Player.life -= Game.rand.nextInt((this.critDamageCap - this.hitDamageCap) + this.hitDamageCap);
					System.out.println("CRITICO");
				}
			}
		}
		collindingBullet();
		if(life <= 0) {
			destroySelf();
			return;
		}
		if(this.isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
				
	}
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);	
		Rectangle player = new Rectangle(Game.player.getX() + Game.player.mask_playerx, 
				Game.player.getY() + Game.player.mask_playery, Game.player.mask_playerw, Game.player.mask_playerh);
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);				
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}		
		return false;
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collindingBullet() {
		for(int i = 0; i < Game.arrowShoot.size(); i++) {
			Entities e = Game.arrowShoot.get(i);
			if(e instanceof Arrowshoot) {
				if(Entities.isColidding(this, e)) {
					this.isDamaged = true;
					life -= Arrowshoot.arrowDamage;
					Game.arrowShoot.remove(e);
					return;
				}
			}
		}
		return;
	}
	
	public void render(Graphics g) {
		if(!this.isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else {
			g.drawImage(spritesDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);		
		}
		
		/*
		super.render(g);
		g.setColor(Color.CYAN);
		g.fillRect(Game.player.getX() - Camera.x + Game.player.mask_playerx,
				Game.player.getY() - Camera.y + Game.player.mask_playery, Game.player.mask_playerw,Game.player.mask_playerh);
		g.fillRect(this.getX() - Camera.x + maskx, this.getY() - Camera.y + masky, maskw, maskh);
		*/

	}

}
