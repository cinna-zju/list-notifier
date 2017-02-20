package com.example.cinna.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;

    private Parser parser;
    private List<item> items;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHelper database = new DatabaseHelper(this);
        final SQLiteDatabase db = database.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT _id, title, date, addr from links",null);
        final ListView listview = (ListView) findViewById(R.id.listview1);
        final SimpleCursorAdapter adapter=new SimpleCursorAdapter(MainActivity.this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"title","date"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text1=(TextView) view.findViewById(android.R.id.text1);


                String sql = "select addr from links where title='"+text1.getText()+"'";

                Cursor cr = db.rawQuery(sql, null);
                if (cr.moveToFirst()==false)
                    Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                else {

                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://opt.zju.edu.cn/" + cr.getString(0));
                    intent.setData(content_url);
                    startActivity(intent);
                }

                return false;

            }
        });



        new Thread() {
            @Override
            public void run() {
                Log.i("dd","in the thread");
                try {
                    URL url = new URL("http://www.cinnabrave.cn/result.xml");
                    InputStream is = url.openStream();

                    parser = new PullParser();
                    items = parser.parse(is);
                    //System.out.println(is.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql1="DELETE FROM links";
                db.execSQL(sql1);
                cursor.requery();
                adapter.notifyDataSetChanged();

                if(items.isEmpty() != true) {
                    for (item item : items) {

                        String title = item.getTitle();
                        String date = item.getDate();
                        String addr = item.getHref();
                        //System.out.println("dd"+addr);
                        ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("date", date);
                        values.put("addr", addr);
                        db.insert("links", "_id", values);
                    }
                }
                    cursor.requery();
                    adapter.notifyDataSetChanged();

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


        }
        if (id == R.id.add) {
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        }

        if(id == R.id.delete){


        }
        return super.onOptionsItemSelected(item);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }








}



