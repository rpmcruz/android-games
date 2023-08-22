package com.cruz.cruzsolitaire2;

import android.graphics.Canvas;

import java.util.Collection;

/**
 * Created by rick2 on 21-06-2015.
 */
public class DeckPool extends Deck
{
    int visible = -1;

    public DeckPool(int capacity) {
        super(capacity);
    }

    public static int xpos(int i) {
        if(i == 0)  return 2;
        else  return 2+Card.WIDTH+4;
    }
    public static int ypos() {
        return 2;
    }

    public void draw(Canvas canvas) {
        final int x1 = xpos(0), x2 = xpos(1);
        final int y = ypos();
        if(visible == size()-1 || isEmpty())
            Card.drawEmpty(canvas,x1,y);
        else
            Card.drawBack(canvas,x1,y);

        if(visible == -1)
            Card.drawEmpty(canvas,x2,y);
        else
            get(visible).draw(canvas,x2,y);
    }

    public int down(float x, float y) {
        if(y < Card.HEIGHT+4) {
            if(x < 4 + Card.WIDTH) {
                if(isEmpty())
                    return -1;
                visible++;
                if(visible == size())
                    visible = -1;
                return -2;
            }
            if(x < 6 + Card.WIDTH * 2)
                return visible;
        }
        return -1;
    }

    public Card removeVisible() {
        return remove(visible);
    }

    public Card remove(int i) {
        visible--;
        return super.remove(i);
    }

    public boolean addAll(int i, Collection<? extends Card> d) {
        visible++;
        return super.addAll(i, d);
    }
}