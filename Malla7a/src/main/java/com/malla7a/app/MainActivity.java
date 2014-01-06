package com.malla7a.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;

public class MainActivity extends ActionBarActivity {
    private WebView mWebView;
    private ProgressBar progress;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpened(getIntent());

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        //progress = (ProgressBar) findViewById(R.id.progressBar);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().toString());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new MyAppWebViewClient());
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mWebView.loadUrl("http://malla7a.com/");


//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mWebView.reload();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public class MyAppWebViewClient extends WebViewClient {
        private ProgressDialog progressDialog;

        @Override
        public void onPageFinished(WebView view, String url) {
            //progress.setVisibility(View.GONE);
            progressDialog.dismiss();

            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();

            //MainActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //progress.setVisibility(View.VISIBLE);
            //MainActivity.this.progress.setProgress(0);
            if(isConnected){
                progressDialog = ProgressDialog.show(MainActivity.this, "",
                        "Eddy El Donia Ta3m ...");
            }else{
                progressDialog = ProgressDialog.show(MainActivity.this, "",
                        "No Internet Connection :(");
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().endsWith("malla7a.com")) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
}
