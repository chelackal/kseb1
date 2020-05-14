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
import lapis.apps.lapislearning.kseb.datamodels.DataComplaint;
import lapis.apps.lapislearning.kseb.datamodels.DataTenders;
import lapis.apps.lapislearning.kseb.fragments.TendersFragment;
import retrofit2.Call;


public class RecyclerViewAdapterTenders extends RecyclerView.Adapter<RecyclerViewAdapterTenders.ViewHolder> {


    public Activity mContext;
    ArrayList<DataTenders> data;
    TendersFragment fragment;

    public RecyclerViewAdapterTenders(Activity mContext, ArrayList<DataTenders> data,TendersFragment fragment) {
        this.mContext = mContext;
        this.data = data;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//{"tender_id":"2","title":"Street light","work_description":"300 meter","date":"2019-3-15","status":"1"}
        public TextView titleTender,work_description,dateTender;
        public LinearLayout itemLayout;

        public ViewHolder(View view) {
            super(view);

            titleTender = view.findViewById(R.id.titletenderTV);
            work_description = view.findViewById(R.id.workdescriptiontenderTV);
            dateTender = view.findViewById(R.id.datetenderTV);
            itemLayout = view.findViewById(R.id.itemLayout);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tender_row, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataTenders product = data.get(position);
//{"tender_id":"2","title":"Street light","work_description":"300 meter","date":"2019-3-15","status":"1"}
        holder.titleTender.setText(""+product.getTitleTender());
        holder.work_description.setText(""+product.getWorkDescriptionTender());
        holder.dateTender.setText(""+product.getDateTender());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReply(product.getIdTender());
                Toast.makeText(mContext, ""+product.getIdTender()+product.getTitleTender()+product.getStatusTender(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
    private void setReply(String tenderId,String content) {
        Map<String, String> params = new HashMap<>();
//{"uid":"5","tender_id":"2","reply":"cvfsgfs"}
        params.put("uid", ""+ Utility.UID);
        params.put("tender_id", ""+tenderId);
        params.put("reply", ""+content);


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.REPLYTENDERURL,params);
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
                            fragment.getTenders();


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

}
