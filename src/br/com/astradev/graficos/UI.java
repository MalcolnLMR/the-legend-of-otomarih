package br.com.astradev.graficos;

import br.com.astradev.game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UI {
	
	private int barReduction = 40;
	
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(8 - 1, 8 - 1, (Game.player.MAX_LIFE*this.barReduction)/100 + 2, 8 + 2);
		g.setColor(Color.red);
		g.fillRect(8, 8, (Game.player.life*this.barReduction)/100, 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD, 8));
		g.drawString(Game.player.life+"/"+Game.player.MAX_LIFE, 8, 15);		
	}

}
