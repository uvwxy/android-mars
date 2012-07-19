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
	public static boolean DEBUG = true;

	private Context context;
	private boolean initOnce = false;

	MarsGenerator gen = null;
	Mars mars = null;
	MarsRenderer renderer_low = null;
	MarsRenderer renderer_med = null;
	MarsRenderer renderer_high = null;
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
			gen = new LevelMarsGenerator((int) (Math.random() * 1024));
		else
			gen = new MarsGenerator((int) (Math.random() * 1024));
		mars = new Mars(gen, 8);
		// renderer = new MarsRenderer(context, R.drawable.mars_block_fixed,226);
		renderer_high = new MarsRenderer(context, R.drawable.mc_fixed_high, 56, 1);
		// renderer = new MarsRenderer(context, R.drawable.mc_fixed_tiny, 30);
		renderer_low = new MarsRenderer(context, R.drawable.mc_fixed_nano, 8, 6);
		renderer_med = new MarsRenderer(context, R.drawable.mc_fixed_tiny, 30, 2);
		camera = new MarsCamera(0, 0, 2, 0);
		// TODO: regain some texture here:
	}

	int c = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		if (!initOnce) {
			init();
			initOnce = true;
		}

		switch (c) {
		case 0:
			renderer_high.render(canvas, mars, camera);
			break;
		case 1:
			renderer_med.render(canvas, mars, camera);
			break;
		case 2:
			renderer_low.render(canvas, mars, camera);

			break;
		}
	}

	int last_z = -1;
	int stage_0 = 20;
	int stage_1 = 60;

	private void switchRenderIfNeeded() {
		if (last_z == -1) {
			last_z = camera.getZ();
			return;
		}
		Log.i("MARS", "last_z = " + last_z + ", camera_z + " + camera.getZ());

		if (camera.getZ() > stage_0 && stage_0 <= last_z) {
			if (c != 1) {
				c = 1;
				mars.clearScreenData();
				Log.i("MARS", "set c = " + c);
				return;
			}
		}

		if (camera.getZ() > stage_1 && stage_1 <= last_z) {
			if (c != 2) {
				c = 2;
				mars.clearScreenData();
				Log.i("MARS", "set c = " + c);
				return;
			}
		}

		if (camera.getZ() < stage_1 && stage_1 >= last_z) {
			if (c != 1) {
				c = 1;
				mars.clearScreenData();
				Log.i("MARS", "set c = " + c);
				return;
			}
		}

		if (camera.getZ() < stage_0 && stage_0 >= last_z) {
			if (c != 0) {
				c = 0;
				mars.clearScreenData();
				Log.i("MARS", "set c = " + c);
				return;
			}
		}

		last_z = camera.getZ();
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
					switchRenderIfNeeded();
				} else {
					camera.down();
					switchRenderIfNeeded();
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
