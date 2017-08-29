package app.msupply.com.ideaurben.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sp = getSharedPreferences("splogin", 0);
        editor = sp.edit();





    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(isReadStorageAllowed())
        {
            //If permission is already having then showing the toast
            //  Toast.makeText(Splash.this,"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            netValidator();
            //  return;
        }

        else    //If the app has not the permission then asking for the permission
            requestStoragePermission();
    }

    public void netValidator()
    {

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                forNext();
            }
        }, 5000);

    }
    private boolean isReadStorageAllowed()
    {
        //Getting the permission status
        // int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int resultread = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultwrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // int resullocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // int resultsms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        //int resultloc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

       /* Log.d("statuscode==","result="+result+"resultread="+resultread+"resultwrite="+resultwrite+
                "resultsms="+resultsms+"resultloc="+resultloc);*/
        //If permission is granted returning true
        if (resultread == PackageManager.PERMISSION_GRANTED && resultwrite == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
       /* if (resultread == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
         if (resultwrite == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }*/
        else
        {
            //If permission is not granted returning false
            return false;
        }
    }
    private void requestStoragePermission()
    {

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
        {

            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //  Toast.makeText(Splash.this, "For Marsmallow,we do need this permission", Toast.LENGTH_SHORT).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request



        Log.d("requestcodefor", "resultsare " + requestCode);

        if(requestCode == 1)
        {

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                netValidator();
                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }
            else
            {
                // finish();
                //Displaying another toast if permission is not granted
                // Toast.makeText(this,"You have just denied the permission",Toast.LENGTH_LONG).show();

                final AlertDialog.Builder alertDialog;
                DialogInterface.OnClickListener diloagclicklistener=new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                requestStoragePermission();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicke
                                dialogInterface.dismiss();
                                finish();
                                break;
                        }
                    }
                };
                alertDialog=new AlertDialog.Builder(SplashScreen.this);
                alertDialog.setMessage("In order to use Vehilce Rescue App, you need to allow all Permission").setPositiveButton("Ok",diloagclicklistener).
                        setNegativeButton("",diloagclicklistener).show();
            }
        }
    }
    private void forNext()
    {
       /* TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String imei= tm.getDeviceId();
        Log.d("imei==", imei);
        editor.putString("IMEI",imei);
        editor.commit();*/


       Log.d("printmessagesp","***   "+sp.getString("status", "-1"));

            if (sp.getString("status", "-1").equals("Successfully")) {
                Log.d("splashstatus", "sucess1");

                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainIntent);
                SplashScreen.this.finish();

            } else {

                Log.d("splashstatus", "sucess0");
                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(mainIntent);
                SplashScreen.this.finish();
            }

        }
}
