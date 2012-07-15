package de.uvwxy.mars;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class Mars {
	private double NOISE_SCALE_MOUNTAINS = 32.0d;
	private double NOISE_HILLS_BELOW = 0.6;
	private double NOISE_SCALE_HILLS = 16.0d;
	private double NOISE_SCALE_HILLS_SIZE = 1.0d / 12.0d;
	private double NOISE_GRAVEL_BELOW = 0.4;
	private double NOISE_SCALE_GRAVEL = 2.0d;
	private double NOISE_SCALE_GRAVEL_SIZE = 1.0d / 64.0d;
	private int[] data;
	private int width, height;

	private int[] color_mars_one = { 163, 111, 72 };

	private boolean gen = false;
	Random r = new Random();

	private double noisyness = 0.2;
	private int limitHigh = 0;
	private int limitStart = 1;
	private int limitSteps = 2;
	private int smoothingSize = 2;

	int seed = 1337;

	public void init(int seed, int width, int height) {
		this.seed = seed;
		this.width = width;
		this.height = height;
		// this.WORLD_SIZE = width;
		data = new int[width * height];

		gen = true;

		Thread t = new Thread(noise_runnable);
		t.start();
	}

	public int[] getData() {
		return data;
	}

	Runnable noise_runnable = new Runnable() {

		@Override
		public void run() {
			Log.i("MARS", "GEN STARTED");
			noise(seed);
			Log.i("MARS", "GEN STOPPED");
		}

	};

	private void noise(int seed) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				data[x + y * width] = randomGray(seed, x, y);
				if (!gen)
					return;
			}
		}
	}

	private int randomGray(int seed, int x, int y) {

		double d = (ImprovedPerlin.noise(x / NOISE_SCALE_MOUNTAINS, y / NOISE_SCALE_MOUNTAINS, seed
				/ NOISE_SCALE_MOUNTAINS) + 1.0) / 2.0;
		d += (ImprovedPerlin.noise(x / NOISE_SCALE_HILLS, y / NOISE_SCALE_HILLS, seed / NOISE_SCALE_MOUNTAINS))
				* NOISE_SCALE_HILLS_SIZE;
		if (NOISE_GRAVEL_BELOW >= d)
			d += (ImprovedPerlin.noise(x / NOISE_SCALE_GRAVEL, y / NOISE_SCALE_GRAVEL, seed / NOISE_SCALE_MOUNTAINS))
					* NOISE_SCALE_GRAVEL_SIZE;
		return Color.argb(255, (int) (d * color_mars_one[0]), (int) (d * color_mars_one[1]),
				(int) (d * color_mars_one[2]));
	}

	public void stopGen() {
		gen = false;
	}

}
