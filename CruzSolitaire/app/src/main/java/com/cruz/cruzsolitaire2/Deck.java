package com.cruz.cruzsolitaire2;

// Solitaire - (C) 2012 Ricardo Cruz <ric8cruz@gmail.com> //

import java.util.ArrayList;

public abstract class Deck extends ArrayList<Card>
{
    public Deck(int capacity) {
        super(capacity);
    }

    public void removeFrom(int i) {
        removeRange(i,size());
    }

    public Card getLast() {
        return get(size()-1);
    }

    public boolean match(Card card) { return false; }
}