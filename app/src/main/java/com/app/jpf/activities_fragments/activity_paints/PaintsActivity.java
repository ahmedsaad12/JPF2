package com.app.jpf.activities_fragments.activity_paints;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.jpf.R;
import com.app.jpf.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.app.jpf.adapters.PaintsAdapter;
import com.app.jpf.databinding.ActivityPaintsBinding;
import com.app.jpf.databinding.ActivityPointsBinding;
import com.app.jpf.language.Language;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PaintsActivity extends AppCompatActivity {
    private ActivityPaintsBinding binding;
    private String lang;
    private List<Object> list;
    private PaintsAdapter adapter;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paints);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaintsAdapter(list, this);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

    }

    public void open() {
        Intent intent=new Intent(PaintsActivity.this, ProductDetialsActivity.class);
        startActivity(intent);
    }
}