package lapis.apps.lapislearning.kseb.recycler_adapters;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
import lapis.apps.lapislearning.kseb.fragments.WorkFragment;
import retrofit2.Call;


public class RecyclerViewAdapterWorks extends RecyclerView.Adapter<RecyclerViewAdapterWorks.ViewHolder> {


    public Activity mContext;
    ArrayList<DataComplaint> data;
    WorkFragment fragment;

    public RecyclerViewAdapterWorks(Activity mContext, ArrayList<DataComplaint> data, WorkFragment fragment) {
        this.mContext = mContext;
        this.data = data;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        <!--complaintNumber,consumerNumber,categoryComplaint,descriptionComplaint,
//        personComplaint,-->
//        <!--addressComplaint,mobileComplaint,areaComplaint,districtComplaint,
//        datecomplaint,replyComplaint;-->
        public TextView complaintDescriptionTV,consumerNumberTV,categoryTV,personTV,addressTV,mobileTV
        ,areaTV,districtTV,dateTextView,replyTV;
        public LinearLayout personDetailsLayout;
        public CardView itemCard;
//        public ImageView deleteImageView;

        public ViewHolder(View view) {
            super(view);

            complaintDescriptionTV = view.findViewById(R.id.complaintdescription);
            consumerNumberTV = view.findViewById(R.id.complaintconsumernumber);
            categoryTV = view.findViewById(R.id.complaintcategory);
            personTV = view.findViewById(R.id.complaintPerson);
            addressTV = view.findViewById(R.id.complaintAddress);
            mobileTV = view.findViewById(R.id.complaintMobile);
            areaTV = view.findViewById(R.id.complaintArea);
            districtTV = view.findViewById(R.id.complaintDistrict);
            replyTV = view.findViewById(R.id.complaintReply);
            dateTextView = view.findViewById(R.id.complaintdate);
            personDetailsLayout = view.findViewById(R.id.persondetailslayout);
            itemCard = view.findViewById(R.id.itemCardview);
//            deleteImageView = view.findViewById(R.id.delete);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_row, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataComplaint product = data.get(position);
//complaintDescriptionTV,consumerNumberTV,categoryTV,personTV,addressTV,mobileTV
//,areaTV,districtTV,dateTextView,replyTV;
        holder.complaintDescriptionTV.setText("Complaint : "+product.getDescriptionComplaint());
        holder.consumerNumberTV.setText("Consumer Number : "+product.getConsumerNumber());
        holder.categoryTV.setText("Category : "+product.getCategoryComplaint());
        holder.personTV.setText("Name : "+product.getPersonComplaint());
        holder.addressTV.setText("Address : "+product.getAddressComplaint());
        holder.mobileTV.setText("Mobile : "+product.getMobileComplaint());
        holder.areaTV.setText("Area : "+product.getAreaComplaint());
        holder.districtTV.setText("Pincode : "+product.getDistrictComplaint());
        holder.dateTextView.setText("PostDate : "+product.getDatecomplaint());

        if (holder.areaTV.getText().toString().trim().length()>0)
            holder.areaTV.setVisibility(View.VISIBLE);
        else
            holder.areaTV.setVisibility(View.GONE);

        if (holder.districtTV.getText().toString().trim().length()>0)
            holder.districtTV.setVisibility(View.VISIBLE);
        else
            holder.districtTV.setVisibility(View.GONE);

        if (product.getReplyComplaint().length()>0)
            holder.replyTV.setText("Reply : "+product.getReplyComplaint());
        else
            holder.replyTV.setText("Not Replied");

        holder.complaintDescriptionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.personDetailsLayout.getVisibility() == View.VISIBLE){
                    holder.personDetailsLayout.setVisibility(View.GONE);
                }else{
                    holder.personDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReply(product.getComplaintNumber());
            }
        });


    }

    public  void addReply(final String tenderId){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_reply_layout);

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatButton sendAppCompatButton = dialog.findViewById(R.id.postBtn);
        final TextInputEditText contentTIET = dialog.findViewById(R.id.contentTIET);

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
                if (stat == true){

                    setReply(tenderId,contentString);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackground(Color.TRANSPARENT);

    }

    //send FeedBack
    private void setReply(String cmp_id,String content) {
        Map<String, String> params = new HashMap<>();
//{"cmp_id":"","reply":""}
        params.put("uid", ""+ Utility.UID);
        params.put("cmp_id", ""+cmp_id);
        params.put("reply", ""+content);


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.SETSTAFFCOMPLAINTREPLYURL,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(mContext, new ResponseCallback() {
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
                            fragment.getComplaints();

                        } else
                            Toast.makeText(mContext, "Reply sending failed,Try again", Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    try{
                        Toast.makeText(mContext, "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}

                }

                if(ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
//        return rresult;

    }

//    private void deleteMessage(String msg_id, final int pos) {
//
//        Map<String, String> params = new HashMap<>();
//
//        params.put("messageid", ""+msg_id);
//
//
//        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.DELETEMESSAGESURL,params);
//        jsonObjectCall.clone().enqueue(new ResponseHandler(mContext, new ResponseCallback() {
//            @Override
//            public void getResponse(int code, JsonObject jsonObject) {
//
//                JSONObject jsonObj = null;
//                try {
//                    jsonObj = new JSONObject(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
////                {"status":1}
//                if (jsonObj != null) {
//                    JSONObject actor = null;
//                    try {
//                        if (jsonObj.has("status")) {
//                            if (jsonObj.getInt("status")>0){
//                                removeAt(pos);
//                            }
//                           }
//                        if(ResponseHandler.progressDialog!=null)
//                            ResponseHandler.progressDialog.dismiss();
//
//
//                    } catch (Exception eee) {
//                        eee.printStackTrace();
//                    }
//
//                }
//            }
//            @Override
//            public void getFailure(Call<JsonObject> call, int code) {
//                if (code==1) {
//
//                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    try{
//                        Toast.makeText(mContext, "Can't Connect with server", Toast.LENGTH_SHORT).show();}catch(Exception e){}
//
//                }
//
//                if(ResponseHandler.progressDialog!=null)
//                    ResponseHandler.progressDialog.dismiss();
//
//            }
//
//
//        }, jsonObjectCall,1));
//    }
//    public void removeAt(int position) {
//        dataFeedbacks.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, dataFeedbacks.size());
//    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
