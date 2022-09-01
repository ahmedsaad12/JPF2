package com.app.jpf.activities_fragments.activity_sign_up;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_home.HomeActivity;
import com.app.jpf.activities_fragments.activity_map.MapActivity;
import com.app.jpf.adapters.SpinnerAreaAdapter;
import com.app.jpf.adapters.SpinnerGovernateAdapter;
import com.app.jpf.databinding.ActivitySignupBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.AreaModel;
import com.app.jpf.models.GovernmentModel;
import com.app.jpf.models.SelectedLocation;
import com.app.jpf.models.SignUpModel;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.share.Common;
import com.app.jpf.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private SignUpModel signUpModel;
    private UserModel userModel;
    private Preferences preferences;
    private String phone;
    private String phone_code;
    private String lang;
    private List<GovernmentModel.Data> dataList;
    private List<AreaModel.Data> arealist;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        getDataFromIntent();
        initView();

    }

    private void initView() {
        dataList = new ArrayList<>();
        arealist = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        signUpModel = new SignUpModel();
        if (userModel != null) {
            binding.btnSignup.setText(getResources().getString(R.string.update_profile));
            signUpModel.setArea_id(userModel.getData().getCity_id());
            signUpModel.setGovernate_id(userModel.getData().getGovernorate_id());
            signUpModel.setLng(userModel.getData().getLongitude());
            signUpModel.setLat(userModel.getData().getLatitude());
            signUpModel.setAddress(userModel.getData().getAddress());
            signUpModel.setNational_num(userModel.getData().getNational_ID());
            signUpModel.setFirst_name(userModel.getData().getFirst_name());
            signUpModel.setSeconed_name(userModel.getData().getSecond_name());
            signUpModel.setThird_name(userModel.getData().getLast_name());
            if(userModel.getData().getLogo()!=null){
                binding.icon.setVisibility(View.GONE);
                Picasso.get().load(Tags.IMAGE_URL+userModel.getData().getLogo()).into(binding.image);
            }
        }

        binding.setModel(signUpModel);
        binding.fl.setOnClickListener(view -> openSheet());
        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });
