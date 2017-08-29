package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Adapter.BulletinAdapter;
import app.msupply.com.ideaurben.Adapter.MyAdapter;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Commonclass.Report_BulletinPojoClass;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Bullentin_Board extends AppCompatActivity {

    public static RecyclerView recyclerView;
    BulletinAdapter adapter_bulletin;
    ArrayList<String> arrayList = new ArrayList<>();

    Typeface regular,bold;

    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressDialog progressDialog;

    SharedPreferences sp;

    SharedPreferences.Editor spt;


    Report_BulletinPojoClass report_bulletinPojoClass;

    ArrayList<Report_BulletinPojoClass> arrayList_bulletin = new ArrayList<>();
    public  static  String fieldtype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullentin__board);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

        connectionDetector  = new ConnectionDetector(this);
        commonMethods        = new CommonMethods(this);

        sp = getSharedPreferences("splogin", 0);
        spt = sp.edit();


        if (connectionDetector.isConnectedToInternet(this))
        {
            Download_ButtetinFile();

        }else
        {
            commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
        }

    }


    public void Download_ButtetinFile(){

        progressDialog = new ProgressDialog(Bullentin_Board.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arrayList_bulletin.clear();

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getbulletindata = adapter_retro.create(IdeaInterface.class);


        Call<ResponseBody> responce_report = getbulletindata.get_BulletInData(sp.getString("auth_key", null));

        responce_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    progressDialog.dismiss();


                    String result = null;

                    try{
                        result = response.body().string();
                        Log.d("createddates","responce"+result);
                        JSONArray jsonArray = new JSONArray(result);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            report_bulletinPojoClass = new Report_BulletinPojoClass();

                            report_bulletinPojoClass.setTitle(jsonObject.getString("file_title"));
                            report_bulletinPojoClass.setDate(jsonObject.getString("posted_date"));
                            report_bulletinPojoClass.setType(jsonObject.getString("file_type"));
                            report_bulletinPojoClass.setUrl(jsonObject.getString("file_path"));



                            arrayList_bulletin.add(report_bulletinPojoClass);
                        }


                        adapter_bulletin = new BulletinAdapter(Bullentin_Board.this,arrayList_bulletin,sp.getString("auth", null));
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter_bulletin);
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Bullentin_Board.this, "Please try after sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
