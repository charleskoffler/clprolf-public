package org.clprolf.examples.games.snake_game.impl.system;

import java.util.Random;

import org.clprolf.examples.games.snake_game.impl.FoodExpertImpl;
import org.clprolf.examples.games.snake_game.impl.SnakeImpl;
import org.clprolf.framework.ClAgent;
import org.clprolf.examples.games.snake_game.interfaces.FoodExpert;
import org.clprolf.examples.games.snake_game.interfaces.Snake;
import org.clprolf.examples.games.snake_game.interfaces.system.SnakeGameScene;
import org.clprolf.examples.games.snake_game.workers.impl.SnakeGameSceneRendererImpl;
import org.clprolf.examples.games.snake_game.workers.interfaces.SnakeGameSceneRenderer;
import org.clprolf.framework.ClSystem;


//Not a pure abstraction, because it has a worker.
@ClSystem
public class SnakeGameSceneImpl implements SnakeGameScene {
	
	private SnakeGameSceneRenderer renderer;
	//Only a getter.
	public SnakeGameSceneRenderer getRenderer() {
		return renderer;
	}

	private Snake snake;
	public Snake getSnake() {
		return snake;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	private Snake snake_two;
	public Snake getSnake_two() {
		return snake_two;
	}

	public void setSnake_two(Snake snake_two) {
		this.snake_two = snake_two;
	}

	private FoodExpert foodExpert;
	public FoodExpert getFoodExpert() {
		return foodExpert;
	}
	private Random randomExpert;

	public Random getRandomExpert() {
		return randomExpert;
	}

	public void setFoodExpert(FoodExpert foodExpert) {
		this.foodExpert = foodExpert;
	}
	//

	public static int SCENE_ROWS_COUNT = 20;
	public static int SCENE_COLUMNS_COUNT = 40;
	
	//A simple class representing a location in the scene
	// public real_world_obj Place
	@ClAgent
	public static class Place {
		int x;
		int y;
		public Place(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public SnakeGameSceneImpl()  {
		//Create agent objects before the window.
		this.snake = new SnakeImpl(this, 0, "blue");
		this.snake_two = new SnakeImpl(this, 19, "grey");
		
		//Positionning the food
		this.foodExpert = new FoodExpertImpl(this);
		this.foodExpert.positionFood();
		//
		randomExpert = new Random();
		
		//Begin the display. Can be considered as a view.
		this.renderer = new SnakeGameSceneRendererImpl(this);
	}
	
	public boolean checkIfInSceneFrame(int x, int y) {
		return x>=0 && x<SnakeGameSceneImpl.SCENE_COLUMNS_COUNT && y>=0 && y<SnakeGameSceneImpl.SCENE_ROWS_COUNT;
	}
	
}
