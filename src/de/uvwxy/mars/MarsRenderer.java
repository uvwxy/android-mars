package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public class MarsRenderer {
	private Bitmap base_image = null;
	private Context context = null;

	private int cube_img_width;
	private int cube_img_height;
	private int cube_diag_pixels;

	private int canvas_width;
	private int canvas_height;
	private int canvas_center_x;
	private int canvas_center_y;

	private Matrix matrix_camera = new Matrix();
	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();

	int scaled_cube_img_width;
	int scaled_cube_img_height;
	int scaled_cube_img_diag_pixels;

	int onscreen_cube_width;
	int onscreen_cube_height;

	Paint black = new Paint();

	public MarsRenderer(Context context, int res_id, int cube_diag_pixels) {
		base_image = BitmapFactory.decodeResource(context.getResources(), res_id);
		cube_img_width = base_image.getWidth();
		cube_img_height = base_image.getHeight();
		this.cube_diag_pixels = cube_diag_pixels;

		black.setColor(Color.BLACK);
		black.setStyle(Style.FILL);
	}

	int radius = 1;

	public void render(Canvas canvas, Mars mars, MarsCamera camera) {
		canvas_width = canvas.getWidth();
		canvas_height = canvas.getHeight();
		canvas_center_x = canvas_width / 2;
		canvas_center_y = canvas_height / 2;

		float s = (1.0f / (camera.getZ()));

		matrix_camera.setScale(s, s, cube_img_width * s, cube_img_height * s);

		scaled_cube_img_width = (int) (s * cube_img_width);
		scaled_cube_img_height = (int) (s * cube_img_height);
		scaled_cube_img_diag_pixels = (int) (s * cube_diag_pixels);

		canvas.drawRect(0, 0, canvas_width, canvas_height, black);

		// for (int x = radius; x >= -radius; x--) {
		// for (int y = radius; y >= -radius; y--) {
		// drawChunk(
		// mars.getChunk(new MarsChunkID(x + camera.getX() / Mars.CHUNK_N, y + camera.getY()
		// / Mars.CHUNK_N)), canvas, camera, mars);
		// }
		// }

		renderChunk(canvas, camera, mars, mars.getChunk(new MarsChunkID(0, 0)));
		// renderCube(canvas, 0, 0, 0, camera, mars);
		// renderOneSquare(canvas, mars, camera);
	}

	// public void renderTestSquares(Canvas canvas, Mars mars, MarsCamera camera) {
	// renderCube(canvas, 0, 1, -1, camera, mars);
	// renderCube(canvas, 1, 0, -1, camera, mars);
	// renderCube(canvas, -1, 1, -1, camera, mars);
	// renderCube(canvas, 1, -1, -1, camera, mars);
	// renderCube(canvas, -1, 0, -1, camera, mars);
	// renderCube(canvas, 0, -1, -1, camera, mars);
	// renderCube(canvas, -1, -1, -1, camera, mars);
	//
	// renderCube(canvas, 0, 0, 0, camera, mars);
	// }

	private void renderChunk(Canvas canvas, MarsCamera camera, Mars mars, MarsChunk c) {
		double s = 1.0 / camera.getZ();
		// check if prerendered screen data exists
		if (c.getScreen_data() == null || c.getScreen_data_scale_value() != s) {
			// if no: redraw (should produce lag)
			createChunkImageData(mars, s, c);
		}

		// draw image data
		renderImageData(canvas, camera, mars, c);
	}

	private void renderImageData(Canvas canvas, MarsCamera camera, Mars mars, MarsChunk c) {
		int z = -mars.getHeight(camera.getX(), camera.getY());
		Matrix m = new Matrix(matrix_camera);
		setMatrixTo(c.getX() - camera.getX() / mars.CHUNK_N, c.getY() - camera.getY() / mars.CHUNK_N, z);
		m.postConcat(matrix);
		// canvas.drawBitmap(c.getScreen_data(), m, paint);
		canvas.drawBitmap(c.getScreen_data(), canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
	}

	private void createChunkImageData(Mars mars, double data_scale_value, MarsChunk c) {
		Log.i("MARS", "Creating image data for " + c.getX() + "/" + c.getY());
		// calculate screen data size:
		// width: n * cube_img_width <- no scaling here yet, can be done later (1.0f / (camera.getZ())
		// closest zoom in distance is 1:1 pixels.
		// height:

		int low_y = Integer.MAX_VALUE;
		int high_y = Integer.MIN_VALUE;
		int buf_y;

		// find lowest y
		// find highest y
		for (int x = c.getN() - 1; x >= 0; x--) {
			for (int y = c.getN() - 1; y >= 0; y--) {
				buf_y = c.getHeight(x, y);
				if (buf_y < low_y)
					low_y = buf_y;
				if (buf_y > high_y)
					high_y = buf_y;
			}
		}

		Log.i("MARS", "high_y/low_y = " + high_y + "/" + low_y);

		int width = mars.CHUNK_N * cube_img_width;
		// determine how many maximum stacked cubes
		int height = low_y < 0 ? high_y + low_y : high_y - low_y;
		// multiply with cube height in pixels
		height *= (cube_img_height - cube_diag_pixels);
		// add cube total height in pixels
		height += (mars.CHUNK_N) * cube_diag_pixels;

		Log.i("MARS", "image data size " + width + "/" + height);

		Bitmap ret = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(ret);
		Paint red = new Paint();
		red.setColor(Color.RED);
		canvas.drawRect(0, 0, ret.getWidth(), ret.getHeight(), red);
		MarsCamera camera = new MarsCamera(0, 0, 2, 0);

		// draw single chunks
		for (int x = c.getN() - 1; x >= 0; x--) {
			for (int y = c.getN() - 1; y >= 0; y--) {
				renderCubeOnData(canvas, x, y, c.getHeight(x, y), camera, mars);
			}
		}

		c.setScreen_data_scale_value(data_scale_value);
		c.setScreen_data(ret);
	}

	private void renderCubeOnData(Canvas canvas, int x, int y, int z, MarsCamera camera, Mars mars) {
		// Log.i("MARS", "Render cube: " + x + "/" + y + "/" + z);
		z -= mars.getHeight(camera.getX(), camera.getY());
		Matrix m = new Matrix();
		m.postTranslate(cube_img_width / 2 + getScreenX(x, y, z, canvas.getWidth() / 2),
				getScreenY(x, y, z, canvas.getHeight()));
		canvas.drawBitmap(base_image, m, paint);
		// Paint p = new Paint();
		// p.setColor(Color.RED);

	}

	private int getScreenX(int x, int y, int z, int canvas_center_x) {
		return canvas_center_x - cube_img_width + cube_img_width / 2 * (x - y);
	}

	private int getScreenY(int x, int y, int z, int canvas_center_y) {
		z *= -1;
		return canvas_center_y - cube_img_height + (cube_img_height - cube_diag_pixels) * z - (cube_diag_pixels / 2)
				* (x + y);
	}

	private int getScreenXScaled(int x, int y, int z) {
		return canvas_center_x - scaled_cube_img_width + scaled_cube_img_width / 2 * (x - y);
	}

	private int getScreenYScaled(int x, int y, int z) {
		z *= -1;
		return canvas_center_y - scaled_cube_img_height + (scaled_cube_img_height - scaled_cube_img_diag_pixels) * z
				- (scaled_cube_img_diag_pixels / 2) * (x + y);
	}

	private void setMatrixTo(int x, int y, int z) {
		matrix.setTranslate(getScreenXScaled(x, y, z), getScreenYScaled(x, y, z));
	}

	private void calculateVisibleChunks() {
		// TODO
	}

	// private void drawChunk(MarsChunk c, Canvas canvas, MarsCamera camera, Mars mars) {
	// for (int x = c.getN() - 1; x >= 0; x--) {
	// for (int y = c.getN() - 1; y >= 0; y--) {
	// renderCube(canvas, x + c.getN() * c.getX() - camera.getX(), y + c.getN() * c.getY() - camera.getY(),
	// c.getHeight(x, y), camera, mars);
	// }
	// }
	// }
}