binding.lllocation.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SignUpActivity.this, MapActivity.class);
        startActivityForResult(intent, 100);
    }
});
        binding.btnCancel.setOnClickListener(view -> closeSheet());
        binding.btnSignup.setOnClickListener(view -> {
            checkDataValid();
        });
        binding.spGovernate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    signUpModel.setGovernate_id(dataList.get(position).getId());
                    getArea(signUpModel.getGovernate_id());
                } else {
                    signUpModel.setGovernate_id(0);
                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    signUpModel.setArea_id(arealist.get(position).getId());
                } else {
                    signUpModel.setArea_id(0);
                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getGovernate();

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
                                    } else {

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

int pos = 0;
        dataList.clear();

        GovernmentModel.Data governatemodel = new GovernmentModel.Data("اختر المحافظه", "Choose Government");

        dataList.add(governatemodel);
        dataList.addAll(data);
        SpinnerGovernateAdapter spinnerCountryAdapter = new SpinnerGovernateAdapter(dataList, this);
        binding.spGovernate.setAdapter(spinnerCountryAdapter);
if(userModel!=null){
    for(int i=0;i<dataList.size();i++){
        if(dataList.get(i).getId()==userModel.getData().getGovernorate_id()){
        pos=i;
        break;
        }
    }
    binding.spGovernate.setSelection(pos);
    getArea(userModel.getData().getGovernorate_id());
}

    }

    private void getArea(int goverid) {

        Api.getService(Tags.base_url)
                .getArea(goverid)
                .enqueue(new Callback<AreaModel>() {
                    @Override
                    public void onResponse(Call<AreaModel> call, Response<AreaModel> response) {

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        updateAreaData(response.body().getData());
                                    } else {

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
                    public void onFailure(Call<AreaModel> call, Throwable t) {
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

    private void updateAreaData(List<AreaModel.Data> data) {
        int pos=0;
        arealist.clear();
        arealist.add(new AreaModel.Data("اختر المنطقه", "Choose Area"));
        arealist.addAll(data);
        SpinnerAreaAdapter spinnerAreaAdapter = new SpinnerAreaAdapter(arealist, this);
        binding.spArea.setAdapter(spinnerAreaAdapter);
        if(userModel!=null){
            for(int i=0;i<arealist.size();i++){
                if(arealist.get(i).getId()==userModel.getData().getCity_id()){
                    pos=i;
                    break;
                }
            }
            binding.spArea.setSelection(pos);
        }

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }


    public void checkDataValid() {

       // navigateToHomeActivity();
        if (signUpModel.isDataValid(this)) {
            Common.CloseKeyBoard(this, binding.edtNational);
            signUp();
        }

    }

    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {
            binding.icon.setVisibility(View.GONE);

            uri = data.getData();
            File file = new File(Common.getImagePath(this, uri));
            Picasso.get().load(file).fit().into(binding.image);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                binding.icon.setVisibility(View.GONE);
                String path = Common.getImagePath(this, uri);

                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.image);

                } else {
                    Picasso.get().load(uri).fit().into(binding.image);

                }
            }


        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            SelectedLocation location = (SelectedLocation) data.getSerializableExtra("location");

            //settings.setCountry_code(countrycode);
            signUpModel.setAddress(location.getAddress());
            signUpModel.setLat(location.getLat());
            signUpModel.setLng(location.getLng());
            binding.setModel(signUpModel);

        }


    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }

    private void signUp() {


        if (userModel == null) {
            if (uri == null) {
                signUpWithoutImage();
            } else {
                signUpWithImage();
            }
        } else {
            if (uri == null) {
                updateProfileWithoutImage();
            } else {
                updateProfileWithImage();
            }
        }

    }

    private void updateProfileWithImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody first_part = Common.getRequestBodyText(signUpModel.getFirst_name());
        RequestBody seconed_part = Common.getRequestBodyText(signUpModel.getSeconed_name());
        RequestBody last_part = Common.getRequestBodyText(signUpModel.getThird_name());


        RequestBody phone_code_part = Common.getRequestBodyText(userModel.getData().getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getData().getPhone());
        RequestBody national_part = Common.getRequestBodyText(signUpModel.getNational_num());

        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(signUpModel.getLng()+"");
        RequestBody governate_part = Common.getRequestBodyText(signUpModel.getGovernate_id()+"");
        RequestBody area_part = Common.getRequestBodyText(signUpModel.getArea_id()+"");
        RequestBody software_type_part = Common.getRequestBodyText("android");

        MultipartBody.Part image = Common.getMultiPartImage(this, uri, "logo");

        Api.getService(Tags.base_url)
                .updateProfileWithImage("Bearer " + userModel.getData().getToken(), first_part,seconed_part,last_part, phone_code_part, phone_part,national_part, address_part, lat_part, lng_part,governate_part,area_part, software_type_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                        //        Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                          //      Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                             //       Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                               //     Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateProfileWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateProfileWithoutImage("Bearer " + userModel.getData().getToken(), signUpModel.getFirst_name(),signUpModel.getSeconed_name(),signUpModel.getThird_name(), userModel.getData().getPhone_code(), userModel.getData().getPhone(),signUpModel.getNational_num(), signUpModel.getAddress(), signUpModel.getLat(), signUpModel.getLng(),signUpModel.getGovernate_id()+"",signUpModel.getArea_id()+"", "android")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (response.code() == 500) {
                              //  Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void signUpWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUpWithoutImage(signUpModel.getFirst_name(),signUpModel.getSeconed_name(),signUpModel.getThird_name(), phone_code, phone,signUpModel.getNational_num(), signUpModel.getAddress(), signUpModel.getLat(), signUpModel.getLng(),signUpModel.getGovernate_id()+"",signUpModel.getArea_id()+"", "android")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (response.code() == 500) {
                           //     Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                             //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                               //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                 //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void signUpWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody first_part = Common.getRequestBodyText(signUpModel.getFirst_name());
        RequestBody seconed_part = Common.getRequestBodyText(signUpModel.getSeconed_name());
        RequestBody last_part = Common.getRequestBodyText(signUpModel.getThird_name());


        RequestBody phone_code_part = Common.getRequestBodyText(phone_code);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody national_part = Common.getRequestBodyText(signUpModel.getNational_num());

        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLat()+"");
        RequestBody lng_part = Common.getRequestBodyText(signUpModel.getLng()+"");
        RequestBody governate_part = Common.getRequestBodyText(signUpModel.getGovernate_id()+"");
        RequestBody area_part = Common.getRequestBodyText(signUpModel.getArea_id()+"");
        RequestBody software_type_part = Common.getRequestBodyText("android");

        MultipartBody.Part image = Common.getMultiPartImage(this, uri, "logo");


        Api.getService(Tags.base_url)
                .signUpWithImage(first_part,seconed_part,last_part, phone_code_part, phone_part,national_part, address_part, lat_part, lng_part,governate_part,area_part, software_type_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.user_exist, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                              //  Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                  //  Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}