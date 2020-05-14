package lapis.apps.lapislearning.kseb.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lapis.apps.lapislearning.kseb.R;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseCallback;
import lapis.apps.lapislearning.kseb.Rectrofit.ResponseHandler;
import lapis.apps.lapislearning.kseb.Rectrofit.Retrofit_Helper;
import lapis.apps.lapislearning.kseb.Utility;
import lapis.apps.lapislearning.kseb.datamodels.DataBill;
import lapis.apps.lapislearning.kseb.datamodels.DataTenders;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterTenders;
import retrofit2.Call;

public class BillsFragment extends Fragment {
    TextView billId,consumerNumberBill,createdDateBill,amountBill,paidDateBill,payBtn,
            fineDateBill,lastDateBill,netAmountBill,accountBill;
    ImageView alertIV;
    RelativeLayout relativeLayout ;
    public ArrayList<DataBill> dataBills;
//    <!-- billId,consumerNumberBill,createdDateBill,amountBill,
//    paidDateBill,billStatus,fineDateBill,
//    lastDateBIll,netAmountBill,accountBill;-->

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bills_fragment, container, false);
        /**Get String from Bundle which is passed from MainActivityNavDrawer.java**/
        String text = getArguments().getString("text", "");
        init(view);
        getBill();
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

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPayment(amountBill.getText().toString().trim(),billId.getText().toString().trim());
                Toast.makeText(getActivity(), "PAY Action", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void init(View view) {
         billId =  view.findViewById(R.id.idBill);
         consumerNumberBill =  view.findViewById(R.id.consumerNumberBill);
         createdDateBill =  view.findViewById(R.id.createdDateBill);
         amountBill =  view.findViewById(R.id.amountBill);
         paidDateBill =  view.findViewById(R.id.paidDate);
         fineDateBill =  view.findViewById(R.id.fineDateBill);
         lastDateBill =  view.findViewById(R.id.lastDateBill);
         netAmountBill =  view.findViewById(R.id.netAmountBill);
         accountBill =  view.findViewById(R.id.accountBill);
         payBtn =  view.findViewById(R.id.payBtn);
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

    private void getBill() {

        Map<String, String> params = new HashMap<>();
        params.put("cno", ""+ Utility.UID);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.SEARCHURL,params);
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
                            dataBills = new ArrayList<>();
                            DataBill ca;
//                            for(int k=0;k<login_array.length();k++){
                                ca=new DataBill();
//{"bill_id":"1","consumer_no":"1212","createdDate":"26-02-2019","amount":"18954","paidDate":"26-02-2019",
// "billStatus":"New","fineDate":"28-03-2019","lastDate":"2019-2-11","netamt":"18954","account":"1"}
                                actor = login_array.getJSONObject(0);
                                ca.setBillId(actor.getString("bill_id"));
                                ca.setConsumerNumberBill(actor.getString("consumer_no"));
                                ca.setCreatedDateBill(actor.getString("createdDate"));
                                ca.setAmountBill(actor.getString("amount"));
                                ca.setPaidDateBill(actor.getString("paidDate"));
                                ca.setBillStatus(actor.getString("billStatus"));
                                ca.setFineDateBill(actor.getString("fineDate"));
                                ca.setLastDateBIll(actor.getString("lastDate"));
                                ca.setNetAmountBill(actor.getString("netamt"));
                                ca.setAccountBill(actor.getString("account"));

                                Log.d("getmessagesList", jsonObj.toString());
                                dataBills.add(ca);

//                            }
//                            recyclerViewAdapterTenders = new RecyclerViewAdapterTenders(getActivity(),dataTenders);
//                            recyclerViewRV.setAdapter(recyclerViewAdapterTenders);
                            if (!dataBills.isEmpty()){
                                DataBill obj = dataBills.get(0);
//billId,consumerNumberBill,createdDateBill,amountBill,paidDateBill,payBtn,fineDateBill,lastDateBill,
// netAmountBill,accountBill;
                                billId.setText("Bill Id : "+obj.getBillId());
                                consumerNumberBill.setText(obj.getConsumerNumberBill());
                                createdDateBill.setText("Creation Date : "+obj.getCreatedDateBill());
                                amountBill.setText(obj.getAmountBill());
                                paidDateBill.setText("Paid Date : "+obj.getPaidDateBill());
                                fineDateBill.setText("Disconnection Date : "+obj.getFineDateBill());
                                lastDateBill.setText("Last Date : "+obj.getLastDateBIll());
                                netAmountBill.setText("NET Amount : "+obj.getNetAmountBill());
                                accountBill.setText("Account : "+obj.getAccountBill());


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

    public  void addPayment(final String amt, final String bill_id){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_payment_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final AppCompatButton cancelAppCompatButton = dialog.findViewById(R.id.cancelBtn);
        final TextView totalAmountTV = dialog.findViewById(R.id.totalamountBill);
        final TextView billIdPaymentTV = dialog.findViewById(R.id.paymentbillid);
        final TextInputEditText acnoTIET = dialog.findViewById(R.id.accountNumberTIET);
        final TextInputEditText acnameTIET = dialog.findViewById(R.id.accountNameTIET);
        final TextInputEditText cvvTIET = dialog.findViewById(R.id.cvvTIET);
        final TextInputEditText cardTIET = dialog.findViewById(R.id.cardTIET);
        final TextInputEditText cpinTIET = dialog.findViewById(R.id.pinTIET);
        final TextInputEditText expyearTIET = dialog.findViewById(R.id.yearTIET);
        final TextInputEditText expmonthTIET = dialog.findViewById(R.id.monthTIET);

//        cpinTIET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                sendAppCompatButton.setEnabled(!charSequence.toString().trim().isEmpty());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        cancelAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        sendAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean stat = true;
//                acno,acname,cvv,card,cpin,expyear,expmonth,amt,bill_id;
                String acno= acnoTIET.getText().toString().trim();
                String acname = acnameTIET.getText().toString().trim();
                String cvv = cvvTIET.getText().toString().trim();
                String card = cardTIET.getText().toString().trim();
                String cpin = cpinTIET.getText().toString().trim();
                String expyear = expyearTIET.getText().toString().trim();
                String expmonth = expmonthTIET.getText().toString().trim();
                if (acname.isEmpty()){
                    stat = false;
                    acnameTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Account Name Field Empty", Toast.LENGTH_SHORT).show();
                }else if (acno.isEmpty()){
                    stat = false;
                    acnoTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Account Number Field Empty", Toast.LENGTH_SHORT).show();
                }else if (cvv.isEmpty()){
                    stat = false;
                    cvvTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "CVV Field Empty", Toast.LENGTH_SHORT).show();
                }else if (card.isEmpty()){
                    stat = false;
                    cardTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Card Field Empty", Toast.LENGTH_SHORT).show();
                }else if (cpin.isEmpty()){
                    stat = false;
                    cpinTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Pin Field Empty", Toast.LENGTH_SHORT).show();
                }else if (expyear.isEmpty()){
                    stat = false;
                    expyearTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Expiry Year Field Empty", Toast.LENGTH_SHORT).show();
                }else if (expmonth.isEmpty()){
                    stat = false;
                    expmonthTIET.setError("Field Required");
                    Toast.makeText(getActivity(), "Expiry Month Field Empty", Toast.LENGTH_SHORT).show();
                }

                if (stat == true){

                    setPayment(acno,acname,cvv,card,cpin,expyear,
                            expmonth,amt,bill_id);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
        totalAmountTV.setText("Total Amount : "+amt+" Rs");
        billIdPaymentTV.setText(""+bill_id);
        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    private void setPayment(String acno,String acname,String cvv,String card,String cpin,String expyear,
                            String expmonth,String amt,String bill_id) {
        Map<String, String> params = new HashMap<>();
//{"acno":"","acname":"","cvv":"","card":"","cpin":"","expyear":"","expmonth":"","amt":"","bill_id":""}
        params.put("acno", ""+acno);
        params.put("acname", ""+acname);
        params.put("cvv", ""+cvv);
        params.put("card", ""+card);
        params.put("cpin", ""+cpin);
        params.put("expyear", ""+expyear);
        params.put("expmonth", ""+expmonth);
        params.put("amt", ""+amt);
        params.put("bill_id", ""+bill_id);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.PAYAMOUNTURL,params);
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
                        if (jsonObj.has("status")&&jsonObj.getInt("status")>0) {
                            if(ResponseHandler.progressDialog!=null)
                                ResponseHandler.progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Payment Completed Successfully", Toast.LENGTH_SHORT).show();
                            getBill();
                        } else
                            Toast.makeText(getActivity(), "Payment failed,Try again", Toast.LENGTH_SHORT).show();

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
}
