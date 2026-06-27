package org.clprolf.examples.games.snake_game.interfaces;

import java.util.ArrayList;

import org.clprolf.examples.games.snake_game.interfaces.system.SnakeGameScene;
import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.examples.games.snake_game.model.Food;

@ClAgent
@ClFamily
public interface FoodExpert {
	public SnakeGameScene getScene();

	public void setScene(SnakeGameScene scene);

	public ArrayList<Food> getFoodList();
	
	public void setFoodList(ArrayList<Food> foodList);
	
	public void positionFood();
	
	public Food getFoodAt(int x, int y);
}
