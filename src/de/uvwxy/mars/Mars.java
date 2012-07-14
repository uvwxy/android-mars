package de.uvwxy.mars;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class Mars {
	private int[] data;
	private int width, height;

	private boolean gen = false;
	Random r = new Random();

	private double noisyness = 0.2;
	private int limitHigh = 16;
	private int limitStart = 1;
	private int limitSteps = 2;
	private int smoothingSize = 1;

	int seed = 1337;

	public void init(int seed, int width, int height) {
		this.seed = seed;
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
			noise(seed);
			Log.i("MARS", "GEN STOPPED");
		}

	};

	private void noise(int seed) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				data[x + y * width] = randomGray(seed, x - width / 2, y - height / 2);
				if (!gen)
					return;
			}
		}
	}

	private int randomGray(int seed, int x, int y) {
		double d = getGray(seed, x, y) + 0.1;

		for (int l = limitStart; l < limitHigh; l += limitSteps) {
			int hash  = 1;
			hash = hash * 17 + x;
			hash = hash * 31 + y;
			d *= getGray(seed - 1, hash/l, hash/l);
		}

		int rnd = ((d) * 255 > 255) ? 255 : (int) ((d) * 255);

		return Color.argb(255, rnd, rnd, rnd);
	}

	private int rnd255() {
		return (int) (Math.random() * 255);
	}

	private double getGray(long seed, int x, int y) {
		double ret = 0;
		double d = 0;
		double num = 0;
		for (int a = -smoothingSize; a < smoothingSize; a++) {
			for (int b = -smoothingSize; b < smoothingSize; b++) {
				d += getGrayHelper(seed, a + x, b + y);
				num++;
			}
		}
		ret = d / num;

		return ret;
	}

	private double fix(double f, double fix) {
		return f > fix ? f : fix;
	}

	private double getGrayHelper(long seed, int x, int y) {
		r.setSeed(x);
		long i = r.nextLong();
		r.setSeed(y * 542789 + x - y);
		long j = r.nextLong();
		r.setSeed(seed * i * j);
		double ret = r.nextDouble();
		// r.setSeed(y + seed);
		r.nextDouble();
		ret += r.nextDouble();
		return ret > 1.0 ? .9999 : ret;
	}

	public void stopGen() {
		gen = false;
	}

}
