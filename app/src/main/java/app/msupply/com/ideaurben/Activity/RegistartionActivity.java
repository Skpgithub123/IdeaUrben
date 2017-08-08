package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Commonclass.Categorylist_zone;
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

public class RegistartionActivity extends AppCompatActivity implements View.OnClickListener {

    Typeface regular,bold;

    TextView create_Profile,tv_registerpage;

    EditText et_input_empcode,et_input_name,et_input_newpassword,et_input_emailaddress,et_input_mbl,et_input_confirmpassword;
    TextInputLayout  input_layout_empcode,input_layout_name,input_layout_newpassword,input_layout_confirmpassword,input_layout_emailaddress,
            input_layout_mblnumber;

    Spinner spinner_txtanuualturnover;

    ArrayList<Categorylist_zone> arrayList = new ArrayList<>();

    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressBar reg_progressbar;

    ProgressDialog progressDialog;
    Categorylist_zone categorylist_zone;

    CustomAdapter customAdapter;

    String Selected_Items="Nothing Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion);

        connectionDetector = new ConnectionDetector(this);
        commonMethods      = new CommonMethods(this);

        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

        create_Profile = (TextView) findViewById(R.id.create_Profile);

        tv_registerpage  = (TextView) findViewById(R.id.tv_registerpage);
        tv_registerpage.setOnClickListener(this);

        spinner_txtanuualturnover = (Spinner) findViewById(R.id.txtanuualturnover);

        et_input_empcode = (EditText) findViewById(R.id.et_input_reg_empcode);
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        et_input_newpassword = (EditText) findViewById(R.id.et_input_newpassword);
        et_input_emailaddress = (EditText) findViewById(R.id.et_input_emailaddress);
        et_input_mbl = (EditText) findViewById(R.id.et_input_mbl);
        et_input_confirmpassword  = (EditText) findViewById(R.id.et_input_confirmpassword);

        input_layout_empcode = (TextInputLayout) findViewById(R.id.input_layout_reg_empcode);
        input_layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        input_layout_newpassword = (TextInputLayout) findViewById(R.id.input_layout_newpassword);
        input_layout_confirmpassword= (TextInputLayout) findViewById(R.id.input_layout_confirmpassword);
        input_layout_emailaddress = (TextInputLayout) findViewById(R.id.input_layout_emailaddress);
        input_layout_mblnumber = (TextInputLayout) findViewById(R.id.input_layout_mblnumber);

        et_input_empcode.addTextChangedListener(new MyTextWatcher(et_input_empcode));
        et_input_name.addTextChangedListener(new MyTextWatcher(et_input_name));
        et_input_newpassword.addTextChangedListener(new MyTextWatcher(et_input_newpassword));
        et_input_confirmpassword.addTextChangedListener(new MyTextWatcher(et_input_confirmpassword));
        et_input_emailaddress.addTextChangedListener(new MyTextWatcher(et_input_emailaddress));
        et_input_mbl.addTextChangedListener(new MyTextWatcher(et_input_mbl));


    //    reg_progressbar = (ProgressBar) findViewById(R.id.reg_progressbar);

        et_input_empcode.setTypeface(regular);
        et_input_name.setTypeface(regular);
        et_input_newpassword.setTypeface(regular);
        et_input_emailaddress.setTypeface(regular);
        et_input_mbl.setTypeface(regular);
        et_input_confirmpassword.setTypeface(regular);

        create_Profile.setTypeface(bold);
        tv_registerpage.setTypeface(bold);

        if (connectionDetector.isConnectedToInternet(this))
        {
            Get_Zoontype();

        }else
        {
            commonMethods.showErrorMessage("", getResources().getString(R.string.error_checkconnection));
        }




        spinner_txtanuualturnover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Log.d("dsfsdfsdf","sdfsdf");

