package lapis.apps.lapislearning.kseb;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import lapis.apps.lapislearning.kseb.activity.LoginActivity;
import lapis.apps.lapislearning.kseb.fragments.BillsFragment;
import lapis.apps.lapislearning.kseb.fragments.ComplaintsFragment;
import lapis.apps.lapislearning.kseb.fragments.LeaveFragment;
import lapis.apps.lapislearning.kseb.fragments.NavDrawerFrag;
import lapis.apps.lapislearning.kseb.fragments.ProfileConsumerFragment;
import lapis.apps.lapislearning.kseb.fragments.ComplaintsStaffFragment;
import lapis.apps.lapislearning.kseb.fragments.ProfileFragment;
import lapis.apps.lapislearning.kseb.fragments.TendersFragment;
import lapis.apps.lapislearning.kseb.fragments.WorkFragment;

public class MainActivityNavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String title = "";
    public SessionManager session;
    public  TextView navHeadTV,navSubTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.USERROLE.equals("staff"))
        setContentView(R.layout.staff_nav_drawer_layout);
        else
        setContentView(R.layout.example_nav_drawer_layout);
        try{
        String alertMsg = getIntent().getExtras().getString("alert_msg");

        if (!alertMsg.isEmpty()){
            showAlertDialog(alertMsg);
        }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**Add below lines in your strings.xml file under res=>values=>strings.xml
         *<string name="navigation_drawer_open">Open navigation drawer</string>
         *<string name="navigation_drawer_close">Close navigation drawer</string>
         **/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v= navigationView.inflateHeaderView(R.layout.nav_header);
        session = new SessionManager(getApplicationContext());
        navHeadTV = v.findViewById(R.id.navHeadTV);
        navSubTV = v.findViewById(R.id.navSubTV);
        navHeadTV.setText(""+Utility.NAME);

//            try {
//                Menu menu = navigationView.getMenu();
//                MenuItem target;
//                if (Utility.PATIENTID.equals(""))
//                    target = menu.findItem(R.id.removepatient);
//                else
//                    target = menu.findItem(R.id.patient);
//
//                target.setVisible(false);
//            }catch(Exception e){
//                Log.d("onCreate:paE ",e.getMessage());
//            }}

        navigationView.setNavigationItemSelectedListener(this);

        Fragment frag = null;
        if (Utility.USERROLE.equals("staff")){
            frag = new ProfileFragment();
            title = " - Profile";
        }
        else{
            frag = new ProfileConsumerFragment();
            title = " - Profile";
        }

        Bundle bundle = new Bundle();
        bundle.putString("text","Profile");
//        title = " - Tenders";
        frag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, frag);
        transaction.commit(); // commit the changes
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name)+title);
    }

    private void showAlertDialog(String alertMsg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogalert);
//        dialog.setTitle(R.string.dialog_title);

        TextView messageTV = dialog.findViewById(R.id.alertmessagetv);
        TextView dismissbtn = dialog.findViewById(R.id.alertdismissbtn);
        messageTV.setText(alertMsg);
        dismissbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//
//            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame);
//            Fragment frag = null;
//            if (Utility.USERROLE.equals("staff")){
//                frag = new ComplaintsStaffFragment();
//                title = " - Complaints";
//            }
//            else{
//                frag = new TendersFragment();
//                title = " - Tenders";
//            }
//
//
//            if (currentFragment instanceof frag){
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
//            }else {
//                Bundle bundle = new Bundle();
//                bundle.putString("text","Tenders");
////        title = " - Tenders";
//                frag.setArguments(bundle);
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame, frag);
//                transaction.commit(); // commit the changes
//                getSupportActionBar().setTitle(getResources().getString(R.string.app_name)+title);
//            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment frag  = null;
        Bundle bundle = new Bundle();
        if (id == R.id.tenders) {
            frag = new TendersFragment();
            bundle.putString("text","Tenders");
            title = " - Tenders";
        } else if (id == R.id.complaints) {
            frag = new ComplaintsFragment();
            bundle.putString("text","Complaints");
            title = " - Complaints";
        } else if (id == R.id.bill) {
            frag = new BillsFragment();
            bundle.putString("text","Bills");
            title = " - Bills";
        } else if (id == R.id.work) {
            frag = new WorkFragment();
            bundle.putString("text","Work");
            title = " - Work";
        } else if (id == R.id.complaintsstaff) {
            frag = new ComplaintsStaffFragment();
            bundle.putString("text","Complaints");
            title = " - Complaints";
        } else if (id == R.id.profilestaff) {
            frag = new ProfileFragment();
            bundle.putString("text","Profile");
            title = " - Profile";
        }else if (id == R.id.profileconsumer) {
            frag = new ProfileConsumerFragment();
            bundle.putString("text","Profile");
            title = " - Profile";
        }else if (id == R.id.salaryslip) {
            frag = new NavDrawerFrag();
            bundle.putString("text","Salary Slip");
            title = " - Salary Slip";
        }else if (id == R.id.leave) {
            frag = new LeaveFragment();
            bundle.putString("text","Leave");
            title = " - Leave";
        }else if (id == R.id.logout) {
           session.logoutUser();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
        try{
        frag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
        transaction.commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name)+title);
        }
        catch (Exception e){
            Log.d(Utility.TAG, "onNavigationItemSelected: "+e.getMessage()+e.getCause());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void makeToast(View view){
        Toast.makeText(this, "IV clicked", Toast.LENGTH_SHORT).show();
    }

}
