package app.msupply.com.ideaurben.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.RegisterUserClass;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Commonclass.Report_TypeBeanclass;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Zest Developer on 8/5/2017.
 */

public class ReportcateorieslistAdapter extends RecyclerView.Adapter<ReportcateorieslistAdapter.MyViewHolder>{

    ArrayList<Report_TypeBeanclass> arrayList = new ArrayList<>();
    Dialog dialog;
    Context context;
    ProgressDialog   mProgressDialog;
    Document document;

    CommonMethods commonMethods;
    String filetypes_store="";



    ConnectionDetector connectionDetector = new ConnectionDetector(context);
    String REGISTER_URL = "http://vehiclerescue.in/ideadarpan_beta/api/reportApp/get_report_data";

   // CardView lltypes_imagadapter;
    public ReportcateorieslistAdapter(Context context, ArrayList<Report_TypeBeanclass> arrayList)
    {

        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();

    }

    @Override
    public ReportcateorieslistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.childitem_reporttype, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tt_id.setText(arrayList.get(position).getId());
        holder.tt_tittle.setText(arrayList.get(position).getTittle());

        holder.tt_id.setTypeface(MainActivity.bold);
        holder.tt_tittle.setTypeface(MainActivity.regular);
        holder.tv_downloadbutton.setTypeface(MainActivity.regular);

