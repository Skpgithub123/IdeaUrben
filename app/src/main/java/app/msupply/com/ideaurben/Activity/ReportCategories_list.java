package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import app.msupply.com.ideaurben.Adapter.MyAdapter;
import app.msupply.com.ideaurben.Adapter.ReportcateorieslistAdapter;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_TypeBeanclass;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.Idea_Apis.ItemClickListener;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportCategories_list extends AppCompatActivity implements ItemClickListener {


    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;


    RecyclerView my_recycler_view_types;
    Typeface regular,bold;

    ProgressDialog progressDialog;

    SharedPreferences sp;

    SharedPreferences.Editor spt;


    TextView tv_title_reportcatgories;

    Report_TypeBeanclass obj_types;

    ArrayList<Report_TypeBeanclass> types_array = new ArrayList<>();

    ReportcateorieslistAdapter type_Adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_categories_list);



        my_recycler_view_types = (RecyclerView) findViewById(R.id.my_recycler_view_types);


        connectionDetector  = new ConnectionDetector(this);
        commonMethods        = new CommonMethods(this);

        sp = getSharedPreferences("splogin", 0);
        spt = sp.edit();



        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

        tv_title_reportcatgories = (TextView) findViewById(R.id.tv_title_reportcatgories);


        tv_title_reportcatgories.setTypeface(bold);



       /* try {

            JSONObject jsonObject = new JSONObject("your result");

            JSONObject jsonObject1_message = jsonObject.getJSONObject("message_type");

            Map<String,String> map = new HashMap<String,String>();
            Iterator iter = jsonObject1_message.keys();

            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = jsonObject1_message.getString(key);
                map.put(key,value);
            }



        }catch (Exception e)
        {
                    Log.d("error","**   "+e.toString());
        }*/


        if (connectionDetector.isConnectedToInternet(this))
        {
            Fetch_Filetypes();

        }else
        {
            commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
        }




    }



    public  void Fetch_Filetypes()
    {

        progressDialog = new ProgressDialog(ReportCategories_list.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        types_array.clear();

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Call<ResponseBody> report_types = getreport.get_Repotstitle(sp.getString("auth_key", null));


        report_types.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();

                        Log.d("resultvalues","*****   "+result);


                        JSONArray jsonArray = new JSONArray(result);

                        for (int i =0;i<jsonArray.length();i++)
                        {
                            obj_types = new Report_TypeBeanclass();

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            obj_types.setId(jsonObject.getString("file_id"));
                            obj_types.setTittle(jsonObject.getString("report_name"));

                            types_array.add(obj_types);

                        }

                        type_Adapter = new ReportcateorieslistAdapter(ReportCategories_list.this,types_array);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        my_recycler_view_types.setLayoutManager(mLayoutManager);
                        my_recycler_view_types.setItemAnimator(new DefaultItemAnimator());
                        my_recycler_view_types.setAdapter(type_Adapter);

                       /* my_recycler_view_types.getViewTreeObserver().addOnPreDrawListener(
                                new ViewTreeObserver.OnPreDrawListener() {

                                    @Override
                                    public boolean onPreDraw() {
                                        my_recycler_view_types.getViewTreeObserver().removeOnPreDrawListener(this);

                                        for (int i = 0; i < my_recycler_view_types.getChildCount(); i++) {
                                            View v = my_recycler_view_types.getChildAt(i);
                                            v.setAlpha(0.0f);
                                            v.animate().alpha(1.0f)
                                                    .setDuration(2000)
                                                    .setStartDelay(i * 50)
                                                    .start();
                                        }

                                        return true;
                                    }
                                });*/


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
                Log.d("responcefailure","falilure"+call.toString());


            }
        });


    }


    @Override
    public void onClick(View view, int position) {

    }
}
