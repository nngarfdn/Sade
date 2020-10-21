package com.udindev.sade.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udindev.sade.R;
import com.udindev.sade.activity.DetailActivity;
import com.udindev.sade.model.Produk;

import java.util.ArrayList;
import java.util.List;

import static com.udindev.sade.activity.DetailActivity.EXTRA_PRODUK;
import static com.udindev.sade.utils.AppUtils.getRupiahFormat;
import static com.udindev.sade.utils.AppUtils.getSimpleKabupaten;
import static com.udindev.sade.utils.AppUtils.loadImageFromUrl;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> implements Filterable {
    private final Activity activity;
    private final ArrayList<Produk> listItem = new ArrayList<>();
    private ArrayList<Produk> listItemFiltered = new ArrayList<>();

    public FavoriteAdapter(Activity activity){
        this.activity = activity;
    }

    public void setData(List<Produk> listItem){
        this.listItem.clear();
        this.listItem.addAll(listItem);

        this.listItemFiltered.clear();
        this.listItemFiltered.addAll(listItem);

        notifyDataSetChanged();
    }

    public ArrayList<Produk> getData(){
        return listItem;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_vertical, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Produk item = listItemFiltered.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(EXTRA_PRODUK, item);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemFiltered.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPhoto;
        private final TextView tvName, tvAddress, tvPrice, tvDescription;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_nama_produk);
            tvAddress = itemView.findViewById(R.id.txt_alamatproduk);
            tvPrice = itemView.findViewById(R.id.txt_harga);
            tvDescription = itemView.findViewById(R.id.txt_deskripsi_produk);
            imgPhoto = itemView.findViewById(R.id.img_item);
        }

        public void bind(Produk item) {
            tvName.setText(item.getNama());
            tvAddress.setText(item.getKecamatan() + ", " + getSimpleKabupaten(item.getKabupaten()));
            tvPrice.setText(getRupiahFormat(item.getHarga(), true));
            tvDescription.setText(item.getDeskripsi());
            loadImageFromUrl(imgPhoto, item.getPhoto());
        }
    }@Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        // Run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Produk> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty() || charSequence.toString().equals("Semua")) filteredList.addAll(listItem);
            else {
                for (Produk product : listItem){
                    if (product.getKategori().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(product);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // Run on a UI thread
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listItemFiltered = (ArrayList<Produk>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
