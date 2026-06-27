package org.clprolf.examples.games.snake_game.workers.interfaces;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;
import org.clprolf.examples.games.snake_game.interfaces.Snake;
import org.clprolf.examples.games.snake_game.interfaces.system.SnakeGameScene;
import org.clprolf.examples.games.snake_game.interfaces.system.SnakeWindow;

// implements a Java interface
@ClWorker
@ClFamily
public interface SnakeGameSceneRenderer extends Runnable {
	public SnakeGameScene getScene();
	
	public SnakeWindow getWindow();

	public void run();

	public void reactToGameOver(Snake concernedSnake);
	
	public void doChangeSpeedGearEffect();
	
	public void doLinkAddingSound();
}
