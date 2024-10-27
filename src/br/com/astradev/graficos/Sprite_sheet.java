package br.com.astradev.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite_sheet {
	
	private BufferedImage Spritesheet;
	
	public Sprite_sheet(String path) {
		try {
			Spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}		
		public BufferedImage getSprite(int x, int y, int width, int height){
			return Spritesheet.getSubimage(x, y, width, height);
		}
}