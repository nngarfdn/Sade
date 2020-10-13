package com.udindev.sade.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Produk(
        var id : String? = "",
        var email : String = "",
        val nama : String? = "",
        val kategori : String? = "",
        val alamat  : String? = "",
        val kecamatan : String? = "",
        val kabupaten : String? = "",
        val provinsi : String? = "",
        val wa : String? = "",
        var harga : Int? = 0,
        val deskripsi : String? = "",
        val photo : ByteArray? = null,
        val isFavorite : Boolean? = false
        ) : Parcelable
