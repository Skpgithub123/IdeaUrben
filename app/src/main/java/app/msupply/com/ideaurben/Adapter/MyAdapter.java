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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {

    ArrayList<Report_BeanClass> arrayList = new ArrayList<>();
    String getDate_Filter;
    RecyclerView recyclerView;

    Context context;
    Document document = new Document();
    ConnectionDetector connectionDetector;

    CommonMethods commonMethods;
    long  downloadedsize, filesize;
    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;

    File myNewFolder= null;
    ProgressDialog mProgressDialog;
    File dir=null;

    String authkey ="";

    public MyAdapter(Context context,ArrayList<Report_BeanClass> arrayList,String authkey) {

        Log.d("sizeofarray","****  "+arrayList.size());
        this.arrayList = arrayList;
         this.context = context;
        this.authkey = authkey;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylierviewchilditems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {


        Log.d("arrylistvalue","*** "+arrayList.get(position));
        holder.tv_date.setText(arrayList.get(position).getDate());
        holder.textview1.setText(arrayList.get(position).getTitle());
        holder.seturl.setText(arrayList.get(position).getUrl());

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
                    getDate_Filter = arrayList.get(position).getDate();

                    fetch_Pdfformat();


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

                Toast.makeText(context,"pease open file",Toast.LENGTH_LONG).show();

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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



    public  void fetch_Pdfformat() {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait....");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();


        Retrofit adapter_retro = new Retrofit.Builder()
                .baseUrl(Constandapi.ROOT_URL)
                .build();

        IdeaInterface getreport = adapter_retro.create(IdeaInterface.class);

        Log.d("authkeyvalue", "*******    " + authkey);

        Call<ResponseBody> responce_distributore_report = getreport.get_ReportDate(authkey, "4", Report_Here.fieldtype);


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

                        File file = new File(dir, "newFile.pdf");
                        FileOutputStream fOut = new FileOutputStream(file);


                        //document.open();
                        PdfPTable tables = new PdfPTable(new float[] {10, 10, 10, 10, 10});

                        PdfPTable tables_two = new PdfPTable(new float[] {10, 10, 10, 10, 10});
                        PdfPTable tables_three = new PdfPTable(new float[] {10, 10, 10, 10, 10});
                        PdfPTable tables_foure = new PdfPTable(new float[] {10, 10, 10, 10, 10});

                        PdfPTable tables_five = new PdfPTable(new float[] {10, 10, 10, 10, 10});



                        PdfPTable tables_six  = new PdfPTable(new float[] {10, 10, 10, 10, 10, 10});

                      //  float[] columnWidths = new float[]{10f, 20f, 30f, 10f};
                      //  tables.setWidths(15);

                      //  tables.setTotalWidth(width);
                       // tables.setLockedWidth(true);
                      tables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                      tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                      tables_two.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                      tables.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

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
                        tables_six.addCell("Grand_total");
                        tables_six.addCell("Added_by");
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

                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                            cellstwo[j].setBackgroundColor(BaseColor.GRAY);
                            cellsthree[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfoure[j].setBackgroundColor(BaseColor.GRAY);
                            cellsfive[j].setBackgroundColor(BaseColor.GRAY);
                            cellssix[j].setBackgroundColor(BaseColor.GRAY);

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
                            tables_six.addCell(obj.getString("grand_total"));
                            tables_six.addCell(obj.getString("added_by"));
                            tables_six.addCell(obj.getString("status"));


                           


                        }



                        PdfWriter writer =  PdfWriter.getInstance(document, fOut);
                        document.open();

                    /*    tables.setTotalWidth(PageSize.A4.getWidth());
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

                        document.add(tables);
                        document.add(tables_two);
                        document.add(tables_three);
                        document.add(tables_foure);
                        document.add(tables_five);
                        document.add(tables_six);
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
                    viewPdf("newFile.pdf", "Dir");


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });

    }

    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }



    }















}
