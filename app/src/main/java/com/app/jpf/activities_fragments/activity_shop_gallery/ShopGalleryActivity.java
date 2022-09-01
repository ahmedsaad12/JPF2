package com.app.jpf.activities_fragments.activity_shop_gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_map.MapActivity;
import com.app.jpf.adapters.ShopGalleryAdapter;
import com.app.jpf.adapters.SpinnerGovernateAdapter;
import com.app.jpf.databinding.ActivityQrCodeBinding;
import com.app.jpf.databinding.ActivityShopGalleryBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.GovernmentModel;
import com.app.jpf.models.SelectedLocation;
import com.app.jpf.models.ShopGalleryDataModel;
import com.app.jpf.models.ShopGalleryModel;
import com.app.jpf.models.ShopGalleryModel2;
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

public class ShopGalleryActivity extends AppCompatActivity {

    private ActivityShopGalleryBinding binding;
    private String lang;
    private List<ShopGalleryModel> list;
    private List<GovernmentModel.Data> governmentList;
    private ShopGalleryAdapter adapter;
    private SpinnerGovernateAdapter spinnerCountryAdapter;
    private ShopGalleryModel selectedModel;
    private Preferences preferences;
    private UserModel userModel;
    private String address;
    private double lat, lng;
    private int id;
    private ShopGalleryModel shopGalleryModel;
    private int adapterPos ;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_gallery);
        initView();

    }


    private void initView() {
        governmentList = new ArrayList<>();
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShopGalleryAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getData(null);
                } else {
                    getData(String.valueOf(governmentList.get(position).getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateGovernateData(new ArrayList<>());
        getGovernate();

        binding.imageCall.setOnClickListener(v -> {
            String number = "";

            if (selectedModel != null) {
                if (selectedModel.getPhone_number1() != null) {
                    number = selectedModel.getPhone_number1();
                } else if (selectedModel.getPhone_number2() != null) {
                    number = selectedModel.getPhone_number2();

                }
            }

            if (!number.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        binding.btnMap.setOnClickListener(v -> {
            double latitude = Double.parseDouble(selectedModel.getLatitude());
            double longitude = Double.parseDouble(selectedModel.getLongitude());

            String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }

    private void getData(String governate_id) {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getShopGallery(governate_id)
                .enqueue(new Callback<ShopGalleryDataModel>() {
                    @Override
                    public void onResponse(Call<ShopGalleryDataModel> call, Response<ShopGalleryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                } else {
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
                    public void onFailure(Call<ShopGalleryDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void getGovernate() {

        Api.getService(Tags.base_url)
                .getGovernate()
                .enqueue(new Callback<GovernmentModel>() {
                    @Override
                    public void onResponse(Call<GovernmentModel> call, Response<GovernmentModel> response) {

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        updateGovernateData(response.body().getData());
                                    }
                                }
                            } else {
                                //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {


                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<GovernmentModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //   Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateGovernateData(List<GovernmentModel.Data> data) {


        governmentList.clear();

        GovernmentModel.Data governatemodel = new GovernmentModel.Data("اختر المحافظة", "Choose Government");

        governmentList.add(governatemodel);
        governmentList.addAll(data);
        if (spinnerCountryAdapter == null) {
            spinnerCountryAdapter = new SpinnerGovernateAdapter(governmentList, this);
            binding.spinner.setAdapter(spinnerCountryAdapter);
        } else {
            spinnerCountryAdapter.notifyDataSetChanged();
        }


    }

    public void openSheet(ShopGalleryModel shopGalleryModel) {
        this.selectedModel = shopGalleryModel;
        binding.setModel(shopGalleryModel);

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

    @Override
    public void onBackPressed() {
        if (binding.flSheet.getVisibility() == View.VISIBLE) {
            closeSheet();
        } else {
            finish();
        }
    }


    public void updateLocation(ShopGalleryModel shopGalleryModel, int adapterPosition) {
        this.shopGalleryModel = shopGalleryModel;
        this.adapterPos = adapterPosition;
        Intent intent = new Intent(ShopGalleryActivity.this, MapActivity.class);
//        intent.putExtra("id", shopGalleryModel.getId());
        startActivityForResult(intent,200);
        id=shopGalleryModel.getId();
     /*   Log.e("bbbbbbb", shopGalleryModel.getId() + "");
        Log.e("bbbbbbb", userModel.getData().getAddress());
        Log.e("bbbbbbbb", userModel.getData().getLatitude() + "");
        Toast.makeText(this, shopGalleryModel.getId() + "__", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, userModel.getData().getAddress(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, userModel.getData().getLatitude() + "______", Toast.LENGTH_SHORT).show();
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==200&&resultCode==RESULT_OK&&data!=null){
            SelectedLocation selectedLocation = (SelectedLocation) data.getSerializableExtra("location");
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .updateLocationOfColorShow(id,selectedLocation.getAddress(),selectedLocation.getLat(), selectedLocation.getLng(), userModel.getData().getId())
                    .enqueue(new Callback<ShopGalleryModel2>() {
                        @Override
                        public void onResponse(Call<ShopGalleryModel2> call, Response<ShopGalleryModel2> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().status == 200) {
                                    Toast.makeText(ShopGalleryActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                    shopGalleryModel.setAddress(selectedLocation.getAddress());
                                    shopGalleryModel.setLatitude(selectedLocation.getLat()+"");
                                    shopGalleryModel.setLongitude(selectedLocation.getLng()+"");
                                    list.set(adapterPos, shopGalleryModel);
                                    adapter.notifyItemChanged(adapterPos);
                                }
                            } else {

                                try {
                                    Log.e("error", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ShopGalleryModel2> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("msg_category_error", t.getMessage() + "__");

                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //              Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        //            Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage() + "__");
                            }
                        }
                    });
        }
    }
}