package com.cruz.cruzsolitaire2;

// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

import android.graphics.Canvas;
import android.graphics.Paint;

public class Card
{
    public static Paint emptyBorder, emptyBg, cardBorder, cardBg, backBorder, backBg, blackText, redText;

    public static final int WIDTH = 16, HEIGHT = 25;

    private int value;

    public Card(int number, int suit) {
        value = number + (suit<<4);
    }

    public int number() { return value & 15; }
    public int suit() { return (value >> 4) & 3; }

    private static String suitToString(int n) {
        switch(n) {
            case 0:  return "\u2663";
            case 1: return "\u2660";
            case 2: return "\u2665";
            case 3: return "\u2666";
        }
        return "?";  // error
    }

    private static String numberToString(int n) {
        switch(n) {
            case 0:  return "A";
            case 10: return "J";
            case 11: return "Q";
            case 12: return "K";
            default:
                return String.valueOf(n+1);
        }
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawRect(x, y, x + WIDTH, y + HEIGHT, cardBg);
        canvas.drawRect(x, y, x + WIDTH, y + HEIGHT, cardBorder);
        final float yh = blackText.descent() + blackText.ascent();
        final float yt = y-yh+2;
        int s = suit();
        Paint text = s >= 2 ? redText : blackText;
        canvas.drawText(suitToString(s), x, yt, text);
        final int xt = x+7+3;
        int n = number();
        if(n == 9) {
            canvas.drawText("1", xt-2, yt, text);
            canvas.drawText("0", xt+1, yt, text);
        }
        else
            canvas.drawText(numberToString(n), xt, yt, text);
    }

    public static void drawBack(Canvas canvas, int x, int y) {
        canvas.drawRect(x, y, x+WIDTH, y+HEIGHT, backBg);
        canvas.drawRect(x, y, x+WIDTH, y+HEIGHT, backBorder);
    }

    public static void drawEmpty(Canvas canvas, int x, int y) {
        canvas.drawRect(x, y, x+WIDTH, y+HEIGHT, emptyBg);
        canvas.drawRect(x, y, x+WIDTH, y+HEIGHT, emptyBorder);
    }
}