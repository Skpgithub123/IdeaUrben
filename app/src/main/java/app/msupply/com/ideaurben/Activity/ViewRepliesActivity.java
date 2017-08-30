package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Adapter.BulletinAdapter;
import app.msupply.com.ideaurben.Adapter.MailReplyAdapter;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BulletinPojoClass;
import app.msupply.com.ideaurben.Commonclass.View_RepliesPojo;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by uOhmac Technologies on 29-Aug-17.
 */

public class ViewRepliesActivity extends AppCompatActivity {


    public static RecyclerView recyclerView;
    MailReplyAdapter mailReplyAdapter;

    Typeface regular,bold;
    String result=null;
    TextView noreplies_feedback;
    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressDialog progressDialog;

    SharedPreferences sp;

    SharedPreferences.Editor spt;

    RelativeLayout relativeLayout;

    View_RepliesPojo view_repliesPojo;

    ArrayList<View_RepliesPojo> arraylist_view_repliesPojos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repliesrecyclerview);
        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_viewreplies);

        relativeLayout = (RelativeLayout)findViewById(R.id.rr_noreplies);
        noreplies_feedback = (TextView)findViewById(R.id.noreplies_feedback);
        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

        connectionDetector  = new ConnectionDetector(this);
        commonMethods        = new CommonMethods(this);

        sp = getSharedPreferences("splogin", 0);
        spt = sp.edit();


        if (connectionDetector.isConnectedToInternet(this))
        {
            ViewAllReplies();

        }else
        {
            commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
        }

    }


    private void ViewAllReplies(){
        progressDialog = new ProgressDialog(ViewRepliesActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arraylist_view_repliesPojos.clear();

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getfeedbackdata = adapter_retro.create(IdeaInterface.class);


        Call<ResponseBody> get_feedback = getfeedbackdata.get_feedback(sp.getString("auth_key", null));

        get_feedback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {

                    progressDialog.dismiss();

                    try{

                        result = response.body().string();

                    if(result.toString().trim().charAt(0)=='[') {


                        Log.d("createdreplies", "mailreplies" + result);
                        JSONArray jsonArray = new JSONArray(result);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            view_repliesPojo = new View_RepliesPojo();

                            view_repliesPojo.setSubject(jsonObject.getString("subject"));
                            view_repliesPojo.setFeedbackmsg(jsonObject.getString("feedback_msg"));
                            view_repliesPojo.setReplymsg(jsonObject.getString("reply_msg"));
                            view_repliesPojo.setReplyby(jsonObject.getString("reply_by"));
                            view_repliesPojo.setFeedbackdate(jsonObject.getString("feedback_added_date"));
                            view_repliesPojo.setReplybackdate((jsonObject.getString("reply_added_date")));


                            arraylist_view_repliesPojos.add(view_repliesPojo);
                        }
                        mailReplyAdapter = new MailReplyAdapter(ViewRepliesActivity.this, arraylist_view_repliesPojos, sp.getString("auth", null));
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mailReplyAdapter);

                    }else{
                        try{
                            JSONObject jsonObject_noreplies = new JSONObject(result.toString());

                            String status = jsonObject_noreplies.getString("status");

                            if(status.equals("0")){
                                Toast.makeText(ViewRepliesActivity.this, jsonObject_noreplies.getString("msg"), Toast.LENGTH_SHORT).show();

                                recyclerView.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                noreplies_feedback.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }

                    }catch (IOException io){
                            io.printStackTrace();
                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }else {
                    progressDialog.dismiss();
                }
                }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ViewRepliesActivity.this, "Please try after sometime", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
