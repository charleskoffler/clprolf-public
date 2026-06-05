package org.clprolf.examples.games.snake_game.interfaces;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;

import java.awt.Graphics;

import javax.swing.JLabel;

@ClAgent
@ClFamily
public interface SnakeGamePanel {
	
	public JLabel getLblVictory();

	public void paintComponent(Graphics g);

}
