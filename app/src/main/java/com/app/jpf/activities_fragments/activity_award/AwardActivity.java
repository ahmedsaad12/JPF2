package com.app.jpf.activities_fragments.activity_award;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.jpf.R;
import com.app.jpf.adapters.AwardAdapter;
import com.app.jpf.databinding.ActivityAwardBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.MyPointsModel;
import com.app.jpf.models.PrizeDataModel;
import com.app.jpf.models.StatusResponse;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.share.Common;
import com.app.jpf.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AwardActivity extends AppCompatActivity {
    private ActivityAwardBinding binding;
    private String lang;
    private List<MyPointsModel.Prize> list;
    private AwardAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private MyPointsModel.Prize selectedPrize;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_award);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new AwardAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        getPrize();
        binding.btnOk.setOnClickListener(v -> {
            closeSheet();
            exchangePoints();
        });

        binding.btnCancel.setOnClickListener(v -> {
            closeSheet();
        });
    }



    private void getPrize() {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getPrize()
                .enqueue(new Callback<PrizeDataModel>() {
                    @Override
                    public void onResponse(Call<PrizeDataModel> call, Response<PrizeDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()==200){
                                if (response.body().getData().size()>0){

                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();

                                }else {
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
                    public void onFailure(Call<PrizeDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void openSheet(MyPointsModel.Prize prize) {
        this.selectedPrize = prize;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        binding.flSheet.clearAnimation();
        binding.flSheet.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flSheet.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        binding.flSheet.clearAnimation();
        binding.flSheet.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flSheet.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void exchangePoints() {
        if (selectedPrize==null){
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .exchangePoints("Bearer "+userModel.getData().getToken(), selectedPrize.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()==200){
                                selectedPrize = null;
                                getUserById(dialog);
                            }else if (response.body().getStatus()==406){
                                dialog.dismiss();
                                UpdataData(0);

                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void UpdataData(int i) {
        if(i==0) {
            binding.tvResult.setText(getResources().getString(R.string.no_enough_points));
            binding.image.setImageDrawable(getResources().getDrawable(R.drawable.nervous));
        }
        else {
        binding.tvResult.setText(getResources().getString(R.string.we_will_contact));
        binding.image.setImageDrawable(getResources().getDrawable(R.drawable.circle_correct));
    }
    binding.flData.setVisibility(View.VISIBLE);
    }

    private void getUserById(ProgressDialog dialog) {
        Api.getService(Tags.base_url)
                .getUserById("Bearer "+userModel.getData().getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()==200){
                                userModel = response.body();
                                preferences.create_update_userdata(AwardActivity.this,response.body());
                              //  Common.CreateDialogAlert(AwardActivity.this,getString(R.string.we_will_contact));
UpdataData(1);
                            }

                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }


}