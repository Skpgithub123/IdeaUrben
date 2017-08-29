package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.msupply.com.ideaurben.Commonclass.GMailSender;
import app.msupply.com.ideaurben.R;

public class FeedBack extends AppCompatActivity {


    EditText subject, emailbody,emailfrom;

    TextView send;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spt;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);


        subject = (EditText) findViewById(R.id.subject);
        emailbody = (EditText) findViewById(R.id.body);
        emailfrom = (EditText)findViewById(R.id.from);
        send = (TextView) findViewById(R.id.send);


        sharedPreferences = getSharedPreferences("splogin", 0);
        spt = sharedPreferences.edit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });



    }


    private void addfeedback(String auth_key,String subject,String feedbackmsg){
        progressDialog = new ProgressDialog(FeedBack.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
