package com.cruz.cruzsolitaire;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

/**
 * Created by rick2 on 21-06-2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private static Dealer dealer;  // setting static because GamePanel may be re-created at any time
	private float zoom;

	public GamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		setWillNotDraw(false);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(dealer == null) {  // HACK: but apparently SurfaceView can be re-created at any time
			Card.emptyBorder = new Paint();
			Card.emptyBorder.setStyle(Paint.Style.STROKE);
			Card.emptyBorder.setColor(Color.rgb(0, 97, 0));
			Card.emptyBg = new Paint();
			Card.emptyBg.setStyle(Paint.Style.FILL);
			Card.emptyBg.setColor(Color.rgb(0, 210, 0));
			Card.backBorder = new Paint();
			Card.backBorder.setStyle(Paint.Style.STROKE);
			Card.backBorder.setColor(Color.rgb(110, 65, 110));
			Card.backBg = new Paint();
			Card.backBg.setStyle(Paint.Style.FILL);
			Card.backBg.setColor(Color.rgb(255, 150, 245));
			Card.cardBorder = new Paint();
			Card.cardBorder.setStyle(Paint.Style.STROKE);
			Card.cardBorder.setColor(Color.BLACK);
			Card.cardBg = new Paint();
			Card.cardBg.setStyle(Paint.Style.FILL);
			Card.cardBg.setColor(Color.rgb(240, 240, 240));
			Card.blackText = new Paint(Paint.LINEAR_TEXT_FLAG);  // LINEAR_TEXT_FLAG is a test
			Card.blackText.setStyle(Paint.Style.FILL);
			Card.blackText.setColor(Color.BLACK);
			Card.blackText.setTextSize(8);
			Card.blackText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			Card.redText = new Paint(Paint.LINEAR_TEXT_FLAG);  // LINEAR_TEXT_FLAG is a test
			Card.redText.setStyle(Paint.Style.FILL);
			Card.redText.setColor(Color.RED);
			Card.redText.setTextSize(8);
			Card.redText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

			dealer = new Dealer();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		final int minHeight = 2+4+Card.HEIGHT+6*DeckBoard.YPAD;
		zoom = Math.min(width/(float)((Card.WIDTH+4)*7), height/(float)minHeight);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.scale(zoom, zoom);
		dealer.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float x = ev.getX()/zoom;
		float y = ev.getY()/zoom;
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(dealer.down(x, y)) {
					performClick();
					invalidate();
				}
				return true;
			case MotionEvent.ACTION_MOVE:
				if(dealer.move(x, y))
					invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				if(dealer.up(x, y)) {
					performClick();
					invalidate();

					final int maxDeck = dealer.getMaxDeckSize();
					int maxCards = (int)((getHeight()/zoom-2-4-Card.HEIGHT)/DeckBoard.YPAD);
					//System.out.format("max cards: %d - max deck: %d\n", maxCards, maxDeck);
					if(maxCards < maxDeck) {
						if(maxDeck-maxCards == 1)
							maxCards = maxDeck+1;
						else
							maxCards = maxDeck;
						final int minHeight = 2+4+Card.HEIGHT+maxCards*DeckBoard.YPAD;
						zoom = Math.min(getWidth()/((Card.WIDTH+4)*7), getHeight()/(float)minHeight);
					}

					if(dealer.hasWon())
						Game.showMessageDialog(getContext(), "Congratulations !", dealer.kudosMessage(), "Hurray!");
				}
				return true;
		}
		return false;
	}

	public void restart() {
		dealer = new Dealer();
		invalidate();
		final int minHeight = 2+4+Card.HEIGHT+6*DeckBoard.YPAD;
		zoom = Math.min(getWidth() / ((Card.WIDTH + 4) * 7), getHeight() / (float) minHeight);
	}
}
