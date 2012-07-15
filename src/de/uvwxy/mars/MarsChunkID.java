package de.uvwxy.mars;

public class MarsChunkID {
	private int x;
	private int y;

	public MarsChunkID(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof MarsChunkID))
			return false;

		return (((MarsChunkID) o).getX() == x) && (((MarsChunkID) o).getY() == y);
	}
}
