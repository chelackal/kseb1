package lapis.apps.lapislearning.kseb.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.R;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import lapis.apps.lapislearning.kseb.Utility;
import lapis.apps.lapislearning.kseb.datamodels.DataComplaintStaff;
import lapis.apps.lapislearning.kseb.datamodels.DataLeave;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterLeave;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterStaffComplaints;
import retrofit2.Call;

public class LeaveFragment extends Fragment {
    RecyclerView recyclerViewRV;
    FloatingActionButton addComplaintBtn;
    public ArrayList<DataLeave> dataComplaints;
    public boolean rresult = false;
    public int mYear;
    public int mMonth;
    public int mDay;
    private RecyclerViewAdapterLeave recyclerViewAdapterLeave;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.complaints_fragment, container, false);
        /**Get String from Bundle which is passed from MainActivityNavDrawer.java**/
        String text = getArguments().getString("text", "");
        init(view);
//        addComplaintBtn.setVisibility(View.GONE);
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
        return view;
    }

    private void init(View view) {
         recyclerViewRV =  view.findViewById(R.id.recyclerview_rv);
         addComplaintBtn =  view.findViewById(R.id.addComplaintBtn);
    }

    public void getComplaints() {

        Map<String, String> params = new HashMap<>();
            params.put("uid", ""+ Utility.UID);
//            params.put("pincode", ""+ Utility.PINCODE);
//        {"uid":"","pincode":""}

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.LISTLEAVEREQUESTURL,params);
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
                            DataLeave ca;
                            for(int k=0;k<login_array.length();k++){
                                ca=new DataLeave();
//{"leave_id":"2","emp_id":"st100","start_date":"2019-5-5","reason":"fsdf","days":"3",
// "status":"Not Approved","request_date":null}
                                actor = login_array.getJSONObject(k);
                                ca.setLeave_id(actor.getString("leave_id"));
                                ca.setEmp_id(actor.getString("emp_id"));
                                ca.setStart_date(actor.getString("start_date"));
                                ca.setReason(actor.getString("reason"));
                                ca.setDays(actor.getString("days"));
                                ca.setStatus(actor.getString("status"));
                                ca.setRequest_date(actor.getString("request_date"));


                                Log.d("getmessagesList", jsonObj.toString());
                                dataComplaints.add(ca);

                            }
                            recyclerViewAdapterLeave = new RecyclerViewAdapterLeave(getActivity(),dataComplaints, LeaveFragment.this);
                            recyclerViewRV.setAdapter(recyclerViewAdapterLeave);
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
        dialog.setContentView(R.layout.new_leave_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final TextInputEditText contentTIET = dialog.findViewById(R.id.contentTIET);
        final TextInputEditText noDaysTIET = dialog.findViewById(R.id.numberofdaysTIET);
        final TextView startDateTV = dialog.findViewById(R.id.startdate);
startDateTV.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        startDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
});

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
                String startDate = startDateTV.getText().toString().trim();
                String noDays = noDaysTIET.getText().toString().trim();

                if (startDate.isEmpty()|| startDate.equals("Start Date")){
                    stat = false;
//                    startDateTV.setText("Field Required");
                    Toast.makeText(getActivity(), "Date Field Empty or Invalid Date", Toast.LENGTH_SHORT).show();
                }else if (noDays.isEmpty()){
                    stat = false;
                    noDaysTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Number of Days Field Empty", Toast.LENGTH_SHORT).show();
                }
                if (stat == true){

                    setComplaint(contentString,startDate,noDays);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    //send FeedBack
    private void setComplaint(String reason,String startDate,String noDays) {
        rresult = false;
        Map<String, String> params = new HashMap<>();
// uid, reason,start_date, no_days
        params.put("uid", ""+ Utility.UID);
        params.put("reason", ""+reason);
        params.put("start_date", ""+startDate);
        params.put("no_days", ""+noDays);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.ADDLEAVEREQUESTURL,params);
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
                            Toast.makeText(getActivity(), "Leave Request sent succesfully", Toast.LENGTH_SHORT).show();

                            getComplaints();

                        } else
                            Toast.makeText(getActivity(), "Leave Request failed,Try again", Toast.LENGTH_SHORT).show();

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
