package lapis.apps.lapislearning.kseb.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.R;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import lapis.apps.lapislearning.kseb.Utility;
import lapis.apps.lapislearning.kseb.datamodels.DataComplaint;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterComplaints;
import retrofit2.Call;

public class ComplaintsFragment extends Fragment {
    RecyclerView recyclerViewRV;
    ImageView alertIV;
    RelativeLayout relativeLayout ;
    FloatingActionButton addComplaintBtn;
    public ArrayList<DataComplaint> dataComplaints;
    public RecyclerViewAdapterComplaints recyclerViewAdapterComplaints;
    public boolean rresult = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.complaints_fragment, container, false);
        /**Get String from Bundle which is passed from MainActivityNavDrawer.java**/
        String text = getArguments().getString("text", "");
        init(view);

        addComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComplaint();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL,false);
        recyclerViewRV.setNestedScrollingEnabled(true);
        recyclerViewRV.setLayoutManager(mLayoutManager);
        recyclerViewRV.setItemAnimator(new DefaultItemAnimator());
        getComplaints();
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
        return view;
    }

    private void init(View view) {
         recyclerViewRV =  view.findViewById(R.id.recyclerview_rv);
         addComplaintBtn =  view.findViewById(R.id.addComplaintBtn);
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
    }

    private void getComplaints() {

        Map<String, String> params = new HashMap<>();
            params.put("uid", ""+ Utility.UID);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.LISTALLCOMPLAINTURL,params);
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
                            dataComplaints = new ArrayList<>();
                            DataComplaint ca;
                            for(int k=0;k<login_array.length();k++){
                                ca=new DataComplaint();
//{"comp_no":"25","cons_no":"1212","category":"GeneralCompalints","description":"Hkkjhh","person":"anoop",
// "adrs":"Gghhj","mobile":"9847131977","areas":"yty","dist":"","dates":"2017-10-19","reply":"8545m,l"}
                                actor = login_array.getJSONObject(k);
                                ca.setComplaintNumber(actor.getString("comp_no"));
                                ca.setConsumerNumber(actor.getString("consumer_no"));
                                ca.setCategoryComplaint(actor.getString("category"));
                                ca.setDescriptionComplaint(actor.getString("description"));
                                ca.setPersonComplaint(actor.getString("person"));
                                ca.setAddressComplaint(actor.getString("adrs"));
                                ca.setMobileComplaint(actor.getString("mobile"));
                                ca.setAreaComplaint(actor.getString("areas"));
                                ca.setDistrictComplaint(actor.getString("pincode"));
                                ca.setDatecomplaint(actor.getString("dates"));
                                ca.setReplyComplaint(actor.getString("reply"));

                                Log.d("getmessagesList", jsonObj.toString());
                                dataComplaints.add(ca);

                            }
                            recyclerViewAdapterComplaints = new RecyclerViewAdapterComplaints(getActivity(),dataComplaints);
                            recyclerViewRV.setAdapter(recyclerViewAdapterComplaints);
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
    public  void addComplaint(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_complaint_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final TextInputEditText contentTIET = dialog.findViewById(R.id.contentTIET);
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

                    setComplaint(contentString);
                    dialog.dismiss();
                   }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    //send FeedBack
    private void setComplaint(String content) {
        rresult = false;
        Map<String, String> params = new HashMap<>();
//        {"cno":"1212","content":"2","nm":"fd","adr":"ekm","mob":"5655656565"}
        params.put("cno", ""+ Utility.UID);
        params.put("content", ""+content);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.SENDCOMPLAINTURL,params);
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
//                            rresult = true;
                            getComplaints();

                        } else
                            Toast.makeText(getActivity(), "Complaint sending failed,Try again", Toast.LENGTH_SHORT).show();

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
