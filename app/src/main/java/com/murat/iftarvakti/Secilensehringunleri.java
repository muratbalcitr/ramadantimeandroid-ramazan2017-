package com.murat.iftarvakti;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class Secilensehringunleri extends AppCompatActivity {
    ArrayList<VakitStructure> vakitlerStr = null;
    Database dbHelper;
    SQLiteDatabase db;
    String sehir;
    ListView listemiz;
    SehirdetayAdapter adaptorumuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secilensehringunleri);

        Intent intent = getIntent();
        sehir = intent.getStringExtra("sehir").toString();

        verilerCek();

        listemiz = (ListView) findViewById(R.id.lvSehirDetay);
        adaptorumuz = new SehirdetayAdapter(this, vakitlerStr);
        listemiz.setAdapter(adaptorumuz);
    }

    private void verilerCek() {
        dbHelper = new Database(this);
        try {
            dbHelper.openDataBase();
            dbHelper.copyDataBase();

        } catch (Exception ex) {
            Log.w("hata", "Veritabanı oluşturulamadı ve kopyalanamadı!");
        }
        vakitlerStr = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        String[] getColumnName = {"gun,tarih,sabah,oglen,ikindi,aksam,yatsi"};

        Cursor imlec = db.query(sehir, getColumnName, null, null, null, null, null);
        while (imlec.moveToNext()) {
          /*  VakitStructure vktstr = new VakitStructure();
            vktstr.gun = imlec.getString(imlec.getColumnIndex("gun"));
            vktstr.tarih = imlec.getString(imlec.getColumnIndex("tarih"));
            vktstr.sabah = imlec.getString(imlec.getColumnIndex("sabah"));
            vktstr.oglen = imlec.getString(imlec.getColumnIndex("oglen"));
            vktstr.ikindi = imlec.getString(imlec.getColumnIndex("ikindi"));
            vktstr.aksam = imlec.getString(imlec.getColumnIndex("aksam"));
            vktstr.yatsi = imlec.getString(imlec.getColumnIndex("yatsi"));*/
            VakitStructure vktstr1 = new VakitStructure(imlec.getString(imlec.getColumnIndex("gun")), imlec.getString(imlec.getColumnIndex("tarih")),
                    imlec.getString(imlec.getColumnIndex("sabah")),null, imlec.getString(imlec.getColumnIndex("oglen")),
                    imlec.getString(imlec.getColumnIndex("ikindi")), imlec.getString(imlec.getColumnIndex("aksam")), imlec.getString(imlec.getColumnIndex("yatsi")),
                    null);
            vakitlerStr.add(vktstr1);
        }

        imlec.close();
        db.close();
    }
}
