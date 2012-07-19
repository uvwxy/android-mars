package de.uvwxy.mars;

import java.util.Random;

public class LevelMarsGenerator extends MarsGenerator {

	int height = 2;

	public LevelMarsGenerator(int seed) {
		super(seed);
		Random x = new Random();
		x.setSeed(seed);
	}

	public int getHeight(int x, int y) {
		x = Math.abs(x);
		y = Math.abs(y);
		int ret = 0;
		ret = 7 - (x+y)/2;
		ret = ret < 0 ? 0 : ret;
		return ret+32;
	}

}
