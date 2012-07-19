package de.uvwxy.mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import de.uvwxy.paintbox.PaintBox;
import de.uvwxy.panzoom.PanZoomListener;
import de.uvwxy.panzoom.PanZoomResult;

public class PBMars extends PaintBox {
	public static boolean DEBUG = false;
	
	private Context context;
	private boolean initOnce = false;

	MarsGenerator gen = null;
	Mars mars = null;
	MarsRenderer renderer = null;
	MarsCamera camera = null;

	private PanZoomListener panZoom = new PanZoomListener();
	private PanZoomResult panZoomResult = null;

	public PBMars(Context context) {
		super(context);
		this.context = context;
		setOnTouchListener(touchClickListener);
	}

	public PBMars(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOnTouchListener(touchClickListener);
	}

	public PBMars(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		setOnTouchListener(touchClickListener);
	}

	public void init() {
		if (DEBUG)
			gen = new LevelMarsGenerator((int) (Math.random()*1024));
		else
			gen = new MarsGenerator((int) (Math.random()*1024));
		mars = new Mars(gen, 8);
		// renderer = new MarsRenderer(context, R.drawable.mars_block_fixed,226);
//		 renderer = new MarsRenderer(context, R.drawable.mc_fixed, 228);
		renderer = new MarsRenderer(context, R.drawable.mc_fixed_tiny, 30);

		camera = new MarsCamera(0, 0, 2, 0);
		// TODO: regain some texture here:
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!initOnce) {
			init();
			initOnce = true;
		}

		renderer.render(canvas, mars, camera);
	}

	private OnTouchListener touchClickListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			panZoom.onTouch(v, event);
			panZoomResult = panZoom.getPanZoomResult();

			switch (panZoomResult.type) {
			case NONE:
				handleFingers(event);
				break;
			case PAN:
				camera.moveXBy((int) panZoomResult.x);
				camera.moveYBy((int) panZoomResult.y);
				break;
			case ZOOM:
				if (panZoomResult.scale < 1) {
					camera.up();
					Log.i("MARS", "CAMERA UP");
				} else {
					camera.down();
					Log.i("MARS", "CAMERA DOWN");
				}
				break;
			}

			return true;
		}
	};

	private void handleFingers(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN: // Finger 0
			setFingersDown(0);
			consumeFingerDown(0, event.getX(0), event.getY(0));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 256: // Finger 1
			setFingersDown(1);
			consumeFingerDown(1, event.getX(1), event.getY(1));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 512: // Finger 2
			setFingersDown(2);
			consumeFingerDown(2, event.getX(2), event.getY(2));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 768: // Finger 3
			setFingersDown(3);
			consumeFingerDown(3, event.getX(3), event.getY(3));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024: // Finger 4
			setFingersDown(4);
			consumeFingerDown(4, event.getX(4), event.getY(4));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024 + 256: // Finger 5
			setFingersDown(5);
			consumeFingerDown(5, event.getX(5), event.getY(5));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024 + 512: // Finger 6
			setFingersDown(6);
			consumeFingerDown(6, event.getX(6), event.getY(6));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024 + 768: // Finger 7
			setFingersDown(7);
			consumeFingerDown(7, event.getX(7), event.getY(7));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024 + 1024: // Finger 8
			setFingersDown(8);
			consumeFingerDown(8, event.getX(8), event.getY(8));
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN + 1024 + 1024 + 256: // 9
			setFingersDown(9);
			consumeFingerDown(9, event.getX(9), event.getY(9));
			break;
		case MotionEvent.ACTION_UP:
			Log.i("REMIND", "Fingers cleared");
			// all fingers gone, reset mask
			for (int i = 0; i < fingerDown.length; i++) {
				fingerDown[i] = false;
				fingerUsed[i] = false;
			}
			break;
		}
	}

	boolean[] fingerDown = new boolean[10];
	boolean[] fingerUsed = new boolean[10];

	private void setFingersDown(int f) {
		Log.i("REMIND", "Fingers down: " + f);
		for (int i = 0; i < f; i++) {
			fingerDown[i] = true;
		}
	}

	private void consumeFingerDown(int index, float x, float y) {
		if (!fingerUsed[index]) {
			fingerUsed[index] = true;
			// pbMars.init((int)(x*y));
		}
	}
}
