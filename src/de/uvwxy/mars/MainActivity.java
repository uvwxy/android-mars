package de.uvwxy.mars;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {
	private PBMars pbMars = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pbMars = (PBMars) findViewById(R.id.pbMars);
		pbMars.setOnTouchListener(touchClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		pbMars.stop();
	}

	private OnTouchListener touchClickListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
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

			return true;
		}
	};

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
			pbMars.init((int)(x*y));
		}
	}
}
