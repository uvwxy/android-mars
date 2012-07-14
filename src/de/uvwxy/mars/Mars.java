package de.uvwxy.mars;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class Mars {
	private int[] data;
	private int width, height;

	private boolean gen = false;

	public void init(int width, int height) {
		this.width = width;
		this.height = height;

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
			noise();
			Log.i("MARS", "GEN STOPPED");
		}

	};

	private void noise() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				data[x + y * width] = randomGray(x, y);
				if (!gen)
					return;
			}
		}
	}

	private int randomGray(int x, int y) {
		int rnd = getGray(1337, x, y);
		return Color.argb(255, rnd, rnd, rnd);
	}

	private int rnd255() {
		return (int) (Math.random() * 255);
	}

	private int getGray(long seed, int x, int y) {
		int ret = 0;

		Random r = new Random();

		double d = 0;

		for (int a = 0; a < 3; a++) {
			for (int b = 0; b < 3; b++) {
				r.setSeed(seed + x - 1 + a + y - 1 + b);
				d += r.nextDouble();
			}
		}

		ret = (int) (d * 255.0 / 9);

		return ret;
	}

	public void stopGen() {
		gen = false;

	}
}
