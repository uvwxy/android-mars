package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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

	private Paint paint = new Paint();

	int scaled_cube_img_width;
	int scaled_cube_img_height;
	int scaled_cube_img_diag_pixels;

	int onscreen_cube_width;
	int onscreen_cube_height;

	Paint black = new Paint();

	public MarsRenderer(Context context, int res_id, int cube_diag_pixels, int radius) {
		Options opts = new Options();
		opts.inScaled = false;
		base_image = BitmapFactory.decodeResource(context.getResources(), res_id, opts);
		cube_img_width = base_image.getWidth();
		cube_img_height = base_image.getHeight();
		this.cube_diag_pixels = cube_diag_pixels;
		this.radius = radius;
		black.setColor(Color.BLACK);
		black.setStyle(Style.FILL);
	}

	private int radius = 1;

	private int bmp_count = 0;

	public void render(Canvas canvas, Mars mars, MarsCamera camera) {
		canvas_width = canvas.getWidth();
		canvas_height = canvas.getHeight();
		canvas_center_x = canvas_width / 2;
		canvas_center_y = canvas_height / 2;

		camera = camera.copy();

		// TODO: fix this scaling to actual height!
		float s = (160.f / (camera.getZ() + 18));
		s *= 0.5f;

		scaled_cube_img_width = (int) (s * cube_img_width);
		scaled_cube_img_height = (int) (s * cube_img_height);
		scaled_cube_img_diag_pixels = (int) (s * cube_diag_pixels);

		canvas.drawRect(0, 0, canvas_width, canvas_height, black);

		float c_x = camera.getX() / mars.CHUNK_N;
		float c_y = camera.getY() / mars.CHUNK_N;

		for (int i = radius; i >= -radius; i--) {
			for (int j = radius; j >= -radius; j--) {
				renderChunk(canvas, camera, mars, mars.getChunk(new MarsChunkID((int) (c_x + i), (int) (c_y + j))), s);
			}
		}

		int radiusToKeep = 6;
		radiusToKeep++;
		// free memory from invisible chunks
		for (MarsChunk c : mars.getChunks().values()) {
			if (c.getScreen_data() != null
					&& (c.getX() < c_x - radius - radiusToKeep || c.getX() > c_x + radius + radiusToKeep
							|| c.getY() < c_y - radius - radiusToKeep || c.getY() > c_y + radius + radiusToKeep)) {
				c.clearScren_data();
			}
		}
	}

	private void renderChunk(Canvas canvas, MarsCamera camera, Mars mars, MarsChunk c, float data_scale_value) {
		// check if prerendered screen data exists
		if (c.getScreen_data() == null) {
			// if no: redraw (should produce lag)
			createChunkImageData(mars, data_scale_value, c);
		}

		// draw image data
		renderImageData(canvas, camera, mars, c, data_scale_value);
	}

	private void renderImageData(Canvas canvas, MarsCamera camera, Mars mars, MarsChunk c, float data_scale_value) {
		float x = c.getX() - camera.getX() / Mars.CHUNK_N;
		float y = c.getY() - camera.getY() / Mars.CHUNK_N;
		// scale according to camera height
		Matrix m = new Matrix();
		m.setScale(data_scale_value, data_scale_value);

		float tx = canvas.getWidth() / 2 - data_scale_value
				* (c.getScreen_data().getWidth() / 2 + (y - x) * c.getScreen_data().getWidth() / 2);
		float ty = canvas.getHeight() / 2 - data_scale_value * (y + x) * (cube_diag_pixels * Mars.CHUNK_N / 2)
				- c.getScreen_right_hand_box_height() * data_scale_value;
		ty += mars.getHeight(0, 0) * data_scale_value * cube_diag_pixels;
		m.postTranslate(tx, ty);
		canvas.drawBitmap(c.getScreen_data(), m, paint);
	}

	private void createChunkImageData(Mars mars, float data_scale_value, MarsChunk c) {
		// Log.i("MARS", "Creating image data for " + c.getX() + "/" + c.getY());

		int low_z = Integer.MAX_VALUE;
		int high_z = Integer.MIN_VALUE;
		int buf_z;

		// find lowest y
		// find highest y
		for (int x = c.getN() - 1; x >= 0; x--) {
			for (int y = c.getN() - 1; y >= 0; y--) {
				buf_z = getCubeScreenYAbsoluteHeight(x, y, c.getHeight(x, y));
				if (buf_z < low_z) {
					low_z = buf_z;
				}
				if (buf_z > high_z)
					high_z = buf_z;
			}
		}

		// Log.i("MARS", "high_y/low_y = " + high_z + "/" + low_z);

		int width = (int) (mars.CHUNK_N * cube_img_width);
		int height = high_z - low_z + cube_img_height;

		// Log.i("MARS", "image data size " + width + "/" + height);

		Bitmap ret = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas canvas = new Canvas(ret);

		if (PBMars.DEBUG) {
			Paint red = new Paint();
			red.setColor(Color.argb(25, (int) (Math.random() * 255), (int) (Math.random() * 255),
					(int) (Math.random() * 255)));
			canvas.drawRect(0, 0, ret.getWidth(), ret.getHeight(), red);
		}

		MarsCamera camera = new MarsCamera(0, 0, 2, 0);

		// draw single chunks
		for (int x = c.getN() - 1; x >= 0; x--) {
			for (int y = c.getN() - 1; y >= 0; y--) {
				renderCubeOnDataCanvas(canvas, x, y, c.getHeight(x, y), low_z - cube_img_height);
			}
		}
		int h = getCubeScreenYAbsoluteHeight(c.getN() - 1, 0, c.getHeight(c.getN() - 1, 0)) - low_z;
		Log.i("MARS", "h = " + h);
		c.setScreen_right_hand_box_height((int) ((high_z)));
		c.setScreen_data_scale_value(data_scale_value);
		c.setScreen_data(ret);
		bmp_count++;
		Log.i("MARS", "BMP COUNT = " + bmp_count);
	}

	private void renderCubeOnDataCanvas(Canvas canvas, int x, int y, int z, int low_z) {
		int ys = getCubeScreenYAbsoluteHeight(x, y, z) - low_z;
		// Log.i("MARS", "Render cube: " + x + "/" + y + "/" + z + "  --->  " + getCubeScreenYAbsoluteHeight(x, y, z));
		Matrix m = new Matrix();
		float xt = (cube_img_width / 2.0f + getScreenX(x, y, canvas.getWidth() / 2));
		float yt = (canvas.getHeight() - ys);
		m.postTranslate(xt, yt);
		canvas.drawBitmap(base_image, m, paint);

	}

	private int getScreenX(int x, int y, int canvas_center_x) {
		return canvas_center_x - cube_img_width + cube_img_width / 2 * (x - y);
	}

	private int getCubeScreenYAbsoluteHeight(int x, int y, int z) {
		return cube_img_height + (cube_img_height - cube_diag_pixels) * z + (cube_diag_pixels / 2) * (x + y);
	}

}
