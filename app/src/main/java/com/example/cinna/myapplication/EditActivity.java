package com.example.cinna.myapplication;



import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.content.*;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;



public class EditActivity extends AppCompatActivity {
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editarea = (EditText) findViewById(R.id.editText);

        Button btn = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById((R.id.button2));
        final DatabaseHelper database = new DatabaseHelper(this);
        final SQLiteDatabase db = database.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT _id, word from keyword",null);
        final ListView listview = (ListView) findViewById(R.id.listview);
        final SimpleCursorAdapter adapter=new SimpleCursorAdapter(EditActivity.this,
                android.R.layout.simple_list_item_activated_2,
                cursor,
                new String[]{"_id","word"},
                new int[]{android.R.id.text1,android.R.id.text2}
        );
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text2=(TextView) view.findViewById(android.R.id.text2);
                TextView text1=(TextView) view.findViewById(android.R.id.text1);
                final String s2 = text2.getText().toString();
                final String s1 = text1.getText().toString();

                new AlertDialog.Builder(EditActivity.this).setTitle("")//设置对话框标题
                        .setMessage("Do you really want to delete "+s2+"?")//设置显示的内容
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String[] args={s1};
                                        db.delete("keyword","_id=?", args);
                                        cursor.requery();
                                        adapter.notifyDataSetChanged();
                                    }
                        }).show();

                return false;
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                String sql1="DELETE FROM keyword";
                db.execSQL(sql1);
                cursor.requery();
                adapter.notifyDataSetChanged();

            }

        });
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String s = String.valueOf(editarea.getText());
                editarea.setText("");
                ContentValues values = new ContentValues();
                values.put("word", s);
                db.insert("keyword", "_id", values);
                cursor.requery();
                adapter.notifyDataSetChanged();

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Edit Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-HTTP-HOST-HERE]/edit"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
