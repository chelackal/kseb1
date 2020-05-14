package lapis.apps.lapislearning.kseb.recycler_adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import lapis.apps.lapislearning.kseb.datamodels.DataComplaint;
import retrofit2.Call;


public class RecyclerViewAdapterComplaints extends RecyclerView.Adapter<RecyclerViewAdapterComplaints.ViewHolder> {


    public Activity mContext;
    ArrayList<DataComplaint> data;

    public RecyclerViewAdapterComplaints(Activity mContext, ArrayList<DataComplaint> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        <!--complaintNumber,consumerNumber,categoryComplaint,descriptionComplaint,
//        personComplaint,-->
//        <!--addressComplaint,mobileComplaint,areaComplaint,districtComplaint,
//        datecomplaint,replyComplaint;-->
        public TextView complaintDescriptionTV,consumerNumberTV,categoryTV,personTV,addressTV,mobileTV
        ,areaTV,districtTV,dateTextView,replyTV;
        public LinearLayout personDetailsLayout;
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
