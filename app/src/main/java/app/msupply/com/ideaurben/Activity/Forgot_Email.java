package app.msupply.com.ideaurben.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class Forgot_Email extends AppCompatActivity implements View.OnClickListener{

    TextView tv_get_otp;

    ConnectionDetector connectionDetector;

    ProgressDialog progressDialog;

    CommonMethods commonMethods;
    EditText et_input_forgot_emailaddress;
    TextInputLayout input_layout_forgot_emailaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__email);

        commonMethods = new CommonMethods(this);

        connectionDetector = new ConnectionDetector(this);


        tv_get_otp = (TextView) findViewById(R.id.tv_get_otp);

        et_input_forgot_emailaddress = (EditText) findViewById(R.id.et_input_forgot_emailaddress);
        et_input_forgot_emailaddress.addTextChangedListener(new MyTextWatcher(et_input_forgot_emailaddress));

        input_layout_forgot_emailaddress = (TextInputLayout) findViewById(R.id.input_layout_forgot_emailaddress);

        tv_get_otp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case  R.id.tv_get_otp:
            {
                Log.d("validateemail","methodresponce"+!validate_email());

                if (!validate_email())
                {
                    return;
                }else
                {
                    Log.d("validateemail","elseloopworkingfine");

                    if (connectionDetector.isConnectedToInternet(Forgot_Email.this))
                    {
                        sent_Forgotpassword_Otp(et_input_forgot_emailaddress.getText().toString().trim());

                    }else
                    {
                        commonMethods.showErrorMessage("",getResources().getString(R.string.error_checkconnection));
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

                case R.id.et_input_forgot_emailaddress:
                    validate_email();

                    break;

            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private  boolean validate_email()
    {

        String user_Email = et_input_forgot_emailaddress.getText().toString().trim();
        if (user_Email.isEmpty() ||!isValidEmail(user_Email) )
        {
            input_layout_forgot_emailaddress.setError(getString(R.string.error_validemail));
            requestFocus(et_input_forgot_emailaddress);
            return false;

        }else
        {
            input_layout_forgot_emailaddress.setErrorEnabled(false);

        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@");
    }


    public  void sent_Forgotpassword_Otp(String email)
    {

        progressDialog = new ProgressDialog(Forgot_Email.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();



        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface report = adapter.create(IdeaInterface.class);

        Call<ResponseBody> responce_otp = report.Forgotpass_Otp(email);


        responce_otp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();

                        Log.d("showotpmessage","****   "+result.toString());

                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getString("status").equals("1"))
                        {
                            commonMethods.displayToast(""+jsonObject.getString("msg"));
                            Intent intent = new Intent(Forgot_Email.this,ForgotPassword.class);
                            intent.putExtra("passemail",et_input_forgot_emailaddress.getText().toString().trim());
                            startActivity(intent);

                        }
                        else
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
                    commonMethods.showErrorMessage("",getString(R.string.error_checkconnection));
                }



            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                commonMethods.showErrorMessage("",getString(R.string.error_checkconnection));

            }
        });
    }




}
