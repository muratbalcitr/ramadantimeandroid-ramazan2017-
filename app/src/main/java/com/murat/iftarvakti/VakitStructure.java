package com.murat.iftarvakti;

/**
 * Created by murat on 11.04.2017.
 */

public class VakitStructure {
    private String gun, tarih, sabah, gunes, oglen, ikindi, aksam, yatsi, sehir;

    public VakitStructure() {
    }

    public VakitStructure(String gun, String tarih, String sabah, String gunes, String oglen, String ikindi, String aksam, String yatsi, String sehir) {
        setGun(gun);
        setTarih(tarih);
        setSabah(sabah);
        setGunes(gunes);
        setOglen(oglen);
        setIkindi(ikindi);
        setAksam(aksam);
        setYatsi(yatsi);
        setSehir(sehir);
    }

    public String getAksam() {
        return aksam;
    }

    public void setAksam(String aksam) {
        this.aksam = aksam;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getGun() {
        return gun;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public String getGunes() {
        return gunes;
    }

    public void setGunes(String gunes) {
        this.gunes = gunes;
    }

    public String getIkindi() {
        return ikindi;
    }

    public void setIkindi(String ikindi) {
        this.ikindi = ikindi;
    }

    public String getOglen() {
        return oglen;
    }

    public void setOglen(String oglen) {
        this.oglen = oglen;
    }

    public String getSabah() {
        return sabah;
    }

    public void setSabah(String sabah) {
        this.sabah = sabah;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public void setYatsi(String yatsi) {
        this.yatsi = yatsi;
    }

    public String getYatsi() {
        return yatsi;
    }
}
