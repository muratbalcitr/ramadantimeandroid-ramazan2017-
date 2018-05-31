package com.murat.iftarvakti;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class IftarVakti extends AppCompatActivity {
    ArrayList<VakitStructure> vakitlerStr = null;
    TextView tvSehir, tvTarih, tvSabah, tvGunes, tvOgle, tvIkindi, tvAksam, tvYatsi, tvIftraKalan;
    Button btnGun, btnTumay,btnsehirsec;
    Database dbHelper;
    SQLiteDatabase db;
    String sehir = "", sehir2;
    Date aksam, currentTime, sabah, oglen, ikindi, yatsi;
    String diff;
    public String PREF_NAME = "myRef";
    String restoredText;
    ArrayList<String> ilahi;
    InterstitialAd mInterstitialAd;
    String sabahvkt = "sabah", oglenvkt = "öğlen", ikindivkt = " ikindi", aksamvkt = "akşam", yatsivkt = "yatsı";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iftar_vakti);
        initialize();
        sharedPrfTutulansehir();
        adMobsInitialize();
        ilahiler();
        Handler handler = new Handler();
       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();

            }
        }, 0000);*/
    }

    private void adMobsInitialize() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();
    }

    private void initialize() {
        btnGun = (Button) findViewById(R.id.btnGunSayisi);
        btnTumay = (Button) findViewById(R.id.btnTumAy);
        btnsehirsec =(Button)findViewById(R.id.btnSehir);
        tvSehir = (TextView) findViewById(R.id.tvSehirAdi);
        tvTarih = (TextView) findViewById(R.id.tvTarih);
        tvSabah = (TextView) findViewById(R.id.tvSabahVakti);
        tvGunes = (TextView) findViewById(R.id.tvGunesVakti);
        tvOgle = (TextView) findViewById(R.id.tvOglenVakti);
        tvIkindi = (TextView) findViewById(R.id.tvIkindiVakti);
        tvAksam = (TextView) findViewById(R.id.tvAksamVakti);
        tvYatsi = (TextView) findViewById(R.id.tvYatsiVakti);
        tvIftraKalan = (TextView) findViewById(R.id.tvIftarakalan);

    }

    private void sharedPrfTutulansehir() {
        Constr sehirGuncelle = new Constr();
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        restoredText = prefs.getString("sehirler", null);
        if (restoredText != null) {
            sehir = prefs.getString("sehirler", "No sehir defined");//"No name defined" is the default value.
            verilerCek();
        } else {
            startActivity(new Intent(IftarVakti.this, Sehirsec.class));

            /*
            sehirGuncelle.dbToString(sehir);
            tvSehir.setText(sehir);
            verilerCek();*/
        }
    }

    DateFormat date;
    long Hours;
    long Mins;
    long days;
    long mills, seconds;
    String localTime;
    Thread t;
    Calendar thatDay;

    public void DateProcess() {
          thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH,27);
        thatDay.set(Calendar.MONTH,4); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, 2017);

        Calendar today = Calendar.getInstance();

        long diff2 = thatDay.getTimeInMillis()-today.getTimeInMillis() ;

          days = diff2 / (24 * 60 * 60 * 1000);

        if(days>0){
            tvIftraKalan.setText("ramazana "+days+" gün kaldı");
            tvSabah.setText("ramazana "+days+" gün kaldı");
            tvGunes.setText("ramazana "+days+" gün kaldı");
            tvOgle.setText("ramazana "+days+" gün kaldı");
            tvIkindi.setText("ramazana "+days+" gün kaldı");
            tvAksam.setText("ramazana "+days+" gün kaldı");
            tvYatsi.setText("ramazana "+days+" gün kaldı");
            tvTarih.setText("ramazana "+days+" gün kaldı");
        }
        else {

            t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
                                    Date currentLocalTime = cal.getTime();
                                    date = new SimpleDateFormat("HH:mm:ss");

                                    date.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));

                                    localTime = date.format(currentLocalTime);

                                    String strIftarVakti = tvAksam.getText().toString();
                                    String strSabahVakti = tvSabah.getText().toString();
                                    String strOglenVakti = tvOgle.getText().toString();
                                    String strIkindiVakti = tvIkindi.getText().toString();
                                    String strYatsiVakti = tvYatsi.getText().toString();
                                    try {
                                        aksam = date.parse(strIftarVakti+":00");
                                        sabah = date.parse(strSabahVakti+":00");
                                        oglen = date.parse(strOglenVakti+":00");
                                        ikindi = date.parse(strIkindiVakti+":00");
                                        yatsi = date.parse(strYatsiVakti+":00");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        currentTime = date.parse(localTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mills = aksam.getTime() - currentTime.getTime();
                                    Hours = mills / (1000 * 60 * 60);
                                    Mins = mills / (60 * 1000) % 60;
                                    seconds = 60 - currentTime.getSeconds();//????
                                    diff = Hours + " saat " + Mins + " dakika " + seconds + " saniye kaldı";
                                    seconds--;
                                    // updated value every1 second
                                    tvIftraKalan.setText("iftara " + diff);
                                    if (currentTime.getTime() == aksam.getTime()) {
                                        addNotification(aksamvkt);
                                        tvIftraKalan.setText("iftar vakti  -  Allah kabul etsin");
                                    }
                                    if (currentTime.getTime() >= aksam.getTime()) {
                                        tvIftraKalan.setText("iftar vakti  -  Allah kabul etsin");
                                    } else if (currentTime.getTime() == sabah.getTime()) {
                                        addNotification(sabahvkt);
                                    } else if (currentTime.getTime() == oglen.getTime()) {
                                        addNotification(oglenvkt);
                                    } else if (currentTime.getTime() == ikindi.getTime()) {
                                        addNotification(ikindivkt);
                                    } else if (currentTime.getTime() == yatsi.getTime()) {
                                        addNotification(yatsivkt);
                                    }
                                }
                            });
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();
        }
    }

    Random rndd = new Random();

    private void addNotification(String vakit) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_autorenew)
                        .setContentTitle(sehir + " için " + vakit + " vakti")
                        .setColor(R.color.colorAccent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(ilahi.get(rndd.nextInt(ilahi.size()))));
        Intent notificationIntent = new Intent(this, IftarVakti.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 113, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);

        builder.setFullScreenIntent(contentIntent, true);
        //Vibration
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        //LED
        builder.setLights(Color.RED, 3000, 3000);

        //Ton
        builder.setContentIntent(contentIntent);
        /*Uri alarmSound = RingtoneManager.getDefaultUri(R.raw.aksam);
        builder.setSound(alarmSound);*/
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(115, builder.build());
    }

    public void verilerCek() {
        Constr sehirGuncelle = new Constr();
        tvSehir.setText(sehirGuncelle.dbToString(sehir));
        SimpleDateFormat bicim2 = new SimpleDateFormat("dd MMMM yyyy EEEE", new Locale("tr"));
        Date tarihSaat = new Date();
        tvTarih.setText(bicim2.format(tarihSaat));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();
            }
        }, 0000);
       // tvTarih.setText("27 Mayıs 2017 Cumartesi");
        dbHelper = new Database(this);
        try {
            dbHelper.openDataBase();
            dbHelper.copyDataBase();

        } catch (Exception ex) {
            Log.w("hata", "Veritabanı oluşturulamadı ve kopyalanamadı!");
        }
        vakitlerStr = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        String[] getColumnName = {"gun,tarih,sabah,gunes,oglen,ikindi,aksam,yatsi"};
        Cursor imlec = db.query(sehir, getColumnName, null, null, null, null, null);
        while (imlec.moveToNext()) {
            VakitStructure vktstr1 = new VakitStructure(imlec.getString(imlec.getColumnIndex("gun")), imlec.getString(imlec.getColumnIndex("tarih")),
                    imlec.getString(imlec.getColumnIndex("sabah")), imlec.getString(imlec.getColumnIndex("gunes")), imlec.getString(imlec.getColumnIndex("oglen")),
                    imlec.getString(imlec.getColumnIndex("ikindi")), imlec.getString(imlec.getColumnIndex("aksam")), imlec.getString(imlec.getColumnIndex("yatsi")),
                    null);
            vakitlerStr.add(vktstr1);
        }
        for (int i = 0; i < 29; i++) {
            if (tvTarih.getText().equals(vakitlerStr.get(i).getTarih())) {
                tvTarih.setText(vakitlerStr.get(i).getTarih().toString());
                btnGun.setText("Ramazanın \b" + String.valueOf(vakitlerStr.get(i).getGun()) + ".günü");
                tvSabah.setText(vakitlerStr.get(i).getSabah().toString());
                tvGunes.setText(vakitlerStr.get(i).getGunes().toString());
                tvOgle.setText(vakitlerStr.get(i).getOglen().toString());
                tvIkindi.setText(vakitlerStr.get(i).getIkindi().toString());
                tvAksam.setText(vakitlerStr.get(i).getAksam().toString());
                tvYatsi.setText(vakitlerStr.get(i).getYatsi().toString());
            }
        }

        imlec.close();
        db.close();
    }

    public void btnTumAy(View view) {
        if(days>0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            setTitle("Bölüm aktif değildir");
            alert.setMessage("Ramazanın başlamasına "+days+" gün kaldı bu bölüm aktif değildir");
            alert.create();
            alert.show();
            mInterstitialAd.show();
        }
        else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent i = new Intent(IftarVakti.this, Secilensehringunleri.class);
                i.putExtra("sehir", sehir);
                startActivity(i);
            }
        }
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void sehirDegistir(View view) {
        mInterstitialAd.show();
        AlertDialog.Builder sehir = new AlertDialog.Builder(this);
        sehir.setIcon(R.drawable.ic_autorenew);
        sehir.setMessage("şehirinizi değiştirmek istediğinize emin misiniz ?");
        sehir.setPositiveButton("Eminim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(IftarVakti.this, Sehirsec.class));

            }
        });
        sehir.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = sehir.create();
        alertDialog.show();

    }

    

    @Override
    protected void onPause() {
        super.onPause();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();

            }
        }, 0000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();

            }
        }, 0000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();

            }
        }, 0000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DateProcess();

            }
        }, 0000);
    }
}
