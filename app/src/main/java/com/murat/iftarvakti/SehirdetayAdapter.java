package com.murat.iftarvakti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by murat on 15.04.2017.
 */

public class SehirdetayAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<VakitStructure> sehirdetayStructure;

    public SehirdetayAdapter(Activity activity, ArrayList<VakitStructure> sehirdetay) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        layoutInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        sehirdetayStructure = sehirdetay;
    }

    @Override
    public int getCount() {
        return sehirdetayStructure.size();
    }

    @Override
    public Object getItem(int position) {
        return sehirdetayStructure.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;
        VakitStructure vakit = new VakitStructure();
        satirView = layoutInflater.inflate(R.layout.gundetay, null);
        TextView tvSabah =(TextView) satirView.findViewById(R.id.tvSabah);
        TextView tvOglen =(TextView) satirView.findViewById(R.id.tvOglen);
        TextView tvIkindi =(TextView) satirView.findViewById(R.id.tvIkindi);
        TextView tvAksam =(TextView) satirView.findViewById(R.id.tvAksam);
        TextView tvYatsi =(TextView) satirView.findViewById(R.id.tvYatsi);
        TextView tvGun =(TextView) satirView.findViewById(R.id.tvGunSayisi);
        Button tarih =(Button) satirView.findViewById(R.id.btnTarih);
        tarih.setText(sehirdetayStructure.get(position).getTarih());
        tvSabah.setText(sehirdetayStructure.get(position).getSabah());
        tvOglen.setText(sehirdetayStructure.get(position).getOglen());
        tvIkindi.setText(sehirdetayStructure.get(position).getIkindi());
        tvAksam.setText(sehirdetayStructure.get(position).getAksam());
        tvYatsi.setText(sehirdetayStructure.get(position).getYatsi());
        tvGun.setText(sehirdetayStructure.get(position).getGun()+".gün");
        return satirView;
    }
}
