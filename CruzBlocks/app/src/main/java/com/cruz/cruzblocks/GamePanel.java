package com.cruz.cruzblocks;

import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by rick2 on 21-06-2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private int speed = 0;

	private static MyData data;  // setting static because GamePanel may be re-created at any time
	private MyDraw draw = new MyDraw();
	private float ev_x, ev_y;
	private GameThread thread;

	public GamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		setWillNotDraw(false);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(data == null) {
			restart();
			thread = new GameThread(holder);
		}
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.done = false;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException ex) {}
		}
	}

	public void myDraw(Canvas canvas) {
		final float zoom = Math.min(canvas.getWidth() / (Board.WIDTH + 5), canvas.getHeight() / Board.HEIGHT);
		canvas.scale(zoom, zoom);
		draw.draw(canvas, data, thread.speedchanged);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(thread.kudos) {
			Game.showMessageDialog(getContext(), "Congratulations !", data.kudosMessage(), "Hurray!");
			thread.kudos = false;
		}

		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ev_x = ev.getX();
				ev_y = ev.getY();
				return true;
			case MotionEvent.ACTION_UP:
				if(ev.getY()-ev_y > 500)
					data.down();
				else if(ev_x-ev.getX() > 5)
					data.move(-1,0);
				else if(ev.getX()-ev_x > 5)
					data.move(+1,0);
				else
					data.rotate();
				performClick();
				invalidate();
				return true;
		}
		return false;
	}

	public void restart() {
		data = new MyData();
		data.nextType();
		if(thread != null)
			thread.done = false;
		thread = new GameThread(getHolder());
		invalidate();
	}

	private class GameThread extends Thread {
		private SurfaceHolder holder;
		private int ticks, speed = 5;
		public boolean done, kudos, speedchanged;

		public GameThread(SurfaceHolder holder) {
			this.holder = holder;
		}

		public void run() {
			done = false;
			while(!done) {
				final int delay = 250 + (9-speed) * 100;
				long t = System.currentTimeMillis();
				data.move(0, +1);

				if((++ticks) % 500 == 0 && speed < 10) {  // 1000=about every 4 mins
					speed++;
					speedchanged = true;
				}
				else
					speedchanged = false;

				if(data.board.isGameover()) {
					done = true;
					kudos = true;
					data.finish = System.currentTimeMillis();
				}

				// "invalidate()"
				SurfaceHolder holder = getHolder();
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();
					synchronized(holder) {
						myDraw(canvas);
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						holder.unlockCanvasAndPost(canvas);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}

				long dt = System.currentTimeMillis() - t;
				if(delay > dt)
					try {
						Thread.sleep(delay - dt);
					} catch(Exception ex) {
					}
			}
		}
	}
}
