package com.cruz.cruzsolitaire2;

import android.graphics.Canvas;

/**
 * Created by rick2 on 21-06-2015.
 */
public class DeckGoal extends Deck
{
    public DeckGoal() {
        super(13);
    }

    static public int xpos(int i) { return (i+3)*(Card.WIDTH+4) + 2; }
    static public int ypos() { return 2; }

    public void draw(Canvas canvas, int i) {
        final int x = xpos(i);
        final int y = ypos();
        if(isEmpty())
            Card.drawEmpty(canvas, x, y);
        else
            getLast().draw(canvas, x, y);
    }

    public boolean match(Card src) {
        if(isEmpty())
            return src.number() == 0;
        Card dst = getLast();
        return src.number() == dst.number()+1 && src.suit() == dst.suit();
    }

    public int down(int i, float x, float y) {
        if(x >= xpos(i) && x <= xpos(i)+Card.WIDTH && y <= 2+Card.HEIGHT)
            return size()-1;
        return -1;
    }
}