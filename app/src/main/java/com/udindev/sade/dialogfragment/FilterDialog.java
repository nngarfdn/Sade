package com.udindev.sade.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.udindev.sade.R;
import com.udindev.sade.model.Location;
import com.udindev.sade.reponse.Attributes;
import com.udindev.sade.reponse.Districts;
import com.udindev.sade.reponse.Provinces;
import com.udindev.sade.reponse.Regencies;
import com.udindev.sade.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CheckBox.OnCheckedChangeListener {
    private final String TAG = getClass().getSimpleName();

    private Activity activity;
    private AlertDialog dialog;
    private LocationViewModel lvm;

    private ArrayList<Location> listProvinces, listRegencies, listDistricts;
    private int idProvince, idRegency, idDistrict = 0;
    private Spinner spinProvince, spinRegency, spinDistrict;
    private CheckBox cbProvince, cbRegency, cbDistrict;

    public FilterDialog(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        Button btnApply = view.findViewById(R.id.btn_apply_filter);
        Button btnCancel = view.findViewById(R.id.btn_cancel_filter);
        btnApply.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        cbProvince = view.findViewById(R.id.cb_province_filter);
        cbRegency = view.findViewById(R.id.cb_regency_filter);
        cbDistrict = view.findViewById(R.id.cb_district_filter);
        spinProvince = view.findViewById(R.id.spin_province_filter);
        spinRegency = view.findViewById(R.id.spin_regency_filter);
        spinDistrict = view.findViewById(R.id.spin_district_filter);

        cbProvince.setEnabled(true);
        cbRegency.setEnabled(false);
        cbDistrict.setEnabled(false);
        spinProvince.setEnabled(false);
        spinRegency.setEnabled(false);
        spinDistrict.setEnabled(false);

        cbProvince.setOnCheckedChangeListener(this);
        cbRegency.setOnCheckedChangeListener(this);
        cbDistrict.setOnCheckedChangeListener(this);
        spinProvince.setOnItemSelectedListener(this);
        spinRegency.setOnItemSelectedListener(this);
        spinDistrict.setOnItemSelectedListener(this);

        lvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LocationViewModel.class);
        loadProvinces();

        // Create alert dialog instance
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }

    private void loadProvinces(){
        lvm.loadProvinces();
        lvm.getProvinces().observe(this, new Observer<Provinces>() {
            @Override
            public void onChanged(Provinces provinces) {
                if (provinces != null){
                    listProvinces = new ArrayList<>();
                    List<String> itemList = new ArrayList<>();
                    for (Attributes attributes : provinces.getProvinces()){ // Fix nama provinsi
                        if (attributes.getId() == 31) listProvinces.add(new Location(attributes.getId(), "DKI Jakarta"));
                        else if (attributes.getId() == 34) listProvinces.add(new Location(attributes.getId(), "DI Yogyakarta"));
                        else listProvinces.add(new Location(attributes.getId(), attributes.getName()));
                    }
                    for (Location location : listProvinces) itemList.add(location.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, itemList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinProvince.setAdapter(adapter);
                }
            }
        });
    }

    private void loadRegencies(int idProvince){
        lvm.loadRegencies(idProvince);
        lvm.getRegencies().observe(this, new Observer<Regencies>() {
            @Override
            public void onChanged(Regencies regencies) {
                if (regencies != null){
                    listRegencies = new ArrayList<>();
                    List<String> itemList = new ArrayList<>();
                    for (Attributes attributes : regencies.getRegencies()) listRegencies.add(new Location(attributes.getId(), attributes.getName()));
                    for (Location location : listRegencies) itemList.add(location.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, itemList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinRegency.setAdapter(adapter);
                }
            }
        });
    }

    private void loadDistricts(int idRegency){
        lvm.loadDistricts(idRegency);
        lvm.getDistricts().observe(this, new Observer<Districts>() {
            @Override
            public void onChanged(Districts districts) {
                if (districts != null){
                    listDistricts = new ArrayList<>();
                    List<String> itemList = new ArrayList<>();
                    for (Attributes attributes : districts.getDistricts()) listDistricts.add(new Location(attributes.getId(), attributes.getName()));
                    for (Location location : listDistricts) itemList.add(location.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, itemList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinDistrict.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spin_province_filter:
                idProvince = listProvinces.get(i).getId();
                loadRegencies(idProvince);
                break;

            case R.id.spin_regency_filter:
                idRegency = listRegencies.get(i).getId();
                loadDistricts(idRegency);
                break;

            case R.id.spin_district_filter:
                idDistrict = listDistricts.get(i).getId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cb_province_filter:
                spinProvince.setEnabled(b);
                cbRegency.setEnabled(b);
                if (!b){
                    cbRegency.setChecked(false);
                    cbDistrict.setChecked(false);
                }
                break;

            case R.id.cb_regency_filter:
                spinRegency.setEnabled(b);
                cbDistrict.setEnabled(b);
                if (!b) cbDistrict.setChecked(false);
                break;

            case R.id.cb_district_filter:
                spinDistrict.setEnabled(b);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_apply_filter:
                dialog.dismiss();
                break;

            case R.id.btn_cancel_filter:
                dialog.dismiss();
                break;
        }
    }
}