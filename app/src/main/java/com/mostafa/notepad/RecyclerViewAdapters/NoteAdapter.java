package com.mostafa.notepad.RecyclerViewAdapters;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.mostafa.notepad.DataModels.Note;
import com.mostafa.notepad.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> notes;
    private ItemTouchedListener<Note> listener;
    public NoteAdapter(List<Note> notes,ItemTouchedListener<Note> listener) {
        this.notes = notes;
        this.listener = listener;
    }
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        holder.bind(notes.get(position),listener,position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void newItems(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    public Note[] getItems(int from, int to) {
        Note[] notes = new Note[Math.abs(from - to) + 1];
        if (from < to) {
            for (int i = from; i < to + 1; i++) {
                notes[i - from] = this.notes.get(i);
            }
        }else{
            for (int i = to; i < from + 1; i++) {
                notes[i - to] = this.notes.get(i);
            }
        }
        return notes;
    }

    public void showAllNormal() {
        notifyDataSetChanged();
    }

    public void setSelected(int p) {

    }

    public Note getNote(int select) {
        return notes.get(select);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView title,text,date;
        private MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(final Note note, final ItemTouchedListener<Note> listener,int p) {
            title.setText(note.getTitle());
            text.setText(note.getText());
            date.setText(note.getDate().toString());
            cardView.setOnClickListener(listener.getListener(note,p));
            cardView.setOnLongClickListener(listener.getLongClickListener(note,true,p));
        }

        public void setSelected() {
            cardView.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.card_selected)));
        }

        public void deSelected() {
            cardView.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.OnBackground)));
        }
    }
}
