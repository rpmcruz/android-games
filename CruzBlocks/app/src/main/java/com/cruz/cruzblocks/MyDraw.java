package com.cruz.cruzblocks;

// Tetris - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com>

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyDraw
{
	public Paint blocks[], board;

	public MyDraw() {
		blocks = new Paint[7];
		for(int i = 0; i < 7; i++) {
			blocks[i] = new Paint();
			blocks[i].setStyle(Paint.Style.FILL);
		}
		blocks[0].setColor(Color.CYAN);  // I
		blocks[1].setColor(Color.BLUE);  // J
		blocks[2].setColor(Color.rgb(255, 128, 0));  // L
		blocks[3].setColor(Color.YELLOW);  // O
		blocks[4].setColor(Color.GREEN);  // S
		blocks[5].setColor(Color.rgb(128, 0, 255));  // T
		blocks[6].setColor(Color.RED);  // Z
		board = new Paint();
		board.setStyle(Paint.Style.FILL_AND_STROKE);
		board.setColor(Color.WHITE);
	}

	public void drawBoard(Canvas c, Board board, boolean speedchanged) {
		c.drawColor(speedchanged ? Color.WHITE : Color.BLACK);
		for(int y = 0; y < Board.HEIGHT; y++)
			for(int x = 0; x < Board.WIDTH; x++) {
				byte type = board.get(x, y);
				if(type > 0)
					c.drawRect(x, y, x+1, y+1, this.board);
			}
	}

	public void drawSquare(Canvas c, int type, int x, int y) {
		c.drawRect(x+0.05f, y+0.05f, x+1-0.05f, y+1-0.05f, blocks[type-1]);
		//c.drawRect(x+0.05f, y, x+1-0.05f, y+1, border);
	}

	public void drawBlock(Canvas c, Block block, int x, int y) {
		for(int _y = 0; _y < 4; _y++)
			for(int _x = 0; _x < 4; _x++)
				if(block.hasInv(_x, _y))
					drawSquare(c, block.type+1, x+_x, y-_y);
	}

	public void drawNext(Canvas c, Block next) {
		c.drawRect(Board.WIDTH+1, 0, Board.WIDTH+5, 5, board);
		drawBlock(c, next, 11, 3);
	}

	public void draw(Canvas c, MyData data, boolean speedchanged) {
		drawBoard(c, data.board, speedchanged);
		c.drawLine(Board.WIDTH,0,Board.WIDTH,Board.HEIGHT,board);
		drawBlock(c, data.block, data.bx, data.by);
		drawNext(c, data.next);
	}
}
