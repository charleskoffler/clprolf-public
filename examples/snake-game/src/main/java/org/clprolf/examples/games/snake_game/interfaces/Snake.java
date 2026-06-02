package org.clprolf.examples.games.snake_game.interfaces;

import java.util.ArrayList;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;
import org.clprolf.examples.games.snake_game.impl.SnakeImpl.SlidingType;
import org.clprolf.examples.games.snake_game.impl.SnakeImpl.SnakeLink;

@Agent
@Family_interf
public interface Snake {
	public int getSpeed();
	
	public void setLastSlidingType(SlidingType lastSlidingType);

	public ArrayList<SnakeLink> getLinks();

	public void setLinks(ArrayList<SnakeLink> links);

	public String getColor();
	public void setColor(String color);
	
	public void makeSliding();
	
	public SnakeLink getLinkAt(int x, int y, Snake snake);
	
	public void increaseSpeed();

	public void decreaseSpeed();

	public void endLongActions();
}
