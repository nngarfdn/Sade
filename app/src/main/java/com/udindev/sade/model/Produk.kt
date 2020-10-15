package com.udindev.sade.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Produk(
        var id : String? = "",
        var email : String = "",
        var nama : String? = "",
        var kategori : String? = "",
        var alamat  : String? = "",
        var kecamatan : String? = "",
        var kabupaten : String? = "",
        var provinsi : String? = "",
        var wa : String? = "",
        var harga : Int? = 0,
        var deskripsi : String? = "",
        var photo : String? = ""
        ) : Parcelable
