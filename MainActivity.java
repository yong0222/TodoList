package com.cookandroid.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("To Do List");
        ListView listView = findViewById(R.id.listView);
        EditText editText = findViewById(R.id.editText);
        Button addButton = findViewById(R.id.addButton);

        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(this, "todo.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT);");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS todos");
                onCreate(db);
            }
        };

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT _id, title FROM todos", null);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"title"}, new int[]{android.R.id.text1}, 0);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText.getText().toString();
                if (!title.isEmpty()) {
                    database.execSQL("INSERT INTO todos (title) VALUES ('" + title + "');");
                    Cursor cursor = database.rawQuery("SELECT _id, title FROM todos", null);
                    adapter.changeCursor(cursor);
                    editText.setText("");
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                database.execSQL("DELETE FROM todos WHERE _id = " + l);
                Cursor cursor2 = database.rawQuery("SELECT _id, title FROM todos", null);
                adapter.changeCursor(cursor2);
                return true;
            }
        });
    }
}
