package com.example.administrator.notepad;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private LinkedList<Note> list = new LinkedList<Note>();
    private NoteAdapter adapter;
    private TextView tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.main_listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCreateNoteBtn();

        tip = (TextView)findViewById(R.id.tip);

        Context mContext = MainActivity.this;
        adapter = new NoteAdapter(list, mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = list.get(i);
                Long milliSeconds = note.getMilliSeconds();
                String content = note.getContent();
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                intent.putExtra("milliSeconds", milliSeconds);
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        updateAdapter();
        if (list.size() > 0)
            tip.setVisibility(View.INVISIBLE);
        else
            tip.setVisibility(View.VISIBLE);
        super.onStart( );
    }


    private void updateAdapter() {
        String dbPath = getString(R.string.database_path);
        SQLiteDatabase db = openOrCreateDatabase(dbPath, MODE_PRIVATE, null);
        list.clear();
        db.execSQL("CREATE TABLE IF NOT EXISTS Notes(milliSeconds INTEGER PRIMARY KEY, content TEXT);");
        Cursor cursor = db.rawQuery("SELECT * FROM Notes", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            do{
                long milliSeconds = cursor.getLong(0);
                String content = cursor.getString(1);
                Note note = new Note(milliSeconds, content);
                list.add(note);
            }while(cursor.moveToNext());
        }

        Collections.reverse(list);
        adapter.notifyDataSetChanged();
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initCreateNoteBtn(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar == null)
            return;
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.createNoteBtn:
                        Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    public void showToast(String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }


}
