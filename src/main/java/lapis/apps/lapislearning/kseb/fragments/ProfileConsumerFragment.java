package lapis.apps.lapislearning.kseb.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.R;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import lapis.apps.lapislearning.kseb.Utility;
import retrofit2.Call;

public class ProfileConsumerFragment extends Fragment {
    TextView iconTV,empIdTV,namePTV,addressPTV,contactPTV,dobPTV,pincodePTV,areaPTV,
            plotPTV,streetPTV,cityPTV,phonePTV,emailPTV,conStatusPTV
            ,editPasswordButton;
    ImageView alertIV;
    RelativeLayout relativeLayout ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profileconsumer_fragment, container, false);
        /**Get String from Bundle which is passed from MainActivityNavDrawer.java**/
        String text = getArguments().getString("text", "");
        init(view);
        getProfile();
        if (Utility.USERROLE.equals("staff"))
            relativeLayout.setVisibility(View.GONE);
        else
            relativeLayout.setVisibility(View.VISIBLE);
        alertIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.alertAdd(getActivity());
            }
        });

        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPassword();
            }
        });
        return view;
    }

    private void init(View view) {

        iconTV = view.findViewById(R.id.iconTV);
        empIdTV = view.findViewById(R.id.consumerNumberProfile);
        namePTV = view.findViewById(R.id.nameProfile);
        addressPTV = view.findViewById(R.id.addressProfile);
        contactPTV = view.findViewById(R.id.contactProfile);
        dobPTV = view.findViewById(R.id.dobPTV);
        pincodePTV = view.findViewById(R.id.pincodePTV);
        areaPTV = view.findViewById(R.id.areaPTV);
        plotPTV = view.findViewById(R.id.plotPTV);
        streetPTV = view.findViewById(R.id.streetPTV);
        cityPTV = view.findViewById(R.id.cityPTV);
        phonePTV = view.findViewById(R.id.phonePTV);
        emailPTV = view.findViewById(R.id.emailProfile);
        conStatusPTV = view.findViewById(R.id.conStatusPTV);
        editPasswordButton = view.findViewById(R.id.editPassword);
        alertIV =  view.findViewById(R.id.alertiv);
        relativeLayout =  view.findViewById(R.id.rL);
        //"http://i.imgur.com/Vth6CBz.gif"
        Glide.with(getActivity())
                .load(Utility.URLT)
                .override(40,40)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.stat_sys_download)
                .error(android.R.drawable.stat_notify_error)
                .into(alertIV);
//iconTV,empIdTV,namePTV,addressPTV,contactPTV,dobPTV,pincodePTV,areaPTV,plotPTV,streetPTV,cityPTV,
// phonePTV,emailPTV,conStatusPTV;
    }

    private void getProfile() {

        Map<String, String> params = new HashMap<>();
        params.put("cno", ""+ Utility.UID);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.VIEWCONSUMERPROFILEURL,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(getActivity(), new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObj != null) {
                    JSONObject actor = null;
                    try {
                        if (jsonObj.has("result")) {
                            JSONArray login_array = jsonObj.getJSONArray("result");
                            actor = login_array.getJSONObject(0);
//{"consumer_no":"1212","cname":"Ajay","address":"Pala mangattukavala","dob":"","pincode":"686686",
// "area":"Kaloor","plot":"Signal","street":"3 A","city":"Kaloor","phone":"0484629584",
// "mobile":"9896857447","email":"ajay@gmail.com","con_status":"1"}
                            String c = String.valueOf(actor.getString("cname").charAt(0)).toUpperCase();
                            iconTV.setText(""+c);
                            empIdTV.setText("Consumer Id : "+actor.getString("consumer_no"));
                            namePTV.setText(actor.getString("cname"));
                            addressPTV.setText("Address : "+actor.getString("address"));
                            dobPTV.setText("DOB : "+actor.getString("dob"));
                            pincodePTV.setText("Pincode : "+actor.getString("pincode"));
                            areaPTV.setText("Area : "+actor.getString("area"));
                            plotPTV.setText("Plot : "+actor.getString("plot"));
                            streetPTV.setText("Street : "+actor.getString("street"));
                            cityPTV.setText("City : "+actor.getString("city"));
                            phonePTV.setText("Phone : "+actor.getString("phone"));
                            contactPTV.setText("Mobile : "+actor.getString("mobile"));
                            emailPTV.setText("email : "+actor.getString("email"));
                            conStatusPTV.setText("Connection Status : "+actor.getString("con_status"));
                            Utility.lm = actor.getString("lm");

                            Log.d("getmessagesList", jsonObj.toString());
                        }
                        if(ResponseHandler.progressDialog!=null)
                            ResponseHandler.progressDialog.dismiss();


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }

                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    try{
                        Toast.makeText(getActivity(), "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}

                }

                if(ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));

    }
    public  void addPassword(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.changepass_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final TextInputEditText oldTIET = dialog.findViewById(R.id.oldpasswordTIET);
        final TextInputEditText newTIET = dialog.findViewById(R.id.newPasswordTIET);
        final TextInputEditText confirmTIET = dialog.findViewById(R.id.confirmPassTIET);

        confirmTIET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendAppCompatButton.setEnabled(!charSequence.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean stat = true;
                String oldPass = oldTIET.getText().toString().trim();
                String newPass = newTIET.getText().toString().trim();
                String confirmPass = confirmTIET.getText().toString().trim();
                if (oldPass.isEmpty()){
                    stat = false;
                    oldTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Old Password Field Empty", Toast.LENGTH_SHORT).show();
                }else if (newPass.isEmpty()){
                    stat = false;
                    newTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "New Password Field Empty", Toast.LENGTH_SHORT).show();
                }else if (newPass.equals(confirmPass)) { }else{
                    stat = false;
                    confirmTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                }

                if (stat == true){

                    setPass(newPass,oldPass);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    //send FeedBack
    private void setPass(String newPass,String oldPass) {
        Map<String, String> params = new HashMap<>();
//{"uid":"1212","upwd":"12123","opwd":"1212"}
        params.put("uid", ""+ Utility.UID);
        params.put("upwd", ""+newPass);
        params.put("opwd", ""+oldPass);


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.CHANGEUSERPASSWORDURL,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(getActivity(), new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //JSONObject jsonObj = new JSONObject(datafromserver);
//    {"status":4}

                if (jsonObj != null) {
                    JSONObject actor = null;
                    try {
                        if (jsonObj.has("status")&&jsonObj.getInt("status")>0) {
                            if(ResponseHandler.progressDialog!=null)
                                ResponseHandler.progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(getActivity(), "Password Changing failed", Toast.LENGTH_SHORT).show();

                        if(ResponseHandler.progressDialog!=null)
                            ResponseHandler.progressDialog.dismiss();


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }

                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    try{
                        Toast.makeText(getActivity(), "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}

                }

                if(ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
//        return rresult;

    }
}
