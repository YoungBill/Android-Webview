package com.android.webview;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

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
                // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);");

                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");");
                super.onPageFinished(view, url);
            }
        }); //对webview页面加载管理、如url重定向


//        mWebView.loadUrl("http://123.207.214.20/reviveads/www/delivery/avw.php?zoneid=30&amp;n=4ef9bc4");
//        mWebView.loadUrl("http://192.168.14.119:8080");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mWebView.loadUrl("https://www.baidu.com/");
//        mWebView.loadUrl("http://123.207.214.20/reviveads/www/delivery/ajs.php?zoneid=31&cb=30340930042&charset=UTF-8&loc=file:///C:/Users/ripple/Desktop/ins.html");
        String content = "\"<\"+\"a href=\\'http://123.207.214.20/reviveads/www/delivery/ck.php?oaparams=2__bannerid=35__zoneid=31__cb=fd336e25ff__oadest=http%3A%2F%2Fwww.baidu.com\\' target=\\'123\\'><\"+\"img src=\\'http://123.207.214.20/reviveads/www/images/4c95812ff67e17ada1874cef6879272d.png\\' width=\\'239\\' height=\\'208\\' alt=\\'Alt text\\' title=\\'Alt text\\' border=\\'0\\' /><\"+\"/a><\"+\"div id=\\'beacon_fd336e25ff\\' style=\\'position: absolute; left: 0px; top: 0px; visibility: hidden;\\'><\"+\"img src=\\'http://123.207.214.20/reviveads/www/delivery/lg.php?bannerid=35&amp;campaignid=17&amp;zoneid=31&amp;loc=file%3A%2F%2F%2FC%3A%2FUsers%2Fripple%2FDesktop%2Fins.html&amp;cb=fd336e25ff\\' width=\\'0\\' height=\\'0\\' alt=\\'\\' style=\\'width: 0px; height: 0px;\\' /><\"+\"/div>\\n\"";
        String script = "<script>" + "document.body.innerHTML=" + content + "</script>";
        String html1 = "\n" +
                "<html>\n" +
                "<body>" + script + "</body>\n" +
                "</html>";
        mWebView.loadData(html1, "text/html", "UTF8");
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
        }
    }
}
