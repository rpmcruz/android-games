package com.cruz.cruzsolitaire2;

import android.graphics.Canvas;

import java.util.Collection;

/**
 * Created by rick2 on 21-06-2015.
 */
public class DeckBoard extends Deck {
    public static int YPAD = 8, YPAD2 = 2;
    private int visible = 1;

    public DeckBoard(int capacity) {
        super(capacity);
    }

    public static int xpos(int i) { return i*(Card.WIDTH+4)+2; }
    public static int ypos() { return Card.HEIGHT+6; }

    public int yposcard(int j) {
        return Math.min(j,size()-visible)*YPAD2 + Math.max(j-(size()-visible),0)*YPAD;
    }

    public void draw(Canvas canvas, int i) {
        final int x = xpos(i);
        final int y = ypos();
        if(isEmpty())
            Card.drawEmpty(canvas, x, y);
        else
            for(int j = 0; j < size(); j++) {
                final int yj = y + yposcard(j);
                if(j >= size()-visible)
                    get(j).draw(canvas, x, yj);
                else
                    Card.drawBack(canvas, x, yj);
            }
    }

    public int down(int i, float x, float y) {
        if(x >= xpos(i) && x <= xpos(i)+Card.WIDTH && y >= ypos()+(size()-visible)*YPAD2) {
            int j = (int)((y-(size()-visible)*YPAD2-ypos())/YPAD)+size()-visible;
            if(j < size() && j >= size()-visible)
                return j;
            if(y <= ypos()+(size()-visible-1)*YPAD2+visible*YPAD+Card.HEIGHT) {  // last card (bigger)
                if(visible == 0) {
                    visible = 1;
                    return -2;
                }
                return size() - 1;
            }
        }
        return -1;
    }

    public void removeFrom(int i) {
        visible -= size() - i;
        super.removeFrom(i);
    }

    public boolean addAll(int i, Collection<? extends Card> d) {
        visible += d.size();
        return super.addAll(i, d);
    }

    public boolean addAll(Collection<? extends Card> d) {
        visible += d.size();
        return super.addAll(d);
    }

    public boolean match(Card src) {
        if(isEmpty())
            return src.number()==12;  //K
        Card dst = getLast();
        return src.number() == dst.number() - 1 && src.suit() / 2 != dst.suit() / 2;
    }

    public int getCardsSize() {
        return (int)Math.ceil(visible + (size()-visible)*(YPAD2/(float)YPAD));
    }
}