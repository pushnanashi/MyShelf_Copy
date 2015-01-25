package com.koba.myshelf;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class ECsite extends Activity {

private WebView webView;

public View.OnClickListener on = new View.OnClickListener() {

	public void onClick(View v) {

		finish();

	}
};



@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.ecsite); // main.xmlをセット
//findViews(); // Viewの読み込み

Intent intent = getIntent();
		// intentから指定キーの文字列を取得する
String ECURL = intent.getStringExtra("EC");



System.out.println("nullpo!!");


WebView webView = (WebView) findViewById(R.id.webview);
webView.setVerticalScrollbarOverlay(true);
webView.setWebViewClient(new WebViewClient());

WebSettings settings = webView.getSettings();
settings.setSupportMultipleWindows(true);
settings.setLoadsImagesAutomatically(true);

settings.setBuiltInZoomControls(true);
settings.setSupportZoom(true);
settings.setLightTouchEnabled(true);
webView.loadUrl(ECURL);

Button btn = (Button)findViewById(R.id.lobt);
btn.setOnClickListener(on);
}
}	