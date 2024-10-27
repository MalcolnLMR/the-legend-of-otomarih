package br.com.astradev.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.astradev.world.World;

public class Menu {
	
	public String[] options = {"Novo Jogo","Carregar Jogo","Salvar", "Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public static boolean saveExists = false, saveGame = false;
	
	public static boolean up, down, enter;
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
				case "level":
					World.restartGame("level"+spl2[1]+".png");
					Game.gameState = "Normal";					
					break;			
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];							
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
	 				}
				}catch(IOException e) {}
			}catch(FileNotFoundException e){}			
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			
		}
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int j = 0; j < value.length; j++) {
				value[j] += encode;
				current += value[j];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) write.newLine();
			}catch(IOException e){}
		}
		try {
			write.flush();
			write.close();			
		}catch(IOException e){}
	}
	
	public void tick() {
		File file = new File("Save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo") {				
				if(Game.currentLevel > Game.maxLevel) World.restartGame("0");
				else Game.gameState = "Normal";
				file = new File("save.txt");
				file.delete();
				
			} else if(options[currentOption] == "Sair") {				
				System.exit(1);
			}else if(options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
				
		}
	}
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("The Legend Of Otomarih", Game.WIDTH*Game.SCALE/2 - 220, 100);
		//op��es ae mano
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(Game.gameState == "Menu") {		
			g.drawString("Novo Jogo", Game.WIDTH*Game.SCALE/2 - 66, Game.HEIGHT*Game.SCALE/2 - 20);
			g.drawString("Sair", Game.WIDTH*Game.SCALE/2 - 26, Game.HEIGHT*Game.SCALE/2 + 40);
		}else if(Game.gameState == "Pause") {
			g.drawString("Continuar", Game.WIDTH*Game.SCALE/2 - 62, Game.HEIGHT*Game.SCALE/2 - 20);
			g.drawString("Salvar", Game.WIDTH*Game.SCALE/2 - 39, Game.HEIGHT*Game.SCALE/2 + 40);
			g.drawString("Sair", Game.WIDTH*Game.SCALE/2 - 26, Game.HEIGHT*Game.SCALE/2 + 70);
		}
		g.drawString("Carregar Jogo", Game.WIDTH*Game.SCALE/2 - 83, Game.HEIGHT*Game.SCALE/2 + 10);
		
		if(Game.currentLevel > Game.maxLevel) {
			g.drawString("Obrigado por jogar! sz", Game.WIDTH*Game.SCALE/2 - 135, Game.WIDTH*Game.SCALE - 250);
		}
		if(options[currentOption] == "Novo Jogo") {
			g.setColor(Color.red);
			g.drawString(">", Game.WIDTH*Game.SCALE/2 - 66 - 20, Game.HEIGHT*Game.SCALE/2 - 20);
		}else if(options[currentOption] == "Sair") {
			g.setColor(Color.red);
			if(Game.gameState == "Menu") {
				g.drawString(">", Game.WIDTH*Game.SCALE/2 - 26 - 20, Game.HEIGHT*Game.SCALE/2 + 40);
			}else if(Game.gameState == "Pause") {
				g.drawString(">", Game.WIDTH*Game.SCALE/2 - 26 - 20, Game.HEIGHT*Game.SCALE/2 + 70);
			}
		}else if(options[currentOption] == "Carregar Jogo") {
			g.setColor(Color.red);
			g.drawString(">", Game.WIDTH*Game.SCALE/2 - 83 - 20, Game.HEIGHT*Game.SCALE/2 + 10);
		}else if(options[currentOption] == "Salvar") {
			g.setColor(Color.red);
			g.drawString(">", Game.WIDTH*Game.SCALE/2 - 39 - 20, Game.HEIGHT*Game.SCALE/2 + 40);
		}
		
	}
}