                if (i !=0)
                {

                    categorylist_zone = arrayList.get(i);
                    Selected_Items = categorylist_zone.getId();

                    Log.d("printids","****   "+Selected_Items);
                }else
                {
                    Selected_Items = categorylist_zone.getId();
                    Log.d("printids","****notcoming   "+Selected_Items);

                    Selected_Items="Nothing Selected";
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {




            }
        });

        //arrayList.add("displayitems");
        //arrayList.add();

      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.,
                arrayList
        );*/
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);

    }

    public  void  Get_Zoontype()
    {
        //reg_progressbar.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(RegistartionActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arrayList.clear();
        categorylist_zone = new Categorylist_zone();
        categorylist_zone.setId("1");
        categorylist_zone.setZone_type("Select Your Zone");
        arrayList.add(categorylist_zone);

       /* RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constandapi.ROOT_URL) //Setting the Root URL
                .build();*/
      //  Retrofitnew Retrofit.Builder();


        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();
       // RetrofitService service = retrofit.create(RetrofitService.class);
       // Call<ResponseBody> result = service.listRepos(username);
        IdeaInterface report = adapter.create(IdeaInterface.class);

        Call<ResponseBody> call = report.display();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {


                    if (response.isSuccessful())
                    {
                       // reg_progressbar.setVisibility(View.GONE);
                        progressDialog.dismiss();

                        String result = response.body().string();

                        Log.d("reponcevalues","****   "+result);

                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            categorylist_zone = new Categorylist_zone();

                            categorylist_zone.setId(jsonObject.getString("zone_id"));
                            categorylist_zone.setZone_type(jsonObject.getString("zone_name"));

                            arrayList.add(categorylist_zone);


                        }

                        Log.d("Sizeofarray","*****   "+arrayList.size());

                        customAdapter = new CustomAdapter();

                        spinner_txtanuualturnover.setAdapter(customAdapter);


                    }else
                    {
                        progressDialog.dismiss();

                        commonMethods.showErrorMessage("","Please Try Again");
                    }



                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                commonMethods.showErrorMessage("","Please Try Again");
                Log.d("failuremethods","*****  "+call.toString());
            }
        });
