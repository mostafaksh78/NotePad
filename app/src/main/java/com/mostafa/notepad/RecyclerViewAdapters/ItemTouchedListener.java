package com.mostafa.notepad.RecyclerViewAdapters;

import android.view.View;

public abstract class ItemTouchedListener<T> implements View.OnClickListener {
    private T t;

    public ItemTouchedListener() {
    }

    public abstract void onItemClick(T t);
    public abstract void onLongClick(T t);
    @Override
    public void onClick(View v) {
        onItemClick(t);
    }

    public void setT(T t) {
        this.t = t;
    }
    public View.OnClickListener getListener(T film) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(film);
            }
        };
    }


    public View.OnLongClickListener getLongClickListener(T note, boolean b) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ItemTouchedListener.this.onLongClick(note);
                return b;
            }
        };
    }
}
