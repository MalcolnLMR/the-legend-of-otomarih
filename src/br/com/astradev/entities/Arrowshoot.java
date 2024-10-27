package br.com.astradev.entities;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;
import br.com.astradev.world.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Arrowshoot extends Entities{
	
	private double dx, dy;
	private double speed = 2;
	private int decayTime = 100, curDecay = 0;
	public static int arrowDamage = 5;
	
	
	public Arrowshoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		curDecay++;
		if(curDecay == decayTime) {
			Game.arrowShoot.remove(this);
		}		
		if(!World.isStaticEntityFree((int)(x+speed), (int)(y+speed)) || !World.isFree_EN((int)(x+speed), (int)(y+speed))) {
			this.destroySelf();				
		}
	}		
	
	public void destroySelf() {
		Game.arrowShoot.remove(this);
	}
	
	public void render(Graphics g) {
		g.drawImage(Entities.ARROWSHOOT_EN, this.getX()- Camera.x, this.getY() - Camera.y, null);
	}
	
}