/*
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("resportsvalues","*****    "+response.body());
                if (response.isSuccessful()){

                    Log.d("resportsvalues","*****    "+response.body());



                }else
                {
                    Log.d("errorvales","*****  "+response.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("errorvales","*****failures  "+call.toString());
            }
        });*/

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
           case  R.id.tv_registerpage:

               SubmitDetails();
               break;

        }
    }

    public  void  SubmitDetails()
    {

        if (!validate_Emp_Id())
        {
            return;
        }else if (!validate_Username())
        {

            return;
        }else if (!validate_NewPassword())
        {
            return;
        }
        else if (!validate_confirmpassword())
        {
            return;
        }else if (!(et_input_newpassword.getText().toString().trim().equals(et_input_confirmpassword.getText().toString().trim())))
        {
            commonMethods.showErrorMessage("Passwordmismatch", getResources().getString(R.string.error_sign_up_password_mismatch));
            return;
        }else if (!validate_email())
        {
            return;
        }else if (!validate_MblNumber())
        {
            return;
        }else if (Selected_Items.equals("Nothing Selected"))
        {
            commonMethods.showErrorMessage("Passwordmismatch", getResources().getString(R.string.error_pleaseSelectzoone));
            return;
        }


        else
        {
            if (connectionDetector.isConnectedToInternet(this))
            {
                SubmitDetailsto_Server(Selected_Items,et_input_empcode.getText().toString().trim(),et_input_name.getText().toString().trim(),
                                       et_input_newpassword.getText().toString().trim(),et_input_confirmpassword.getText().toString().trim(),
                                        et_input_emailaddress.getText().toString().trim(),et_input_mbl.getText().toString().trim());
            }else
            {
                commonMethods.showErrorMessage("Network", getResources().getString(R.string.error_checkconnection));
            }
        }
    }

    private  boolean validate_Username()
    {
        String username = et_input_name.getText().toString().trim();
        if (username.isEmpty())
        {
            input_layout_name.setError(getString(R.string.error_username));
            requestFocus(et_input_name);
            return false;

        }else
        {
            input_layout_name.setErrorEnabled(false);
        }
        return true;
    }

    private  boolean validate_email()
    {

        String user_Email = et_input_emailaddress.getText().toString().trim();
        if (user_Email.isEmpty() ||!isValidEmail(user_Email) )
        {
            input_layout_emailaddress.setError(getString(R.string.error_validemail));
            requestFocus(et_input_emailaddress);
            return false;

        }else
        {
            input_layout_emailaddress.setErrorEnabled(false);

        }

        return true;
    }

    private  boolean validate_MblNumber()
    {
        String Mbl_number = et_input_mbl.getText().toString().trim();
        if (Mbl_number.isEmpty() || !isValidMbl(Mbl_number)) {

            Log.d("notvalidmbdl","error");
            input_layout_mblnumber.setError(getString(R.string.err_msg_mblno));
            requestFocus(et_input_mbl);

            return false;
        }else {

            input_layout_mblnumber.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidMbl(String mbl) {
        return !TextUtils.isEmpty(mbl) && mbl.length()==10;
    }


    private boolean validate_NewPassword()
    {
        String newpassword = et_input_newpassword.getText().toString().trim();
        if (newpassword.isEmpty() || !isValidPass(newpassword) )
        {
            input_layout_newpassword.setError(getString(R.string.err_enterpassword));
            requestFocus(et_input_newpassword);
            return false;

        }else
        {
            input_layout_newpassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validate_confirmpassword()
    {
        String confirmpassword = et_input_confirmpassword.getText().toString().trim();
        if (confirmpassword.isEmpty() || !isValidPass(confirmpassword) )
        {
            input_layout_confirmpassword.setError(getString(R.string.err_enterconfirmpassword));
            requestFocus(et_input_confirmpassword);
            return false;

        }else
        {
            input_layout_confirmpassword.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidPass(String mbl) {
        return mbl.length() >4;
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean validate_Emp_Id() {
        String user_id = et_input_empcode.getText().toString().trim();

        if (user_id.isEmpty()) {

            Log.d("notvalidmbdl","error");
            input_layout_empcode.setError(getString(R.string.err_enteruserid));
            requestFocus(et_input_empcode);

            return false;
        }
        else {

            input_layout_empcode.setErrorEnabled(false);
        }
        return true;

    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.et_input_reg_empcode:

                    Log.d("values","aftertextchecdcalling");
                     validate_Emp_Id();
                    break;
                case R.id.et_input_name:

                    validate_Username();
                    break;

                case R.id.et_input_newpassword:

                    validate_NewPassword();
                    break;

                case  R.id.et_input_confirmpassword:

                   validate_confirmpassword();
                    break;

                case R.id.et_input_emailaddress:

                    validate_email();

                    break;

                case R.id.et_input_mbl:
                    validate_MblNumber();

                    break;
            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public class CustomAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i)
        {
            return arrayList.get(i).getId();

        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder_dialog holder;
            LayoutInflater inflter=RegistartionActivity.this.getLayoutInflater();

            if(view==null){
                view = inflter.inflate(R.layout.singlerow_addproduct_spinner, null);
                holder = new ViewHolder_dialog();
                holder.taxcategoryname = (TextView) view.findViewById(R.id.txtcatname_addproduct);
                holder.  txtcategoryid = (TextView) view.findViewById(R.id.txtcategoryid_addproduct);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder_dialog) view.getTag();
            }
            //  Log.d("id_are",spinnerarray.get(i).getTaxname()+"  "+spinnerarray.get(i).getId());

            Categorylist_zone cat = (Categorylist_zone) arrayList.get(i);



            holder. taxcategoryname.setText(cat.getZone_type().toString());
            holder.   txtcategoryid.setText(cat.getId());

            return view;
        }

    }
    public  class ViewHolder_dialog
    {
        TextView taxcategoryname ;
        TextView txtcategoryid ;

    }

    public  void SubmitDetailsto_Server(String Spinnerselected_id,String user_id,String user_name,String new_password,String confirmpassword,
                                         String user_email,String moblile_no)
    {


        progressDialog = new ProgressDialog(RegistartionActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();


        IdeaInterface report = adapter.create(IdeaInterface.class);


        Call<ResponseBody> responce_registeration = report.post_register(Spinnerselected_id,user_id,user_name,
                new_password,confirmpassword,user_email,moblile_no);

        responce_registeration.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();

                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getString("status").equals("0"))
                        {
                            commonMethods.showErrorMessage("",jsonObject.getString("msg"));
                        }else
                        {

                            commonMethods.displayToast("Successfully Registered");

                            Intent intent = new Intent(RegistartionActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();



                        }

                        Log.d("resultvalues","*****   "+result);




                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    progressDialog.dismiss();
                    commonMethods.showErrorMessage("","Please Try Once Again");
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
        /*call.enqueue(new Callback<ResponseBody>() {
            @*/


    }


}
