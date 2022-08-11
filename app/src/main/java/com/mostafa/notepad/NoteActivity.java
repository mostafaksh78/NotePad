package com.mostafa.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;

import com.mostafa.notepad.DataModels.Note;
import com.mostafa.notepad.Database.NoteDatabase;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteActivity extends AppCompatActivity {
    private EditText titleTv,textTv;
    int noteIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        titleTv = findViewById(R.id.editText);
        textTv = findViewById(R.id.editTextTextPersonName);
        noteIndex = getIntent().getIntExtra("Note",-1);
        if (noteIndex!= -1){
            Note note =  Global.database.getNoteDao().getNote(noteIndex);
            titleTv.setText(note.getTitle());
            textTv.setText(note.getText());
        }
    }
    public static void starter(Context context, Note note){
        context.startActivity(new Intent(context,NoteActivity.class).putExtra("Note",note.getIndex()));
    }
    public static void starter(Context context){
        context.startActivity(new Intent(context,NoteActivity.class).putExtra("Note",-1));
    }

    @Override
    public void onBackPressed() {
        Observable<Integer> databaseObservable = Observable.fromCallable(() -> {
            if (noteIndex == -1){
                Global.database.getNoteDao().insert(new Note(titleTv.getEditableText().toString(),textTv.getEditableText().toString(),new Date()));

            }else {
                Global.database.getNoteDao().update(new Note(titleTv.getEditableText().toString(),textTv.getEditableText().toString(),noteIndex,new Date()));
            }
            return 0;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        databaseObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer notes) {
                NoteActivity.super.onBackPressed();
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
    protected void onDestroy() {

        super.onDestroy();
    }
}