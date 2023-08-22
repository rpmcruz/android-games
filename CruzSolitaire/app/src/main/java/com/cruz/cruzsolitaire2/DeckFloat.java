package com.cruz.cruzsolitaire2;

import android.graphics.Canvas;

/**
 * Created by rick2 on 21-06-2015.
 */
public class DeckFloat extends Deck
{
    private Deck deck;
    private int deckpos;
    private int x,y, xoffset,yoffset;

    public DeckFloat(Deck deck, int pos, float x, float y, int xdeck, int ydeck) {
        super(13);
        this.deck = deck;
        deckpos = pos;
        setPos(x,y);
        xoffset = (int)x-xdeck;
        yoffset = (int)y-ydeck;
    }

    public Deck getDeck() { return deck; }
    public int getDeckPos() { return deckpos; }

    public void setPos(float x, float y) {
        this.x = (int)x;
        this.y = (int)y;
    }

    public void draw(Canvas canvas) {
        for(int i = 0; i < size(); i++) {
            final int yi = y + i*DeckBoard.YPAD;
            get(i).draw(canvas, x-xoffset, yi-yoffset);
        }
    }
}