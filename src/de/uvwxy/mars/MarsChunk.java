package de.uvwxy.mars;

import java.io.Serializable;

public class MarsChunk implements Serializable {
	private static final long serialVersionUID = 3530480190643342044L;

	private final int n;
	private final MarsChunkID id;

	private int[][] heightmap;
	private int[][] object_data;

	/**
	 * Initialize chunk with n*n size, with id (x,y)
	 * 
	 * @param n
	 * @param x
	 * @param y
	 */
	public MarsChunk(MarsGenerator gen, int n, MarsChunkID id) {
		this.n = n;
		this.id = new MarsChunkID(id.getX(), id.getY());
		heightmap = new int[n][n];
		object_data = new int[n][n];

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				object_data[x][y] = 0;
				heightmap[x][y] = gen.getHeight(x + n * id.getX(), y + n * id.getY());
			}
		}
	}

	public MarsChunkID getId() {
		return id;
	}

	public int getX() {
		return id.getX();
	}

	public int getY() {
		return id.getY();
	}

	public int getN() {
		return n;
	}

	@Override
	public boolean equals(Object m) {
		if (m == null)
			return false;

		if (!(m instanceof MarsChunk))
			return false;

		return (id.equals(((MarsChunk) m).getId())) && (((MarsChunk) m).getN() == n);
	}
}
