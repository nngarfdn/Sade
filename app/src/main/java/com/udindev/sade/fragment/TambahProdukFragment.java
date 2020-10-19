package com.udindev.sade.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udindev.sade.R;
import com.udindev.sade.callback.OnProductAddCallback;
import com.udindev.sade.model.Location;
import com.udindev.sade.model.Produk;
import com.udindev.sade.reponse.Attributes;
import com.udindev.sade.viewmodel.LocationViewModel;
import com.udindev.sade.viewmodel.ProdukViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.udindev.sade.utils.AppUtils.isValidPhone;


public class TambahProdukFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TambahProdukFragment";
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private Button btnChose;
    private FirebaseUser firebaseUser;
    private Button btnTambahProduk;
    private ImageView imgUpload;
    private Spinner spinProvinces;
    private Spinner spinRegencies;
    private Spinner spinDistricts;
    private ProdukViewModel produkViewModel;
    TextView txtUploading;
    Spinner kategoriSpinner;
    private ArrayList<Location> listProvinces, listRegencies, listDistricts;
    private LocationViewModel lvm;
    private EditText edtNamaProduk, edtAlamatProduk, edtNoWA, edtHarga, edtDeskripsi;
    private final OnProductAddCallback callback;

    StorageReference objectStorageReference;
    FirebaseFirestore objectFirebaseFirestore;

    public TambahProdukFragment(OnProductAddCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);

        objectStorageReference = FirebaseStorage.getInstance().getReference("imageFolder"); // Create folder to Firebase Storage
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tambah_produk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChose = view.findViewById(R.id.btn_upload_image);
        imgUpload = view.findViewById(R.id.img_upload_result);
        kategoriSpinner = view.findViewById(R.id.spinner_kategori);
        spinProvinces = view.findViewById(R.id.spin_provinces);
        spinRegencies = view.findViewById(R.id.spin_regencies);
        spinDistricts = view.findViewById(R.id.spin_districts);
        edtNamaProduk = view.findViewById(R.id.edt_nama_produk);
        edtAlamatProduk = view.findViewById(R.id.edt_alamat_produk);
        edtDeskripsi = view.findViewById(R.id.edt_alamat_deskripsi);
        edtHarga = view.findViewById(R.id.edt_alamat_harga);
        edtNoWA = view.findViewById(R.id.edt_alamat_nowa);
        txtUploading = view.findViewById(R.id.txt_uploading);

        btnTambahProduk = view.findViewById(R.id.btn_tambah_produk);

        spinProvinces.setEnabled(true);
        spinRegencies.setEnabled(true);
        spinDistricts.setEnabled(true);

        spinProvinces.setOnItemSelectedListener(this);
        spinRegencies.setOnItemSelectedListener(this);
        spinDistricts.setOnItemSelectedListener(this);

        lvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LocationViewModel.class);
        loadProvinces();

        btnChose.setOnClickListener(v -> selectImage());
        List<String> spinnerKategori = new ArrayList<>();

        addAdapterKategoriSpinner(spinnerKategori);

        ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, spinnerKategori);
        kategoriSpinner.setAdapter(adapterKategori);

        btnTambahProduk.setOnClickListener(v -> {
            String nama = edtNamaProduk.getText().toString();
            String alamat = edtAlamatProduk.getText().toString();
            String wa = edtNoWA.getText().toString();
            String harga = edtHarga.getText().toString();
            String deskripsi = edtDeskripsi.getText().toString();
            if (nama.length() <= 0) {
                edtNamaProduk.setError("Masukkan nama produk/barang/jasa");
                Toast.makeText(getContext(), "Masukkan nama produk/barang/jasa", Toast.LENGTH_SHORT).show();
            }

            if (TextUtils.isEmpty(alamat)) {
                edtAlamatProduk.setError("Masukkan alamat produk/barang/jasa");
            }

            if (TextUtils.isEmpty(wa)) {
                edtNoWA.setError("Masukkan nomor WhatsApp");
            }

            if (!TextUtils.isEmpty(wa)) {
                if (!isValidPhone(wa)) {
                    edtNoWA.setError("Awali nomor dengan 628xxx");
                }
            }

            if (TextUtils.isEmpty(harga)) {
                edtHarga.setError("Masukkan harga");
            }

            if (TextUtils.isEmpty(deskripsi)) {
                edtDeskripsi.setError("Masukkan deskripsi");
            }

            Toast.makeText(getContext(), "Pastikan Foto telah diupload", Toast.LENGTH_SHORT).show();

        });

    }


    private void loadProvinces() {
        lvm.loadProvinces();
        lvm.getProvinces().observe(this, provinces -> {
            if (provinces != null) {
                listProvinces = new ArrayList<>();
                List<String> itemList = new ArrayList<>();
                for (Attributes attributes : provinces.getProvinces()) { // Fix nama provinsi
                    if (attributes.getId() == 31)
                        listProvinces.add(new Location(attributes.getId(), "DKI Jakarta"));
                    else if (attributes.getId() == 34)
                        listProvinces.add(new Location(attributes.getId(), "DI Yogyakarta"));
                    else listProvinces.add(new Location(attributes.getId(), attributes.getName()));
                }
                for (Location location : listProvinces) itemList.add(location.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, itemList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinProvinces.setAdapter(adapter);
            }
        });
    }

    private void loadRegencies(int idProvince) {
        lvm.loadRegencies(idProvince);
        lvm.getRegencies().observe(this, regencies -> {
            if (regencies != null) {
                listRegencies = new ArrayList<>();
                List<String> itemList = new ArrayList<>();
                for (Attributes attributes : regencies.getRegencies())
                    listRegencies.add(new Location(attributes.getId(), attributes.getName()));
                for (Location location : listRegencies) itemList.add(location.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, itemList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinRegencies.setAdapter(adapter);
            }

        });
    }

    private void loadDistricts(int idRegency) {
        lvm.loadDistricts(idRegency);
        lvm.getDistricts().observe(this, districts -> {
            if (districts != null) {
                listDistricts = new ArrayList<>();
                List<String> itemList = new ArrayList<>();
                for (Attributes attributes : districts.getDistricts())
                    listDistricts.add(new Location(attributes.getId(), attributes.getName()));
                for (Location location : listDistricts) itemList.add(location.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, itemList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinDistricts.setAdapter(adapter);
            }
        });
    }

    private void addAdapterKategoriSpinner(List<String> spinnerKategori) {
        spinnerKategori.add("Usaha");
        spinnerKategori.add("Jasa");
        spinnerKategori.add("Produk");
        spinnerKategori.add("Lainnya");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                Objects.requireNonNull(getContext()).getContentResolver(),
                                filePath);
                imgUpload.setImageBitmap(bitmap);
                btnChose.setText("Upload");
                btnChose.setOnClickListener(v -> uploadImage());

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if (filePath != null) {

            txtUploading.setVisibility(View.VISIBLE);
            String namaImage = UUID.randomUUID().toString(); // diganti id produk di firebase

            String nameOfimage = namaImage + "." + getExtension(filePath);

            final StorageReference imageRef = objectStorageReference.child(nameOfimage);

            UploadTask objectUploadTask = imageRef.putFile(filePath);


            objectUploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    txtUploading.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show();
                    btnTambahProduk.setOnClickListener(v -> {
                        String photo = Objects.requireNonNull(task.getResult()).toString();

                        Log.d(TAG, "uploadImage: photoUrl : " + photo);
                        String id = "";
                        String email = firebaseUser.getEmail();
                        String nama = edtNamaProduk.getText().toString();
                        String kategori = kategoriSpinner.getSelectedItem().toString();
                        String alamat = edtAlamatProduk.getText().toString();
                        String kecamatan;
                        if(spinDistricts != null && spinDistricts.getSelectedItem() !=null ) {
                            kecamatan = (String)spinDistricts.getSelectedItem();
                        } else  {
                            kecamatan = "-";
                        }

                        String kabupaten;
                        if(spinRegencies != null && spinRegencies.getSelectedItem() !=null ) {
                            kabupaten = (String)spinRegencies.getSelectedItem();
                        } else  {
                            kabupaten = "-";
                        }

                        String prov;
                        if(spinProvinces != null && spinProvinces.getSelectedItem() !=null ) {
                            prov = (String)spinProvinces.getSelectedItem();
                        } else  {
                            prov = "-";
                        }

                        String wa = edtNoWA.getText().toString();
                        String harga = edtHarga.getText().toString();
                        String deskripsi = edtDeskripsi.getText().toString();

                        int hargaInt;
                        if (Long.parseLong(harga.trim()) <= Integer.MAX_VALUE) hargaInt = Integer.parseInt(harga);
                        else{
                            edtHarga.setError("Harga barang terlalu besar");
                            return;
                        }

                        if (nama.length() <= 0) {
                            edtNamaProduk.setError("Masukkan nama produk/barang/jasa");
                            Toast.makeText(getContext(), "Masukkan nama produk/barang/jasa", Toast.LENGTH_SHORT).show();
                        }

                        if (TextUtils.isEmpty(alamat)) {
                            edtAlamatProduk.setError("Masukkan alamat produk/barang/jasa");
                        }
                        
                        if (TextUtils.isEmpty(wa)) {
                            edtNoWA.setError("Masukkan nomor WhatsApp");
                        }

                        if (!isValidPhone(wa)) {
                            edtNoWA.setError("Awali nomor dengan 628xxx");
                        }

                        if (TextUtils.isEmpty(harga)) {
                            edtHarga.setError("Masukkan harga");
                        }

                        if (TextUtils.isEmpty(deskripsi)) {
                            edtDeskripsi.setError("Masukkan deskripsi");
                        }

                        if (TextUtils.isEmpty(photo)){
                            Toast.makeText(getContext(), "Upload foto dulu", Toast.LENGTH_SHORT).show();
                        }
                        assert email != null;
                        Produk produk = new Produk(id, email, nama, kategori, alamat, kecamatan,
                                kabupaten, prov, wa, hargaInt, deskripsi, photo);
                        produkViewModel.insertProduk(produk);

                        if (callback != null) callback.onAdd();
                        if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
                    });


                } else if (!task.isSuccessful()) {

                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
        switch (parent.getId()) {
            case R.id.spin_provinces:
                int idProvince = listProvinces.get(i).getId();
                loadRegencies(idProvince);
                break;

            case R.id.spin_regencies:
                int idRegency = listRegencies.get(i).getId();
                loadDistricts(idRegency);
                break;

            case R.id.spin_districts:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getExtension(Uri uri) {
        try {
            ContentResolver objectContentResolver = Objects.requireNonNull(getContext()).getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}