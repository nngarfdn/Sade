package com.udindev.sade.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udindev.sade.R
import com.udindev.sade.adapter.KontenPusatBantuan
import com.udindev.sade.adapter.PusatBantuanAdapter
import com.udindev.sade.model.PusatBantuan
import kotlinx.android.synthetic.main.activity_pusat_bantuan.*

class PusatBantuanActivity : AppCompatActivity() {
    private lateinit var adapter : PusatBantuanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pusat_bantuan)

        val allPusatBantuan : ArrayList<PusatBantuan> = arrayListOf()

        addData(allPusatBantuan)

        adapter = PusatBantuanAdapter(allPusatBantuan)
        
        rv_pusat_bantuan.adapter = adapter
        initRecView()
    }

    private fun addData(allPusatBantuan: java.util.ArrayList<PusatBantuan>) {
        for (item in 0..KontenPusatBantuan.judulPusatBantuan.size - 1) {
            val nama = KontenPusatBantuan.judulPusatBantuan[item]
            val deskripsi = KontenPusatBantuan.deskripsiPusatBantuan[item]
            val pusatBantuan = PusatBantuan(nama, deskripsi)
            allPusatBantuan.add(pusatBantuan)
        }
    }

    private fun initRecView() {
        rv_pusat_bantuan.layoutManager = LinearLayoutManager(this)
        rv_pusat_bantuan.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
    }
}