package org.clprolf.examples.games.snake_game.interfaces;

import java.util.Random;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.examples.games.snake_game.workers.interfaces.SnakeGameSceneRenderer;

@ClAgent
@ClFamily
public interface SnakeGameScene {
	
	public SnakeGameSceneRenderer getRenderer();

	public Snake getSnake();

	public void setSnake(Snake snake);

	public Snake getSnake_two();

	public void setSnake_two(Snake snake_two);

	public FoodExpert getFoodExpert();
	
	public Random getRandomExpert();

	public void setFoodExpert(FoodExpert foodExpert);
	//
	public boolean checkIfInSceneFrame(int x, int y);
	
}
