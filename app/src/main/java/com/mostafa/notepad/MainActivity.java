package com.mostafa.notepad;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mostafa.notepad.DataModels.Note;
import com.mostafa.notepad.Database.NoteDatabase;
import com.mostafa.notepad.RecyclerViewAdapters.ItemTouchedListener;
import com.mostafa.notepad.RecyclerViewAdapters.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    private RecyclerView list;
    private TextInputLayout searchBar;
    private NoteAdapter adapter;
    private FloatingActionButton add;
    private Animation toMiddle;
    private Animation fromMiddle;
    private boolean isFrontOfCardShowing = false;
    private boolean showMode = true;
    private List<Integer> selected = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        searchBar = findViewById(R.id.textInputLayout);
        add = findViewById(R.id.add);
        add.setOnClickListener(addNote);
        adapter = new NoteAdapter(new ArrayList<>(), new ItemTouchedListener<Note>() {
            @Override
            public void onItemClick(Note note,int p) {
                if (showMode) {
                    NoteActivity.starter(MainActivity.this,note);
                }else {
                    NoteAdapter.ViewHolder holder = (NoteAdapter.ViewHolder) (list.findViewHolderForAdapterPosition(p));
                    if (holder != null) {
                        if (!selected.contains(p)) {
                            holder.setSelected();
                            selected.add(p);
                        }else {
                            selected.remove(Integer.valueOf(p));
                            holder.deSelected();
                        }
                    }
                }
            }

            @Override
            public void onLongClick(Note note,int p) {
                if (showMode) {
                    toggle();
                    showMode = false;
                    NoteAdapter.ViewHolder holder = (NoteAdapter.ViewHolder) (list.findViewHolderForAdapterPosition(p));
                    if (holder != null) {
                        holder.setSelected();
                        if (!selected.contains(p)) {
                            selected.add(p);
                        }else {
                            selected.remove(Integer.valueOf(p));
                        }
                    }
                }
            }
        });
        list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2,RecyclerView.VERTICAL,false));
        list.setAdapter(adapter);
        toMiddle = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        toMiddle.setAnimationListener(this);
        fromMiddle = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        fromMiddle.setAnimationListener(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(list);
    }
    private void bindNotes(){
        Observable<List<Note>> databaseObservable = Observable.fromCallable(() -> {
            Global.database = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "db-Notes")
                    .allowMainThreadQueries()   //Allows room to do operation on main thread
                    .build();
            return  Global.database.getNoteDao().getAll();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        databaseObservable.subscribe(new Observer<List<Note>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Note> notes) {
                Log.d("Database","Notes : " + notes);
                adapter.newItems(notes);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindNotes();
    }
    private void toggle(){
        add.setAnimation(toMiddle);
        add.startAnimation(toMiddle);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == toMiddle) {
            if (isFrontOfCardShowing) {
                add.setImageResource(R.drawable.ic_baseline_note_add_24);
                add.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.colorAccent)));
                add.setOnClickListener(addNote);
            } else {
                add.setImageResource(R.drawable.ic_baseline_delete_24);
                add.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.Error)));
                add.setOnClickListener(deleteNote);
            }
            add.clearAnimation();
            add.setAnimation(fromMiddle);
            add.startAnimation(fromMiddle);
        } else {
            isFrontOfCardShowing = !isFrontOfCardShowing;
            add.setEnabled(true);
        }
    }
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    private View.OnClickListener addNote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NoteActivity.starter(MainActivity.this);
        }
    };
    private View.OnClickListener deleteNote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < selected.size(); i++) {
                int select = selected.get(i);
                adapter.notifyItemRemoved(select);
                Global.database.getNoteDao().delete(adapter.getNote(select));
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (showMode) {
            super.onBackPressed();
        }else {
            showMode = true;
            toggle();
            adapter.showAllNormal();
        }
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |ItemTouchHelper.START | ItemTouchHelper.END ,0 ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int from = viewHolder.getAdapterPosition();
            int to  = target.getAdapterPosition();
            NoteAdapter adapter = (NoteAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.notifyItemMoved(from,to);
            }
            Global.database.getNoteDao().scrollIndex(from,to);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("scrollDebug","On swiped : " + direction);
        }
    };
}