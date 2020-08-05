package com.derron.readmp3filesfromstorage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {

    private final Handler mWaitHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkUserPermissions();

    }

    private void splashScreenFor2Seconds() {
        //The following code will execute after the 2 seconds.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //The following code will execute after the 2 seconds.
                try {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mWaitHandler.postDelayed(runnable, 2000);  // Give a 2 seconds delay.
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkUserPermissions() {
        //checking if Phone Call permission is granted by Android or not
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            splashScreenFor2Seconds();
        }
        //if the Phone Call permission is not granted, the App will request for Phone Call permission
        else {
            requestRuntimePermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestRuntimePermission() {

        /* Android provides a utility method, "shouldShowRequestPermissionRationale()", that returns true
         * if the Android has previously denied the request, and returns false if a user has denied a permission
         * and selected the Don't ask again option in the permission request dialog. */

        //To know More about Runtime Permission request, Go to https://developer.android.com/training/permissions/requesting

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            //Showing AlertDialog for explaining "Why we need this Permission in the App?"
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("These permissions are needed for the app to read device storage memory.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Requesting User to give Direct Phone Call Permission and Internet Access Permission(Optional)
                            ActivityCompat.requestPermissions(SplashActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                            // MULTIPLE_PERMISSION_CODE is an
                            // app-defined int constant. The callback method "onRequestPermissionsResult" gets the
                            // result of the request.
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();


        } else {
            //Requesting User to give Direct Phone Call Permission and Internet Access Permission(Optional)
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    //This Method gets Call Automatically afer "requestRuntimePermission" method for checking purpose
    //This Method Checks if the User has Granted permission or Not
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1)  {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                //Permission Granted
                splashScreenFor2Seconds();

            } else {
                endTask();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void endTask() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

        finishAndRemoveTask();
        finish();
    }

}