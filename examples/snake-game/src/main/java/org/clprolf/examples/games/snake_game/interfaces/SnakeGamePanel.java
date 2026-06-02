package org.clprolf.examples.games.snake_game.interfaces;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;

import java.awt.Graphics;

import javax.swing.JLabel;

@Agent
@Family_interf
public interface SnakeGamePanel {
	
	public JLabel getLblVictory();

	public void paintComponent(Graphics g);

}
