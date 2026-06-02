package org.clprolf.examples.games.snake_game.workers.impl;

import java.awt.EventQueue;

import org.clprolf.framework.Worker;
import org.clprolf.examples.games.snake_game.impl.SnakeWindowImpl;
import org.clprolf.examples.games.snake_game.interfaces.Snake;
import org.clprolf.examples.games.snake_game.interfaces.SnakeGameScene;
import org.clprolf.examples.games.snake_game.interfaces.SnakeWindow;
import org.clprolf.examples.games.snake_game.workers.interfaces.SnakeGameSceneRenderer;

@Worker
public class SnakeGameSceneRendererImpl implements SnakeGameSceneRenderer {

	private SnakeGameScene scene;

	public SnakeGameScene getScene() {
		return scene;
	}
	
	private SnakeWindow window;
	//Just a getter
	public SnakeWindow getWindow() {
		return window;
	}

	//

	public SnakeGameSceneRendererImpl(SnakeGameScene lifeScene) {
		this.scene = lifeScene;
		EventQueue.invokeLater(this);
	}
	
	// Just for the EventQueue.invokeLater().
	@Override
	public void run() {
			// Creation of the window using the dispatch thread.
			// The window will refresh itself his display, many times per seconds.
			window = new SnakeWindowImpl(this);
	}

	public void reactToGameOver(Snake concernedSnake) {
		this.window.setBlnContinue(false);
		String msg = new String("The " + concernedSnake.getColor() + " snake is loosing!");
		java.awt.Toolkit.getDefaultToolkit().beep();
		this.getWindow().getGamePanel().getLblVictory().setText(msg);
	}
	
	public void doChangeSpeedGearEffect() {
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	
	public void doLinkAddingSound() {
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
}
