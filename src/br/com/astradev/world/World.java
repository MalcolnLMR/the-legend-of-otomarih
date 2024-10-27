package br.com.astradev.world;

import br.com.astradev.entities.*;
import br.com.astradev.game.Game;
import br.com.astradev.graficos.Sprite_sheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class World {
	
	private static Tile[] tiles;
	private static Entities[] entities;
	private static int vida;
	public static int WIDTH, HEIGHT; 
	public static final int TILE_SIZE = 16;
	
	
	public World(String path) {
		try {
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			entities = new Entities[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {				
				for(int yy = 0; yy < map.getHeight(); yy++) {	
					
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.GRASS1_FLOOR);
					
					if(pixels[xx + (yy*map.getWidth())]== 0xFF000000) {
						//Grass1
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.GRASS1_FLOOR);
					}else if(pixels[xx + (yy*map.getWidth())]== 0xFF00FF21) {
						//Stone
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.STONE1_FLOOR);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFFFFFFFF) {
						//Trees	
						/*
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TREE_EN);
						*/
						entities[xx + (yy * WIDTH)] = new Staticentities(xx*16, yy*16, 16, 16,Entities.TREE_EN);
						Game.entities.add(entities[xx + (yy * WIDTH)]);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFF0026FF) {
						//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF0000) {
						//enemy
						Enemy en = new Enemy(xx*16, yy*16, 16, 16,Entities.RED_FLOWER_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF7F7F) {
						//Life
						Life pack = new Life(xx*16, yy*16, 16, 16, Items.LIFE_EN);
						pack.setMask(2, 5, 12, 11);
						Game.items.add(pack);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFFFFD800) {
						//ammo
						Ammo ammo = new Ammo(xx*16, yy*16, 16, 16,Items.AMMO_EN);
						ammo.setMask(2, 4, 12, 12);
						Game.items.add(ammo);
					}else if(pixels[xx + (yy*map.getWidth())] == 0xFF00FFFF) {
						//Bow
						Bow bow = new Bow(xx*16, yy*16, 16, 16,Weapons.BOW_EN);
						bow.setMask(5, 1, 8, 15);
						Game.weapons.add(bow);
					}if(pixels[xx + (yy*map.getWidth())] == 0xFFB200FF) {
						//Save Statue
						Savestatue saveStatue1 = new Savestatue(xx*16, yy*16, 16, 16,Savestatue.SAVESTATUE_EN);
						saveStatue1.setMask(1, 1, 14, 14);
						Game.saveS.add(saveStatue1);
					}
				}
				
			}
 		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext/TILE_SIZE;
		int y1 = ynext/TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y2 = ynext/TILE_SIZE;
		
		int x3 = xnext/TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1)/TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1)/TILE_SIZE;		
		
		if(!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
			     (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile))) {
			return true;
		}		
		if(Player.z > 0) return true;		
		return false;
	}
	
	public static boolean isFree_EN(int xnext, int ynext) {
		int x1 = xnext/TILE_SIZE;
		int y1 = ynext/TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y2 = ynext/TILE_SIZE;
		
		int x3 = xnext/TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1)/TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1)/TILE_SIZE;		
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
			     (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));		
	}
	
	public static boolean isStaticEntityFree(int xnext, int ynext) {
		int x1 = xnext/TILE_SIZE;
		int y1 = ynext/TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y2 = ynext/TILE_SIZE;
		
		int x3 = xnext/TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1)/TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1)/TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1)/TILE_SIZE;
		
		return !((entities[x1 + (y1*World.WIDTH)] instanceof Staticentities) ||
				 (entities[x2 + (y2*World.WIDTH)] instanceof Staticentities) ||
			     (entities[x3 + (y3*World.WIDTH)] instanceof Staticentities) ||
				 (entities[x4 + (y4*World.WIDTH)] instanceof Staticentities));		
	}
	
	public static void nextLevel(String level) {
		vida = Player.life;
		Game.entities = new ArrayList<Entities>();
		Game.enemies = new ArrayList<Enemy>();
		Game.items = new ArrayList<Items>();
		Game.weapons = new ArrayList<Weapons>();
		Game.spritesheet = new Sprite_sheet("/Spritesheet.png");
		Game.player = new Player(0, 0, 16, 16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		Player.hasGun = true;
		Player.life = vida;
		return;
	}
	public static void restartGame(String level) {		
		Game.entities = new ArrayList<Entities>();
		Game.enemies = new ArrayList<Enemy>();
		Game.items = new ArrayList<Items>();
		Game.weapons = new ArrayList<Weapons>();
		Game.spritesheet = new Sprite_sheet("/Spritesheet.png");	
		Game.player = new Player(0, 0, 16, 16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		Player.life = 20;
		return;
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4) + 3;
		int yfinal = ystart + (Game.HEIGHT >> 4) + 3;
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) continue; 
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
				/*
				Entities e = entities[xx + (yy*WIDTH)];
				e.render(g);
				*/
			}
		}
		
	}	

}
	
