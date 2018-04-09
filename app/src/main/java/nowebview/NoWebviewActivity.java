package nowebview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.webview.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import nowebview.model.AdBean;
import retrofit2.Call;

/**
 * Created by baina on 18-4-9.
 */

public class NoWebviewActivity extends AppCompatActivity {

    private static final String TAG = NoWebviewActivity.class.getSimpleName();

    private ImageView mImageView1;
    private ImageView mImageView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowebview);
        mImageView1 = findViewById(R.id.image1);
        mImageView2 = findViewById(R.id.image2);

        querySwitchConfig("6", "30", new CommonCallback<AdBean>() {
            @Override
            public void onResponse(final AdBean adBean) {
                Picasso.with(NoWebviewActivity.this).load(adBean.getImgUrl()).into(mImageView1);
                mImageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(adBean.getLocationUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        querySwitchConfig("6", "32", new CommonCallback<AdBean>() {
            @Override
            public void onResponse(final AdBean adBean) {
                Picasso.with(NoWebviewActivity.this).load(adBean.getImgUrl()).into(mImageView2);
                mImageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(adBean.getLocationUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void querySwitchConfig(String publishId, String zoneId, CommonCallback<AdBean> callback) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("publishId", publishId);
        queryParams.put("zoneId", zoneId);
        final Call<AdBean> call = RetrofitRequest.getInstance().getRetrofitApi().loadAdBean(queryParams);
        call.enqueue(callback);
    }
}
