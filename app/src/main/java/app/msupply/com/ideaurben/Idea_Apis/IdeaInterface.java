package app.msupply.com.ideaurben.Idea_Apis;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Zest Developer on 7/28/2017.
 */

public interface IdeaInterface {



    /*Get zoontype for registeration*/
    @GET("api/user/get_zone")
    Call<ResponseBody> display();

     /*Post User registeration api below*/

    @FormUrlEncoded
    @POST("api/user/user_register")
    Call<ResponseBody> post_register(@Field("zone_id") String zone_id,
                                     @Field("user_id") String user_id,
                                     @Field("name") String user_name,
                                     @Field("password") String user_password,
                                     @Field("conf_password") String user_confirm,
                                     @Field("email") String user_email,
                                     @Field("mobile") String user_mobilenumber);


        /*Post User Login api below for disputors*/

    @FormUrlEncoded
    @POST("api/auth/login_process")
    Call<ResponseBody> post_Login(@Field("scd_code") String zone_id,
                                     @Field("password") String user_id,
                                  @Field("role_type") String user_roll);

    /*Post User Login api below for only ASM TSM ZSM*/

    @FormUrlEncoded
    @POST("api/auth/login_process")
    Call<ResponseBody> post_LoginManagers(@Field("mobile") String zone_id,
                                  @Field("password") String user_id,
                                  @Field("role_type") String user_roll);



    /* Below api for get forgot password otp   */
    @FormUrlEncoded
    @POST("api/auth/forgot_pwd")
    Call<ResponseBody> Forgotpass_Otp(@Field("email") String Forgt_email);


    /*Below api for change validate otp and change the forgot password */


    @FormUrlEncoded
    @POST("api/auth/forgot_pwd_change")
    Call<ResponseBody> change_Forgotpassword(@Field("email") String Forgt_email,
                                             @Field("otp") String Forgt_email_otp,
                                             @Field("password") String new_password,
                                             @Field("conf_password") String confirm_password);


    /*        Getting Profile Details         */

    @FormUrlEncoded
    @POST("api/user/get_profile_data")
    Call<ResponseBody> GetUserDetails(@Field("auth_key") String auth_key);


    /*     Edit and update profile details  api/user/edit_profile*/



    @FormUrlEncoded
    @POST("api/user/get_profile_data")
    Call<ResponseBody> Edit_UpdateDetails(@Field("auth_key") String auth_key,
                                          @Field("name") String username,
                                          @Field("email") String useremail,
                                          @Field("mobile") String usermobile);

    /* Get the details of ALl asm and dsm data*/

    @FormUrlEncoded
    @POST("api/reportApp/get_report_data")
    Call<ResponseBody> get_ReportData(@Field("auth_key") String auth_key);


    /*/api/reportApp/get_tsm_data*/

    @FormUrlEncoded
    @POST("api/reportApp/get_report_data")
    Call<ResponseBody> get_ReportDate(@Field("auth_key") String auth_key,
                                      @Field("file_id") String file_id);

    @FormUrlEncoded
    @POST("api/reportApp/get_bulletin_data")
    Call<ResponseBody> get_BulletInData(@Field("auth_key") String auth_key);


    /* Get the details of distriputor  datas*/
    @FormUrlEncoded
    @POST("api/reportApp/get_tsm_data")
    Call<ResponseBody> get_Distributor_ReportData(@Field("auth_key") String auth_key);



    /*Below api for types fo Reports */
  /*api/reportApp/get_file_name*/

    @FormUrlEncoded
    @POST("api/reportApp/get_file_name")
    Call<ResponseBody> get_Repotstitle(@Field("auth_key") String auth_key);

    @FormUrlEncoded
    @POST("api/feedbackApp/add_feedback")
    Call<ResponseBody> add_feedback(@Field("auth_key") String auth_key,
                                        @Field("subject") String subject,
                                        @Field("feedback_msg") String feedbackmsg);




}
