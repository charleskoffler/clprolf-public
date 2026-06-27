package org.clprolf.examples.games.snake_game.workers.impl;

import org.clprolf.framework.ClWorker;
import org.clprolf.examples.games.snake_game.impl.system.SnakeGameSceneImpl;
import org.clprolf.examples.games.snake_game.interfaces.system.SnakeGameScene;

/**
 * A simple 2D Snake game with two players and two snakes.
 * The rendering is done with Swing, inside a JFrame.
 *
 * There are four speed gears.
 *
 * Controls:
 *  - Snake 1: Arrow keys to move, and comma/space to change speed.
 *  - Snake 2: Z/W for up/down, Q/S for left/right, and A/X to change speed.
 *
 * Possible improvements:
 *  - Add a scoring system and display it on the screen.
 *  - The winner is the one who doesn’t lose — unless all the food is eaten.
 *
 * Version: 20260602, 1.1
 * 
 * @author Charles Koffler
 *
 */

@ClWorker
public class SnakeGameLauncher {
	public static void main(String[] args) {
		//Just create a scene, and the game will start.
		SnakeGameScene scene = new SnakeGameSceneImpl();
	}
}
