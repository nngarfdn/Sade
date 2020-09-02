package com.udindev.sade.model

data class Produk(
        var id : String? = "",
        val nama : String? = "",
        val kategori : String? = "",
        val alamat  : String? = "",
        val kecamatan : String? = "",
        val kabupaten : String? = "",
        val provinsi : String? = "",
        val wa : String? = "",
        var harga : Int? = 0,
        val deskripsi : String? = "",
        val photo : String?=""
        )
