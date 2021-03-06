package lapis.apps.lapislearning.kseb.recycler_adapters;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
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
import lapis.apps.lapislearning.kseb.datamodels.DataComplaintStaff;
import lapis.apps.lapislearning.kseb.datamodels.DataLeave;
import lapis.apps.lapislearning.kseb.fragments.ComplaintsStaffFragment;
import lapis.apps.lapislearning.kseb.fragments.LeaveFragment;
import retrofit2.Call;


public class RecyclerViewAdapterLeave extends RecyclerView.Adapter<RecyclerViewAdapterLeave.ViewHolder> {


    public Activity mContext;
    ArrayList<DataLeave> data;
    LeaveFragment fragment;

    public RecyclerViewAdapterLeave(Activity mContext, ArrayList<DataLeave> data, LeaveFragment fragment) {
        this.mContext = mContext;
        this.data = data;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//{"leave_id":"2","emp_id":"st100","start_date":"2019-5-5","reason":"fsdf","days":"3",
// "status":"Not Approved","request_date":null}
        public TextView startDateTV,reasonTV,noDaysTV,statusTV,requestDateTV;
        public LinearLayout itemCard;
//        public ImageView deleteImageView;

        public ViewHolder(View view) {
            super(view);

            startDateTV = view.findViewById(R.id.startdateTV);
            reasonTV = view.findViewById(R.id.reasonTV);
            noDaysTV = view.findViewById(R.id.noDaysTV);
            statusTV = view.findViewById(R.id.statusTV);
            requestDateTV = view.findViewById(R.id.requestdateTV);

            itemCard = view.findViewById(R.id.itemCardview);
//            deleteImageView = view.findViewById(R.id.delete);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave_row, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataLeave product = data.get(position);
        holder.statusTV.setText(""+product.getStatus());
        holder.startDateTV.setText(""+product.getStart_date());
        holder.reasonTV.setText(""+product.getReason());
        holder.noDaysTV.setText(""+product.getDays());
        holder.requestDateTV.setText(""+product.getRequest_date());




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

    @Override
    public int getItemCount() {
        return data.size();
    }

}
