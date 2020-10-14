package com.udindev.sade.cobacoba;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udindev.sade.R;
import com.udindev.sade.activity.DetailActivity;
import com.udindev.sade.model.Produk;

import java.util.ArrayList;
import java.util.List;

import static com.udindev.sade.activity.DetailActivity.EXTRA_PRODUK;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private Activity activity;
    private ArrayList<Produk> listItem = new ArrayList<>();
    private ArrayList<Produk> listItemFiltered = new ArrayList<>();

    public SearchAdapter(Activity activity){
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
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_horizontal, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
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

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvAddress, tvPrice;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_nama_produk);
            tvAddress = itemView.findViewById(R.id.txt_alamatproduk);
            tvPrice = itemView.findViewById(R.id.txt_hargaproduk);
        }

        public void bind(Produk item) {
            tvName.setText(item.getNama());
            tvAddress.setText(item.getKecamatan() + ", " + item.getKabupaten() + ", " + item.getProvinsi());
            tvPrice.setText("Rp" + item.getHarga());
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        // Run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Produk> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) filteredList.addAll(listItem);
            else {
                for (Produk product : listItem){
                    if (product.getNama().toLowerCase().contains(charSequence.toString().toLowerCase())){
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
