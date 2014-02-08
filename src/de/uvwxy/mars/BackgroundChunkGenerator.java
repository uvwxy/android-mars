package de.uvwxy.mars;

public class BackgroundChunkGenerator implements Runnable {
	int x, y, w, h;
	int[] data;
	private MarsGenerator land = null;

	public BackgroundChunkGenerator(MarsGenerator land) {
		this.land = land;
	}

	public void setParameters(int x, int y, int w, int h, int[] data) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.data = data;
	}

	@Override
	public void run() {
//		land.genChunk(x, y, w, h, data);
	}

}
