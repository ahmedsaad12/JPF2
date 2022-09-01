package com.app.jpf.activities_fragments.activity_qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.app.jpf.R;
import com.app.jpf.databinding.ActivityLoginBinding;
import com.app.jpf.databinding.ActivityQrCodeBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.QrCodeModel;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.tags.Tags;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.ScanMode;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeActivity extends AppCompatActivity {
    private ActivityQrCodeBinding binding;
    private String lang;
    private CodeScanner mCodeScanner;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int  CAMERA_REQ = 2;
    private Animation animation;
    private UserModel userModel;
    private Preferences preferences;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_code);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        checkCameraPermission();
        binding.imageBack.setOnClickListener(v -> finish());
        binding.edtScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.imageScan.setVisibility(View.GONE);
                }else {
                    binding.imageScan.setVisibility(View.VISIBLE);

                }
            }
        });

        binding.imageScan.setOnClickListener(v -> {
            String code = binding.edtScan.getText().toString();
            getProduct(code);
        });


    }

    private void initScanner(){

        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            binding.scannerView.setVisibility(View.GONE);
            getProduct(result.getText());
        }));
        binding.scannerView.setVisibility(View.VISIBLE);
        mCodeScanner.startPreview();
    }

    private void getProduct(String text) {
        Log.e("dddd", text);
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getProductByQrCode("Bearer "+userModel.getData().getToken(),text)
                .enqueue(new Callback<QrCodeModel>() {
                    @Override
                    public void onResponse(Call<QrCodeModel> call, Response<QrCodeModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        Log.e("code", response.body().getStatus()+"_");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()==200) {
                                binding.setModel(response.body().getData().getProduct());
                                slideUp();
                            }else if (response.body().getStatus()==406){
                                binding.scannerView.setVisibility(View.VISIBLE);
                                mCodeScanner.startPreview();
                                Toast.makeText(QrCodeActivity.this, R.string.qr_used, Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus()==407){
                                binding.scannerView.setVisibility(View.VISIBLE);
                                mCodeScanner.startPreview();

                                Toast.makeText(QrCodeActivity.this, R.string.no_pro_found, Toast.LENGTH_SHORT).show();
                            }else {

                                binding.scannerView.setVisibility(View.VISIBLE);

                                mCodeScanner.startPreview();

                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            binding.scannerView.setVisibility(View.VISIBLE);
                            mCodeScanner.startPreview();

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<QrCodeModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            binding.scannerView.setVisibility(View.VISIBLE);
                            mCodeScanner.startPreview();

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });



    }




    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            initScanner();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScanner();

            }

        }
    }

    private void slideUp(){
        animation = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        binding.flData.clearAnimation();
        binding.flData.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flData.setVisibility(View.VISIBLE);
                binding.scannerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void slideDown(){
        animation = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        binding.flData.clearAnimation();
        binding.flData.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flData.setVisibility(View.GONE);
                binding.scannerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (mCodeScanner!=null){
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.flData.getVisibility()==View.VISIBLE){
            slideDown();
        }else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCodeScanner!=null){
            mCodeScanner.releaseResources();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCodeScanner!=null){
            mCodeScanner.startPreview();

        }
    }
}