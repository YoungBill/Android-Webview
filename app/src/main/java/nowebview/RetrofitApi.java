package nowebview;

import java.util.Map;

import nowebview.model.AdBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by baina on 17-12-12.
 */

public interface RetrofitApi {

    @GET("http://123.206.205.11:8082/getAdinfo")
    Call<AdBean> loadAdBean(@QueryMap Map<String, String> params);

}
