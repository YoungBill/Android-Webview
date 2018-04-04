package com.android.webview;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private static final java.lang.String URL_ROOT = "http://123.207.214.20/reviveads/www/delivery/ajs.php?zoneid=29&cb=31751745195&charset=windows-1252&loc=file%3A///C%3A/Users/taochen/Desktop/index.html";

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON); // this is for newer API's
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和sefatUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(false);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUserAgentString("User-Agent:Android");//设置用户代理，一般不用
        mWebView.setWebChromeClient(new WebChromeClient()); //对js交互的对话框、title以及页面加载进度条的管理
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(URL_ROOT)) {
                    // 获取页面内容
                    view.loadUrl("javascript:window.java_obj.showSource("
                            + "document.getElementsByTagName('body')[0].innerHTML);");

                    // 获取解析<meta name="share-description" content="获取到的值">
                    view.loadUrl("javascript:window.java_obj.showDescription("
                            + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                            + ");");
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                super.onPageFinished(view, url);
            }
        });

        mWebView.loadUrl(URL_ROOT);
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
            html = html.replaceAll("<pre.+?>", "").replaceAll("</pre>", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            String script = "<script>" + html + "</script>";
            final String replacedHtml = "\n" +
                    "<html>\n" +
                    "<body>" + script + "</body>\n" +
                    "</html>";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadData(replacedHtml, "text/html", "UTF8");
                }
            });
        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
        }
    }
}
