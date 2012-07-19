package de.uvwxy.mars;

import java.util.Random;

public class LevelMarsGenerator extends MarsGenerator{

	int height = 0;
	public LevelMarsGenerator(int seed) {
		super(seed);
		Random x = new Random();
		x.setSeed(seed);
		height = (int)(64*x.nextDouble());
	}
	
	public int getHeight(int x, int y) {
		return height;
	}

}
