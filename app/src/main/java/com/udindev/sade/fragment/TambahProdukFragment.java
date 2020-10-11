package com.udindev.sade.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udindev.sade.R;
import com.udindev.sade.reponse.Attributes;
import com.udindev.sade.viewmodel.LocationViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class TambahProdukFragment extends Fragment {

    private int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private StorageReference storageReference;
    private Button btnChose;
    private ImageView imgUpload;
    private Spinner kategoriSpinner, provinsiSpinner, kabupatenSpinner, kecamatanSpinner;
    private LocationViewModel locationViewModel;
    private String selectedProvinsi = "Riau" ;


    public TambahProdukFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

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
        kategoriSpinner = view.findViewById(R.id.spinner_kategori);
        provinsiSpinner = view.findViewById(R.id.spinner_provinsi);
        kabupatenSpinner = view.findViewById(R.id.spinner_kabupaten);
        kecamatanSpinner = view.findViewById(R.id.spinner_kecamatann);


        btnChose.setOnClickListener(v -> selectImage());
        List<String> spinnerKategori = new ArrayList<>();
        List<String> spinnerProvinsi = new ArrayList<>();
        List<Integer> idProvinsi = new ArrayList<>();
        List<String> spinnerKabupaten = new ArrayList<>();
        List<String> spinnerKecamatan = new ArrayList<>();
        addAdapterKategoriSpinner(spinnerKategori);

        ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, spinnerKategori);
        kategoriSpinner.setAdapter(adapterKategori);


        locationViewModel.loadProvinces();
        locationViewModel.getProvinces().observe(this, result -> {

            ArrayList<Attributes> attributesProvinsi = result.getProvinces();

            for (int i = 0; i < attributesProvinsi.size(); i++) {
                spinnerProvinsi.add(attributesProvinsi.get(i).getName());
                idProvinsi.add(attributesProvinsi.get(i).getId());
            }

            ArrayAdapter<String> adapterProvinsi = new ArrayAdapter<>(
                    Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, spinnerProvinsi);
            provinsiSpinner.setAdapter(adapterProvinsi);
            selectedProvinsi = provinsiSpinner.getSelectedItem().toString();

        });




        Toast.makeText(getContext(), selectedProvinsi, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < spinnerProvinsi.size(); i++) {
            if (selectedProvinsi.equals(spinnerProvinsi.get(i))){

                locationViewModel.loadRegencies(idProvinsi.get(i));
                locationViewModel.getRegencies().observe(this, kab -> {

                    ArrayList<Attributes> attributesKabupaten = kab.getRegencies();

                    for (int j = 0; j < attributesKabupaten.size(); j++) {
                        spinnerKabupaten.add(attributesKabupaten.get(j).getName());
//                            idProvinsi.add(attributesProvinsi.get(j).getId());
                    }

                    ArrayAdapter<String> adapterKabupaten = new ArrayAdapter<>(
                            Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, spinnerKabupaten);
                    kabupatenSpinner.setAdapter(adapterKabupaten);

                });
            }
        }

    }

    private void addAdapterKategoriSpinner(List<String> spinnerKategori) {
        spinnerKategori.add("Usaha");
        spinnerKategori.add("Jasa");
        spinnerKategori.add("Produk");
        spinnerKategori.add("Lainnya");
    }

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
                                getContext().getContentResolver(),
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
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
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
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}