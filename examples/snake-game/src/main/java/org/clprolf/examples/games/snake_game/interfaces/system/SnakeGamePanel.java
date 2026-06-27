package org.clprolf.examples.games.snake_game.interfaces.system;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClSystem;

import java.awt.Graphics;

import javax.swing.JLabel;

@ClSystem
@ClFamily
public interface SnakeGamePanel {
	
	public JLabel getLblVictory();

	public void paintComponent(Graphics g);

}
