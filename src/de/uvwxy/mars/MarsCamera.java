package de.uvwxy.mars;

public class MarsCamera {
	private static final float MINIMUM_HEIGHT = 2;
	private static final float MAXIMUM_HEIGHT = 256;

	float x, y, z;

	// compass angles:
	// 0 = "up"
	// 90 = "right"
	// 180 = "down"
	// 270 = "left"
	double angle;

	public MarsCamera(float x, float y, float z, double angle) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void moveXBy(float diff) {
		x += diff;
	}

	public void moveYBy(float diff) {
		y += diff;
	}

	public void up() {
		z++;
	}

	public void down() {
		z--;
		z = z < MINIMUM_HEIGHT ? MINIMUM_HEIGHT : z;
	}

	public MarsCamera copy() {
		return new MarsCamera(x,y,z, angle);
	}
}
