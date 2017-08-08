package app.msupply.com.ideaurben.Activity;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.id.list;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backgroundOne,backgroundtwo;
    TextView tv_signup,tv_forgot;
    TextView tv_login;

    private TextInputLayout inputLayout_Employeecode, inputLayout_EmpPassword;
    private EditText et_input_empycode,et_input_password;

    ImageView imageView_biglog;

    Typeface regular,bold;

    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spt;


    Spinner rol_type;

    String Selectrole="0";

    ArrayList<String>  arrayList = new ArrayList<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backgroundOne = (ImageView) findViewById(R.id.movieimage);
        backgroundtwo = (ImageView) findViewById(R.id.movieimage2);

        imageView_biglog = (ImageView) findViewById(R.id.movieimage2);

        tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_forgot = (TextView) findViewById(R.id.tv_forgotpass);
        tv_login  = (TextView) findViewById(R.id.tv_login);

        rol_type = (Spinner) findViewById(R.id.rol_type);
  /*<array name="select_type">Choose Customer type,ZBM,ASM,TSM,DISTRIBUTOR,RETAILER</array>*/

        arrayList.add("Choose ROLL TYPE");
        arrayList.add("ZBM");
        arrayList.add("ASM");
        arrayList.add("TSM");
        arrayList.add("DISTRIBUTOR");
        arrayList.add("RETAILER");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_row, arrayList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_row);
        rol_type.setAdapter(dataAdapter);




        tv_signup.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        connectionDetector = new ConnectionDetector(this);
        commonMethods  = new CommonMethods(this);

        sharedPreferences = getSharedPreferences("splogin", 0);
        spt = sharedPreferences.edit();

        //et_input_empycode = (EditText) findViewById(R.id.et_input_empycode);

        // imageView_biglog.setAlpha((float) 0.7);
        tv_login.setAlpha((float) 0.7);
        tv_signup.setAlpha((float) 0.7);
        tv_forgot.setAlpha((float) 0.7);
       // rol_type.setAlpha((float) 0.8);

        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");


        inputLayout_Employeecode = (TextInputLayout) findViewById(R.id.input_layout_empcode);
        inputLayout_EmpPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        et_input_empycode = (EditText) findViewById(R.id.et_input_empycode);
        et_input_password = (EditText) findViewById(R.id.et_input_password);

        et_input_empycode.setAlpha((float) 0.7);
        et_input_password.setAlpha((float) 0.7);

        et_input_empycode.addTextChangedListener(new MyTextWatcher(et_input_empycode));
        et_input_password.addTextChangedListener(new MyTextWatcher(et_input_password));

        et_input_empycode.setTypeface(bold);
        et_input_password.setTypeface(bold);

        tv_signup.setTypeface(bold);
        tv_forgot.setTypeface(bold);
        tv_login.setTypeface(bold);

        rol_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0)
                {
                    Selectrole = ""+i;
                }else
                {
                    Selectrole = ""+i;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Selectrole = ""+0;
            }
        });


      /*  final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(30000L);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundtwo.setTranslationX(translationX - width);
            }
        });
        animator.start();*/
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_signup:
                Intent intent = new Intent(LoginActivity.this,RegistartionActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tv_login:

            {

                if (!validate_Emp_Id())
                {
                    return;
                }
                if (!validate_NewPassword())
                {
                    return;
                }
                if (Selectrole.equals("0"))
                {
                    commonMethods.showErrorMessage("","Please Select Roll Type");
                    return;
                }

                else
                {
                    login_process();
                }
                break;

            }
            case R.id.tv_forgotpass:
            {
                Intent intent1 = new Intent(LoginActivity.this,Forgot_Email.class);
                startActivity(intent1);

                break;
            }


        }
    }

    public  void login_process()
    {


        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface report = adapter.create(IdeaInterface.class);

        Log.d("rolltypevalues","print"+Selectrole);
        Call<ResponseBody> responce_login = report.post_Login(et_input_empycode.getText().toString().trim(),
                et_input_password.getText().toString().trim(),Selectrole);

        responce_login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();

                        Log.d("resultdisplayLogin","***   "+result);

                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getString("status").equals("0"))
                        {
                            commonMethods.showErrorMessage("",jsonObject.getString("msg"));
                        }else
                        {

                            spt.putString("status", "Successfully");
                            spt.putString("auth", jsonObject.getString("auth_key"));
                            spt.putString("role_type", jsonObject.getString("role_type"));
                            spt.commit();

                             Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                             startActivity(intent);
                             finish();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    progressDialog.dismiss();
                    commonMethods.showErrorMessage("","Please Try Once Again");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

                Log.d("failuremethods","content   "+call.toString());
            }
        });

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

                case R.id.et_input_empycode:

                    Log.d("values","aftertextchecdcalling");
                    validate_Emp_Id();


                    break;
                case R.id.et_input_password:


                    validate_NewPassword();



                    break;

            }
        }
    }
    private boolean validate_Emp_Id() {
        String user_id = et_input_empycode.getText().toString().trim();

        if (user_id.isEmpty()) {

            Log.d("notvalidmbdl","error");
            inputLayout_Employeecode.setError(getString(R.string.err_enteruserid));
            requestFocus(et_input_empycode);

            return false;
        }
        else {

            inputLayout_Employeecode.setErrorEnabled(false);
        }
        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validate_NewPassword()
    {
        String userpassword = et_input_password.getText().toString().trim();
        if (userpassword.isEmpty()/* || !isValidPass(userpassword) */)
        {
            inputLayout_EmpPassword.setError(getString(R.string.err_loginpassword));
            requestFocus(et_input_password);
            return false;

        }else
        {
            inputLayout_EmpPassword.setErrorEnabled(false);
        }
        return true;
    }


    private static boolean isValidPass(String password) {
        return password.length() >4;
    }


}
