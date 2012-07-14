package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import de.uvwxy.paintbox.PaintBox;

public class PBMars extends PaintBox {

	Bitmap p;
	private boolean initOnce = false;
	private Matrix matrix = new Matrix();
	private Paint paint = new Paint();

	Mars m = new Mars();
	
	public PBMars(Context context) {
		super(context);
	}

	public PBMars(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PBMars(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init() {
		p = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		m.init(getWidth(), getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!initOnce) {
			init();

			initOnce = true;
		}
		p.setPixels(m.getData(), 0, getWidth(), 0, 0, getWidth(), getHeight());
		canvas.drawBitmap(p, matrix, paint);

	}

	public void stop() {
		m.stopGen();
	}

}
