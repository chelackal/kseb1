package lapis.apps.lapislearning.kseb.Rectrofit;


import com.google.gson.JsonObject;

/**
 * Created by az-sys on 19/8/17.
 */

public interface ResponseCallback {

    public void getResponse(int code, JsonObject jsonObject);

   public void  getFailure(retrofit2.Call<JsonObject> call, int code);


}
