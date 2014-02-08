package de.uvwxy.mars;


public class MarsGenerator {

	public final int MAP_HEIGHT = 64;
	private final int MAP_N = 67108864; // bit more than a year to cross at 2 blocks/second
	
	private double NOISE_SCALE_MOUNTAINS = 128.0d;
	private double NOISE_CRATERS_BELOW = 0.7;
	private double NOISE_SCALE_CRATERS = 32.0d;
	private double NOISE_SCALE_CRATERS_SIZE = 1.0d / 12.0d;
	private double NOISE_GRAVEL_BELOW = 0.5;
	private double NOISE_SCALE_GRAVEL = 2.0d;
	private double NOISE_SCALE_GRAVEL_SIZE = 1.0d / 64.0d;

	int seed;

	public MarsGenerator(int seed) {
		this.seed = seed;
	}
	
	public int getHeight(int x, int y) {
		// get random mountain map [0,1]
		double d = (ImprovedPerlin.noise(x / NOISE_SCALE_MOUNTAINS, y / NOISE_SCALE_MOUNTAINS, seed
				/ NOISE_SCALE_MOUNTAINS) + 1.0) / 2.0;

		// add some craters
		if (NOISE_CRATERS_BELOW >= d)
			d -= ((ImprovedPerlin.noise(x / NOISE_SCALE_CRATERS, y / NOISE_SCALE_CRATERS, seed / NOISE_SCALE_MOUNTAINS) + 1) / 2.0)
					* NOISE_SCALE_CRATERS_SIZE;
		
		// add some noise/rockiness
		if (NOISE_GRAVEL_BELOW >= d)
			d += (ImprovedPerlin.noise(x / NOISE_SCALE_GRAVEL, y / NOISE_SCALE_GRAVEL, seed / NOISE_SCALE_MOUNTAINS))
					* NOISE_SCALE_GRAVEL_SIZE;
		
		// normalize value to [0,MAP_HEIGHT]
		return (int)(d*MAP_HEIGHT);
	}
}
