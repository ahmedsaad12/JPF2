package com.app.jpf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_shop_gallery.ShopGalleryActivity;
import com.app.jpf.databinding.ShopGalleryRowBinding;
import com.app.jpf.models.ShopGalleryModel;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;

import java.util.List;

import io.paperdb.Paper;

public class ShopGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShopGalleryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ShopGalleryActivity activity;
    private String lang = "ar";
    private Preferences preferences;
    private UserModel userModel;
    public ShopGalleryAdapter(List<ShopGalleryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ShopGalleryActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ShopGalleryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.shop_gallery_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvDetails.setOnClickListener(v -> {
            activity.openSheet(list.get(myHolder.getAdapterPosition()));
        });

        if (userModel.getData().getId()==1){
            myHolder.binding.btnEdit.setVisibility(View.VISIBLE);
        }else {
            myHolder.binding.btnEdit.setVisibility(View.GONE);

        }
        myHolder.binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.updateLocation(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ShopGalleryRowBinding binding;

        public MyHolder(ShopGalleryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
