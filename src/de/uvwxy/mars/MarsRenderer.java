package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

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

	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();

	public MarsRenderer(Context context, int res_id, int cube_diag_pixels) {
		base_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.mars_block_fixed);
		cube_img_width = base_image.getWidth();
		cube_img_height = base_image.getHeight();
		this.cube_diag_pixels = cube_diag_pixels;
	}

	public void render(Canvas canvas, Mars mars, MarsCamera camera) {
		canvas_width = canvas.getWidth();
		canvas_height = canvas.getHeight();
		canvas_center_x = canvas_width / 2;
		canvas_center_y = canvas_height / 2;

		renderOneSquare(canvas, mars, camera);
	}

	public void renderOneSquare(Canvas canvas, Mars mars, MarsCamera camera) {
		setMatrixTo(1, 1, 0);
		canvas.drawBitmap(base_image, matrix, paint);
		setMatrixTo(0, 0, 0);
		canvas.drawBitmap(base_image, matrix, paint);
		setMatrixTo(-1,-1, 0);
		canvas.drawBitmap(base_image, matrix, paint);
		setMatrixTo(1,-1, 0);
		canvas.drawBitmap(base_image, matrix, paint);
		setMatrixTo(-1,1, 0);
		canvas.drawBitmap(base_image, matrix, paint);
		paint.setColor(Color.WHITE);

		canvas.drawText("TEST", 20, 20, paint);
	}

	private void setMatrixTo(int x, int y, int z) {
		z*=-1;
		int x_t = canvas_center_x - cube_img_width / 2 + cube_img_width / 2 * (x-y);
		int y_t = canvas_center_y - cube_img_height / 2 + (cube_img_height-cube_diag_pixels) * z - (cube_diag_pixels/2) * (x+y);

		matrix.setTranslate(x_t, y_t);
	}
}
