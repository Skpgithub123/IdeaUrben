package app.msupply.com.ideaurben.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Setting_Activity extends AppCompatActivity implements View.OnClickListener {


    Typeface regular,bold;


    TextView tv_tittleprofile,tv_saveprofile;

    ConnectionDetector connectiondetector;

    CommonMethods commonMethods;


    LinearLayout llusername,llusername_Id,lluser_mbl,lluser_email,lluser_password;

    TextView tvdialog_setusername,tvdialog_setusername_ID,tvdialog_setmobilenumber,tvdialog_email;

    TextView light_name,lightuserid,light_mblno,light_emailid;

    ProgressDialog progressDialog;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spt;

    Dialog username_dialog;

    EditText Editdialog_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);


        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");


        connectiondetector = new ConnectionDetector(this);

        commonMethods   = new CommonMethods(this);

        sharedPreferences = getSharedPreferences("splogin", 0);
        spt = sharedPreferences.edit();


        tv_tittleprofile = (TextView) findViewById(R.id.tv_tittleprofile);
        tv_saveprofile = (TextView) findViewById(R.id.tv_saveprofile);

        llusername = (LinearLayout) findViewById(R.id.llusername);
        llusername_Id = (LinearLayout) findViewById(R.id.llusername_Id);
        lluser_mbl = (LinearLayout) findViewById(R.id.lluser_mbl);
        lluser_email = (LinearLayout) findViewById(R.id.lluser_email);
        lluser_password = (LinearLayout) findViewById(R.id.lluser_password);

        llusername.setOnClickListener(this);
        llusername_Id.setOnClickListener(this);
        lluser_mbl.setOnClickListener(this);
        lluser_email.setOnClickListener(this);
        lluser_password.setOnClickListener(this);



        tvdialog_setusername = (TextView) findViewById(R.id.tvdialog_setusername);
        tvdialog_setusername_ID = (TextView) findViewById(R.id.tvdialog_setusername_ID);
        tvdialog_setmobilenumber = (TextView) findViewById(R.id.tvdialog_setmobilenumber);
        tvdialog_email  = (TextView) findViewById(R.id.tvdialog_email);

        tvdialog_setusername.setText("");
        tvdialog_setusername_ID.setText("");
        tvdialog_setmobilenumber.setText("");
        tvdialog_email.setText("");


        if (connectiondetector.isConnectedToInternet(Setting_Activity.this))
        {

            Set_Usernamepassword();

        }else
        {
            commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
        }


        light_name  = (TextView) findViewById(R.id.light_name);
        lightuserid  = (TextView) findViewById(R.id.lightuserid);
        light_mblno  = (TextView) findViewById(R.id.light_mblno);
        light_emailid  = (TextView) findViewById(R.id.light_emailid);


        tv_tittleprofile.setTypeface(bold);
        tv_saveprofile.setTypeface(bold);

        tv_saveprofile.setOnClickListener(this);

        tvdialog_setusername.setTypeface(bold);
        tvdialog_setusername_ID.setTypeface(bold);
        tvdialog_setmobilenumber.setTypeface(bold);
        tvdialog_email.setTypeface(bold);

        light_name.setTypeface(regular);
        lightuserid.setTypeface(regular);
        light_mblno.setTypeface(regular);
        light_emailid.setTypeface(regular);



    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.llusername:
            {


                username_dialog = new Dialog(Setting_Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                username_dialog.setContentView(R.layout.dialogusername);
                username_dialog.show();

                TextView cancel = (TextView) username_dialog.findViewById(R.id.cancelusername);
                TextView update = (TextView) username_dialog.findViewById(R.id.updateusername);
                Editdialog_username = (EditText) username_dialog.findViewById(R.id.dausername);

                Editdialog_username.setText(null);
                Editdialog_username.setHint("Enter User Name");


                cancel.setTypeface(bold);
                update.setTypeface(bold);
                Editdialog_username.setTypeface(regular);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username_dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //    Edit_Dialog_username = Editdialog_username.getText().toString().trim();
                        if (Editdialog_username.getText().toString().trim().equals("")) {

                            Editdialog_username.setError("Please Enter Name");

                        } else {
                            username_dialog.dismiss();
                            tvdialog_setusername.setText(Editdialog_username.getText().toString().trim());

                        }
                    }
                });




                break;
            }
         /*   case R.id.llusername_Id:
            {


                username_dialog = new Dialog(Setting_Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                username_dialog.setContentView(R.layout.dialogusername);
                username_dialog.show();

                TextView cancel = (TextView) username_dialog.findViewById(R.id.cancelusername);
                TextView update = (TextView) username_dialog.findViewById(R.id.updateusername);
                Editdialog_username = (EditText) username_dialog.findViewById(R.id.dausername);

                cancel.setTypeface(bold);
                update.setTypeface(bold);
                Editdialog_username.setTypeface(regular);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username_dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //    Edit_Dialog_username = Editdialog_username.getText().toString().trim();
                        if (Editdialog_username.getText().toString().trim().equals("")) {

                            Editdialog_username.setError("Please Enter Name");

                        } else {

                            tvdialog_setusername_ID.setText(Editdialog_username.getText().toString().trim());

                        }
                    }
                });


                break;
            }*/
            case R.id.lluser_mbl:
            {

                username_dialog = new Dialog(Setting_Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                username_dialog.setContentView(R.layout.dialogusername);
                username_dialog.show();

                TextView cancel = (TextView) username_dialog.findViewById(R.id.cancelusername);
                TextView update = (TextView) username_dialog.findViewById(R.id.updateusername);
                Editdialog_username = (EditText) username_dialog.findViewById(R.id.dausername);

                Editdialog_username.setInputType(InputType.TYPE_CLASS_NUMBER);

                Editdialog_username.setText(null);
                Editdialog_username.setHint("Enter Mobile Number");

                cancel.setTypeface(bold);
                update.setTypeface(bold);
                Editdialog_username.setTypeface(regular);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username_dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //    Edit_Dialog_username = Editdialog_username.getText().toString().trim();
                        if (Editdialog_username.getText().toString().trim().equals("")) {

                            Editdialog_username.setError("Please Enter Name");

                        } else {
                            username_dialog.dismiss();
                            tvdialog_setmobilenumber.setText(Editdialog_username.getText().toString().trim());

                        }
                    }
                });



                break;
            }
            case R.id.lluser_email:
            {


                username_dialog = new Dialog(Setting_Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                username_dialog.setContentView(R.layout.dialogusername);
                username_dialog.show();

                TextView cancel = (TextView) username_dialog.findViewById(R.id.cancelusername);
                TextView update = (TextView) username_dialog.findViewById(R.id.updateusername);
                Editdialog_username = (EditText) username_dialog.findViewById(R.id.dausername);

                Editdialog_username.setText(null);
                Editdialog_username.setHint("Enter Email ID");

                cancel.setTypeface(bold);
                update.setTypeface(bold);
                Editdialog_username.setTypeface(regular);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username_dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //    Edit_Dialog_username = Editdialog_username.getText().toString().trim();
                        if (Editdialog_username.getText().toString().trim().equals("")) {

                            Editdialog_username.setError("Please Enter Name");

                        } else {
                            username_dialog.dismiss();
                            tvdialog_email.setText(Editdialog_username.getText().toString().trim());

                        }
                    }
                });



                break;
            }

            case R.id.tv_saveprofile:
            {

                if (connectiondetector.isConnectedToInternet(Setting_Activity.this))
                {
                    save_ProfileDetails();
                }else
                {
                  commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
                }


                break;
            }

        }

    }

   public void Set_Usernamepassword()
   {

       progressDialog = new ProgressDialog(Setting_Activity.this);
       progressDialog.setMessage("Please Wait..");
       progressDialog.setCancelable(false);
       progressDialog.show();



       Retrofit adapter = new Retrofit.Builder()
               .baseUrl(Constandapi.ROOT_URL)
               .build();


       IdeaInterface report = adapter.create(IdeaInterface.class);


       Call<ResponseBody> responce_registeration = report.GetUserDetails(sharedPreferences.getString("auth", null));

       responce_registeration.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

               if (response.isSuccessful())
               {
                   progressDialog.dismiss();

                   try {


                       String result = response.body().string();

                       Log.d("setpasswordvalues","****   "+result);

                       JSONObject obj = new JSONObject(result);


                       tvdialog_setusername.setText(obj.getString("name"));
                       tvdialog_setusername_ID.setText(obj.getString("user_id"));
                       tvdialog_setmobilenumber.setText(obj.getString("mobile"));
                       tvdialog_email.setText(obj.getString("email"));


                   } catch (IOException e) {
                       e.printStackTrace();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }else
               {
                   progressDialog.dismiss();
                   commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));

               }



           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               progressDialog.dismiss();
               commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
           }
       });
   }

   public  void  save_ProfileDetails()
   {



       progressDialog = new ProgressDialog(Setting_Activity.this);
       progressDialog.setMessage("Please Wait..");
       progressDialog.setCancelable(false);
       progressDialog.show();



       Retrofit adapter = new Retrofit.Builder()
               .baseUrl(Constandapi.ROOT_URL)
               .build();


       IdeaInterface report = adapter.create(IdeaInterface.class);


       Call<ResponseBody> updateuserdata = report.Edit_UpdateDetails(sharedPreferences.getString("auth", null),
               tvdialog_setusername.getText().toString().trim(),tvdialog_email.getText().toString().trim(),
               tvdialog_setmobilenumber.getText().toString().trim() );

       updateuserdata.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

               if (response.isSuccessful())
               {
                   progressDialog.dismiss();

                   commonMethods.displayToast("Updated Successfully");

                   try {

                       String result = response.body().string();
                       Log.d("resultvalus","*******   "+result);





                   } catch (IOException e) {
                       e.printStackTrace();
                   }


               }


           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {




           }
       });


       /*tvdialog_setusername.setText(obj.getString("name"));
       tvdialog_setusername_ID.setText(obj.getString("user_id"));
       tvdialog_setmobilenumber.setText(obj.getString("mobile"));
       tvdialog_email.setText(obj.getString("email"));*/



   }
}
