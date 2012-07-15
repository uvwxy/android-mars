package de.uvwxy.mars;

public class MarsCamera {
	private static final int MINIMUM_HEIGHT = 2;
	private static final int MAXIMUM_HEIGHT = 256;

	int x, y, z;

	// compass angles:
	// 0 = "up"
	// 90 = "right"
	// 180 = "down"
	// 270 = "left"
	double angle;

	public MarsCamera(int x, int y, int z, double angle) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void moveXBy(int diff) {
		x += diff;
	}

	public void moveYBy(int diff) {
		y += diff;
	}

	public void up() {
		z++;
	}

	public void down() {
		z--;
		z = z < MINIMUM_HEIGHT ? MINIMUM_HEIGHT : z;
	}
}
