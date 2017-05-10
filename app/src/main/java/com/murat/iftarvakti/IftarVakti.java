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
        mInterstitialAd.setAdUnitId("ca-app-pub-9057596586284356~8535497822");

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

    public void ilahiler() {
        ilahi = new ArrayList<>();
        ilahi.add("Kolaylaştırınız, güçleştirmeyiniz, müjdeleyiniz, nefret ettirmeyiniz." +
                "Buhârî, İ lm, 12; Müslim, Cihâd, 6." +
                "İslâm, güzel ahlâktır." +

                "Kenzü'l-Ummâl, 3/17, HadisNo: 5225");

        ilahi.add(" İnsanlara merhamet etmeyene Allah merhamet etmez." +

                "Müslim, Fedâil, 66; Tirmizî, Birr, 16");

        ilahi.add("Nerede olursan ol Allah'a karşı gelmekten sakın; yaptığın kötülüğün arkasından bir iyilik yap ki bu onu yok etsin. İnsanlara karşı güzel ahlakın gereğine göre davran." +

                "Tirmizî, Birr, 55");

        ilahi.add("Hayra vesile olan, hayrı yapan gibidir." +

                "Tirmizî, İlm, 14.");

        ilahi.add("Mümin, bir delikten iki defa sokulmaz.  Mümin, iki defa aynı yanılgıya düşmez" +

                "Buhârî, Edeb, 83; Müslim, Zühd, 63.");

        ilahi.add("Allah, sizden birinizin yaptığı işi, ameli ve görevi sağlam ve iyi yapmasından hoşnut olur." +

                "Taberânî, el-Mu'cemü'l-Evsat, 1/275; Beyhakî,.");

        ilahi.add(" İman, yetmiş küsur derecedir. En üstünü “Lâ ilâhe illallah  Allah'tan başka ilah yoktur  sözüdür, en düşük derecesi de rahatsız edici bir şeyi yoldan kaldırmaktır. Haya da imandandır." +

                "Buhârî, Îmân, 3; Müslim, Îmân, 57, 58");

        ilahi.add("Müslüman, insanların elinden ve dilinden emin olduğu kimsedir." +

                "Tirmizî, Îmân, 12; Nesâî, Îmân, 8.");

        ilahi.add("Mümin  kardeşinle münakaşa etme, onun hoşuna gitmeyecek şakalar yapma ve ona yerine getirmeyeceğin bir söz verme." +

                "Tirmizî, Birr, 58.");

        ilahi.add("İnsanların Peygamberlerden öğrenegeldikleri sözlerden biri de: “Utanmadıktan sonra dilediğini yap!” sözüdür." +

                "Buhârî, Enbiyâ, 54; EbuDâvûd, Edeb, 6. ");

        ilahi.add("Allah Rasûlü  “Din nasihattır/samimiyettir” buyurdu. “Kime Yâ Rasûlallah?” diye sorduk. O da; “Allah'a, Kitabına, Peygamberine, Müslümanların yöneticilerine ve bütün müslümanlara” diye cevap verdi." +

                "Müslim, İ mân, 95 ");

        ilahi.add("Kim kötü ve çirkin bir iş görürse onu eliyle düzeltsin; eğer buna gücü yetmiyorsa diliyle düzeltsin; buna da gücü yetmezse, kalben karşı koysun. Bu da imanın en zayıf derecesidir." +

                "Müslim, Îmân, 78; Ebû Dâvûd, Salât, 248.");

        ilahi.add("İki göz vardır ki, cehennem ateşi onlara dokunmaz: Allah korkusundan ağlayan göz, bir de gecesini Allah yolunda, nöbet tutarak geçiren göz." +

                "Tirmizî , Fedâilü'l-Cihâd, 12.");

        ilahi.add("Zarar vermek ve zarara zararla karşılık vermek yoktur." +

                "İbn Mâce, Ahkâm, 17; Muvatta', Akdıye, 31.");

        ilahi.add(" Hiçbiriniz kendisi için istediğini  mü'min  kardeşi için istemedikçe  gerçek  iman etmiş olamaz." +

                "Buhârî, Îmân, 7; Müslim, Îmân, 71.");

        ilahi.add(" Müslüman müslümanın kardeşidir. Ona zulmetmez, onu  düşmanına  teslim etmez." +
                " Kim, mümin  kardeşinin bir ihtiyacını giderirse Allah da onun bir ihtiyacını giderir.");

        ilahi.add(" Kim müslümanı bir sıkıntıdan kurtarırsa, bu sebeple Allah da onu kıyamet günü sıkıntılarının birinden kurtarır." +
                " Kim bir müslümanın kusurunu  örterse, Allah da Kıyamet günü onu n kusurunu  örter." +

                "Buhârî, Mezâlim, 3; Müslim, Birr, 58.");

        ilahi.add(" İman etmedikçe cennete giremezsiniz, birbirinizi sevmedikçe de  gerçek anlamda  iman etmiş olamazsınız." +

                "Müslim, Îmân, 93; Tirmizî, Sıfâtu'l-Kıyâme, 56.");

        ilahi.add(" İşçiye ücretini,  alnının  teri kurumadan veriniz." +

                "İbn Mâce, Ruhûn, 4 .");

        ilahi.add(" Rabbinize karşı gelmekten sakının, beş vakit namazınızı kılın, Ramazan orucunuzu tutun," +
                " mallarınızın zekatını verin, yöneticilerinize itaat edin." +
                "  Böylelikle  Rabbinizin cennetine girersiniz." +

                "Tirmizî, Cum'a, 80.");

        ilahi.add(" Hiç şüphe yok ki doğruluk iyiliğe götürür. İyilik de cennete götürür." +
                " Kişi doğru söyleye söyleye Allah katında sıddîk  doğru sözlü  diye yazılır. " +
                "Yalancılık kötüye götürür. Kötülük de cehenneme götürür." +
                " Kişi yalan söyleye söyleye Allah katında kezzâb  çok yalancı  diye yazılır." +

                "Buhârî, Edeb, 69; Müslim, Birr, 103, 104.");

        ilahi.add(" Mümin  kardeşine tebessüm etmen sadakadır. İyiliği emredip kötülükten sakındırman sadakadır." +
                " Yolunu kaybeden kimseye yol göstermen sadakadır. Yoldan taş, diken, " +
                "kemik gibi şeyleri kaldırıp atman da senin için sadakadır." +

                "Tirmizî, Birr, 36.");

        ilahi.add(" Allah sizin ne dış görünüşünüze ne de mallarınıza bakar. Ama o sizin kalplerinize ve işlerinize bakar." +

                "Müslim, Birr, 33; ‹bn Mâce, Zühd, 9; Ahmed b. Hanbel, 2/285, 539.");

        ilahi.add("  Allah'ın rızası, anne ve babanın rızasındadır. Allah'ın öfkesi de anne babanın öfkesindedir." +

                "Tirmizî, Birr, 3.");

        ilahi.add(" Üç dua vardır ki, bunlar şüphesiz kabul edilir: Mazlumun duası, misafirin duası ve babanın evladına duası." +

                "İbn Mâce, Dua, 11.");

        ilahi.add(" Hiçbir baba, çocuğuna, güzel terbiyeden daha üstün bir hediye veremez." +

                "Tirmizî, Birr, 33.");

        ilahi.add(" Peygamberimiz işaret parmağı ve orta parmağıyla işaret ederek: “ Gerek kendisine ve gerekse başkasına ait herhangi bir yetimi görüp gözetmeyi üzerine alan kimse ile ben, cennette işte böyle yanyanayız” buyurmuştur." +

                "Buhârî, Talâk, 25, Edeb, 24; Müslim, Zühd, 42.");

        ilahi.add(" Küçüklerimize merhamet etmeyen, büyüklerimize saygı göstermeyen bizden değildir." +

                "Tirmizî, Birr, 15; Ebû Dâvûd, Edeb, 66");

        ilahi.add(" Sizin en hayırlılarınız, hanımlarına karşı en iyi davrananlarınızdır." +

                "Tirmizî, Radâ', 11; ‹bn Mâce, Nikâh, 50. Tirmizî, Birr, 15; Ebû Dâvûd, Edeb, 66.");

        ilahi.add(" Cebrâil bana komşu hakkında o kadar çok tavsiyede bulundu ki; ben   Allah Teâlâ  komşuyu komşuya mirasçı kılacak zannettim." +

                "Buhârî, Edeb, 28; Müslim, Birr, 140, 141");

        ilahi.add(" Birbirinize buğuz etmeyin, birbirinize haset etmeyin, birbirinize arka çevirmeyin; ey Allah'ın kulları, kardeş olun. Bir müslümana, üç günden fazla" +
                "  din  kardeşi ile dargın durması helal olmaz." +

                "Buhârî, Edeb, 57, 58.");

        ilahi.add(" İnsanı  helâk eden şu yedi şeyden kaçının. Onlar nelerdir ya Resulullah dediler. Bunun üzerine: Allah'a şirk koşmak, sihir, Allah'ın haram kıldığı cana kıymak, faiz yemek, yetim malı yemek, savaştan kaçmak, suçsuz ve namuslu mümin kadınlara iftirada bulunmak buyurdu." +

                "Buhârî, Vasâyâ, 23, Tıbb, 48; Müslim, Îmân, 144");

        ilahi.add(" Allah'a ve ahiret gününe imân eden kimse, komşusuna eziyet etmesin. Allah'a ve ahiret gününe imân eden misafirine ikramda bulunsun. Allah'a ve ahiret gününe imân eden kimse, ya hayır söylesin veya sussun." +

                "Buhârî, Edeb, 31, 85; Müslim, Îmân, 74, 75");

        ilahi.add(" Söz taşıyanlar  cezalarını çekmeden yada affedilmedikçe  cennete giremezler." +

                "Müslim, Îmân, 168; Tirmizî, Birr, 79.");

        ilahi.add(" Dul ve fakirlere yardım eden kimse, Allah yolunda cihad eden veya gündüzleri  nafile  oruç tutup, gecelerini  nafile  ibadetle geçiren kimse gibidir." +

                "Buhârî, Nafakât, 1; Müslim, Zühd, 41; Tirmizî, Birr, 44; Nesâî, Zekât, 78");

        ilahi.add(" Her insan hata eder. Hata işleyenlerin en hayırlıları tevbe edenlerdir." +

                "Tirmizî, Kıyâme, 49; İbn Mâce, Zühd, 30.");

        ilahi.add("  İnsanda bir organ vardır. Eğer o sağlıklı ise bütün vücut sağlıklı olur; eğer o bozulursa bütün vücut bozulur. Dikkat edin! O, kalptir." +

                "Buhârî, Îmân, 39; Müslim, Müsâkât, 107.");

        ilahi.add("  Mü'minin başka hiç kimsede bulunmayan ilginç bir hali vardır; O'nun her işi hayırdır. Eğer bir genişliğe  nimete  kavuşursa şükreder ve bu onun için bir hayır olur. Eğer bir darlığa  musibete  uğrarsa sabreder ve bu da onun için bir hayır olur." +

                "Müslim, Zühd, 64; Dârim”, Rikâk, 61.");

        ilahi.add(" Bir müslümanın diktiği ağaçtan veya ektiği ekinden insan, hayvan ve kuşların yedikleri şeyler, o müslüman için birer sadakadır." +

                "Buhârî, Edeb, 27; Müslim, Müsâkât, 7, 10.");

        ilahi.add(" Bizi aldatan bizden değildir." +

                "Müslim, Îmân, 164.");
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
