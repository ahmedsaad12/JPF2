package com.app.jpf.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;


import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_home.fragments.Fragment_Home;
import com.app.jpf.databinding.SliderRowBinding;
import com.app.jpf.models.SliderModel;
import com.app.jpf.tags.Tags;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<SliderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home fragment_home;

    public SliderAdapter(List<SliderModel> list, Context context,Fragment_Home fragment_home) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_home = fragment_home;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SliderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.slider_row, container, false);
        binding.setSliderData(list.get(position).getImage());
        Log.e("dlldl", Tags.IMAGE_URL+list.get(position).getImage());
        container.addView(binding.getRoot());
        binding.getRoot().setOnClickListener(v -> {
            fragment_home.setItemProduct(list.get(position));
        });
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
