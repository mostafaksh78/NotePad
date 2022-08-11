package com.mostafa.notepad.RecyclerViewAdapters;

import android.view.View;

public abstract class ItemTouchedListener<T>{
    private T t;

    public ItemTouchedListener() {
    }

    public abstract void onItemClick(T t , int p);
    public abstract void onLongClick(T t, int p);

    public void setT(T t) {
        this.t = t;
    }
    public View.OnClickListener getListener(T film,int p) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(film,p);
            }
        };
    }


    public View.OnLongClickListener getLongClickListener(T note, boolean b, int p) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ItemTouchedListener.this.onLongClick(note,p);
                return b;
            }
        };
    }
}
