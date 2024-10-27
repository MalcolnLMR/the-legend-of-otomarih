package br.com.astradev.entities;

import br.com.astradev.game.Game;
import br.com.astradev.world.Camera;
import br.com.astradev.world.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entities{
	
	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, rightUp_dir = 2, leftUp_dir = 3;
	public int dir = right_dir;	
	public double speed = 2;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private int damageFrames = 0;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightUpPlayer;
	private BufferedImage[] leftUpPlayer;
	private BufferedImage[] damagedPlayer;
	
	public static boolean hasGun = false, Bow = false;
	public boolean isDamaged = false;
	public boolean isShooting = false;
	public boolean jump = false, isJumping = false;
	public boolean jumpUp = false, jumpDown = false;
	public static int z = 0;
	
	public int jumpFrames = 10, jumpCur = 0;
	
	public int mask_playerx = 3, mask_playery = 3, mask_playerw = 10, mask_playerh = 12; 
	
	public static int life = 20, MAX_LIFE = 100;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);	
		
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		leftUpPlayer = new BufferedImage[3];
		rightUpPlayer = new BufferedImage[3];
		damagedPlayer = new BufferedImage[2];
		
		for(int i = 0; i < 3; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite((32 + (i*16)), 0, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite((32 + (i*16)), 16, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			leftUpPlayer[i] = Game.spritesheet.getSprite((79 + (i*16)), 16, 16, 16);
		}
		for(int i = 0; i < 3; i++) {
			rightUpPlayer[i] = Game.spritesheet.getSprite((79 + (i*16)), 0, 16, 16);
		}
		for(int i = 0; i < 2; i++) {
			damagedPlayer[i] = Game.spritesheet.getSprite((i*16), 16*2, 16, 16);
		}
		
	}	
	
	public void tick() {		
		moved = false;
		
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		if(isJumping == true) {
			if(jumpCur <= jumpFrames) {
				if(jumpUp)	jumpCur++;
				else if(jumpDown) {
					jumpCur--;
					if(jumpCur <= 0) {
						isJumping = false;
						jumpUp = false;
						jumpDown = false;
					}
				}
				z = jumpCur;
				if(jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
				}
			}
		}
		
		
		if(right && World.isFree((int)(x + speed), this.getY()) && World.isStaticEntityFree((int)(x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=this.speed;
			if(right && up) {
				moved = true;
				dir = rightUp_dir;
			}
		}
		if(left && World.isFree((int)(x - speed), this.getY()) && World.isStaticEntityFree((int)(x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=this.speed;
			
			if(left && up) {
				moved = true;
				dir = leftUp_dir;
			}
		}
		if(up && World.isFree(this.getX(), (int)(y - speed)) && World.isStaticEntityFree(this.getX(), (int)(y - speed))) {	
			moved = true;
			dir = rightUp_dir;
			y-=this.speed;
		}
		if(down && World.isFree(this.getX(), (int)(y + speed)) && World.isStaticEntityFree(this.getX(), (int)(y + speed))) {
			moved = true;
			dir = right_dir;
			y+=this.speed;
			if(left && down) {
				moved = true;
				dir = left_dir;
			}
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if (index == maxIndex) {
					index = 0;
				}
			}
		}else {
			index = 0;
		}
		
		checkCollisionLife();
		checkCollisionAmmo();
		checkCollisionWeapon();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 10) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		//ATIRAR PELO TECLADO
		/*
		if(isShooting) {
			isShooting = false;			
			if(hasGun && ammo > 0){
				ammo--;
				int dx = 0, dy = 0;
				if(Game.shootRight && Game.horizontal) {
					dx = 1;
					dy = 0;
				}else if(!Game.shootRight && Game.horizontal) {
					dx = -1;
					dy = 0;
				}
				if(Game.shootUp && Game.vertical) {
					dy = -1;
					dx = 0;
				}else if(!Game.shootUp && Game.vertical) {
					dy = 1;
					dx = 0;
				}
				Arrowshoot arrow = new Arrowshoot(this.getX(),this.getY(), 16, 16, Items.AMMO_EN, dx, dy);
				Game.arrowShoot.add(arrow);
			}
		}
		*/
		//ATIRAR PELO MOUSE
		if(isShooting) {
			isShooting = false;				
			if(hasGun && Game.ammo > 0){
				Game.ammo--;
				double angle = Math.atan2(Game.mouseY - (this.getY() - Camera.y), Game.mouseX - (this.getX() - Camera.x));
				double dx = Math.cos(angle);
			    double dy = Math.sin(angle);				
				Arrowshoot arrow = new Arrowshoot(this.getX(),this.getY(), 16, 16, Items.AMMO_EN, dx, dy);
				Game.arrowShoot.add(arrow);
			}
		}
		if(life <= 0) {
			life = 0;
			Game.gameState = "Game_Over";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
		
	}
	
	public void checkCollisionLife() {
		for(int i = 0; i < Game.items.size(); i++) {
			Items e = Game.items.get(i);
			if(e instanceof Life) {
				if(Items.isColidding(this,e)) {
					life += 10;
					if(life >= MAX_LIFE) life = MAX_LIFE;
					Game.items.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.items.size(); i++) {
			Items e = Game.items.get(i);
			if(e instanceof Ammo) {
				if(Items.isColidding(this,e)) {
					Game.ammo += 10;
					if(Game.ammo >= Game.MAX_AMMO) Game.ammo = Game.MAX_AMMO;
					Game.items.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.weapons.size(); i++) {
			Weapons e = Game.weapons.get(i);
			if(e instanceof Weapons) {
				if(Weapons.isColidding(this,e)) {
					hasGun = true;
					Bow = true;
					Game.weapons.remove(i);
					return;
				}
			}
		}
	}	
	
	
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
						g.drawImage(Weapons.BOW_RIGHT, this.getX() - Camera.x + 5, this.getY() - Camera.y - z, null);
					}	
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
						g.drawImage(Weapons.BOW_LEFT, this.getX() - Camera.x - 5, this.getY() - Camera.y - z, null);
					}				
				}
			}
			if(dir == rightUp_dir) {
					g.drawImage(rightUpPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
					if(hasGun) {
						if(Bow) {
							g.drawImage(Weapons.BOW_RIGHT, this.getX() - Camera.x + 5, this.getY() - Camera.y - z, null);
						}				
					}	
			}else if(dir == leftUp_dir) {
				g.drawImage(leftUpPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
							g.drawImage(Weapons.BOW_LEFT, this.getX() - Camera.x - 5, this.getY() - Camera.y - z, null);
					}		
				}
			}
		}else {
			if(dir == right_dir) {
				g.drawImage(damagedPlayer[1], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
						g.drawImage(Weapons.BOW_RIGHT, this.getX() - Camera.x + 5, this.getY() - Camera.y - z, null);
					}	
				}
			}else if(dir == left_dir) {
				g.drawImage(damagedPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
						g.drawImage(Weapons.BOW_LEFT, this.getX() - Camera.x - 5, this.getY() - Camera.y - z, null);
					}				
				}
			}
			if(dir == rightUp_dir) {
				g.drawImage(damagedPlayer[1], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {						
						g.drawImage(Weapons.BOW_RIGHT, this.getX() - Camera.x + 5, this.getY() - Camera.y - z, null);
					}				
				}	
			}else if(dir == leftUp_dir) {
				g.drawImage(damagedPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					if(Bow) {
						g.drawImage(Weapons.BOW_LEFT, this.getX() - Camera.x - 5, this.getY() - Camera.y - z, null);
					}		
				
				}
			}
		}
		
		
	}
}


