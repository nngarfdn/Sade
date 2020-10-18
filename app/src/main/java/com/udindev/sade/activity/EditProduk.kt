package com.udindev.sade.activity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.model.Location
import com.udindev.sade.model.Produk
import com.udindev.sade.reponse.Districts
import com.udindev.sade.reponse.Provinces
import com.udindev.sade.reponse.Regencies
import com.udindev.sade.viewmodel.LocationViewModel
import com.udindev.sade.viewmodel.ProdukViewModel
import kotlinx.android.synthetic.main.activity_edit_produk.*
import java.io.IOException
import java.util.*

class EditProduk : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var firebaseUser: FirebaseUser? = null
    private var produkViewModel: ProdukViewModel? = null
    var objectStorageReference: StorageReference? = null
    var objectFirebaseFirestore: FirebaseFirestore? = null
    private var lvm: LocationViewModel? = null
    private var listProvinces: ArrayList<Location>? = null
    private var listRegencies: ArrayList<Location>? = null
    private var listDistricts: ArrayList<Location>? = null
    private val PICK_IMAGE_REQUEST = 22
    private var filePath: Uri? = null
    private lateinit var produk: Produk


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel::class.java)
        lvm = ViewModelProvider(this, NewInstanceFactory()).get(LocationViewModel::class.java)
        objectStorageReference = FirebaseStorage.getInstance().getReference("imageFolder") // Create folder to Firebase Storage
        objectFirebaseFirestore = FirebaseFirestore.getInstance()

        produk = intent.getParcelableExtra<Produk>(DetailActivity.EXTRA_PRODUK)!!

        loadProvinces()

        spin_provinces.setEnabled(true)
        spin_regencies.setEnabled(true)
        spin_districts.setEnabled(true)

        spin_provinces.setOnItemSelectedListener(this)
        spin_regencies.setOnItemSelectedListener(this)
        spin_districts.setOnItemSelectedListener(this)

        edt_nama_produk.setText(produk?.nama)
        edt_alamat_produk.setText(produk?.alamat)
        edt_alamat_nowa.setText(produk?.wa)
        edt_alamat_harga.setText(produk?.harga.toString())
        edt_alamat_deskripsi.setText(produk?.deskripsi)


        var kategori = spinner_kategori.selectedItem.toString()
        var select = 0

        when (kategori) {
            "Usaha" -> select = 0
            "Jasa" -> select = 1
            "Produk" -> select = 2
            "Lainnya" -> select = 3
            else -> select = 1
        }

        spinner_kategori.setSelection(select)

        Picasso.get()
                .load(produk?.photo)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(img_upload_result)



        btn_upload_image.setOnClickListener {
            selectImage()
        }

        imgbtn_delete_produk.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hapus produk")
            builder.setMessage("Apakah kamu yakin ingin menghapus ?")

            builder.setPositiveButton("Ya") { dialog, which ->
                produkViewModel!!.deteteProduk(produk.id.toString())
                Toast.makeText(this, "Berhasil Menghapus Produk", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            builder.setNegativeButton("Tidak") { dialog, which ->
            }

            builder.show()

        }

        btn_tambah_produk.setOnClickListener(View.OnClickListener { v: View? ->
            val email = firebaseUser!!.email
            val nama: String = edt_nama_produk.getText().toString()
            val kategori: String = spinner_kategori.getSelectedItem().toString()
            val alamat: String = edt_alamat_produk.getText().toString()
            var kecamatan: String? = null
            kecamatan = if (spin_districts != null && spin_districts.getSelectedItem() != null) {
                spin_districts.getSelectedItem() as String
            } else {
                "-"
            }

            var kabupaten: String? = null
            kabupaten = if (spin_regencies != null && spin_regencies.getSelectedItem() != null) {
                spin_regencies.getSelectedItem() as String
            } else {
                "-"
            }

            var prov: String? = null
            prov = if (spin_provinces != null && spin_provinces.getSelectedItem() != null) {
                spin_provinces.getSelectedItem() as String
            } else {
                "-"
            }

            val wa: String = edt_alamat_nowa.getText().toString()
            val harga: String = edt_alamat_harga.getText().toString()
            val hargaInt = harga.toInt()
            val deskripsi: String = edt_alamat_deskripsi.getText().toString()

            var cek = true
            if (nama.length <= 0) {
                edt_nama_produk.setError("Masukan nama produk/barang/jasa")
                cek = false
            }

            if (TextUtils.isEmpty(alamat)) {
                edt_alamat_produk.setError("Masukan alamat produk/barang/jasa")
                cek = false
            }

            if (TextUtils.isEmpty(wa)) {
                edt_alamat_nowa.setError("Masukan nomer Whatsapp")
                cek = false
            }

            if (wa[0] != '6' && wa[1] != '2') {
                edt_alamat_nowa.setError("Awali nomer dengan 628xxx")
                cek = false
            }

            if (TextUtils.isEmpty(harga)) {
                edt_alamat_harga.setError("Masukan harga")
                cek = false
            }

            if (TextUtils.isEmpty(deskripsi)) {
                edt_alamat_deskripsi.setError("Masukan deskripsi")
                cek = false
            }

            if (TextUtils.isEmpty(produk.photo)) {
                Toast.makeText(this, "Upload foto dulu", Toast.LENGTH_SHORT).show()
                cek = false
            }
            assert(email != null)
            val p = Produk(produk.id, email!!, nama, kategori, alamat, kecamatan,
                    kabupaten, prov, wa, hargaInt, deskripsi, produk.photo)

            if (cek){
                produkViewModel!!.updateProduk(p)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        })

    }

    private fun selectImage() {
        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
        when (parent!!.id) {
            R.id.spin_provinces -> {
                val idProvince: Int = listProvinces?.get(i)!!.getId()
                loadRegencies(idProvince)
            }
            R.id.spin_regencies -> {
                val idRegency: Int = listRegencies?.get(i)!!.getId()
                loadDistricts(idRegency)
            }
            R.id.spin_districts -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun loadProvinces() {
        lvm?.loadProvinces()
        lvm?.getProvinces()?.observe(this, Observer<Provinces> { provinces: Provinces? ->
            if (provinces != null) {
                listProvinces = ArrayList<Location>()
                val itemList: MutableList<String> = ArrayList()
                for (attributes in provinces.provinces) { // Fix nama provinsi
                    if (attributes.id == 31) listProvinces!!.add(Location(attributes.id, "DKI Jakarta")) else if (attributes.id == 34) listProvinces!!.add(Location(attributes.id, "DI Yogyakarta")) else listProvinces!!.add(Location(attributes.id, attributes.name))
                }
                for (location in listProvinces!!) itemList.add(location.name)
                val adapter = ArrayAdapter(Objects.requireNonNull(this), android.R.layout.simple_spinner_item, itemList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spin_provinces.setAdapter(adapter)
            }
        })
    }

    private fun loadRegencies(idProvince: Int) {
        lvm?.loadRegencies(idProvince)
        lvm?.getRegencies()?.observe(this, Observer<Regencies> { regencies: Regencies? ->
            if (regencies != null) {
                listRegencies = ArrayList<Location>()
                val itemList: MutableList<String> = ArrayList()
                for (attributes in regencies.regencies) listRegencies!!.add(Location(attributes.id, attributes.name))
                for (location in listRegencies!!) itemList.add(location.name)
                val adapter = ArrayAdapter(Objects.requireNonNull(this), android.R.layout.simple_spinner_item, itemList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spin_regencies.setAdapter(adapter)
            }
        })
    }

    private fun loadDistricts(idRegency: Int) {
        lvm?.loadDistricts(idRegency)
        lvm?.getDistricts()?.observe(this, Observer<Districts> { districts: Districts? ->
            if (districts != null) {
                listDistricts = ArrayList<Location>()
                val itemList: MutableList<String> = ArrayList()
                for (attributes in districts.districts) listDistricts!!.add(Location(attributes.id, attributes.name))
                for (location in listDistricts!!) itemList.add(location.name)
                val adapter = ArrayAdapter(Objects.requireNonNull(this), android.R.layout.simple_spinner_item, itemList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spin_districts.setAdapter(adapter)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            // Get the Uri of data
            filePath = data.data
            try {
                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                        .getBitmap(
                                Objects.requireNonNull(this).getContentResolver(),
                                filePath)
                img_upload_result.setImageBitmap(bitmap)
                btn_upload_image.setText("Upload")
                btn_upload_image.setOnClickListener(View.OnClickListener { v: View? -> uploadImage() })
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            txt_uploading.setVisibility(View.VISIBLE)
            val namaImage = UUID.randomUUID().toString() // diganti id produk di firebase
            val nameOfimage = namaImage + "." + getExtension(filePath!!)
            val imageRef = objectStorageReference!!.child(nameOfimage)
            val objectUploadTask = imageRef.putFile(filePath!!)
            objectUploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri>> { task: Task<UploadTask.TaskSnapshot?> ->
                if (!task.isSuccessful) {
                    throw Objects.requireNonNull(task.exception)!!
                }
                imageRef.downloadUrl
            }).addOnCompleteListener { task: Task<Uri?> ->
                if (task.isSuccessful) {
                    txt_uploading.setVisibility(View.INVISIBLE)
                    Toast.makeText(this, "Upload Gamabr Berhasil", Toast.LENGTH_SHORT).show()
                    btn_tambah_produk.setOnClickListener(View.OnClickListener { v: View? ->
                        val photo = Objects.requireNonNull(task.result).toString()
                        Log.d(TAG, "uploadImage: photoUrl : $photo")
                        val email = firebaseUser!!.email
                        val nama: String = edt_nama_produk.getText().toString()
                        val kategori: String = spinner_kategori.getSelectedItem().toString()
                        val alamat: String = edt_alamat_produk.getText().toString()
                        var kecamatan: String? = null
                        kecamatan = if (spin_districts != null && spin_districts.getSelectedItem() != null) {
                            spin_districts.getSelectedItem() as String
                        } else {
                            "-"
                        }
                        var kabupaten: String? = null
                        kabupaten = if (spin_regencies != null && spin_regencies.getSelectedItem() != null) {
                            spin_regencies.getSelectedItem() as String
                        } else {
                            "-"
                        }

                        var prov: String? = null
                        prov = if (spin_provinces != null && spin_provinces.getSelectedItem() != null) {
                            spin_provinces.getSelectedItem() as String
                        } else {
                            "-"
                        }

                        val wa: String = edt_alamat_nowa.getText().toString()
                        val harga: String = edt_alamat_harga.getText().toString()
                        val hargaInt = harga.toInt()
                        val deskripsi: String = edt_alamat_deskripsi.getText().toString()

                        var cek = true
                        if (nama.length <= 0) {
                            edt_nama_produk.setError("Masukan nama produk/barang/jasa")
                            cek = false
                        }

                        if (TextUtils.isEmpty(alamat)) {
                            edt_alamat_produk.setError("Masukan alamat produk/barang/jasa")
                            cek = false
                        }

                        if (TextUtils.isEmpty(wa)) {
                            edt_alamat_nowa.setError("Masukan nomer Whatsapp")
                            cek = false
                        }

                        if (wa[0] != '6' && wa[1] != '2') {
                            edt_alamat_nowa.setError("Awali nomer dengan 628xxx")
                            cek = false
                        }

                        if (TextUtils.isEmpty(harga)) {
                            edt_alamat_harga.setError("Masukan harga")
                            cek = false
                        }

                        if (TextUtils.isEmpty(deskripsi)) {
                            edt_alamat_deskripsi.setError("Masukan deskripsi")
                            cek = false
                        }

                        if (TextUtils.isEmpty(photo)) {
                            Toast.makeText(this, "Upload foto dulu", Toast.LENGTH_SHORT).show()
                            cek = false
                        }
                        assert(email != null)
                        val p = Produk(produk.id, email!!, nama, kategori, alamat, kecamatan,
                                kabupaten, prov, wa, hargaInt, deskripsi, photo)

                        if (cek){
                            produkViewModel!!.updateProduk(p)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    })
                } else if (!task.isSuccessful) {
                    Toast.makeText(this, Objects.requireNonNull(task.exception).toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getExtension(uri: Uri): String? {
        try {
            val objectContentResolver: ContentResolver = Objects.requireNonNull(this).getContentResolver()
            val objectMimeTypeMap = MimeTypeMap.getSingleton()
            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        return null
    }


    companion object {
        private const val TAG = "EditProduk"
    }


}