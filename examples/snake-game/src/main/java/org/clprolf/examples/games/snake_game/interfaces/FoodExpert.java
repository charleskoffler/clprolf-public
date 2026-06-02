package org.clprolf.examples.games.snake_game.interfaces;

import java.util.ArrayList;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;
import org.clprolf.examples.games.snake_game.model.Food;

@Agent
@Family_interf
public interface FoodExpert {
	public SnakeGameScene getScene();

	public void setScene(SnakeGameScene scene);

	public ArrayList<Food> getFoodList();
	
	public void setFoodList(ArrayList<Food> foodList);
	
	public void positionFood();
	
	public Food getFoodAt(int x, int y);
}
