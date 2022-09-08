package com.app.jpf.activities_fragments.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_home.HomeActivity;
import com.app.jpf.activities_fragments.activity_sign_up.SignUpActivity;
import com.app.jpf.databinding.ActivityVerificationCodeBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.share.Common;
import com.app.jpf.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;
    private String lang;
    private String phone_code;
    private String phone;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private boolean canSend = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        String phone = phone_code+this.phone;
        binding.setPhone(phone);
        binding.tvResend.setOnClickListener(view -> {
            if (canSend){
                sendSmsCode();
            }
        });
        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
            //navigateToSignUpActivity();
            login();
            if (!code.isEmpty()) {
                binding.edtCode.setError(null);
                Common.CloseKeyBoard(this, binding.edtCode);
                checkValidCode(code);
            } else {
                binding.edtCode.setError(getString(R.string.field_required));
            }

        });
      // sendSmsCode();
       login();
    }

    private void sendSmsCode() {

        startTimer();

        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                checkValidCode(smsCode);

            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                VerificationCodeActivity.this.verificationId = verification_id;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(VerificationCodeActivity.this, e.getMessage());
                } else {
                    Common.CreateDialogAlert(VerificationCodeActivity.this, getString(R.string.failed));

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code + phone,
                        120,
                        TimeUnit.SECONDS,
                        this,
                        mCallBack

                );


    }

    private void startTimer() {
        canSend = false;
        binding.tvResend.setEnabled(false);
        timer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.tvResendCode.setText(time);
            }

            @Override
            public void onFinish() {
                canSend = true;
                binding.tvResendCode.setText("00:00");
                binding.tvResend.setEnabled(true);
            }
        };
        timer.start();
    }


    private void checkValidCode(String code) {

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        login();
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(this, e.getMessage());
                } else {
                  //  Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void login() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(phone_code, phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        Log.e("res",response.code()+" "+phone_code+" "+phone);
                        if (response.isSuccessful()&&response.body()!=null) {
                            Log.e("code", response.body().getStatus()+"__");

                            if (response.body().getStatus()==200){
                                preferences.create_update_userdata(VerificationCodeActivity.this, response.body());
                                navigateToHomeActivity();
                            }else  if (response.body().getStatus()==404){
                                navigateToSignUpActivity();
                            }else if (response.body().getStatus()==406){
                                Toast.makeText(VerificationCodeActivity.this, R.string.user_blocked, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            try {
                                Log.e("msg_category_error", response.code() + "__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                              //  Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                  //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("phone_code", phone_code);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
