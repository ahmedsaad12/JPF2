package com.app.jpf.activities_fragments.activity_points;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.jpf.R;
import com.app.jpf.adapters.PointsAdapter;
import com.app.jpf.databinding.ActivityPointsBinding;
import com.app.jpf.databinding.ActivityShopGalleryBinding;
import com.app.jpf.language.Language;
import com.app.jpf.models.MyPointsDataModel;
import com.app.jpf.models.MyPointsModel;
import com.app.jpf.models.UserModel;
import com.app.jpf.preferences.Preferences;
import com.app.jpf.remote.Api;
import com.app.jpf.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ActivityPointsBinding binding;
    private String lang;
    private DatePickerDialog datePickerDialog;
    private List<MyPointsModel> list;
    private PointsAdapter adapter;
    private String from_date=null;
    private String to_date=null;
    private UserModel userModel;
    private Preferences preferences;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_points);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PointsAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());
        binding.tvDatefrome.setOnClickListener(v -> createDateDialog());
        binding.tvDateto.setOnClickListener(v -> createDateDialog());

        binding.setModel(userModel);
        getPoints(from_date,to_date);

    }

    private void getPoints(String from_date,String to_date) {
        list.clear();
        adapter.notifyDataSetChanged();

        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getMyPoints("Bearer "+userModel.getData().getToken(),from_date,to_date,"desc")
                .enqueue(new Callback<MyPointsDataModel>() {
                    @Override
                    public void onResponse(Call<MyPointsDataModel> call, Response<MyPointsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        PointsActivity.this.from_date = null;
                        PointsActivity.this.to_date = null;

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
                    public void onFailure(Call<MyPointsDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void createDateDialog() {
        try {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

            datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setOkText(getString(R.string.select));
            datePickerDialog.setCancelText(getString(R.string.cancel));
            datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
            datePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
            datePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
            datePickerDialog.setLocale(Locale.ENGLISH);
            datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
            try {
                datePickerDialog.show(getSupportFragmentManager(),"");
            }catch (Exception e){}
        }catch (Exception e){}
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
            if (from_date==null){
                from_date = date;
                binding.tvDatefrome.setText(date);

                createDateDialog();
            }else {
                if (to_date==null){
                    to_date = date;
                    binding.tvDateto.setText(date);

                }
                getPoints(from_date,to_date);
            }

        }catch (Exception e){}

    }


}