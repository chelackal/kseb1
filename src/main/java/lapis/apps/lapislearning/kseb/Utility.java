package lapis.apps.lapislearning.kseb;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import retrofit2.Call;

public class Utility {
    public static final String BASEURL = "https://lapis.co.in/kseb/";
    public static final String LISTALLTENDERURL = "listalltender.php";
    public static final String REPLYTENDERURL = "replytender.php";
    public static final String LOGINURL = "login.php";
    public static final String SENDCOMPLAINTURL = "sendComplaint.php";
    public static final String LISTALLCOMPLAINTURL = "listallcomplaint.php";
    public static final String LISTALLSTAFFCOMPLAINTURL = "listallempcomplaint.php";
    public static final String SETSTAFFCOMPLAINTREPLYURL = "setreply.php";
    public static final String SEARCHURL = "search.php";
    public static final String PAYAMOUNTURL = "payamount.php";
    public static final String VIEWPROFILEURL = "viewprofile.php";
    public static final String VIEWCONSUMERPROFILEURL = "viewconsumerprofile.php";
    public static final String LISTALLWORKSURL = "listallwork.php";
    public static final String ADDSTAFFCOMPLAINTURL = "addempcomplaint.php";
    public static final String ADDLEAVEREQUESTURL = "addleaverequest.php";
    public static final String LISTLEAVEREQUESTURL = "listleaverequest.php";
    public static final String CHANGEUSERPASSWORDURL = "changeuserpwd.php";
    public static final String CHANGESTAFFPASSWORDURL = "changestaffpwd.php";
    public static final String LIMITURL = "set_limit.php";//cno,lm


    public static final String TAG = "KSEB";
    public static final String URLN = "https://cdn.dribbble.com/users/796725/screenshots/2772521/notification.gif";
    public static final String URLT = "https://media.giphy.com/media/dYhu2zdas2Gd2/giphy.gif";

    public static String UID = "";
    public static String USERROLE = "";
    public static String NAME = "";
    public static String lm = "";
    public static String PINCODE = "";
    private static Context contextU;

    public static void alertAdd(Context context){
        contextU = context;
        addLimit();
//        Toast.makeText(context, "IVG Clicked", Toast.LENGTH_SHORT).show();

    }

    public static void addLimit(){
        final Dialog dialog = new Dialog(contextU);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_limit_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final TextInputEditText contentTIET = dialog.findViewById(R.id.contentTIET);
        final TextView previousLM = dialog.findViewById(R.id.previouslimittv);
//        final TextInputEditText consumerNumberTIET = dialog.findViewById(R.id.consumerNumberTIET);
//        final TextInputEditText nameTIET = dialog.findViewById(R.id.personTIET);
//        final TextInputEditText addressTIET = dialog.findViewById(R.id.addressAddTIET);
//        final TextInputEditText mobileTIET = dialog.findViewById(R.id.phoneTIET);

        contentTIET.addTextChangedListener(new TextWatcher() {
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
                String contentString = contentTIET.getText().toString().trim();
//                String cno = consumerNumberTIET.getText().toString().trim();
//                String nm = nameTIET.getText().toString().trim();
//                String adr = addressTIET.getText().toString().trim();
//                String mob = mobileTIET.getText().toString().trim();
//                if (nm.isEmpty()){
//                    stat = false;
//                    nameTIET.setError("Field Required");
//                    Toast.makeText(getActivity(), "Name Field Empty", Toast.LENGTH_SHORT).show();
//                }else if (cno.isEmpty()){
//                    stat = false;
//                    consumerNumberTIET.setError("Field Required");
//                    Toast.makeText(getActivity(), "Consumer Number Field Empty", Toast.LENGTH_SHORT).show();
//                }else if (adr.isEmpty()){
//                    stat = false;
//                    addressTIET.setError("Field Required");
//                    Toast.makeText(getActivity(), "Address Field Empty", Toast.LENGTH_SHORT).show();
//                }else if (mob.isEmpty()){
//                    stat = false;
//                    mobileTIET.setError("Field Required");
//                    Toast.makeText(getActivity(), "Phone Field Empty", Toast.LENGTH_SHORT).show();
//                }else if (mob.length()<10||mob.length()>10){
//                    stat = false;
//                    mobileTIET.setError("Valid Number Required");
//                    Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
//                }

                if (stat == true){

                    setLimit(contentString);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        previousLM .setText("Previous Limit : "+Utility.lm);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    //send FeedBack
    private static void setLimit(String content) {
        Map<String, String> params = new HashMap<>();
//        {"cno":"1212","content":"2","nm":"fd","adr":"ekm","mob":"5655656565"}
        params.put("cno", ""+ Utility.UID);
        params.put("lm", ""+content);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.LIMITURL,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(contextU, new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //JSONObject jsonObj = new JSONObject(datafromserver);
//    {"result":[{"lm":"500"}]}

                if (jsonObj != null) {
                    JSONObject actor = null;
                    try {
                        if (jsonObj.has("result")){
                            JSONArray login_array = jsonObj.getJSONArray("result");
                            actor = login_array.getJSONObject(0);
//                            rresult = true;
                            Utility.lm = actor.getString("lm");
                            Log.d( "getResponse:lm ",""+ Utility.lm);
                            Toast.makeText(contextU, "Alert Limit Set Succesfully", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(contextU, "Alert Limit sending failed,Try again", Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(contextU, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    try{
                        Toast.makeText(contextU, "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}

                }

                if(ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
//        return rresult;

    }

}


/*
view tender
reply tender

send complint
view reply, status

view bill amount
pay bill

BASE URL
https://lapis.co.in/kseb/

view limit exceed (toast)

KSEB URLS
*********

1. listalltender.php
{"result":[{"tender_id":"2","title":"Street light","work_description":"300 meter","date":"2019-3-15","status":"1"},{"tender_id":"3","title":"Cleanning","work_description":"150 Post painting","date":"2019-3-25","status":"1"}]}

2. replytender.php
{"uid":"5","tender_id":"2","reply":"cvfsgfs"}


3. login.php
{"uid":"1212","upwd":"1212"}


4.sendComplaint.php
{"cno":"1212","content":"2","nm":"fd","adr":"ekm","mob":"5655656565"}


5. listallcomplaint.php
{"uid":"1212"}
{"result":[{"comp_no":"25","cons_no":"1212","category":"General Compalints","description":"Hkkjhh","person":"anoop","adrs":"Gghhj","mobile":"9847131977","areas":"yty","dist":"","dates":"2017-10-19","reply":"8545m,l"}]}



6.searchWithQrCode.php
{"cno":"1212"}

7.search.php
{"cno":"1212"}

8. listallempcomplaint.php//staff
{"uid":"","pincode":""}


9. setreply.php//staff
{"cmp_id":"","reply":""}


10. payamount.php
{"acno":"","acname":"","cvv":"","card":"","cpin":"","expyear":"","expmonth":"","amt":"","bill_id":""}
{"status":1}

11.viewprofile.php
{"uid":"st100"}

{"result":[{"emp_id":"st100","name":"Jayan","address":"Pala 13B","contact":"988978987","designation":"Technician","salary":"16000"}]}
 */