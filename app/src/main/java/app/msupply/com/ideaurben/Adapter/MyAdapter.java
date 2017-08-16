package app.msupply.com.ideaurben.Adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Idea_Apis.IdeaInterface;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;
import app.msupply.com.ideaurben.Utils.Constandapi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.width;


/**
 * Created by Zest Developer on 6/27/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    ArrayList<Report_BeanClass> arrayList = new ArrayList<>();

    private ArrayList<Report_BeanClass> mFilteredList  = new ArrayList<>();


    String getDate_Filter;
    RecyclerView recyclerView;

    Context context;
    Document document;
    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;
    long  downloadedsize, filesize;
    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;

    File myNewFolder= null;
    ProgressDialog mProgressDialog;

    String filetypes_store="";

    File dir=null;

    String authkey ="";
    String roll_type ="";

    public MyAdapter(Context context,ArrayList<Report_BeanClass> arrayList,String authkey,String roll_type) {

        Log.d("sizeofarray","****  "+arrayList.size());
        this.arrayList = arrayList;
        this.mFilteredList= arrayList;
         this.context = context;
        this.authkey = authkey;
        this.roll_type = roll_type;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylierviewchilditems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {


        Log.d("arrylistvalue","*** "+mFilteredList.get(position));
        holder.tv_date.setText(mFilteredList.get(position).getDate());
        holder.textview1.setText(mFilteredList.get(position).getTitle());
        holder.seturl.setText(mFilteredList.get(position).getUrl());

        holder.textview1.setTypeface(MainActivity.bold);
        holder.tv_date.setTypeface(MainActivity.regular);
        holder.tv_download.setTypeface(MainActivity.bold);
        holder.tv_open.setTypeface(MainActivity.regular);



        holder.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (connectionDetector.isConnectedToInternet(context))
                {
                    dir = new File(Environment.getExternalStorageDirectory() + "/Download/Idea_Reports/");
                    dir.mkdirs();

                   String PATH = Environment.getExternalStorageDirectory() + "/Download/Idea_Reports/";
                    if (!(new File(PATH)).exists()) {
                        new File(PATH).mkdirs();
                    }

                    /*String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    myNewFolder = new File(extStorageDirectory + newFolder);
                    myNewFolder.mkdir();*/

                   // new Download_file().execute(holder.seturl.getText().toString().trim(),  holder.textview1.getText().toString().trim());

                    /*if user type is distriputor execture the functions     */
                    getDate_Filter="";
                    getDate_Filter = mFilteredList.get(position).getDate();

                    Log.d("fetchfiles","***  "+Integer.parseInt(Report_Here.fieldtype));

                    if (roll_type.equals("4") ||roll_type.equals("2") || roll_type.equals("3")) {

                        if (Integer.parseInt(Report_Here.fieldtype) == 3) {
                            fetch_Pdfformat_1streport("3");
                        } else if (Integer.parseInt(Report_Here.fieldtype) == 1) {
                            fetch_Pdfformat_1streport("1");

                        } else if (Integer.parseInt(Report_Here.fieldtype) == 2) {

                            fetch_Pdfformat_1streport("2");
                        }else if (Integer.parseInt(Report_Here.fieldtype) == 4)
                        {
                            fetch_Pdfformat_1streport("4");
                        }else if(Integer.parseInt(Report_Here.fieldtype) == 5)
                        {
                            fetch_Pdfformat_1streport("5");
                        }else if (Integer.parseInt(Report_Here.fieldtype) == 6)
                        {
                            fetch_Pdfformat_1streport("6");
                        }
                        else if (Integer.parseInt(Report_Here.fieldtype) == 7)
                        {
                            fetch_Pdfformat_1streport("7");
                        }else if (Integer.parseInt(Report_Here.fieldtype) == 8)
                        {
                            fetch_Pdfformat_1streport("8");
                        }
                    }

                }else
                {
                    commonMethods = new CommonMethods(context);

                    commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));
                }
              //  Toast.makeText(context,"downloadclicked",Toast.LENGTH_LONG).show();

            }
        });

        holder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //Toast.makeText(context,"pease open file",Toast.LENGTH_LONG).show();

                getDate_Filter="";
                getDate_Filter = mFilteredList.get(position).getDate();

                Toast.makeText(context,"peaseopenfile"+getDate_Filter+ "  *  "+ Report_Here.fieldtype,Toast.LENGTH_LONG).show();

         /*       File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir" + "/" + Report_Here.fieldtype+""+getDate_Filter+".pdf");
*/
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA Files";
                File dir = new File(path);

               /* if (!dir.exists())
                    dir.mkdirs();*/

                File file = new File(dir, Report_Here.fieldtype+""+getDate_Filter+".csv");

                Log.d("fileexitornot","**  "+file);


                if (file.exists())
                {

                    viewPdfs(file, "Dir");
                   // viewPdf(Report_Here.fieldtype+""+getDate_Filter+".pdf", "Dir");

                }
                else
                {
                    commonMethods = new CommonMethods(context);
                    commonMethods.showErrorMessage("","File Not Yet Downloaded Please Download File..");
                }

            }
        });




    }

    @Override
    public int getItemCount() {

        Log.d(" mFilteredList.size()","**"+ mFilteredList.size());
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = arrayList;
                } else {

                    ArrayList<Report_BeanClass> filteredList = new ArrayList<>();

                    for (Report_BeanClass androidVersion : arrayList) {

                        if (androidVersion.getDate().toLowerCase().contains(charString) /*  ||androidVersion.toLowerCase().contains(charString) || androidVersion.getVer().toLowerCase().contains(charString)*/) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Report_BeanClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textview1;

        public  TextView tv_download,tv_open,seturl,tv_date;


        public MyViewHolder(View itemView) {
            super(itemView);

            textview1 = (TextView) itemView.findViewById(R.id.textview1);
            tv_download = (TextView) itemView.findViewById(R.id.tv_download);
            tv_open = (TextView) itemView.findViewById(R.id.tv_open);
            seturl  = (TextView) itemView.findViewById(R.id.seturl);
            tv_date  = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }


    class Download_file extends AsyncTask<String,Integer,String>
    {
        int length;
        String filenames="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Here you can set a message");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();


        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            try {
                URL url = new URL(strings[0]);
                filenames = strings[0];
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100% progress bar
                 length = connection.getContentLength();
                filesize = length;
                File file = new File(dir,""+ strings[1]+".csv");

                Log.d("filepath","*****   "+file);

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());

                OutputStream output = new FileOutputStream(file);
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int)(total*100/length));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                 /* File file = new File(myNewFolder, "Week" + pptx_filestore_postion + ".pptx");


            // Output stream
            OutputStream output = new FileOutputStream(file);*/


            } catch (MalformedURLException e) {
                e.printStackTrace();

                Log.d("MalformedURLException","Filedonwload"+e.toString());

            } catch (IOException e) {

                e.printStackTrace();
                Log.d("IOException","Filedonwload"+e.toString());
            }


            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           // mProgressDialog.setProgress(values[0]);
          //  mProgressDialog.setProgressNumberFormat((values[1]/(1024*1024)) + "" +(values[1]/(1024*1024)));
            mProgressDialog.setProgress(values[0]);
            mProgressDialog.setProgressNumberFormat((bytes2String(values[0])) + "/" + (bytes2String(filesize)));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

          //  mProgressDialog.dismiss();

            /*mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mProgressDialog.dismiss();
                }
            });*/

            /*Environment.getExternalStorageDirectory() + "/Download/Idea_Reports/*/
            String filepath_fromsdcared = Environment.getExternalStorageDirectory().toString() + dir +filenames+"csv";





            // setting downloaded into image view
            //dismissDialog(progress_bar_type);
            Log.d("filepath", "====" + filepath_fromsdcared);



        }
    }
    public static String bytes2String(long sizeInBytes) {

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if ( sizeInBytes < SPACE_KB ) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if ( sizeInBytes < SPACE_MB ) {
                return nf.format(sizeInBytes/SPACE_KB) + " KB";
            } else if ( sizeInBytes < SPACE_GB ) {
                return nf.format(sizeInBytes/SPACE_MB) + " MB";
            } else if ( sizeInBytes < SPACE_TB ) {
                return nf.format(sizeInBytes/SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes/SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }

    }



    public void fetch_Pdfformat_1streport(String filetypes)
    {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait....");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        document = new Document();
        filetypes_store = filetypes;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport_1st = adapter_retro.create(IdeaInterface.class);

        Call<ResponseBody> responce_distributore_report = getreport_1st.get_ReportDate(authkey, roll_type,filetypes);


        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    try {

                        String result = response.body().string();


                        Log.d("files1st_reports", "***   " + result);

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA Files";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".csv");

                        JSONArray docs = new JSONArray(result);
                      //  File file=new File(  Environment.getExternalStorageDirectory().getAbsolutePath() + "/gowthamguru"+"/tmp2/fromJSONss.csv");
                        String csv = CDL.toString(docs);
                        FileUtils.writeStringToFile(file, csv);

                       /* Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                        context.startActivity(intent);*/

                        viewPdfs(file, "Dir");




                        /*
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);

                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_foure = new PdfPTable(new float[] {10, 10, 10, 10, 10,10});

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_foure.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


                        tables.addCell("ID");
                        tables.addCell("Demo_number");
                        tables.addCell("Circle_name");
                        tables.addCell("Phase_name");
                        tables.addCell("Rollout_type");
                        tables.addCell("Device_serial_number");
                        tables.addCell("Device_description");

                        tables_two.addCell("Deployed_by");
                        tables_two.addCell("Deployeddatetime");
                        tables_two.addCell("Jan_actv");
                        tables_two.addCell("Feb_actv");
                        tables_two.addCell("Mar_actv");
                        tables_two.addCell("Apr_actv");
                        tables_two.addCell("May_actv");

                        tables_three.addCell("June_actv");
                        tables_three.addCell("July_actv");
                        tables_three.addCell("August_actv");
                        tables_three.addCell("September_actv");
                        tables_three.addCell("Octomber_actv");
                        tables_three.addCell("November_actv");
                        tables_three.addCell("December_actv");

                        tables_foure.addCell("Distno");
                        tables_foure.addCell("Dist_name");
                        tables_foure.addCell("Zone");
                        tables_foure.addCell("Asm_name");
                        tables_foure.addCell("Officer_name");
                        tables_foure.addCell("Created_date");

                        tables.setHeaderRows(1);

                        tables_two.setHeaderRows(1);

                        tables_three.setHeaderRows(1);
                        tables_foure.setHeaderRows(1);

                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();
                        PdfPCell[] cellsfoure = tables_foure.getRow(0).getCells();


                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[j].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[j].setBackgroundColor(BaseColor.GRAY);

                        }

                        for (int j=0;j<cellsfoure.length;j++)
                        {
                            cellsfoure[j].setBackgroundColor(BaseColor.GRAY);
                        }

                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0 ;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (getDate_Filter.equals(jsonObject.getString("created_date"))) {
                                tables.addCell(jsonObject.getString("id"));
                                tables.addCell(jsonObject.getString("demo_number"));
                                tables.addCell(jsonObject.getString("circle_name"));
                                tables.addCell(jsonObject.getString("phase_name"));
                                tables.addCell(jsonObject.getString("rollout_type"));
                                tables.addCell(jsonObject.getString("device_serial_number"));
                                tables.addCell(jsonObject.getString("device_description"));

                                tables_two.addCell(jsonObject.getString("deployed_by"));
                                tables_two.addCell(jsonObject.getString("deployeddatetime"));
                                tables_two.addCell(jsonObject.getString("deployeddatetime"));
                                tables_two.addCell(jsonObject.getString("feb_actv"));
                                tables_two.addCell(jsonObject.getString("mar_actv"));
                                tables_two.addCell(jsonObject.getString("apr_actv"));
                                tables_two.addCell(jsonObject.getString("may_actv"));


                                tables_three.addCell(jsonObject.getString("june_actv"));
                                tables_three.addCell(jsonObject.getString("july_actv"));
                                tables_three.addCell(jsonObject.getString("august_actv"));
                                tables_three.addCell(jsonObject.getString("september_actv"));
                                tables_three.addCell(jsonObject.getString("octomber_actv"));
                                tables_three.addCell(jsonObject.getString("november_actv"));
                                tables_three.addCell(jsonObject.getString("december_actv"));


                                tables_foure.addCell(jsonObject.getString("distno"));
                                tables_foure.addCell(jsonObject.getString("dist_name"));
                                tables_foure.addCell(jsonObject.getString("zone"));
                                tables_foure.addCell(jsonObject.getString("asm_name"));
                                tables_foure.addCell(jsonObject.getString("officer_name"));
                                tables_foure.addCell(jsonObject.getString("created_date"));
                            }
                        }
                        PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                        document.open();


                        tables.setSpacingAfter(20f);

                        tables_two.setSpacingAfter(20f);
                        tables_three.setSpacingAfter(20f);
                        tables_foure.setSpacingAfter(20f);


                        document.add(tables);
                        document.add(tables_two);
                        document.add(tables_three);
                        document.add(tables_foure);

                        //  document.addCreationDate();
                        document.close();



                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");*/
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


    public void fetch_Pdfformat_2ndreports(String filetypes)
    {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait....");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        document = new Document();
        filetypes_store = filetypes;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport_1st = adapter_retro.create(IdeaInterface.class);

        Call<ResponseBody> responce_distributore_report = getreport_1st.get_ReportDate(authkey, roll_type,filetypes);


        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    mProgressDialog.dismiss();
                    try {

                        String  result = response.body().string();

                        Log.d("resultof2ndreports","****   "+result);


                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10});

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.addCell("ID");
                        tables.addCell("Scd_code");
                        tables.addCell("Zone");
                        tables.addCell("Distributor_name");
                        tables.addCell("Distributor_no");
                        tables.addCell("Asm_name");
                        tables.addCell("Officer_name");

                        tables_two.addCell("type");
                        tables_two.addCell("total_uao_ot");
                        tables_two.addCell("ekyc_uao_ot");
                        tables_two.addCell("ekyc_ot");
                        tables_two.addCell("ekyc_y");
                        tables_two.addCell("ekyc_n");
                        tables_two.addCell("total_321");
                        tables_two.addCell("ekyc");

                        tables_three.addCell("slab");
                        tables_three.addCell("outstation_customer");
                        tables_three.addCell("outstation_cust_on_321");
                        tables_three.addCell("rv_count");
                        tables_three.addCell("created_date");
                        tables_three.addCell("added_by");



                        tables.setHeaderRows(1);
                        tables_two.setHeaderRows(1);
                        tables_three.setHeaderRows(1);

                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();

                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int j =0;j<cellstwo.length;j++)
                        {
                            cellstwo[j].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int j =0;j<cellsthree.length;j++)
                        {
                            cellsthree[j].setBackgroundColor(BaseColor.GRAY);
                        }


                        JSONArray jsonArray = new JSONArray(result);

                        for (int i =0;i<jsonArray.length();i++)
                        {


                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (getDate_Filter.equals(jsonObject.getString("created_date")))
                            {


                                tables.addCell(jsonObject.getString("id"));
                                tables.addCell(jsonObject.getString("scd_code"));
                                tables.addCell(jsonObject.getString("zone"));
                                tables.addCell(jsonObject.getString("distributor_name"));
                                tables.addCell(jsonObject.getString("distributor_no"));
                                tables.addCell(jsonObject.getString("asm_name"));
                                tables.addCell(jsonObject.getString("officer_name"));

                                tables_two.addCell(jsonObject.getString("type"));
                                tables_two.addCell(jsonObject.getString("total_uao_ot"));
                                tables_two.addCell(jsonObject.getString("ekyc_uao_ot"));
                                tables_two.addCell(jsonObject.getString("ekyc_ot"));
                                tables_two.addCell(jsonObject.getString("ekyc_y"));
                                tables_two.addCell(jsonObject.getString("ekyc_n"));
                                tables_two.addCell(jsonObject.getString("total_321"));
                                tables_two.addCell(jsonObject.getString("ekyc"));


                                tables_three.addCell(jsonObject.getString("slab"));
                                tables_three.addCell(jsonObject.getString("outstation_customer"));
                                tables_three.addCell(jsonObject.getString("outstation_cust_on_321"));
                                tables_three.addCell(jsonObject.getString("rv_count"));
                                tables_three.addCell(jsonObject.getString("created_date"));
                                tables_three.addCell(jsonObject.getString("added_by"));





                            }

                            PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                            document.open();


                            tables.setSpacingAfter(20f);

                            tables_two.setSpacingAfter(20f);
                            tables_three.setSpacingAfter(20f);

                            document.add(tables);
                            document.add(tables_two);
                            document.add(tables_three);

                            //  document.addCreationDate();
                            document.close();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();
                Log.d("result2ndreports","***  "+call.toString());
                commonMethods =new CommonMethods(context);
                commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));

            }
        });

    }

    public  void fetch_Pdfformat_3rd(String filedtypes) {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait....");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        document = new Document();

        filetypes_store = filedtypes;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, roll_type,filedtypes);


        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    mProgressDialog.dismiss();
                    try {


                        String result_distriputor = response.body().string();

                        Log.d("dispalyresults", "****   " + result_distriputor);

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10});
                        PdfPTable tables_foure = new PdfPTable(new float[] {10, 10, 10, 10, 10});

                        PdfPTable tables_five = new PdfPTable(new float[] {10, 10, 10, 10, 10});



                        PdfPTable tables_six  = new PdfPTable(new float[] {10, 10, 10, 10, 10, 10,10});

                        PdfPTable tables_seven  = new PdfPTable(new float[] {10, 10, 10, 10, 10, 10,10});

                        PdfPTable tables_eight = new PdfPTable(new float[] {10, 10, 10, 10});

                      //  float[] columnWidths = new float[]{10f, 20f, 30f, 10f};
                      //  tables.setWidths(15);

                      //  tables.setTotalWidth(width);
                       // tables.setLockedWidth(true);

                        tables_eight.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_eight.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_seven.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_seven.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                      tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                      tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                      tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_foure.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_five.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_six.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_six.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);



                        tables.addCell("Zone");
                        tables.addCell("Scd_code");
                        tables.addCell("Asm_name");
                        tables.addCell("Tsm_tse_name");
                        tables.addCell("Tsm_tse_type");
                        tables_two.addCell("Distributorname");
                        tables_two.addCell("Dist_msisdn");
                        tables_two.addCell("Se_msisdn");
                        tables_two.addCell("Day 1");
                        tables_two.addCell("Day 2");
                        tables_three.addCell("Day 3");
                        tables_three.addCell("Day 4");
                        tables_three.addCell("Day 5");
                        tables_three.addCell("Day 6");
                        tables_three.addCell("Day 7");
                        tables_foure.addCell("Day 8");
                        tables_foure.addCell("Day 9");
                        tables_foure.addCell("Day 10");
                        tables_foure.addCell("Day 11");
                        tables_foure.addCell("Day 12");
                        tables_five.addCell("Day 13");
                        tables_five.addCell("Day 14");
                        tables_five.addCell("Day 15");
                        tables_five.addCell("Day 16");
                        tables_five.addCell("Day 17");

                        tables_six.addCell("Day 18");
                        tables_six.addCell("Day 19");
                        tables_six.addCell("Day 20");
                        tables_six.addCell("Day 21");
                        tables_six.addCell("Day 22");
                        tables_six.addCell("Day 23");
                        tables_six.addCell("Day 24");

                        tables_seven.addCell("Day 25");
                        tables_seven.addCell("Day 26");
                        tables_seven.addCell("Day 27");
                        tables_seven.addCell("Day 28");
                        tables_seven.addCell("Day 29");
                        tables_seven.addCell("Day 30");
                        tables_seven.addCell("Day 31");


                        tables_eight.addCell("Grand_total");
                        tables_eight.addCell("Added_by");
                        tables_eight.addCell("Status");
                        tables_eight.addCell("Created_date");

                        tables_eight.setHeaderRows(1);

                        tables.setHeaderRows(1);

                        tables_two.setHeaderRows(1);

                        tables_three.setHeaderRows(1);
                        tables_foure.setHeaderRows(1);
                        tables_five.setHeaderRows(1);
                        tables_six.setHeaderRows(1);
                        tables_seven.setHeaderRows(1);
                        tables_eight.setHeaderRows(1);


                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();
                        PdfPCell[] cellsfoure = tables_foure.getRow(0).getCells();
                        PdfPCell[] cellsfive = tables_five.getRow(0).getCells();
                        PdfPCell[] cellssix = tables_six.getRow(0).getCells();

                        PdfPCell[] cellsseven = tables_seven.getRow(0).getCells();

                        PdfPCell[] cellseight = tables_eight.getRow(0).getCells();


                        for (int i =0;i<cellseight.length;i++)
                        {
                            cellseight[i].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int i =0;i<cellsseven.length;i++)
                        {
                            cellsseven[i].setBackgroundColor(BaseColor.GRAY);
                        }



                        for (int i = 0;i<cellssix.length;i++)
                        {
                            cellssix[i].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[j].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfoure[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfive[j].setBackgroundColor(BaseColor.GRAY);
                          //  cellssix[j].setBackgroundColor(BaseColor.GRAY);

                        }


                        /*document.add(tables);
                        document.addCreationDate();*/


                     /*  PdfPTable tables = new PdfPTable(1);
                        tables.addCell("zone");
                        tables.addCell("scd_code");
                        tables.addCell("asm_name");
                        tables.addCell("tsm_tse_name");
                        tables.addCell("tsm_tse_type");
                        tables.addCell("distributorname");
                        tables.addCell("dist_msisdn");
                        tables.addCell("se_msisdn");
                        tables.addCell("july_1st");
                        tables.addCell("july_2nd");
                        tables.addCell("july_3rd");
                        tables.addCell("july_4th");
                        tables.addCell("july_5th");
                        tables.addCell("july_6th");
                        tables.addCell("july_7th");
                        tables.addCell("july_8th");
                        tables.addCell("july_9th");
                        tables.addCell("july_10th");
                        tables.addCell("july_11th");
                        tables.addCell("july_12th");
                        tables.addCell("july_13th");
                        tables.addCell("july_14th");
                        tables.addCell("july_15th");
                        tables.addCell("july_16th");
                        tables.addCell("july_17th");
                        tables.addCell("july_18th");
                        tables.addCell("july_19th");
                        tables.addCell("july_20th");
                        tables.addCell("grand_total");
                        tables.addCell("added_by");
                        tables.addCell("status");
                        document.add(tables);
                        document.addCreationDate();*/

                      //  PdfPTable table = null;

                        JSONArray jsonarray = new JSONArray(result_distriputor);

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject obj = jsonarray.getJSONObject(i);

                            //PdfPCell myCell = new PdfPCell(new Paragraph(""));
                          //  table = new PdfPTable(i+1);

                            if (getDate_Filter.equals(obj.getString("created_date"))) {
                                tables.addCell(obj.getString("zone"));
                                tables.addCell(obj.getString("scd_code"));
                                tables.addCell(obj.getString("asm_name"));
                                tables.addCell(obj.getString("tsm_tse_name"));
                                tables.addCell(obj.getString("tsm_tse_type"));
                                tables_two.addCell(obj.getString("distributorname"));
                                tables_two.addCell(obj.getString("dist_msisdn"));
                                tables_two.addCell(obj.getString("se_msisdn"));
                                tables_two.addCell(obj.getString("july_1st"));
                                tables_two.addCell(obj.getString("july_2nd"));
                                tables_three.addCell(obj.getString("july_3rd"));
                                tables_three.addCell(obj.getString("july_4th"));
                                tables_three.addCell(obj.getString("july_5th"));
                                tables_three.addCell(obj.getString("july_6th"));
                                tables_three.addCell(obj.getString("july_7th"));
                                tables_foure.addCell(obj.getString("july_8th"));
                                tables_foure.addCell(obj.getString("july_9th"));
                                tables_foure.addCell(obj.getString("july_10th"));
                                tables_foure.addCell(obj.getString("july_11th"));
                                tables_foure.addCell(obj.getString("july_12th"));
                                tables_five.addCell(obj.getString("july_13th"));
                                tables_five.addCell(obj.getString("july_14th"));
                                tables_five.addCell(obj.getString("july_15th"));
                                tables_five.addCell(obj.getString("july_16th"));
                                tables_five.addCell(obj.getString("july_17th"));
                                tables_six.addCell(obj.getString("july_18th"));
                                tables_six.addCell(obj.getString("july_19th"));
                                tables_six.addCell(obj.getString("july_20th"));

                                tables_six.addCell(obj.getString("july_21st"));
                                tables_six.addCell(obj.getString("july_22nd"));
                                tables_six.addCell(obj.getString("july_23rd"));
                                tables_six.addCell(obj.getString("july_24th"));


                                tables_seven.addCell(obj.getString("july_25th"));
                                tables_seven.addCell(obj.getString("july_26th"));
                                tables_seven.addCell(obj.getString("july_27th"));
                                tables_seven.addCell(obj.getString("july_28th"));
                                tables_seven.addCell(obj.getString("july_29th"));
                                tables_seven.addCell(obj.getString("july_30th"));
                                tables_seven.addCell(obj.getString("july_31st"));


                                tables_eight.addCell(obj.getString("grand_total"));
                                tables_eight.addCell(obj.getString("added_by"));
                                tables_eight.addCell(obj.getString("status"));
                                tables_eight.addCell(obj.getString("created_date"));


                            }

                        }

                        PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                        document.open();


                       /* tables.setTotalWidth(PageSize.A4.getWidth());
                        tables.setLockedWidth(true);
                        PdfContentByte canvas = writer.getDirectContent();
                        PdfTemplate template = canvas.createTemplate(
                                tables.getTotalWidth(), tables.getTotalHeight());
                        tables.writeSelectedRows(0, -1, 0, tables.getTotalHeight(), template);
                        Image img = Image.getInstance(template);
                        img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                        img.setAbsolutePosition(
                                0, (PageSize.A4.getHeight() - tables.getTotalHeight()) / 2);*/


                        tables.setSpacingAfter(20f);

                        tables_two.setSpacingAfter(20f);
                        tables_three.setSpacingAfter(20f);
                        tables_foure.setSpacingAfter(20f);
                        tables_five.setSpacingAfter(20f);
                        tables_six.setSpacingAfter(20f);
                        tables_seven.setSpacingAfter(20f);
                        tables_eight.setSpacingAfter(20f);

                        document.add(tables);
                        document.add(tables_two);
                        document.add(tables_three);
                        document.add(tables_foure);
                        document.add(tables_five);
                        document.add(tables_six);
                        document.add(tables_seven);
                        document.add(tables_eight);

                      //  document.addCreationDate();
                        document.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }/* finally {

                    }*/
                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();

                Log.d("failureresponce","***   "+call.toString());

                commonMethods = new CommonMethods(context);
                commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));

            }
        });

    }

    public void fetch_Pdfformat_4th(String filetype)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        document = new Document();


        filetypes_store = filetype;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, roll_type,filetype);


        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    mProgressDialog.dismiss();

                    try {

                        String results_4threport = response.body().string();

                        Log.d("results_4threport","***   "+results_4threport.toString());


                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});
                        PdfPTable tables_foure = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_five = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_six  = new PdfPTable(new float[] {10, 10, 10, 10, 10, 10,10});
                        PdfPTable tables_seven  = new PdfPTable(new float[] {10, 10, 10});


                        tables_seven.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_seven.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_foure.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_five.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_six.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_six.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


                        tables.addCell("ID");
                        tables.addCell("Zone");
                        tables.addCell("Scd_code");
                        tables.addCell("Asm_name");
                        tables.addCell("Tsm_tse_name");
                        tables.addCell("Tsm_tse_type");
                        tables.addCell("Distributorname");


                        tables_two.addCell("Dist_msisdn");
                        tables_two.addCell("Se_msisdn");
                        tables_two.addCell("Ret_msisdn");
                        tables_two.addCell("Day 1");
                        tables_two.addCell("Day 2");
                        tables_two.addCell("Day 3");
                        tables_two.addCell("Day 4");

                        tables_three.addCell("Day 5");
                        tables_three.addCell("Day 6");
                        tables_three.addCell("Day 7");
                        tables_three.addCell("Day 8");
                        tables_three.addCell("Day 9");
                        tables_three.addCell("Day 10");
                        tables_three.addCell("Day 11");


                        tables_foure.addCell("Day 12");
                        tables_foure.addCell("Day 13");
                        tables_foure.addCell("Day 14");
                        tables_foure.addCell("Day 15");
                        tables_foure.addCell("Day 16");
                        tables_foure.addCell("Day 17");
                        tables_foure.addCell("Day 18");


                        tables_five.addCell("Day 19");
                        tables_five.addCell("Day 20");
                        tables_five.addCell("Day 21");
                        tables_five.addCell("Day 22");
                        tables_five.addCell("Day 23");
                        tables_five.addCell("Day 24");
                        tables_five.addCell("Day 25");


                        tables_six.addCell("Day 26");
                        tables_six.addCell("Day 27");
                        tables_six.addCell("Day 28");
                        tables_six.addCell("Day 29");
                        tables_six.addCell("Day 30");
                        tables_six.addCell("Day 31");
                        tables_six.addCell("Grand_total");


                        tables_seven.addCell("Status");
                        tables_seven.addCell("Created_date");
                        tables_seven.addCell("Added_by");



                        tables.setHeaderRows(1);
                        tables_two.setHeaderRows(1);
                        tables_three.setHeaderRows(1);
                        tables_foure.setHeaderRows(1);
                        tables_five.setHeaderRows(1);
                        tables_six.setHeaderRows(1);
                        tables_seven.setHeaderRows(1);



                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();
                        PdfPCell[] cellsfoure = tables_foure.getRow(0).getCells();
                        PdfPCell[] cellsfive = tables_five.getRow(0).getCells();
                        PdfPCell[] cellssix = tables_six.getRow(0).getCells();

                        PdfPCell[] cellsseven = tables_seven.getRow(0).getCells();



                        for (int i =0;i<cellsseven.length;i++)
                        {
                            cellsseven[i].setBackgroundColor(BaseColor.GRAY);
                        }


                        for (int i = 0;i<cellssix.length;i++)
                        {
                            cellssix[i].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[j].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfoure[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfive[j].setBackgroundColor(BaseColor.GRAY);
                            //  cellssix[j].setBackgroundColor(BaseColor.GRAY);

                        }



                        JSONArray jsonArray = new JSONArray(results_4threport);

                        for (int i = 0;i<jsonArray.length();i++)
                        {
                         JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (getDate_Filter.equals(jsonObject.getString("created_date"))) {
                                tables.addCell(jsonObject.getString("id\t"));
                                tables.addCell(jsonObject.getString("zone"));
                                tables.addCell(jsonObject.getString("scd_code"));
                                tables.addCell(jsonObject.getString("asm_name"));
                                tables.addCell(jsonObject.getString("tsm_tse_name"));
                                tables.addCell(jsonObject.getString("tsm_tse_type"));
                                tables.addCell(jsonObject.getString("distributorname"));

                                tables_two.addCell(jsonObject.getString("dist_msisdn"));
                                tables_two.addCell(jsonObject.getString("se_msisdn"));
                                tables_two.addCell(jsonObject.getString("ret_msisdn"));
                                tables_two.addCell(jsonObject.getString("july_1st"));
                                tables_two.addCell(jsonObject.getString("july_2nd"));
                                tables_two.addCell(jsonObject.getString("july_3rd"));
                                tables_two.addCell(jsonObject.getString("july_4th"));

                                tables_three.addCell(jsonObject.getString("july_5th"));
                                tables_three.addCell(jsonObject.getString("july_6th"));
                                tables_three.addCell(jsonObject.getString("july_7th"));
                                tables_three.addCell(jsonObject.getString("july_8th"));
                                tables_three.addCell(jsonObject.getString("july_9th"));
                                tables_three.addCell(jsonObject.getString("july_10th"));
                                tables_three.addCell(jsonObject.getString("july_11th"));

                                tables_foure.addCell(jsonObject.getString("july_12th"));
                                tables_foure.addCell(jsonObject.getString("july_13th"));
                                tables_foure.addCell(jsonObject.getString("july_14th"));
                                tables_foure.addCell(jsonObject.getString("july_15th"));
                                tables_foure.addCell(jsonObject.getString("july_16th"));
                                tables_foure.addCell(jsonObject.getString("july_17th"));
                                tables_foure.addCell(jsonObject.getString("july_18th"));

                                tables_five.addCell(jsonObject.getString("july_19th"));
                                tables_five.addCell(jsonObject.getString("july_20th"));
                                tables_five.addCell(jsonObject.getString("july_21st"));
                                tables_five.addCell(jsonObject.getString("july_22nd"));
                                tables_five.addCell(jsonObject.getString("july_23rd"));
                                tables_five.addCell(jsonObject.getString("july_24th"));
                                tables_five.addCell(jsonObject.getString("july_25th"));

                                tables_six.addCell(jsonObject.getString("july_26th"));
                                tables_six.addCell(jsonObject.getString("july_27th"));
                                tables_six.addCell(jsonObject.getString("july_28th"));
                                tables_six.addCell(jsonObject.getString("july_29th"));
                                tables_six.addCell(jsonObject.getString("july_30th"));
                                tables_six.addCell(jsonObject.getString("july_31st"));
                                tables_six.addCell(jsonObject.getString("grand_total"));

                                tables_seven.addCell(jsonObject.getString("status"));
                                tables_seven.addCell(jsonObject.getString("created_date"));
                                tables_seven.addCell(jsonObject.getString("added_by"));

                            }else
                            {
                                mProgressDialog.dismiss();
                            }

                       }
                        PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                        document.open();


                        tables.setSpacingAfter(20f);

                        tables_two.setSpacingAfter(20f);
                        tables_three.setSpacingAfter(20f);
                        tables_foure.setSpacingAfter(20f);
                        tables_five.setSpacingAfter(20f);
                        tables_six.setSpacingAfter(20f);
                        tables_seven.setSpacingAfter(20f);


                        document.add(tables);
                        document.add(tables_two);
                        document.add(tables_three);
                        document.add(tables_foure);
                        document.add(tables_five);
                        document.add(tables_six);
                        document.add(tables_seven);

                        //  document.addCreationDate();
                        document.close();



                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                commonMethods = new CommonMethods(context);
                commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));
            }
        });
    }

    public void fetch_Pdfformat_5th(String filetype){

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        document = new Document();


        filetypes_store = filetype;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue5th", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, roll_type,filetype);

        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    mProgressDialog.dismiss();
                    try {

                        String results = response.body().string();

                        Log.d("resultvaues","****  "+results.toString());


                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.addCell("ID");
                        tables.addCell("Ret_msisdn");
                        tables.addCell("Scd_code");
                        tables.addCell("Se_msisdn");
                        tables.addCell("Dist_msisdn");
                        tables.addCell("Zone");

                        tables_two.addCell("Scd");
                        tables_two.addCell("Distributorname");
                        tables_two.addCell("Asm_name");
                        tables_two.addCell("Asm_no");
                        tables_two.addCell("Tsm_tse_name");
                        tables_two.addCell("Tsm_tse_no");

                        tables_three.addCell("tsm_tse_type");
                        tables_three.addCell("retailer_wise_rech_final_out_put1_teritary");
                        tables_three.addCell("balance");
                        tables_three.addCell("retailer_wise_rech_final_out_put_teritary");
                        tables_three.addCell("added_by");
                        tables_three.addCell("status");
                        tables_three.addCell("created_date");






                        tables.setHeaderRows(1);
                        tables_two.setHeaderRows(1);
                        tables_three.setHeaderRows(1);



                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();

                        for (int i =0;i<cells.length;i++)
                        {
                            cells[i].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[i].setBackgroundColor(BaseColor.GRAY);
                        }
                        for (int i =0;i<cellsthree.length;i++)
                        {
                            cellsthree[i].setBackgroundColor(BaseColor.GRAY);
                        }

                        JSONArray jsonArray = new JSONArray(results);

                        for (int i = 0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (getDate_Filter.equals(jsonObject.getString("created_date")))
                            {

                                tables.addCell(jsonObject.getString("id"));
                                tables.addCell(jsonObject.getString("ret_msisdn"));
                                tables.addCell(jsonObject.getString("scd_code"));
                                tables.addCell(jsonObject.getString("se_msisdn"));
                                tables.addCell(jsonObject.getString("dist_msisdn"));
                                tables.addCell(jsonObject.getString("zone"));


                                tables_two.addCell(jsonObject.getString("scd"));
                                tables_two.addCell(jsonObject.getString("distributorname"));
                                tables_two.addCell(jsonObject.getString("asm_name"));
                                tables_two.addCell(jsonObject.getString("asm_no"));
                                tables_two.addCell(jsonObject.getString("tsm_tse_name"));
                                tables_two.addCell(jsonObject.getString("tsm_tse_no"));

                                tables_three.addCell(jsonObject.getString("tsm_tse_type"));
                                tables_three.addCell(jsonObject.getString("retailer_wise_rech_final_out_put1_teritary"));
                                tables_three.addCell(jsonObject.getString("balance"));
                                tables_three.addCell(jsonObject.getString("retailer_wise_rech_final_out_put_teritary"));
                                tables_three.addCell(jsonObject.getString("added_by"));
                                tables_three.addCell(jsonObject.getString("status"));
                                tables_three.addCell(jsonObject.getString("created_date"));




                            }
                            PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                            document.open();


                            tables.setSpacingAfter(20f);

                            tables_two.setSpacingAfter(20f);
                            tables_three.setSpacingAfter(20f);



                            document.add(tables);
                            document.add(tables_two);
                            document.add(tables_three);


                            //  document.addCreationDate();
                            document.close();





                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d("refilre","***  "+call.toString());

                commonMethods = new CommonMethods(context);
                commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));


            }
        });

    }


    public void  fetch_Pdfformat_7th(String filetypes)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        document = new Document();


        filetypes_store = filetypes;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue5th", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, roll_type,filetypes);

        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    mProgressDialog.dismiss();

                    try {

                        String results = response.body().string();

                        Log.d("report7ths","****   "+results);

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.addCell("ID");
                        tables.addCell("Ret_msisdn");
                        tables.addCell("Scd_code");
                        tables.addCell("Se_msisdn");
                        tables.addCell("Dist_msisdn");
                        tables.addCell("Region");


                        tables_two.addCell("Zone");
                        tables_two.addCell("Ret_name");
                        tables_two.addCell("Se_name");
                        tables_two.addCell("Scd");
                        tables_two.addCell("Distributor_name");
                        tables_two.addCell("Asm_name");
                        tables_two.addCell("asm_no");

                        tables_three.addCell("tsm_tse_name");
                        tables_three.addCell("tsm_tse_no");
                        tables_three.addCell("tsm_tse_type");
                        tables_three.addCell("teritary");
                        tables_three.addCell("added_by");
                        tables_three.addCell("status");
                        tables_three.addCell("created_date");


                        tables.setHeaderRows(1);
                        tables_two.setHeaderRows(1);
                        tables_three.setHeaderRows(1);



                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();

                        for (int i =0;i<cellstwo.length;i++)
                        {
                            cellstwo[i].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[i].setBackgroundColor(BaseColor.GRAY);
                        }

                        for (int i =0;i<cells.length;i++)
                        {
                            cells[i].setBackgroundColor(BaseColor.GRAY);
                        }


                        JSONArray jsonArray = new JSONArray(results);

                        for (int i = 0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (getDate_Filter.equals(jsonObject.getString("created_date")))
                            {

                                tables.addCell(jsonObject.getString("id"));
                                tables.addCell(jsonObject.getString("ret_msisdn"));
                                tables.addCell(jsonObject.getString("scd_code"));
                                tables.addCell(jsonObject.getString("se_msisdn"));
                                tables.addCell(jsonObject.getString("dist_msisdn"));
                                tables.addCell(jsonObject.getString("region"));


                                tables_two.addCell(jsonObject.getString("zone"));
                                tables_two.addCell(jsonObject.getString("ret_name"));
                                tables_two.addCell(jsonObject.getString("se_name"));
                                tables_two.addCell(jsonObject.getString("scd"));
                                tables_two.addCell(jsonObject.getString("distributor_name"));
                                tables_two.addCell(jsonObject.getString("asm_name"));
                                tables_two.addCell(jsonObject.getString("asm_no"));


                                tables_three.addCell(jsonObject.getString("tsm_tse_name"));
                                tables_three.addCell(jsonObject.getString("tsm_tse_no"));
                                tables_three.addCell(jsonObject.getString("tsm_tse_type"));
                                tables_three.addCell(jsonObject.getString("teritary"));
                                tables_three.addCell(jsonObject.getString("added_by"));
                                tables_three.addCell(jsonObject.getString("status"));
                                tables_three.addCell(jsonObject.getString("created_date"));





                            }
                            PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                            document.open();


                            tables.setSpacingAfter(20f);

                            tables_two.setSpacingAfter(20f);
                            tables_three.setSpacingAfter(20f);



                            document.add(tables);
                            document.add(tables_two);
                            document.add(tables_three);


                            //  document.addCreationDate();
                            document.close();



                        }




                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("failure7threports","****    "+call.toString());

                commonMethods = new CommonMethods(context);
                commonMethods.showErrorMessage("",context.getString(R.string.error_checkconnection));


            }
        });

    }


    public  void fetch_Pdfformat_8th(String filetypes)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        document = new Document();


        filetypes_store = filetypes;

        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue8th", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, roll_type,filetypes);


        responce_distributore_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful())
                {
                    mProgressDialog.dismiss();
                    try {

                        String result = response.body().string();

                        Log.d("8threport","responcevalues"+result);

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
                        File dir = new File(path);

                        if (!dir.exists())
                            dir.mkdirs();

                        File file = new File(dir, filetypes_store+""+getDate_Filter+".pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_foure = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_five = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10});

                        PdfPTable tables_six = new PdfPTable(new float[] {10, 10, 10, 10, 10,10,10,10});

                        tables_foure.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_foure.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_five.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_five.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_six.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_six.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_two.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables_three.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        tables_three.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                        tables.addCell("ID");
                        tables.addCell("Zone");
                        tables.addCell("Scd_code");
                        tables.addCell("Asm_name");
                        tables.addCell("Tsm_tse_name");
                        tables.addCell("Tsm_tse_type");
                        tables.addCell("Distributorname");

                        tables_two.addCell("Dist_msisdn");
                        tables_two.addCell("Day 1");
                        tables_two.addCell("Day 2");
                        tables_two.addCell("Day 3");
                        tables_two.addCell("Day 4");
                        tables_two.addCell("Day 5");
                        tables_two.addCell("Day 6");

                        tables_three.addCell("Day 7");
                        tables_three.addCell("Day 8");
                        tables_three.addCell("Day 9");
                        tables_three.addCell("Day 10");
                        tables_three.addCell("Day 11");
                        tables_three.addCell("Day 12");
                        tables_three.addCell("Day 13");


                        tables_foure.addCell("Day 14");
                        tables_foure.addCell("Day 15");
                        tables_foure.addCell("Day 16");
                        tables_foure.addCell("Day 17");
                        tables_foure.addCell("Day 18");
                        tables_foure.addCell("Day 19");
                        tables_foure.addCell("Day 20");

                        tables_five.addCell("Day 21");
                        tables_five.addCell("Day 22");
                        tables_five.addCell("Day 23");
                        tables_five.addCell("Day 24");
                        tables_five.addCell("Day 25");
                        tables_five.addCell("Day 26");
                        tables_five.addCell("Day 27");


                        tables_six.addCell("Day 28");
                        tables_six.addCell("Day 29");
                        tables_six.addCell("Day 30");
                        tables_six.addCell("Day 31");
                        tables_six.addCell("Grand_total");
                        tables_six.addCell("Added_by");
                        tables_six.addCell("Created_date");
                        tables_six.addCell("Status");


                        tables.setHeaderRows(1);
                        tables_two.setHeaderRows(1);
                        tables_three.setHeaderRows(1);
                        tables_foure.setHeaderRows(1);
                        tables_five.setHeaderRows(1);
                        tables_six.setHeaderRows(1);




                        PdfPCell[] cells = tables.getRow(0).getCells();
                        PdfPCell[] cellstwo = tables_two.getRow(0).getCells();
                        PdfPCell[] cellsthree = tables_three.getRow(0).getCells();
                        PdfPCell[] cellsfoure = tables_foure.getRow(0).getCells();
                        PdfPCell[] cellsfive = tables_five.getRow(0).getCells();
                        PdfPCell[] cellssix = tables_six.getRow(0).getCells();

                        for (int i = 0; i<cellssix.length;i++)
                        {
                            cellssix[i].setBackgroundColor(BaseColor.GRAY);
                        }


                        for (int i =0;i<cellstwo.length;i++)
                        {
                            cells[i].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[i].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[i].setBackgroundColor(BaseColor.GRAY);
                            cellsfoure[i].setBackgroundColor(BaseColor.GRAY);
                            cellsfive[i].setBackgroundColor(BaseColor.GRAY);


                        }

                        JSONArray jsonArray = new JSONArray(result);

                        for (int  i =0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (getDate_Filter.equals(jsonObject.getString("created_date")))
                            {

                                tables.addCell(jsonObject.getString("id"));
                                tables.addCell(jsonObject.getString("zone"));
                                tables.addCell(jsonObject.getString("scd_code"));
                                tables.addCell(jsonObject.getString("asm_name"));
                                tables.addCell(jsonObject.getString("tsm_tse_name"));
                                tables.addCell(jsonObject.getString("tsm_tse_type"));
                                tables.addCell(jsonObject.getString("distributorname"));

                                tables_two.addCell(jsonObject.getString("dist_msisdn"));
                                tables_two.addCell(jsonObject.getString("july_1st"));
                                tables_two.addCell(jsonObject.getString("july_2nd"));
                                tables_two.addCell(jsonObject.getString("july_3rd"));
                                tables_two.addCell(jsonObject.getString("july_4th"));
                                tables_two.addCell(jsonObject.getString("july_5th"));
                                tables_two.addCell(jsonObject.getString("july_6th"));


                                tables_three.addCell(jsonObject.getString("july_7th"));
                                tables_three.addCell(jsonObject.getString("july_8th"));
                                tables_three.addCell(jsonObject.getString("july_9th"));
                                tables_three.addCell(jsonObject.getString("july_10th"));
                                tables_three.addCell(jsonObject.getString("july_11th"));
                                tables_three.addCell(jsonObject.getString("july_12th"));
                                tables_three.addCell(jsonObject.getString("july_13th"));


                                tables_foure.addCell(jsonObject.getString("july_14th"));
                                tables_foure.addCell(jsonObject.getString("july_15th"));
                                tables_foure.addCell(jsonObject.getString("july_16th"));
                                tables_foure.addCell(jsonObject.getString("july_17th"));
                                tables_foure.addCell(jsonObject.getString("july_18th"));
                                tables_foure.addCell(jsonObject.getString("july_19th"));
                                tables_foure.addCell(jsonObject.getString("july_20th"));

                                tables_five.addCell(jsonObject.getString("july_21st"));
                                tables_five.addCell(jsonObject.getString("july_22nd"));
                                tables_five.addCell(jsonObject.getString("july_23rd"));
                                tables_five.addCell(jsonObject.getString("july_24th"));
                                tables_five.addCell(jsonObject.getString("july_25th"));
                                tables_five.addCell(jsonObject.getString("july_26th"));
                                tables_five.addCell(jsonObject.getString("july_27th"));


                                tables_six.addCell(jsonObject.getString("july_28th"));
                                tables_six.addCell(jsonObject.getString("july_29th"));
                                tables_six.addCell(jsonObject.getString("july_30th"));
                                tables_six.addCell(jsonObject.getString("july_31st"));
                                tables_six.addCell(jsonObject.getString("grand_total"));
                                tables_six.addCell(jsonObject.getString("added_by"));
                                tables_six.addCell(jsonObject.getString("created_date"));
                                tables_six.addCell(jsonObject.getString("status"));

                            }

                            PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                            document.open();


                            tables.setSpacingAfter(20f);

                            tables_two.setSpacingAfter(20f);
                            tables_three.setSpacingAfter(20f);
                            tables_foure.setSpacingAfter(20f);
                            tables_five.setSpacingAfter(20f);
                            tables_six.setSpacingAfter(20f);




                            document.add(tables);
                            document.add(tables_two);
                            document.add(tables_three);
                            document.add(tables_foure);
                            document.add(tables_five);
                            document.add(tables_six);


                            //  document.addCreationDate();
                            document.close();


                        }






                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                    viewPdf(filetypes_store+""+getDate_Filter+".pdf", "Dir");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




    }

    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA Files"+ file);
       // String path = ;

        Log.d("pdffilelocation","****    "+pdfFile);

        Uri path = Uri.fromFile(pdfFile);


        // Setting the intent for pdf reader
        /*Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        */
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile),"application/vnd.ms-excel");



        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewPdfs(File file, String directory) {

      //  File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDEA Files"+ file);
        // String path = ;

       /// Log.d("pdffilelocation","****    "+pdfFile);

       // Uri path = Uri.fromFile(pdfFile);


        // Setting the intent for pdf reader
        /*Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        */
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");



        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}
