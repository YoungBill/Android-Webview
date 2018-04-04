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

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final java.lang.String URL_ROOT = "http://123.207.214.20/reviveads/www/delivery/spc.php?zones=32%7C29%7C30&amp;source=&amp;r=";
    private String mUrl;

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
                if (url.equals(mUrl)) {
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
        Random rand = new Random();
        mUrl = URL_ROOT + rand.nextInt(999999999) + "&amp;charset=UTF-8&amp;loc=file%3A///C%3A/Users/ripple/Desktop/ins.html";
        mWebView.loadUrl(mUrl);
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<script >\n" +
                    "\t\t\n" +
                    "\n" +
                    "var OA_outputs =" + "`" + html + "`" + "\n" +
                    "function unescapeHTML (a){//解码\n" +
                    "    a = \"\" + a;\n" +
                    "   return a.replace(/&lt;/g, \"<\").replace(/&gt;/g, \">\").replace(/&amp;/g, \"&\").replace(/&quot;/g, '\"').replace(/&apos;/g, \"'\");\n" +
                    "}\n" +
                    "function removePre(a){ //去除<pre>\n" +
                    "    return a.replace(/(<pre.+?>)|(<\\/pre>)|(\\\"\\+\\\")/g,'')\n" +
                    "}\n" +
                    "function getOA_output(a){ //得到数组\n" +
                    "    var reg = /(OA_output.+?v\\>)/ig\n" +
                    "    return a.match(reg)\n" +
                    "}\n" +
                    "function OA_show(name) {\n" +
                    "   if (typeof(adArray[name]) == 'undefined') {\n" +
                    "       return;\n" +
                    "    } else {\n" +
                    "        console.log(adArray[name])\n" +
                    "       document.write(adArray[name]);\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "var adArray = []; \n" +
                    "OA_outputs = unescapeHTML(OA_outputs)\n" +
                    "OA_outputs = removePre(OA_outputs)\n" +
                    "OA_outputs = getOA_output(OA_outputs)\n" +
                    "\n" +
                    "\n" +
                    "OA_outputs.forEach(function(v,i){\n" +
                    "    var reg = /\\[.+?\\]/g\n" +
                    "    var num = v.match(reg)[0].replace(/(\\[\\')|(\\'\\])/g,'')\n" +
                    "\n" +
                    "    num = parseFloat(num)\n" +
                    "    adArray[num] = v.substring(v.indexOf('<a'))\n" +
                    "})\n" +
                    "\n" +
                    "OA_show(29)\n" +
                    "OA_show(30)\n" +
                    "OA_show(32)\n" +
                    "\t</script>\n" +
                    "</body>\n" +
                    "</html>";
            final String finalHtml = html;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadData(finalHtml, "text/html", "UTF8");
                }
            });
        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
        }
    }
}
