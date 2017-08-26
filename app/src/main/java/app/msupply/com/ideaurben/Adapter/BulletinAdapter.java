package app.msupply.com.ideaurben.Adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import app.msupply.com.ideaurben.Activity.ShowImageWebview;
import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Commonclass.Report_BulletinPojoClass;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;


/**
 * Created by uOhmac Technologies on 08-Aug-17.
 */

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.MyViewHolder> {

    ArrayList<Report_BulletinPojoClass> arrayList = new ArrayList<>();
    String getDate_Filter;

    Context context;
    ConnectionDetector connectionDetector;
    CommonMethods commonMethods;

    ProgressDialog mProgressDialog;
    File dir=null;
    String filenames="";
    String authkey ="";
    long  downloadedsize, filesize;
    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;
    public BulletinAdapter(Context context,ArrayList<Report_BulletinPojoClass> arrayList,String authkey) {

        Log.d("sizeofarray","****  "+arrayList.size());
        this.arrayList = arrayList;
        this.context = context;
        this.authkey = authkey;
    }
    @Override
    public BulletinAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bulletin_childitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BulletinAdapter.MyViewHolder holder, final int position) {

        holder.txt_posteddate.setText(arrayList.get(position).getDate());
        holder.txt_filetitle.setText(arrayList.get(position).getTitle());
        holder.txt_filetype.setText(arrayList.get(position).getType());
        holder.txt_filepath.setText(arrayList.get(position).getUrl());

        holder.txt_filetype.setTypeface(MainActivity.bold);
        holder.txt_posteddate.setTypeface(MainActivity.regular);
        holder.tv_download_forbulletin.setTypeface(MainActivity.bold);
        holder.tv_open_forbulletin.setTypeface(MainActivity.regular);


        holder.tv_download_forbulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectedToInternet(context))
                {
                    dir = new File(Environment.getExternalStorageDirectory() + "/Download/Reports/");
                    dir.mkdirs();

                    String PATH = Environment.getExternalStorageDirectory() + "/Download/Reports/";
                    if (!(new File(PATH)).exists()) {
                        new File(PATH).mkdirs();
                    }

                    /*String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    myNewFolder = new File(extStorageDirectory + newFolder);
                    myNewFolder.mkdir();*/

                     new Download_file().execute(holder.txt_filepath.getText().toString().trim(),holder.txt_filetitle.getText().toString().trim(),  holder.txt_filetype.getText().toString().trim());

                    /*if user type is distriputor execture the functions     */
                    getDate_Filter="";
                    getDate_Filter = arrayList.get(position).getDate();



                }else{
                    commonMethods = new CommonMethods(context);

                    commonMethods.showErrorMessage("",context.getResources().getString(R.string.error_checkconnection));
                }
            }
        });


        holder.tv_open_forbulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.txt_filetype.getText().toString().equals("pdf")) {
                    viewPdf(holder.txt_filetitle.getText().toString().trim()+"."+holder.txt_filetype.getText().toString().trim(), "Dir");
                }else if(holder.txt_filetype.getText().toString().equals("xlsx")){
                    viewexcelfile(holder.txt_filetitle.getText().toString().trim()+"."+holder.txt_filetype.getText().toString().trim(), "Dir");
                }else if(holder.txt_filetype.getText().toString().equals("docx")){
                    viewdocfile(holder.txt_filetitle.getText().toString().trim()+"."+holder.txt_filetype.getText().toString().trim(), "Dir");

                }else if(holder.txt_filetype.getText().toString().equals("images")){

                    if(ConnectionDetector.isConnectedToInternet(context)) {
                        Intent i = new Intent(context, ShowImageWebview.class);
                        i.putExtra("imageurl", holder.txt_filepath.getText().toString().trim());
                        context.startActivity(i);
                    }else {
                        commonMethods.showErrorMessage("", context.getResources().getString(R.string.error_checkconnection));
                    }

                }
            }
        });

    }


    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/Reports/" + file);
        Uri path = Uri.fromFile(pdfFile);
            Log.d("pdffilepath", ""+path);
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

    private void viewexcelfile(String file, String directory){

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/Reports/" + file);
        Uri path = Uri.fromFile(pdfFile);
        Log.d("excelfilepath", ""+path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewdocfile(String file, String directory){
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/Reports/" + file);
        Uri path = Uri.fromFile(pdfFile);
        Log.d("excelfilepath", ""+path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/msword");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
        }
    }




    class Download_file extends AsyncTask<String,Integer,String>{
        int length;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();


        }
        @Override
        protected String doInBackground(String... strings) {
            int count;

            try{
                URL url = new URL(strings[0]);
                filenames = strings[0];
                Log.d("filename", filenames);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100% progress bar
                length = connection.getContentLength();
                filesize = length;
                File file = new File(dir,""+ strings[1]+"."+strings[2]);


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
            }catch (MalformedURLException e) {
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
            mProgressDialog.setProgress(values[0]);
            mProgressDialog.setProgressNumberFormat((bytes2String(values[0])) + "/" + (bytes2String(filesize)));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String filepath_fromsdcared = Environment.getExternalStorageDirectory().toString() + dir +filenames;
            mProgressDialog.dismiss();
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_filetype;

        public  TextView tv_open_forbulletin,tv_download_forbulletin,txt_filetitle,txt_posteddate,txt_filepath;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_download_forbulletin = (TextView) itemView.findViewById(R.id.tv_download_forbulletin);
            tv_open_forbulletin = (TextView) itemView.findViewById(R.id.tv_open_forbulletin);
            txt_filetype = (TextView)itemView.findViewById(R.id.txt_filetype);
            txt_filetitle  = (TextView) itemView.findViewById(R.id.txt_filetitle);
            txt_posteddate  = (TextView) itemView.findViewById(R.id.txt_posteddate);
            txt_filepath = (TextView) itemView.findViewById(R.id.txt_filepath);
        }
    }
}
