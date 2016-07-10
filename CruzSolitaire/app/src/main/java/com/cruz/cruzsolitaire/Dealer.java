package com.cruz.cruzsolitaire;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by rick2 on 21-06-2015.
 */
public class Dealer
{
	private DeckBoard decks[];
	private DeckPool pool;
	private DeckGoal goals[];
	private DeckFloat dfloat;
	private long start;

	public Dealer() {
		// shuffle cards
		Card cards[] = new Card[13*4];
		for(int i = 0; i < cards.length; i++)
			cards[i] = new Card(i%13, i/13);
		// Fisher–Yates shuffle algorithm
		Random gen = new Random();
		for(int i = cards.length-1; i >= 1; i--) {
			int j = gen.nextInt(i+1);
			Card t = cards[i];
			cards[i] = cards[j];
			cards[j] = t;
		}

		// give cards
		int n = 0;
		decks = new DeckBoard[7];
		for(int i = 0; i < 7; i++) {
			decks[i] = new DeckBoard(13 + i);
			for(int j = 0; j < i+1; j++)
				decks[i].add(cards[n+j]);
			n += i+1;
		}
		pool = new DeckPool(cards.length - n);
		for(int i = n; i < cards.length; i++)
			pool.add(cards[i]);
		goals = new DeckGoal[4];
		for(int i = 0; i < 4; i++)
			goals[i] = new DeckGoal();
		start = System.currentTimeMillis();
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(Color.GREEN);
		for(int i = 0; i < decks.length; i++)
			decks[i].draw(canvas, i);
		for(int i = 0; i < 4; i++)
			goals[i].draw(canvas, i);
		pool.draw(canvas);
		if(dfloat != null)
			dfloat.draw(canvas);
	}

	public boolean down(float x, float y) {
		int j = pool.down(x,y);
		if(j == -2)
			return true;
		if(j >= 0) {
			dfloat = new DeckFloat(pool, j, x, y, DeckPool.xpos(1), DeckPool.ypos());
			dfloat.add(pool.removeVisible());
			return true;
		}
		for(int i = 0; i < decks.length; i++) {
			j = decks[i].down(i, x, y);
			if(j == -2)
				return true;
			if(j >= 0) {
				dfloat = new DeckFloat(decks[i], j, x, y, DeckBoard.xpos(i), DeckBoard.ypos()+decks[i].yposcard(j));
				for(int k = j; k < decks[i].size(); k++)
					dfloat.add(decks[i].get(k));
				decks[i].removeFrom(j);
				return true;
			}
		}
		for(int i = 0; i < goals.length; i++) {
			j = goals[i].down(i, x, y);
			if(j >= 0) {
				dfloat = new DeckFloat(goals[i], j, x, y, DeckGoal.xpos(i), DeckGoal.ypos());
				dfloat.add(goals[i].get(j));
				goals[i].removeFrom(j);
				return true;
			}
		}
		return false;
	}

	public boolean move(float x, float y) {
		if(dfloat != null) {
			dfloat.setPos(x, y);
			return true;
		}
		return false;
	}

	public boolean up(float x, float y) {
		if(dfloat != null) {
			if(y > DeckBoard.ypos()+Card.HEIGHT+10*6)
				;
			else if(y >= DeckBoard.ypos()) {
				int i = (int)((x-2)/(Card.WIDTH+4));
				if(i >= 0 && i < decks.length)
					if(dropcards(decks[i]))
						return true;
			}
			else if(x >= DeckGoal.xpos(0) && x < DeckGoal.xpos(3)+Card.WIDTH) {
				int i = (int)((x-2)/(Card.WIDTH+4)-3);
				if(dropcards(goals[i]))
					return true;
			}

			// return cards to the owner
			dfloat.getDeck().addAll(dfloat.getDeckPos(), dfloat);
			dfloat = null;
			return true;
		}
		return false;
	}

	public boolean dropcards(Deck deck) {
		if(deck.match(dfloat.get(0))) {
			deck.addAll(dfloat);
			dfloat = null;
			return true;
		}
		return false;
	}

	public int getMaxDeckSize() {
		int n = 0;
		for(int i = 0; i < decks.length; i++)
			n = Math.max(n, decks[i].getCardsSize());
		return n;
	}

	public boolean hasWon() {
		return pool.size()==0 && getMaxDeckSize()==0;
	}

	public String kudosMessage() {
		long sec = (System.currentTimeMillis()-start)/1000;
		return String.format("Your time: %02d mins %02d secs", (int)(sec/60), sec%60);
	}
}
