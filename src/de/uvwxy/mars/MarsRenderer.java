package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;

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

	Paint black = new Paint();

	public MarsRenderer(Context context, int res_id, int cube_diag_pixels) {
		base_image = BitmapFactory.decodeResource(context.getResources(), res_id);
		cube_img_width = base_image.getWidth();
		cube_img_height = base_image.getHeight();
		this.cube_diag_pixels = cube_diag_pixels;

		black.setColor(Color.BLACK);
		black.setStyle(Style.FILL);
	}

	int radius = 5;
	
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
		
		for (int x = radius; x >= -radius; x--) {
			for (int y = radius; y >= -radius; y--) {
				drawChunk(
						mars.getChunk(new MarsChunkID(x + camera.getX() / Mars.CHUNK_N, y + camera.getY()
								/ Mars.CHUNK_N)), canvas, camera, mars);
			}
		}

		renderCube(canvas, 0, 0, 0, camera, mars);
		// renderOneSquare(canvas, mars, camera);
	}

	public void renderOneSquare(Canvas canvas, Mars mars, MarsCamera camera) {
		renderCube(canvas, 0, 1, -1, camera, mars);
		renderCube(canvas, 1, 0, -1, camera, mars);
		renderCube(canvas, -1, 1, -1, camera, mars);
		renderCube(canvas, 1, -1, -1, camera, mars);
		renderCube(canvas, -1, 0, -1, camera, mars);
		renderCube(canvas, 0, -1, -1, camera, mars);
		renderCube(canvas, -1, -1, -1, camera, mars);

		renderCube(canvas, 0, 0, 0, camera, mars);
	}

	private void renderCube(Canvas canvas, int x, int y, int z, MarsCamera camera, Mars mars) {
		z -= mars.getHeight(camera.getX(), camera.getY());
		Matrix m = new Matrix(matrix_camera);
		setMatrixTo(x, y, z, camera);
		m.postConcat(matrix);
		canvas.drawBitmap(base_image, m, paint);
	}

	private void setMatrixTo(int x, int y, int z, MarsCamera camera) {
		z *= -1;
		int x_t = canvas_center_x - scaled_cube_img_width + scaled_cube_img_width / 2 * (x - y);
		int y_t = canvas_center_y - scaled_cube_img_height + (scaled_cube_img_height - scaled_cube_img_diag_pixels) * z
				- (scaled_cube_img_diag_pixels / 2) * (x + y);

		matrix.setTranslate(x_t, y_t);
	}

	private void calculateVisibleChunks() {
		// TODO
	}

	private void drawChunk(MarsChunk c, Canvas canvas, MarsCamera camera, Mars mars) {
		for (int x = c.getN() - 1; x >= 0; x--) {
			for (int y = c.getN() - 1; y >= 0; y--) {
				renderCube(canvas, x + c.getN() * c.getX() - camera.getX(), y + c.getN() * c.getY() - camera.getY(),
						c.getHeight(x, y), camera, mars);
			}
		}
	}
}
