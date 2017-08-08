package app.msupply.com.ideaurben.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.msupply.com.ideaurben.R;

public class FeedBack extends AppCompatActivity {


    EditText subject, emailbody;

    TextView send;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);


        subject = (EditText) findViewById(R.id.subject);
        emailbody = (EditText) findViewById(R.id.body);
        send = (TextView) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s  ="123456@gmail.com";


                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("message/rfc822");
              //      intent.putExtra(Intent.EXTRA_EMAIL, "abc@gmail.com");
                intent.setData(Uri.parse("mailto:"+s));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, emailbody.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                try {

                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("Email error:",e.toString());
                }

                /*Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText());
                intent.putExtra(Intent.EXTRA_TEXT, emailbody.getText());
                intent.setData(Uri.parse("mailto:"+s)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                FeedBack.this.startActivity(intent);*/

            /*    *//**//*
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "gowtham.app@gmail.com");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject.getText());
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,emailbody.getText());
                FeedBack.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));*/



            }
        });



    }
}
