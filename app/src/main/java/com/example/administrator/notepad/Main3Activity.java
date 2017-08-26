package com.example.administrator.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;

public class Main3Activity extends AppCompatActivity {
    private Long milliSeconds;
    EditText editText;
    MenuItem deleteBtn;
    String content;
    boolean hasEdited = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        milliSeconds = intent.getLongExtra("milliSeconds", -1);
        content = intent.getStringExtra("content");

        initEditText();
    }

    private void initEditText() {
        editText = (EditText)findViewById(R.id.editText);
        if (content != null) {
            editText.setText(content);
            editText.setSelection(content.length());
        }
        editText.addTextChangedListener(new Watcher()); // 检查用户是否改变过文本
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        deleteBtn = menu.getItem(0);

        if (milliSeconds != -1){
            deleteBtn.setVisible(true);

        }
        else{
            deleteBtn.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                askIfSaveAndLeave();
                return true;
            case R.id.saveBtn:{
                saveOrUpdate();
                finish();
                return true;
            }

            case R.id.deleteBtn:{
                String dbPath = getString(R.string.database_path);
                SQLiteDatabase db = openOrCreateDatabase(dbPath, MODE_PRIVATE, null);
                db.delete("Notes", "milliSeconds = ?", new String[]{ milliSeconds.toString() });
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        askIfSaveAndLeave();
    }

    public void showToast(String str){
        Toast.makeText(Main3Activity.this, str, Toast.LENGTH_LONG).show();
    }

    public void saveOrUpdate(){
        if (milliSeconds == -1)
            createNote();
        else
            updateNote();
    }

    public void updateNote(){
        String dbPath = getString(R.string.database_path);
        SQLiteDatabase db = openOrCreateDatabase(dbPath, MODE_PRIVATE, null);
        String content = editText.getText().toString();

        Long ms = System.currentTimeMillis();
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("milliSeconds", System.currentTimeMillis());
        db.update("Notes", cv, "milliSeconds = ?", new String[]{ milliSeconds.toString() });
        db.close();
    }

    public void createNote(){
        String dbPath = getString(R.string.database_path);
        SQLiteDatabase db = openOrCreateDatabase(dbPath, MODE_PRIVATE, null);
        String content = editText.getText().toString();

        ContentValues cv = new ContentValues();
        Note note = new Note(content);
        cv.put("milliSeconds", note.getMilliSeconds());
        cv.put("content", note.getContent());
        db.insert("Notes", null, cv);
        db.close();
    }
    public void askIfSaveAndLeave(){
        if (hasEdited){
            new AlertDialog.Builder(Main3Activity.this).setTitle("您还在编辑状态")
                    .setMessage("是否保存并退出?")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener( ) {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveOrUpdate();
                            finish();
                        }
                    }).setNegativeButton("不保存", new DialogInterface.OnClickListener( ) {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setCancelable(true).show();
        }
        else
            finish();
    }
     public void testDatabaseNotes(){
         String dbPath = getString(R.string.database_path);
         SQLiteDatabase db = openOrCreateDatabase(dbPath, MODE_PRIVATE, null);
         LinkedList<Note> list = new LinkedList<Note>();

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
         Integer i = list.size();
         showToast(i.toString());

         cursor.close();
         db.close();
     }
     private class Watcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Main3Activity.this.hasEdited = true;
        }
    }

}
