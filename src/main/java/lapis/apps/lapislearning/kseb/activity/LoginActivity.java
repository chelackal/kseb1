package lapis.apps.lapislearning.kseb.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.MainActivityNavDrawer;
import lapis.apps.lapislearning.kseb.R;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import lapis.apps.lapislearning.kseb.SessionManager;
import lapis.apps.lapislearning.kseb.Utility;
import lapis.apps.lapislearning.kseb.datamodels.DataUser;
import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText userNameTextInputEditText,passwordTextInputEditText ;
    TextView signUpTextView,signInTextView ;
    public ArrayList<DataUser> dataUser;
    public ProgressBar progIndicator;
    private SessionManager session;//global variable

    @Override
    protected void onStart() {
        super.onStart();
        String uid = session.getuid();
        if (!uid.isEmpty()){
            Utility.UID = uid;
            Utility.USERROLE =session.getuserrole();
            Utility.NAME = session.getNameUser();
            if (Utility.USERROLE.equals("staff")){
                Utility.PINCODE = session.getPincode();
            }else{
//                Utility.lm = session.getLm();
            }
//            Utility.MOBILE = session.getMobileUser();
            startActivity(new Intent(getApplicationContext(), MainActivityNavDrawer.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(getString(R.string.app_name)+" - Login");
        session = new SessionManager(getApplicationContext());
        init();

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean stat = validate();
                if (stat){
                    progIndicator.setVisibility(View.VISIBLE);
                    getLogin();
                }else {
                    Toast.makeText(LoginActivity.this, "Login Failed"+stat, Toast.LENGTH_SHORT).show();
                }
            }
        });
//        signUpTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(LoginActivity.this,UserRegistrationActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }

    public boolean validate() {
        boolean stat = true;
        if (userNameTextInputEditText.getText().toString().trim().equals("")){
            stat = false;
            userNameTextInputEditText.setError("Field required");
        }
        if (passwordTextInputEditText.getText().toString().trim().equals("")){
            passwordTextInputEditText.setError("Field required");
            stat = false;
        }
        return stat;
    }

    public void init(){
        userNameTextInputEditText = findViewById(R.id.username_textinputedittext);
        passwordTextInputEditText = findViewById(R.id.password_textinputedittext);
        signUpTextView = findViewById(R.id.signup_textview);
        signInTextView = findViewById(R.id.signin_textview);
        progIndicator = findViewById(R.id.progIndication);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit!")
                .setMessage("Do want to close the application?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                        finish();
                    }
                }).create().show();

    }

    //i.	{"uid":"abc","upwd":"abcp"}
    private void getLogin() {

        Map<String, String> params = new HashMap<>();

        params.put("uid", ""+userNameTextInputEditText.getText().toString().trim());
        params.put("upwd", ""+passwordTextInputEditText.getText().toString().trim());
//i.	{“input”:”hi hello”,”uid”:””}


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.LOGINURL,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(LoginActivity.this, new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//{"result":[{"login_id":"2","username":"1212","password":"1212","role":"user","show_alert":1,"alert_msg":"Your Usage 578. You have exceeded the limit 25"}]}
                if (jsonObj != null) {
                    JSONObject actor = null;
                    try {
                        if (jsonObj.has("result")) {
                            JSONArray login_array = jsonObj.getJSONArray("result");


                            dataUser = new ArrayList<>();
                            DataUser ca;
//  {"login_id":"2","username":"1212","password":"1212","role":"user","name":"Ajay","lm":"34","show_alert":0}
//{"login_id":"3","username":"st100","password":"st100","role":"staff","pincode":"686686","name":"Jayan"}
//                                       for(int k=0;k<1;k++){
                                ca=new DataUser();
                                actor = login_array.getJSONObject(0);

                                ca.setLogin_id(actor.getString("login_id"));
                                ca.setUsername(actor.getString("username"));
                                ca.setPassword(actor.getString("password"));
                                ca.setRole(actor.getString("role"));
                                ca.setNameU(actor.getString("name"));
                                if (actor.getString("role").equals("staff")){
                                    ca.setPinCode(actor.getString("pincode"));
                                }else {
                                    ca.setShow_alert(actor.getString("show_alert"));
                                    ca.setLimitU(actor.getString("lm"));
                                    if (actor.has("alert_msg"))
                                    ca.setAlert_msg(actor.getString("alert_msg"));
                                }

                                Log.d("getSearchList", jsonObj.toString());
                                dataUser.add(ca);

//                            }
                            if (!dataUser.isEmpty()){
                                Utility.UID  = dataUser.get(0).getUsername();
                                Utility.USERROLE  = dataUser.get(0).getRole();
                                Utility.NAME  = dataUser.get(0).getNameU();
                                session.setuid(Utility.UID);
                                session.setuserrole(Utility.USERROLE);
                                session.setNameUser(Utility.NAME);
//                                Intent i  = new Intent(getApplicationContext(),MainActivity.class);
                                Intent i  = new Intent(getApplicationContext(),MainActivityNavDrawer.class);
                                if (Utility.USERROLE.equals("staff")){
                                    Log.d("getResponse: ",""+dataUser.get(0).getPinCode());
                                    session.setPincode(dataUser.get(0).getPinCode());
                                    Utility.PINCODE = dataUser.get(0).getPinCode();

                                }else{
//                                    Utility.lm  = dataUser.get(0).getLimitU();
//                                    session.setLm(Utility.lm);
                                if (dataUser.get(0).getShow_alert().equals("1")){
                                    i.putExtra("alert_msg",dataUser.get(0).getAlert_msg());
                                }}
                                startActivity(i);
                                finish();
                            }else{
                                progIndicator.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
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
progIndicator.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    try{
                        progIndicator.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}

                }

                if(ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));

    }

}

