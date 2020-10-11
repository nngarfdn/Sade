package com.udindev.sade.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udindev.sade.R;
import com.udindev.sade.model.Location;
import com.udindev.sade.model.Produk;
import com.udindev.sade.reponse.Attributes;
import com.udindev.sade.viewmodel.LocationViewModel;
import com.udindev.sade.viewmodel.ProdukViewModel;
import com.udindev.sade.viewmodel.ProfileViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class TambahProdukFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "TambahProdukFragment";
    private int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private StorageReference storageReference;
    private Button btnChose;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btnTambahProduk;
    private ImageView imgUpload;
    private Spinner spinProvinces;
    private String imgaUrl ;
    private Spinner spinRegencies;
    private Spinner spinDistricts;
    private LocationViewModel locationViewModel;
    private ProdukViewModel produkViewModel;
    private ProfileViewModel profileViewModel;
    private CheckBox cbRegencies;
    private CheckBox cbDistricts;
    private ArrayList<Location> listProvinces, listRegencies, listDistricts;
    private LocationViewModel lvm;
    private EditText edtNamaProduk, edtAlamatProduk, edtNoWA, edtHarga, edtDeskripsi ;


    public TambahProdukFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tambah_produk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChose = view.findViewById(R.id.btn_upload_image);
        imgUpload = view.findViewById(R.id.img_upload_result);
        Spinner kategoriSpinner = view.findViewById(R.id.spinner_kategori);
        spinProvinces = view.findViewById(R.id.spin_provinces);
        spinRegencies = view.findViewById(R.id.spin_regencies);
        spinDistricts = view.findViewById(R.id.spin_districts);
        edtNamaProduk = view.findViewById(R.id.edt_nama_produk);
        edtAlamatProduk = view.findViewById(R.id.edt_alamat_produk);
        edtDeskripsi = view.findViewById(R.id.edt_alamat_deskripsi);
        edtHarga = view.findViewById(R.id.edt_alamat_harga);
        edtNoWA = view.findViewById(R.id.edt_alamat_nowa);

        btnTambahProduk = view.findViewById(R.id.btn_tambah_produk);
        CheckBox cbProvinces = view.findViewById(R.id.cb_provinces);
        cbRegencies = view.findViewById(R.id.cb_regencies);
        cbDistricts = view.findViewById(R.id.cb_districts);


        cbProvinces.setEnabled(true);
        cbRegencies.setEnabled(false);
        cbDistricts.setEnabled(false);
        spinProvinces.setEnabled(false);
        spinRegencies.setEnabled(false);
        spinDistricts.setEnabled(false);

        cbProvinces.setOnCheckedChangeListener(this);
        cbRegencies.setOnCheckedChangeListener(this);
        cbDistricts.setOnCheckedChangeListener(this);
        spinProvinces.setOnItemSelectedListener(this);
        spinRegencies.setOnItemSelectedListener(this);
        spinDistricts.setOnItemSelectedListener(this);

        lvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LocationViewModel.class);
        loadProvinces();

        btnChose.setOnClickListener(v -> selectImage());
        List<String> spinnerKategori = new ArrayList<>();
        List<String> spinnerProvinsi = new ArrayList<>();
        List<Integer> idProvinsi = new ArrayList<>();
        addAdapterKategoriSpinner(spinnerKategori);

        ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, spinnerKategori);
        kategoriSpinner.setAdapter(adapterKategori);

        btnTambahProduk.setOnClickListener(v -> {
            StorageReference ref
                    = storageReference
                    .child("images/");

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgaUrl = String.valueOf(uri);

                            String id = "";
                            String email = firebaseUser.getEmail();
                            String nama = edtNamaProduk.getText().toString();
                            String kategori = kategoriSpinner.getSelectedItem().toString();
                            String alamat = edtAlamatProduk.getText().toString();
                            String kecamatan = spinDistricts.getSelectedItem().toString();
                            String kabupaten = spinRegencies.getSelectedItem().toString();
                            String prov = spinProvinces.getSelectedItem().toString();
                            String wa = edtNoWA.getText().toString();
                            String harga = edtHarga.getText().toString();
                            int hargaInt = Integer.parseInt(harga);
                            String deskripsi = edtDeskripsi.getText().toString();
                            String photo = imgaUrl;
                            Produk produk = new Produk(id,email, nama, kategori, alamat, kecamatan,
                                    kabupaten, prov, wa ,hargaInt, deskripsi,photo);

                            produkViewModel.insertProduk(produk);

                            Log.d(TAG, "onSuccess: "+imgaUrl);


                            Toast.makeText(getContext(), "imgaUrl : "+ imgaUrl, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });





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

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            String uuid = UUID.randomUUID().toString();
            StorageReference ref
                    = storageReference
                    .child("images/");

            // adding listeners on upload
            // or failure of image
            // Progress Listener for loading
// percentage on the dialog box
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(getContext(),
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(getContext(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");
                            });



        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean b) {

        switch (buttonView.getId()) {
            case R.id.cb_provinces:
                spinProvinces.setEnabled(b);
                cbRegencies.setEnabled(b);
                if (!b) {
                    cbRegencies.setChecked(false);
                    cbDistricts.setChecked(false);
                }
                break;

            case R.id.cb_regencies:
                spinRegencies.setEnabled(b);
                cbDistricts.setEnabled(b);
                if (!b) cbDistricts.setChecked(false);
                break;

            case R.id.cb_districts:
                spinDistricts.setEnabled(b);
                break;
        }

    }

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
}