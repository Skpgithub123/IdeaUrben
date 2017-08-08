package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {


    Typeface regular,bold;


    TextView tv_update_forgotpassword;

    EditText et_input_forgot_otp,et_input_forgotnewpassword,et_input_forgotconfimpassword;

    TextInputLayout input_layout_forgotnew_otp,
                input_layout_forgotnewpassword,input_layout_Forgot_confirmpassword;


    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;

    ProgressDialog progressDialog;

    String email_sent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        connectionDetector  = new ConnectionDetector(this);
        commonMethods       = new CommonMethods(this);



        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");



        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        email_sent = b.getString("passemail");


        tv_update_forgotpassword = (TextView) findViewById(R.id.tv_update_forgotpassword);

      //  tv_get_otp.setOnClickListener(this);
        tv_update_forgotpassword.setOnClickListener(this);

        et_input_forgot_otp = (EditText) findViewById(R.id.et_input_forgot_otp);
        et_input_forgotnewpassword = (EditText) findViewById(R.id.et_input_forgotnewpassword);
        et_input_forgotconfimpassword = (EditText) findViewById(R.id.et_input_forgotconfimpassword);


        et_input_forgot_otp.addTextChangedListener(new MyTextWatcher(et_input_forgot_otp));
        et_input_forgotnewpassword.addTextChangedListener(new MyTextWatcher(et_input_forgotnewpassword));
        et_input_forgotconfimpassword.addTextChangedListener(new MyTextWatcher(et_input_forgotconfimpassword));



        input_layout_forgotnew_otp = (TextInputLayout) findViewById(R.id.input_layout_forgotnew_otp);
        input_layout_forgotnewpassword = (TextInputLayout) findViewById(R.id.input_layout_forgotnewpassword);
        input_layout_Forgot_confirmpassword = (TextInputLayout) findViewById(R.id.input_layout_Forgot_confirmpassword);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.tv_update_forgotpassword:
            {
                if (!validate_Otp())
                {
                    return;
                }

                if (!validate_NewPassword())
                  {

                    return;

                }if (!validate_confirmpassword())
                {
                    return;

                }else if (!(et_input_forgotnewpassword.getText().toString().trim().equals(et_input_forgotconfimpassword.getText().toString().trim())))
                {
                    commonMethods.showErrorMessage("",getResources().getString(R.string.error_sign_up_password_mismatch));
                    return;
                }else
                {

                    if (connectionDetector.isConnectedToInternet(ForgotPassword.this))
                    {

                        Change_forgotpassword();
                    }else
                    {
                       commonMethods.showErrorMessage("",getResources().getString(R.string.error_sign_up_password_mismatch));
                    }

                }
            }

        }
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


                case R.id.et_input_forgot_otp:
                    validate_Otp();
                    break;

                case R.id.et_input_forgotnewpassword:
                    validate_NewPassword();

                    break;

                case R.id.et_input_forgotconfimpassword:

                    validate_confirmpassword();

                    break;


            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private boolean validate_NewPassword()
    {
        String newpassword = et_input_forgotnewpassword.getText().toString().trim();
        if (newpassword.isEmpty() || !isValidPass(newpassword) )
        {
            input_layout_forgotnewpassword.setError(getString(R.string.err_enterpassword));
            requestFocus(et_input_forgotnewpassword);
            return false;

        }else
        {
            input_layout_forgotnewpassword.setErrorEnabled(false);
        }
        return true;
    }

    private  boolean validate_Otp()
    {
        String getotp = et_input_forgot_otp.getText().toString().trim();
        if (getotp.isEmpty())
        {
            input_layout_forgotnew_otp.setError(getString(R.string.err_enterotp));
            requestFocus(et_input_forgot_otp);
            return false;

        }else
        {
            input_layout_forgotnew_otp.setErrorEnabled(false);
        }
        return true;
    }



    private static boolean isValidPass(String mbl) {
        return mbl.length() >4;
    }
    private boolean validate_confirmpassword()
    {
        String confirmpassword = et_input_forgotconfimpassword.getText().toString().trim();
        if (confirmpassword.isEmpty() || !isValidPass(confirmpassword) )
        {
            input_layout_Forgot_confirmpassword.setError(getString(R.string.err_enterconfirmpassword));
            requestFocus(et_input_forgotconfimpassword);
            return false;

        }else
        {
            input_layout_Forgot_confirmpassword.setErrorEnabled(false);
        }
        return true;
    }




    public void  Change_forgotpassword()
    {


        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();



        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface report_obj = adapter.create(IdeaInterface.class);

      Call<ResponseBody> responce_otp = report_obj.change_Forgotpassword(email_sent,et_input_forgot_otp.getText().toString().trim(),
              et_input_forgotnewpassword.getText().toString().trim(),et_input_forgotconfimpassword.getText().toString().trim());


        responce_otp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful())
                {
                    progressDialog.dismiss();
                    try {


                        String result = response.body().string();


                        Log.d("resultofchangepassword","****   "+result.toString());

                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getString("status").equals("1"))
                        {
                           // commonMethods.displayToast("working done");
                            Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                            commonMethods.displayToast("Password has been changed successfully.");
                        }else
                        {

                            commonMethods.showErrorMessage("",jsonObject.getString("msg"));
                        }



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

                progressDialog.dismiss();


            }
        });


    }
}
