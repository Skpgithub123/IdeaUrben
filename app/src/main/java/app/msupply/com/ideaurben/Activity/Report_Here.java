package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Adapter.MyAdapter;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Report_Here extends AppCompatActivity {

    public static RecyclerView recyclerView;
    MyAdapter adapter;

    ArrayList<String> arrayList = new ArrayList<>();

    Typeface regular,bold;

    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressDialog progressDialog;

    SharedPreferences sp;

    SharedPreferences.Editor spt;

    ArrayList<Report_BeanClass>  arrayList_obj = new ArrayList<>();

    Report_BeanClass report_beanClass;

    ArrayList<String> arrayList_removedate = new ArrayList<>();
    public  static  String fieldtype="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__here);

        arrayList_removedate.add("demo");
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

        connectionDetector  = new ConnectionDetector(this);
        commonMethods        = new CommonMethods(this);


        Intent intent = getIntent();

        Bundle b = intent.getExtras();

         fieldtype = b.getString("passid");

        Log.d("fieldtypes","****   "+fieldtype);


        sp = getSharedPreferences("splogin", 0);
        spt = sp.edit();

        if (connectionDetector.isConnectedToInternet(this))
        {
            FetchDownloadingfiles();

        }else
        {
            commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
        }

       /* arrayList.add("Report One");
        arrayList.add("Report Two");
        arrayList.add("Report Three");
        arrayList.add("Report Five");
        arrayList.add("Report Foure");
        arrayList.add("Report Six");*/



        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                    Log.d("itemtouched","****   ");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_Here.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?");    //set message

                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position);    //item removed from recylcerview
                            //  sqldatabase.execSQL("delete from " + TABLE_NAME + " where _id='" + (position + 1) + "'"); //query for delete
                            arrayList.remove(position);  //then remove item

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview

    }

    public void FetchDownloadingfiles()
    {



        progressDialog = new ProgressDialog(Report_Here.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arrayList_removedate.clear();


        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue","*******    "+sp.getString("auth", null));

        Call<ResponseBody> responce_report = getreport.get_ReportDate(sp.getString("auth", null),"4",fieldtype);



        responce_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    progressDialog.dismiss();


                    String result = null;
                    try {

                        result = response.body().string();

                        Log.d("createddates","responce"+result);

                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            report_beanClass = new Report_BeanClass();

                            Log.d("printarrayls","displays"+jsonObject.getString("created_date"));

                            if (arrayList_removedate.contains(jsonObject.getString("created_date")))
                            {
                                Log.d("printarrayls","displays");
                            }else
                            {
                                arrayList_removedate.add(jsonObject.getString("created_date"));
                                Log.d("printarraylsexecte","displays");
                                report_beanClass.setTitle("Uploaded Date");
                                report_beanClass.setDate(jsonObject.getString("created_date"));
                                report_beanClass.setUrl("");
                                arrayList_obj.add(report_beanClass);
                            }

                        }

                        Log.d("arraylistobjects","*****    "+arrayList_obj.size());

                        adapter = new MyAdapter(Report_Here.this,arrayList_obj,sp.getString("auth", null));
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    progressDialog.dismiss();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {





            }
        });





    }





}