        int lastPosition = -1;

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.buttom_one
                        : R.anim.buttom_two);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


       /* holder.lltypes_imagadapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Log.d("databaseids","valuesprintln"+holder.tt_id.getText().toString()+ holder.tt_tittle.getText().toString());
                Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, Report_Here.class);
                intent.putExtra("passid",holder.tt_id.getText().toString().trim());
                context.startActivity(intent);




            }
        });*/


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tt_id,tt_tittle,tv_downloadbutton;




        public MyViewHolder(View itemView) {
            super(itemView);

            tt_id = (TextView) itemView.findViewById(R.id.tv_idoftypes);
            tt_tittle = (TextView) itemView.findViewById(R.id.typesofreports);
            tv_downloadbutton= (TextView) itemView.findViewById(R.id.tv_downloadbutton);
           // lltypes_imagadapter  = (CardView) itemView.findViewById(R.id.lltypes_imagadapter);

            tv_downloadbutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

                Log.d("showdetails","clickbutton");

            switch (view.getId()) {

                case R.id.tv_downloadbutton: {
                    Log.d("showdetails","clickbutton2");
                    if (connectionDetector.isConnectedToInternet(context)) {
                        final int pos = getAdapterPosition();

                        dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                        dialog.setContentView(R.layout.dialog_chooseoptions);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();

                        RadioGroup radioGroup = dialog.findViewById(R.id.radio_guropid);
                        RadioButton rb_pdf = dialog.findViewById(R.id.rb_pdf);
                        RadioButton rb_excel = dialog.findViewById(R.id.rb_excel);


                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                                switch (i) {
                                    case R.id.rb_pdf: {
                                        dialog.dismiss();


                                        fetchPDF_Reports(arrayList.get(pos).getId(), arrayList.get(pos).getTittle());

                                        break;


                                    }
                                    case R.id.rb_excel: {

                                        dialog.dismiss();


                                        fetch_ExcelReport(arrayList.get(pos).getId(), arrayList.get(pos).getTittle());

                                        break;
                                    }


                                }


                            }
                        });


                        Log.d("databaseids", "valuesprintln" + arrayList.get(pos).getId() + arrayList.get(pos).getTittle());
                        //   Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();


                        /*below line is working fine*/
                        //




                 /*   SharedPreferences  sp = context.getSharedPreferences("splogin", 0);
                    SharedPreferences.Editor spt = sp.edit();

                    new DownloadExcels().execute(sp.getString("auth_key", null),arrayList.get(pos).getId());*/


                   /* Intent intent = new Intent(context, Report_Here.class);
                    intent.putExtra("passid",arrayList.get(pos).getId());
                    context.startActivity(intent);*/

                        break;

                    } else {
                        commonMethods = new CommonMethods(context);
                        commonMethods.showErrorMessage("", context.getResources().getString(R.string.error_checkconnection));
                    }
                }


            }
        }


      /*  class  DownloadExcels extends AsyncTask<String,String,String>
        {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();

            }

            @Override
            protected String doInBackground(String... strings) {

                RegisterUserClass objreg = new RegisterUserClass();
                HashMap<String,String> data = new HashMap<String,String>();

                String result = null;
                SharedPreferences  sp = context.getSharedPreferences("splogin", 0);
                SharedPreferences.Editor spt = sp.edit();

                data.put("auth_key",strings[1]);
                data.put("file_id",strings[0]);

                result = objreg.sendPostRequest(REGISTER_URL, data);

                return null;

            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }


        }*/


        public void fetch_ExcelReport(String file_ID,String file_name)
        {

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Fetching Files....");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            document = new Document();

            Log.d("filename","****   0"+file_name);

            filetypes_store = file_name;

            Retrofit adapter_retro = new Retrofit.Builder()
                    .baseUrl(Constandapi.ROOT_URL)
                    .build();

            IdeaInterface getreport_1st = adapter_retro.create(IdeaInterface.class);

            SharedPreferences  sp = context.getSharedPreferences("splogin", 0);
            SharedPreferences.Editor spt = sp.edit();

            Log.d("displayidsandvalues","***    "+sp.getString("auth_key", null)+" "+file_ID);

            Call<ResponseBody> responce_distributore_report = getreport_1st.get_ReportDate(sp.getString("auth_key", null),file_ID);


            responce_distributore_report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        mProgressDialog.dismiss();

                        CommonMethods  commonMethods = new CommonMethods(context);



                        try {

                            String result = response.body().string();

                            Log.d("files1st_reports", "***   " + result);


                            //JSONArray jsonArray = new JSONArray(result);

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA FILES/";
                            File dir = new File(path);

                            if (!dir.exists())
                                dir.mkdirs();

                            File file = new File(dir, filetypes_store+".csv");

                            if (result.trim().charAt(0) == '{')
                            {
                                JSONObject jsonObject = new JSONObject(result);
                                /*{"status":0,"msg":"file not available."}*/

                                commonMethods.showErrorMessage("", jsonObject.getString("msg"));


                            }else {

                                JSONArray docss = new JSONArray(result);
                                // File file=new File(  Environment.getExternalStorageDirectory().getAbsolutePath() + "/gowthamguru"+"/tmp2/fromJSONss.csv");
                                String csv = CDL.toString(docss);
                                FileUtils.writeStringToFile(file, csv);

                                //     commonMethods.showErrorMessage("","CSV File Downloaded Successfully");


                                String paths = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA FILES/";
                                File dirs = new File(paths);
                                File files = new File(dirs, filetypes_store + ".csv");

                                Log.d("filefecthopenview", "**  " + file);
                                Log.d("filefecthopenview", "**  " + files);

                                if (files.exists()) {
                                    viewExecel(files);
                                } else {
                                    commonMethods.showErrorMessage("", "File not fount");
                                }


                            }



                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                    mProgressDialog.dismiss();
                    commonMethods = new CommonMethods(context);
                    commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));


                }
            });

        }

        public void  fetchPDF_Reports(String file_ID,String file_name)
        {



            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Fetching Files....");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            document = new Document();

            Log.d("PDFfilename","****   0"+file_name);

            filetypes_store = file_name;

            Retrofit adapter_retro = new Retrofit.Builder()
                    .baseUrl(Constandapi.ROOT_URL)
                    .build();

            IdeaInterface getreport_1st = adapter_retro.create(IdeaInterface.class);

            SharedPreferences  sp = context.getSharedPreferences("splogin", 0);
            SharedPreferences.Editor spt = sp.edit();

            Log.d("PDFdisplayidsandvalues","***    "+sp.getString("auth_key", null)+" "+file_ID);

            Call<ResponseBody> responce_PDF_report = getreport_1st.get_ReportDate(sp.getString("auth_key", null),file_ID);


            responce_PDF_report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        mProgressDialog.dismiss();
                        CommonMethods commonMethods = new CommonMethods(context);


                        try {

                            String result = response.body().string();

                            Log.d("resultvalues","***   "+result);

                            if (result.trim().charAt(0) == '{')
                            {
                                JSONObject jsonObject = new JSONObject(result);
                                /*{"status":0,"msg":"file not available."}*/

                                commonMethods.showErrorMessage("", jsonObject.getString("msg"));


                            }else {

                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA FILES/";
                                File dir = new File(path);

                                if (!dir.exists())
                                    dir.mkdirs();

                                File file = new File(dir, filetypes_store+".pdf");
                                FileOutputStream fOut = new FileOutputStream(file);


                                JSONArray jsonArray = new JSONArray(result);


                                if (jsonArray.length()!=0)
                                {
                                    Log.d("sizeofarray","***  "+jsonArray.length());



                                    LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();

                                    map.clear();
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject json = jsonArray.getJSONObject(i);

                                        Iterator iter = json.keys();

                                        while(iter.hasNext()){
                                            String key = (String)iter.next();

                                        String value = json.getString(key);
                                        map.put(key,value);

                                          // tables.addCell(value);
                                            Log.d("keywithvalues","print    "+key+"   "+value);

                                        }
                                    }

                                    PdfPTable tables=null;
                                    if (map.size() >=0)
                                    {
                                        Log.d("sizeofhasmap","***"+map.size());

                                         tables = new PdfPTable(map.size());


                                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                                       // JSONObject json = jsonArray.getJSONObject(i);


                                        //Iterator iter = map.keys();

                                        for (String key :map.keySet()){

                                            Log.d("keyvalues","displauy"+key);
                                                tables.addCell(key);


                                        }
                                        for (String key :map.keySet()){

                                            String value=(String)map.get(key);
                                            tables.addCell(value);


                                        }
                                       //

                                    }


                                    tables.setHeaderRows(1);
                                    PdfPCell[] cells = tables.getRow(0).getCells();
                                    for (int j=0;j<cells.length;j++){
                                        cells[j].setBackgroundColor(BaseColor.GRAY);
                                    }
                                    PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                                    document.open();

                                    tables.setTotalWidth(PageSize.A4.getWidth());
                                    tables.setLockedWidth(true);
                                    PdfContentByte canvas = writer.getDirectContent();
                                    PdfTemplate template = canvas.createTemplate(
                                            tables.getTotalWidth(), tables.getTotalHeight());
                                    tables.writeSelectedRows(0, -1, 0, tables.getTotalHeight(), template);
                                    Image img = Image.getInstance(template);
                                    img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                                    img.setAbsolutePosition(
                                            0, (PageSize.A4.getHeight() - tables.getTotalHeight()) / 2);



                                    document.add(tables);
                                    document.close();
                                    viewPdf(file,"");
                                }

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                    commonMethods = new CommonMethods(context);
                    commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));



                }
            });








        }

    }

public  void viewExecel(File file)
{

    Log.d("filefecthopenview","** "+file);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");

    try {
        context.startActivity(intent);
    }catch (Exception e)
    {
        Toast.makeText(context,"Please Install Excel Application",Toast.LENGTH_LONG).show();
        Log.d("printexeption",""+e.toString());

    }

}

    private void viewPdf(File file, String directory) {

        //  File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA PDF"+ file);
        // String path = ;/IDEA PDF

        Log.d("pdffilelocation","****    "+file);

        Uri path = Uri.fromFile(file);


        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile),"application/vnd.ms-excel");*/



        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }


}
