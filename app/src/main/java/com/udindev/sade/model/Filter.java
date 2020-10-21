package com.udindev.sade.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Filter implements Parcelable {
    private String kataKunci;
    private boolean produk;
    private boolean jasa;
    private boolean usaha;
    private boolean lainnya;
    private boolean provinsi;
    private boolean kabupaten;
    private boolean kecamatan;
    private boolean murah;
    private String namaProvinsi;
    private String namaKabupaten;
    private String namaKecamatan;
    private final int posisiProvinsi;
    private final int posisiKabupaten;
    private final int posisiKecamatan;

    public Filter(String kataKunci, boolean produk, boolean jasa, boolean usaha, boolean lainnya, boolean provinsi, boolean kabupaten, boolean kecamatan, boolean murah, String namaProvinsi, String namaKabupaten, String namaKecamatan, int posisiProvinsi, int posisiKabupaten, int posisiKecamatan) {
        this.kataKunci = kataKunci;
        this.produk = produk;
        this.jasa = jasa;
        this.usaha = usaha;
        this.lainnya = lainnya;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.murah = murah;
        this.namaProvinsi = namaProvinsi;
        this.namaKabupaten = namaKabupaten;
        this.namaKecamatan = namaKecamatan;
        this.posisiProvinsi = posisiProvinsi;
        this.posisiKabupaten = posisiKabupaten;
        this.posisiKecamatan = posisiKecamatan;
    }

    protected Filter(Parcel in) {
        kataKunci = in.readString();
        produk = in.readByte() != 0;
        jasa = in.readByte() != 0;
        usaha = in.readByte() != 0;
        lainnya = in.readByte() != 0;
        provinsi = in.readByte() != 0;
        kabupaten = in.readByte() != 0;
        kecamatan = in.readByte() != 0;
        murah = in.readByte() != 0;
        namaProvinsi = in.readString();
        namaKabupaten = in.readString();
        namaKecamatan = in.readString();
        posisiProvinsi = in.readInt();
        posisiKabupaten = in.readInt();
        posisiKecamatan = in.readInt();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public String getKataKunci() {
        return kataKunci;
    }

    public void setKataKunci(String kataKunci) {
        this.kataKunci = kataKunci;
    }

    public boolean isProduk() {
        return produk;
    }

    public void setProduk(boolean produk) {
        this.produk = produk;
    }

    public boolean isJasa() {
        return jasa;
    }

    public void setJasa(boolean jasa) {
        this.jasa = jasa;
    }

    public boolean isUsaha() {
        return usaha;
    }

    public void setUsaha(boolean usaha) {
        this.usaha = usaha;
    }

    public boolean isLainnya() {
        return lainnya;
    }

    public void setLainnya(boolean lainnya) {
        this.lainnya = lainnya;
    }

    public boolean isProvinsi() {
        return provinsi;
    }

    public void setProvinsi(boolean provinsi) {
        this.provinsi = provinsi;
    }

    public boolean isKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(boolean kabupaten) {
        this.kabupaten = kabupaten;
    }

    public boolean isKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(boolean kecamatan) {
        this.kecamatan = kecamatan;
    }

    public boolean isMurah() {
        return murah;
    }

    public void setMurah(boolean murah) {
        this.murah = murah;
    }

    public String getNamaProvinsi() {
        return namaProvinsi;
    }

    public void setNamaProvinsi(String namaProvinsi) {
        this.namaProvinsi = namaProvinsi;
    }

    public String getNamaKabupaten() {
        return namaKabupaten;
    }

    public void setNamaKabupaten(String namaKabupaten) {
        this.namaKabupaten = namaKabupaten;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public void setNamaKecamatan(String namaKecamatan) {
        this.namaKecamatan = namaKecamatan;
    }

    public int getPosisiProvinsi() {
        return posisiProvinsi;
    }

    public int getPosisiKabupaten() {
        return posisiKabupaten;
    }

    public int getPosisiKecamatan() {
        return posisiKecamatan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(kataKunci);
        parcel.writeByte((byte) (produk ? 1 : 0));
        parcel.writeByte((byte) (jasa ? 1 : 0));
        parcel.writeByte((byte) (usaha ? 1 : 0));
        parcel.writeByte((byte) (lainnya ? 1 : 0));
        parcel.writeByte((byte) (provinsi ? 1 : 0));
        parcel.writeByte((byte) (kabupaten ? 1 : 0));
        parcel.writeByte((byte) (kecamatan ? 1 : 0));
        parcel.writeByte((byte) (murah ? 1 : 0));
        parcel.writeString(namaProvinsi);
        parcel.writeString(namaKabupaten);
        parcel.writeString(namaKecamatan);
        parcel.writeInt(posisiProvinsi);
        parcel.writeInt(posisiKabupaten);
        parcel.writeInt(posisiKecamatan);
    }
}
