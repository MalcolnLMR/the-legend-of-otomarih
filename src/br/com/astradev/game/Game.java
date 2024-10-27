package br.com.astradev.game;

import br.com.astradev.entities.*;
import br.com.astradev.graficos.Sprite_sheet;
import br.com.astradev.graficos.UI;
import br.com.astradev.world.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public static int ammo = 0, MAX_AMMO = 100;
	
	private BufferedImage image;
	
	public static int currentLevel = 0, maxLevel = 2;
	
	public static List<Entities> entities;
	public static List<Enemy> enemies;
	public static List<Items> items;
	public static List<Weapons> weapons;
	public static List<Arrowshoot> arrowShoot;
	public static List<Savestatue> saveS;
	public static Sprite_sheet spritesheet;
	
	public static World world;
	public static Player player;	
	public static Random rand;
	public UI ui;
	public static String gameState = "Menu";	
	public static double mouseX, mouseY;
	public static int mx, my;
	public static boolean shootRight, horizontal;
	public static boolean shootUp, vertical;
	public Menu menu;	
	
	//public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	//public Font newfont;
	
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartButton = false;
	public boolean saveGame = false;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//carregar obj		
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entities>();
		enemies = new ArrayList<Enemy>();
		items = new ArrayList<Items>();
		weapons = new ArrayList<Weapons>();
		arrowShoot = new ArrayList<Arrowshoot>();
		saveS = new ArrayList<Savestatue>();		
		menu = new Menu();
		
		spritesheet = new Sprite_sheet("/Spritesheet.png");
		player = new Player(0, 0, 16, 16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level0.png");
		/*
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(40);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		}
	
	public void initFrame() {
		frame = new JFrame("The Legend Of Otomarih!");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();		
	}
	
    public synchronized void stop() {
    	isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}		
	}
    
	public static void main(String[] agrs) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {		
		if(gameState == "Normal") {	
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {Game.currentLevel};
				Menu.saveGame(opt1, opt2, 10);	
				System.out.println("Salvo");
			}
			this.restartButton = false;
			for(int i = 0; i < entities.size(); i++) {
				Entities e = entities.get(i);
				e.tick();
			}	
			for(int i = 0; i < items.size(); i++) {
				Items e = items.get(i);
				e.tick();
			}
			for(int i = 0; i < weapons.size(); i++) {
				Weapons e = weapons.get(i);
				e.tick();
			}
			for(int i = 0; i < arrowShoot.size(); i++) {
				Arrowshoot e = arrowShoot.get(i);
				e.tick();
			}
			if(enemies.size() == 0) {
				//Avan�ar n�vel
				currentLevel++;
				if(currentLevel > maxLevel) {
					Game.gameState = "Menu";
				}else {
					String newWorld = "level"+currentLevel+".png";
					World.nextLevel(newWorld);
				}				
			}
		}else if(gameState == "Game_Over") {
			this.framesGameOver++;
			if(this.framesGameOver == 50) {
				this.framesGameOver= 0;
				if(this.showMessageGameOver) this.showMessageGameOver = false;
				else this.showMessageGameOver= true;				
			}
			if(restartButton) {
				this.restartButton = false;
				Game.gameState = "Menu";
				currentLevel = 0;
				String newWorld = "level"+currentLevel+".png";
				World.restartGame(newWorld);				
			}
		}else if(gameState == "Menu") {
			menu.tick();
		}else if(gameState == "Pause") {
			menu.tick();
		}
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(40, 40, 40));
		g.fillRect(0, 0, WIDTH, HEIGHT);	
		world.render(g);
		for(int i = 0 ; i < entities.size(); i++) {
			Entities e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < items.size(); i++) {
			Items e = items.get(i);
			e.render(g);
		}
		for(int i = 0; i < weapons.size(); i++) {
			Weapons e = weapons.get(i);
			e.render(g);
		}
		for(int i = 0; i < arrowShoot.size(); i++) {
			Arrowshoot e = arrowShoot.get(i);
			e.render(g);
		}
		for(int i = 0; i < saveS.size(); i++) {
			Savestatue e = saveS.get(i);
			e.render(g);
		}
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 16));
		g.drawString("Muni��o: "+ Game.ammo, WIDTH*SCALE - 100, 30);
		if(gameState == "Game_Over") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 28));			
			g.setColor(new Color(255, 255, 255));			
			g.drawString("GAME OVER (  ._.)", WIDTH*SCALE / 2 - 99, HEIGHT*SCALE / 2 + 11);
			if(showMessageGameOver) {
				g.setColor(new Color(255, 255, 255));			
				g.drawString("Pressione Enter para Reiniciar", WIDTH*SCALE / 2 - 190, HEIGHT*SCALE / 2 + 50);
			}
		}else if(gameState == "Menu") {
			menu.render(g);
		}else if(gameState == "Pause") {
			menu.render(g);
		}
		/*
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(my - 225, mx - 225);		
		g2.rotate(angleMouse, 225, 225);
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 50);
		*/
		//Show
		bs.show();		
	}
	

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;		
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}			
			if(System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}		
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "Menu" || gameState == "Pause") Menu.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if(gameState == "Menu" || gameState == "Pause") Menu.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_E) {
			this.restartButton = true;
			if(gameState == "Menu" || gameState == "Pause") Menu.enter = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(gameState == "Menu" || gameState == "Pause") Menu.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(gameState == "Menu" || gameState == "Pause") Menu.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "Pause";
		}
		if(e.getKeyCode() == KeyEvent.VK_P) {
			if(Game.gameState == "Normal") {
				this.saveGame = true;
			}
			
		}
		
		//ATIRAR PELO TECLADO
		/*
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.isShooting = true;
			shootRight = true;
			horizontal = true;
			vertical = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.isShooting = true;
			shootRight = false;	
			horizontal = true;
			vertical = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			player.isShooting = true;
			shootUp = true;
			vertical = true;
			horizontal = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.isShooting = true;
			shootUp = false;
			vertical = true;
			horizontal = false;
		}
		*/
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}	
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = false;
		}
		//ATIRAR PELO TECLADO
		/*
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.isShooting = false;
			shootRight = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.isShooting = false;
			shootRight = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			player.isShooting = false;
			shootUp = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.isShooting = false;
			shootUp = false;
		}
		*/
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.isShooting = true;
		Game.mouseX = (e.getX() / Game.SCALE);
		Game.mouseY = (e.getY() / Game.SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}
}

