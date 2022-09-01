package com.app.jpf.activities_fragments.activity_product_detials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jpf.R;
import com.app.jpf.adapters.ColorsAdapter;
import com.app.jpf.adapters.SliderDetailsAdapter;
import com.app.jpf.databinding.ActivityPaintsBinding;
import com.app.jpf.databinding.ActivityProductDetialsBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.ProductModel;
import com.app.jpf.models.SingleProductModel;
import com.app.jpf.remote.Api;
import com.app.jpf.tags.Tags;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetialsActivity extends AppCompatActivity {
    private ActivityProductDetialsBinding binding;
    private String lang;
    private int product_id;
    private ProductModel productModel;




    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        product_id = intent.getIntExtra("data", 0);

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.llBack.setOnClickListener(v -> finish());
        getProductById();
    }

    private void getProductById() {
        Api.getService(Tags.base_url)
                .getProductById(product_id)
                .enqueue(new Callback<SingleProductModel>() {
                    @Override
                    public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                productModel = response.body().getData();
                                binding.setModel(productModel);
                                updateData();
                            } else {

                            }


                        } else {
                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SingleProductModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateData() {
        binding.progBar.setVisibility(View.GONE);
        if (productModel.getHave_offer().equals("with_offer")){
            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ColorsAdapter adapter = new ColorsAdapter(this,productModel.getColors());
        binding.recView.setAdapter(adapter);

        if (productModel.getImages().size()>0){
            binding.flNoSlider.setVisibility(View.GONE);
            binding.flSlider.setVisibility(View.VISIBLE);
            SliderDetailsAdapter sliderDetailsAdapter = new SliderDetailsAdapter(productModel.getImages(),this);
            binding.tab.setupWithViewPager(binding.pager);
            binding.pager.setAdapter(sliderDetailsAdapter);


        }else {
            binding.flNoSlider.setVisibility(View.VISIBLE);
            binding.flSlider.setVisibility(View.GONE);
        }
    }
}