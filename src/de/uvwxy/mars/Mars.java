package de.uvwxy.mars;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class Mars {
	private int[] data;
	private int width, height;

	private boolean gen = false;
	Random r = new Random();

	private int smoothingSize = 10;
	private double smoothingDiv = (smoothingSize * 2 + 1.0) / 2.0;

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
				data[x + y * width] = randomGray(x - width / 2, y - height / 2);
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
		double d = 0;

		for (int a = -smoothingSize; a < smoothingSize; a++) {
			for (int b = -smoothingSize; b < smoothingSize; b++) {
				d += getGrayHelper(seed, a + x, b + y);
			}
		}

		ret = (int) (d * 255.0 / smoothingDiv);

		return ret;
	}

	private double getGrayHelper(long seed, int x, int y) {
		r.setSeed(seed + x - seed*y);
		double ret = r.nextDouble();
		//r.setSeed(y + seed);
		r.nextDouble();
		ret += r.nextDouble();
		return ret > 1.0 ? ret - 1.0 : ret;
	}

	public void stopGen() {
		gen = false;
	}
}
