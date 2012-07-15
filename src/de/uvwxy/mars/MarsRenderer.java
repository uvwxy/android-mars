package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class MarsRenderer {
	private Bitmap base_image = null;
	private Context context = null;

	private int cube_img_width;
	private int cube_img_height;
	private int cube_diag_pixels;

	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();
	
	public MarsRenderer(Context context, int res_id, int cube_diag_pixels) {
		base_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.mars_block_fixed);
		cube_img_width = base_image.getWidth();
		cube_img_height = base_image.getHeight();
		this.cube_diag_pixels = cube_diag_pixels;
	}
	
	
	public void render(Canvas canvas, Mars mars, MarsCamera camera){
		
	}
	
	public void renderOneSquare(Canvas canvas, Mars mars, MarsCamera camera){
		matrix.setTranslate(canvas.getWidth()/2, canvas.getHeight()/2);
		canvas.drawBitmap(base_image, matrix, paint);
	}
}
