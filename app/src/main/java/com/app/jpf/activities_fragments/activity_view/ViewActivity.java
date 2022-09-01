 package com.app.jpf.activities_fragments.activity_view;

 import android.content.Context;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.os.Bundle;
 import android.view.View;
 import android.webkit.WebSettings;
 import android.webkit.WebView;
 import android.webkit.WebViewClient;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.databinding.DataBindingUtil;

 import com.app.jpf.R;
 import com.app.jpf.databinding.ActivityViewBinding;
 import com.app.jpf.language.Language;

 import io.paperdb.Paper;

 public class ViewActivity extends AppCompatActivity {

     private ActivityViewBinding binding;
     private String url = "";
     private String lang;

     @Override
     protected void attachBaseContext(Context newBase) {
         super.attachBaseContext(Language.updateResources(newBase, "ar"));

     }

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_view);
         getDataFromIntent();
         initView();
     }

     private void getDataFromIntent() {
         Intent intent = getIntent();
         url = intent.getStringExtra("url");

     }

     private void initView() {
         Paper.init(this);
         lang = Paper.book().read("lang", "ar");
         binding.setLang(lang);
         binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
         binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
         binding.webView.getSettings().setAllowContentAccess(true);
         binding.webView.getSettings().setAllowFileAccess(true);
         binding.webView.getSettings().setBuiltInZoomControls(false);
         binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
         binding.webView.getSettings().setJavaScriptEnabled(true);
         binding.webView.getSettings().setLoadWithOverviewMode(true);
         binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

         binding.webView.setWebViewClient(new WebViewClient() {
             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 super.onPageStarted(view, url, favicon);
             }

             @Override
             public void onPageFinished(WebView view, String url) {
                 super.onPageFinished(view, url);
                 binding.progBar.setVisibility(View.GONE);
             }

             @Override
             public void onPageCommitVisible(WebView view, String url) {
                 super.onPageCommitVisible(view, url);
                 binding.progBar.setVisibility(View.GONE);
             }
         });

         binding.webView.loadUrl(url);



         binding.llBack.setOnClickListener(v -> finish());

     }


 }