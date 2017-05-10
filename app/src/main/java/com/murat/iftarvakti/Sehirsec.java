package com.murat.iftarvakti;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Sehirsec extends AppCompatActivity {
    ListView lvSehirsec;
    Database dbHelper;
    ArrayList<String> arrTblNames = null;
    SQLiteDatabase db;
    ArrayAdapter<String> adapter;
    Constr sehirGuncelle;
    String sehir;
    String sehir2;
    TextView textView;
    SharedPreferences tutulansehir;
    SharedPreferences.Editor sehirEditor;
    public String PREF_NAME = "myRef";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sehirsec);
        sehirGuncelle = new Constr();
        lvSehirsec = (ListView) findViewById(R.id.lvSehirsec);
        /*tutulansehir = getSharedPreferences("Sehir", MODE_PRIVATE);/*Mode private bu shared preferencenin sadece
        bizim buygulamamızda kullanılacağı başka packagelerde kullanılmayacağı anlamına gelir*/

        tabloisimler();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrTblNames) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        lvSehirsec.setAdapter(adapter);
        lvSehirsec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sehir = sehirGuncelle.stringToDb(arrTblNames.get(position));
                    savePref(sehir);
                    startActivity(new Intent(Sehirsec.this, IftarVakti.class));
            }
        });

    }

    private String savePref(String sehirPref) {
        tutulansehir = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sehirEditor = tutulansehir.edit();
        sehirEditor.putString("sehirler", ""+sehirPref);
        sehirEditor.apply();
        return sehirPref;
    }

    private void tabloisimler() {
        dbHelper = new Database(this);
        db = dbHelper.getReadableDatabase();
        arrTblNames = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'  ", null);
        if (c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(sehirGuncelle.dbToString(c.getString(0)));
                c.moveToNext();
            }
        }
    }

}

