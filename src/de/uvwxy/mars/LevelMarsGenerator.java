package de.uvwxy.mars;

import java.util.Random;

public class LevelMarsGenerator extends MarsGenerator{

	int height = 2;
	public LevelMarsGenerator(int seed) {
		super(seed);
		Random x = new Random();
		x.setSeed(seed);
	}
	
	public int getHeight(int x, int y) {
		return (int) (Math.random()*height);
	}

}
