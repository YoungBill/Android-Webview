package nowebview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CommonCallback<T> implements Callback<T> {

    private static final String TAG = "CommonCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onResponse(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t);
    }

    public abstract void onResponse(T response);

    public abstract void onFailure(Throwable t);
}