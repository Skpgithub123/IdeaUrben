package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.GMailSender;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedBack extends AppCompatActivity {


    EditText subject, emailbody,emailfrom;

    TextView send,txtviewreplies;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spt;
    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        connectionDetector = new ConnectionDetector(FeedBack.this);
        commonMethods = new CommonMethods(FeedBack.this);
        subject = (EditText) findViewById(R.id.subject);
        emailbody = (EditText) findViewById(R.id.body);
        //emailfrom = (EditText)findViewById(R.id.from);
        send = (TextView) findViewById(R.id.send);
        txtviewreplies = (TextView)findViewById(R.id.txtviewreplies);

        sharedPreferences = getSharedPreferences("splogin", 0);
        spt = sharedPreferences.edit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (connectionDetector.isConnectedToInternet(FeedBack.this))
                {
                    addfeedback(sharedPreferences.getString("auth_key", null),subject.getText().toString().trim(),emailbody.getText().toString().trim());

                }else
                {
                    commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
                }


            }
        });


        txtviewreplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_viewreplies = new Intent(FeedBack.this,ViewRepliesActivity.class);
                startActivity(i_viewreplies);
            }
        });



    }


    private void addfeedback(String auth_key,String subjectstring,String feedbackmsg){
        progressDialog = new ProgressDialog(FeedBack.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface addfeedback = adapter.create(IdeaInterface.class);

        Call<ResponseBody> adding_feedback = addfeedback.add_feedback(auth_key,subjectstring,feedbackmsg);

        adding_feedback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    progressDialog.dismiss();

                    String result = null;

                    try{
                        result = response.body().string();
                        Log.d("createddates","responce"+result);
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("feedbackresponse", ""+jsonObject);

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");

                        if(status.equals("1")){
                            Toast.makeText(FeedBack.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(FeedBack.this, message,Toast.LENGTH_SHORT).show();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException jsone){
                        jsone.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FeedBack.this, "Please try after sometime", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
