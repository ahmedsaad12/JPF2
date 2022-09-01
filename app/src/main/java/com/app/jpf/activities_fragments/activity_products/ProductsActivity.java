package com.app.jpf.activities_fragments.activity_products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.app.jpf.adapters.ProductAdapter;
import com.app.jpf.databinding.ActivityProductsBinding;
import com.app.jpf.databinding.ActivityShopGalleryBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.ProductDataModel;
import com.app.jpf.models.ProductModel;
import com.app.jpf.remote.Api;
import com.app.jpf.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private ActivityProductsBinding binding;
    private String lang;
    private List<ProductModel> list;
    private ProductAdapter adapter;
    private int category_id;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        category_id = intent.getIntExtra("data", 0);

    }

    private void initView()
    {
        list = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(list,this);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator( new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        getData();

    }
    private void getData()
    {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getProductByCategoryId(category_id)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()==200) {
                                if (response.body().getData().size()>0){
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                }else{
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
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
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }


    public void setItemData(ProductModel productModel) {
        Intent intent = new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("data", productModel.getId());
        startActivity(intent);
    }
}