package com.app.jpf.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_award.AwardActivity;
import com.app.jpf.activities_fragments.activity_home.HomeActivity;
import com.app.jpf.activities_fragments.activity_points.PointsActivity;
import com.app.jpf.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.app.jpf.activities_fragments.activity_products.ProductsActivity;
import com.app.jpf.activities_fragments.activity_shop_gallery.ShopGalleryActivity;
import com.app.jpf.adapters.CategoryAdapter;
import com.app.jpf.adapters.SliderAdapter;
import com.app.jpf.databinding.FragmentHomeBinding;
import com.app.jpf.models.CategoryDataModel;
import com.app.jpf.models.CategoryModel;
import com.app.jpf.models.SliderDataModel;
import com.app.jpf.models.SliderModel;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private List<CategoryModel> categoryModelList;
    private CategoryAdapter adapter;
    private Timer timer;
    private TimerTask timerTask;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {
        categoryModelList = new ArrayList<>();
        sliderModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.cardViewShops.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ShopGalleryActivity.class);
            startActivity(intent);
        });

        binding.cardViewPoints.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PointsActivity.class);
            startActivity(intent);
        });

        binding.cardViewGifts.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AwardActivity.class);
            startActivity(intent);
        });
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new CategoryAdapter(activity,categoryModelList,this);
        binding.recView.setAdapter(adapter);



        getUserData();
        getSlider();
        getCategory();
    }

    private void getCategory() {
        Api.getService(Tags.base_url)
                .getCategory()
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                categoryModelList.clear();
                                categoryModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            binding.progBar.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getSlider() {
        Api.getService(Tags.base_url)
                .getSlider()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0) {
                                updateSliderUi(response.body().getData());

                            } else {
                                binding.flSlider.setVisibility(View.GONE);

                            }

                        } else {
                            binding.flSlider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            binding.progBarSlider.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void updateSliderUi(List<SliderModel> data) {
        sliderModelList.addAll(data);
        sliderAdapter = new SliderAdapter(sliderModelList, activity,this);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(90, 8, 90, 8);
        binding.pager.setPageMargin(24);
        binding.pager.setVisibility(View.VISIBLE);

        if (data.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }
    }

    public void setItemProduct(SliderModel sliderModel) {
        if (sliderModel.getProduct()!=null){
            Log.e("ddd", sliderModel.getProduct().getId()+"__");
            Intent intent = new Intent(activity, ProductDetialsActivity.class);
            intent.putExtra("data", sliderModel.getProduct().getId());
            startActivity(intent);
        }

    }


    public void getUserData(){
        Api.getService(Tags.base_url)
                .getUserById("Bearer " + userModel.getData().getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                userModel = response.body();
                                preferences.create_update_userdata(activity,userModel);
                                binding.setModel(userModel);
                            }

                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void setCategoryItem(CategoryModel categoryModel) {

        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.putExtra("data", categoryModel.getId());
        startActivity(intent);
    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }

    }

}
