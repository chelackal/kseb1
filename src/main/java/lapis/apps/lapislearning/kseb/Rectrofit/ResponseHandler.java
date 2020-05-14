package lapis.apps.lapislearning.kseb.Rectrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by az-sys on 19/8/17.
 */

public class ResponseHandler implements Callback<JsonObject> {

    Context context;
    ResponseCallback web_interface;
    Call<JsonObject> jsonObjectCall;
    int show;


    public static ProgressDialog progressDialog;



    public ResponseHandler(Activity context, ResponseCallback web_interface, Call<JsonObject> jsonObjectCall, int n) {
        this.context = context;
        show = n;
        this.web_interface = web_interface;
        this.jsonObjectCall = jsonObjectCall;
        String ss = context.getClass().getName();
        Log.i("acttt", context.getClass().getName());
//        Toast.makeText(context,ss, Toast.LENGTH_SHORT).show();

        //   if((context.getClass().getName().contains("LoginActivity"))||(context.getClass().getName().contains("RegistrationActivity"))||(context.getClass().getName().contains("SavedPcFragment"))) {
        if (show == 1) {

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading.....");
                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                }


//           progressDialog = new Dialog(context);
//           progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//           progressDialog.setContentView(R.layout.dialog);
//           progressDialog.setCancelable(false);
//           progressDialog.show();


    }
    public ResponseHandler(Context context, ResponseCallback web_interface, Call<JsonObject> jsonObjectCall, int n) {
        this.context = context;
        show = n;
        this.web_interface = web_interface;
        this.jsonObjectCall = jsonObjectCall;
        String ss = context.getClass().getName();
        Log.i("acttt", context.getClass().getName());
//        Toast.makeText(context,ss, Toast.LENGTH_SHORT).show();

        //   if((context.getClass().getName().contains("LoginActivity"))||(context.getClass().getName().contains("RegistrationActivity"))||(context.getClass().getName().contains("SavedPcFragment"))) {
        if (show == 1) {

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading.....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }


//           progressDialog = new Dialog(context);
//           progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//           progressDialog.setContentView(R.layout.dialog);
//           progressDialog.setCancelable(false);
//           progressDialog.show();


    }



    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


        if(show==1) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        switch (response.code()) {
            case Responsecode.ok:
                if (web_interface != null) {
                    if (response.body() != null) {
                        web_interface.getResponse(response.code(), response.body());
                    }
                }

                break;

            case Responsecode.server_error:
               // Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Internal Server Error", Snackbar.LENGTH_SHORT);

                //showalert("Server Error");
                break;

            case Responsecode.notfound:
                // Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Internal Server Error", Snackbar.LENGTH_SHORT);

              //  showalert("Not found");
                break;
        }

    }

    @Override
    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//        if(context.getClass().getName().contains("LoginActivity")) {
//        if(progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }}
        int code;
        if (!Retrofit_Helper.isNetworkAvailable(context)) {

            // CustomDialogUI.dialog(context,"Try again","No Internet Connection");
            //   showalert("No Internet Connection");
//            MyAppUtility.showToast("No Internet Connection");
            code=1;
        } else {
            //  Utility.alertdialoguePasswordChange(context,"Sign_in","You have changed your password. Please login with your new password");

           // MyAppUtility.showToast("Can't Connect with server");
            code=2;
            //  showalert("Can't Connect with server");


        }
web_interface.getFailure(call,code);


    }



}
