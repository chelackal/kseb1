package lapis.apps.lapislearning.kseb.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import lapis.apps.lapislearning.kseb.datamodels.DataComplaint;
import lapis.apps.lapislearning.kseb.datamodels.DataTenders;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterComplaints;
import lapis.apps.lapislearning.kseb.recycler_adapters.RecyclerViewAdapterTenders;
import retrofit2.Call;

public class TendersFragment extends Fragment {
    RecyclerView recyclerViewRV;
    ImageView alertIV;
    RelativeLayout relativeLayout ;
    public ArrayList<DataTenders> dataTenders;
    public RecyclerViewAdapterTenders recyclerViewAdapterTenders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tenders_fragment, container, false);
        /**Get String from Bundle which is passed from MainActivityNavDrawer.java**/
        String text = getArguments().getString("text", "");
        init(view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL,false);
        recyclerViewRV.setNestedScrollingEnabled(true);
        recyclerViewRV.setLayoutManager(mLayoutManager);
        recyclerViewRV.setItemAnimator(new DefaultItemAnimator());
        getTenders();
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

    public void getTenders() {

        Map<String, String> params = new HashMap<>();
        params.put("uid", ""+ Utility.UID);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.LISTALLTENDERURL,params);
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
                            dataTenders = new ArrayList<>();
                            DataTenders ca;
                            for(int k=0;k<login_array.length();k++){
                                ca=new DataTenders();
//{"tender_id":"2","title":"Street light","work_description":"300 meter","date":"2019-3-15","status":"1"}
                                actor = login_array.getJSONObject(k);
                                ca.setIdTender(actor.getString("tender_id"));
                                ca.setTitleTender(actor.getString("title"));
                                ca.setWorkDescriptionTender(actor.getString("work_description"));
                                ca.setDateTender(actor.getString("date"));
                                ca.setStatusTender(actor.getString("status"));

                                Log.d("getmessagesList", jsonObj.toString());
                                dataTenders.add(ca);

                            }
                            recyclerViewAdapterTenders = new RecyclerViewAdapterTenders(getActivity(),dataTenders,TendersFragment.this);
                            recyclerViewRV.setAdapter(recyclerViewAdapterTenders);
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
}
