package com.threedev.machinetest.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.threedev.machinetest.R;
import com.threedev.machinetest.home.adpter.ProfileListAdapter;
import com.threedev.machinetest.home.model.HomeModel;
import com.threedev.machinetest.login.LoginActivity;
import com.threedev.machinetest.login.model.LoginModel;
import com.threedev.machinetest.utils.Constant;
import com.threedev.machinetest.utils.network.ApiClient;
import com.threedev.machinetest.utils.network.ApiInterface;
import com.threedev.machinetest.utils.network.NetworkUtils;

import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String toolbarTitle = "Home ";
    private TextView mTitle;
    private ImageView ivLocation, ivLogout;

    private RecyclerView rvProfileDetails;
    private ApiInterface apiInterface;

    private ProfileListAdapter adapter;
    private ArrayList<HomeModel.Data> dataArrayList= new ArrayList<>();


    //
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    private TextView txtEmpty;
    private EditText edtSearch;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inittoolbar();
        setupToolbar();
        setUpStatusBar();
        rvProfileDetails=findViewById(R.id.rv_profile_details);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        txtEmpty=findViewById(R.id.txt_empty);
        edtSearch=findViewById(R.id.edt_search);




        if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
            try {

                   getProfileList();


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this, Constant.NIC, Toast.LENGTH_SHORT).show();
        }

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }


    private void inittoolbar() {
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = toolbar.findViewById(R.id.toolbar_title);
        ivLocation = toolbar.findViewById(R.id.iv_location);
        ivLogout = toolbar.findViewById(R.id.iv_logout);
        mTitle.setText(toolbarTitle);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTrack(MainActivity.this);


                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

//                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) +
//                            "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);

                } else {

                    locationTrack.showSettingsAlert();
                }

            }
        });

    }
    private void setupToolbar() {
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void setUpStatusBar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    private void showLogoutDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You want to Logout ");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void getProfileList() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<HomeModel> call = apiInterface.getHomeData("2");

        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                pDialog.dismiss();
                HomeModel model = response.body();

                if(model != null){
                    dataArrayList=model.getData();

                    if(dataArrayList != null && !dataArrayList.isEmpty()){
                        adapter = new ProfileListAdapter(MainActivity.this,dataArrayList);
                        rvProfileDetails.setAdapter(adapter);
                        setSearch();
                    }


                }
                else {
                    Toast.makeText(MainActivity.this, ""+Constant.DNF, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void setSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
                    if (dataArrayList != null && !dataArrayList.isEmpty()) {



                            rvProfileDetails.setVisibility(View.VISIBLE);
                           // txtEmpty.setVisibility(View.GONE);
                            adapter.getFilter().filter(s);

                    }
                }


                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationTrack != null) {
            locationTrack.stopListener();
        }
    }

}