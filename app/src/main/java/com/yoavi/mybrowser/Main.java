package com.yoavi.mybrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Activity{

    WebView wb;
    EditText addressbar;
    ImageButton stop,enter;
    String link;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        wb=(WebView)findViewById(R.id.webView);
        wb.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                addressbar.setText(url);
                return true;
            }
        });

        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        wb.loadUrl("http://www.google.com");

        addressbar = (EditText)findViewById(R.id.addressbar);
        addressbar.setFocusable(false);
        addressbar.setFocusableInTouchMode(true);

        enter = (ImageButton) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link = addressbar.getText().toString();
                load(link);
            }
        });

        //make a method to load url
        addressbar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    link = addressbar.getText().toString();
                    load(link);
                }
                return true;
            }
        });

        stop=(ImageButton)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                wb.stopLoading();
            }
        });

        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.options, android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            switch (position) {
                case 0:
                    wb.goForward();
                break;
                case 2:
                   wb.loadUrl( "javascript:window.location.reload( true )" );
                break;
                case 3:
                    wb.clearHistory();
                break;
                case 4:
                    
            }
        }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
                closeOptionsMenu();
            }
        });
    }

    @Override
    public void onBackPressed() {
            if (wb.canGoBack()) {
                    wb.goBack();
                addressbar.setText(wb.getOriginalUrl());
                } else {
                    super.onBackPressed();
                   }
        wb.requestFocus();
        addressbar.clearFocus();
    }

    public void load(String url){
        String [] parts = url.split("\\s+");
        int count=0;
        for(int i=0;i<parts.length;i++){
            count++;
        }

        //credits to some guy on stackoverflow. he has done a marvellous job
        final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(url);
        if(m.find()&& count==1){
            if((URLUtil.isHttpsUrl(url))||(URLUtil.isHttpUrl(url))) {
                wb.loadUrl(url);
            }
            else{
                url="https://"+url;
                wb.loadUrl(url);
                addressbar.setText(url);}
        }
        else{
            String ext = "";
            for(int i=0;i<parts.length;i++){
                ext+=parts[i];
                ext+="+";
            }
            ext=ext.substring(0,ext.length()-1);
            url="https://www.google.com/search?q="+ext;
            wb.loadUrl(url);
            addressbar.setText(url);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        addressbar.setFocusable(false);
        addressbar.setFocusableInTouchMode(true);
    }

}
