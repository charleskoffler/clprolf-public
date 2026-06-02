package org.clprolf.examples.games.snake_game.interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;
import org.clprolf.examples.games.snake_game.workers.interfaces.SnakeGameSceneRenderer;

//extends Java interfaces, and Runnable should be capacity interfaces with worker_agent_like advice!
@Agent
@Family_interf
public interface SnakeWindow extends Runnable, KeyListener{
	
		public SnakeGameSceneRenderer getRenderer();

		public SnakeGamePanel getGamePanel();

		public void setGamePanel(SnakeGamePanel globalPanel);
		public void setBlnContinue(boolean blnContinue);
		
		public boolean timePassed(int paintCycles);

		@Override
		public void run();
		
		@Override
		public void keyPressed(KeyEvent e);

		@Override
		public void keyReleased(KeyEvent e);
		
		@Override
		public void keyTyped(KeyEvent e);
}
