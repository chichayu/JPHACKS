package jp.ac.u_tokyo.jphacks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by xixi on 10/29/16.
 */
public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //setTitle("メインページ");

        Uri uri = Uri.parse("http://jigenji.biz/mdl/nativeDroid2-master/index.html");
        Intent i = new Intent(getIntent().ACTION_VIEW, uri);
        startActivity(i);
        /*
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://jigenji.biz/mdl/nativeDroid2-master/index.html");
        */
    }

}
