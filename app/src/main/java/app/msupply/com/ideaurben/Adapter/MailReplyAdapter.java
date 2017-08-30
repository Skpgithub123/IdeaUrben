package app.msupply.com.ideaurben.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.msupply.com.ideaurben.Commonclass.CommonMethods;
import app.msupply.com.ideaurben.Commonclass.ConnectionDetector;
import app.msupply.com.ideaurben.Commonclass.Report_BulletinPojoClass;
import app.msupply.com.ideaurben.Commonclass.View_RepliesPojo;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;

/**
 * Created by uOhmac Technologies on 29-Aug-17.
 */

public class MailReplyAdapter extends RecyclerView.Adapter<MailReplyAdapter.MyViewHolder> {


    ArrayList<View_RepliesPojo> arrayList = new ArrayList<>();
    String getDate_Filter;

    Context context;
    ConnectionDetector connectionDetector;
    CommonMethods commonMethods;
    ProgressDialog mProgressDialog;
    String authkey = "";

    public MailReplyAdapter(Context context, ArrayList<View_RepliesPojo> arrayList, String authkey) {

        Log.d("sizeofarray", "****  " + arrayList.size());
        this.arrayList = arrayList;
        this.context = context;
        this.authkey = authkey;
    }

    @Override
    public MailReplyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_viewreplies, parent, false);

        return new MailReplyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MailReplyAdapter.MyViewHolder holder, final int position) {

        holder.tv_subject.setText("Subject:" +arrayList.get(position).getSubject());
        holder.tv_yourmessage.setText("Your Mail:" +arrayList.get(position).getFeedbackmsg());
        holder.tv_replymessage.setText("Reply Mail:" +arrayList.get(position).getReplymsg());
        holder.tv_mailsentdate.setText("Mail date:" +arrayList.get(position).getFeedbackdate());
        holder.tv_mailreceiveddate.setText("Reply date:" +arrayList.get(position).getReplybackdate());
        holder.tv_reply_by.setText("Replied by:" +arrayList.get(position).getReplyby());


        if((holder.tv_replymessage.getText().toString().equals("Reply Mail:" +null))&&holder.tv_reply_by.getText().toString().equals("Replied by:" +null)){
            holder.tv_replymessage.setText("Reply Mail:" +"There is no reply to your mail yet");
            holder.tv_reply_by.setText("Replied by:" +"No one has replied");
        }


        holder.tv_subject.setTypeface(MainActivity.bold);
        holder.tv_yourmessage.setTypeface(MainActivity.bold);
        holder.tv_replymessage.setTypeface(MainActivity.bold);
        holder.tv_mailsentdate.setTypeface(MainActivity.bold);
        holder.tv_mailreceiveddate.setTypeface(MainActivity.bold);
        holder.tv_reply_by.setTypeface(MainActivity.bold);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView tv_subject, tv_yourmessage, tv_replymessage, tv_mailsentdate, tv_mailreceiveddate,tv_reply_by;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_subject = (TextView) itemView.findViewById(R.id.subject_by);
            tv_yourmessage = (TextView) itemView.findViewById(R.id.message_by);
            tv_replymessage = (TextView) itemView.findViewById(R.id.replymessage_by);
            tv_mailsentdate = (TextView) itemView.findViewById(R.id.txt_mailsentdate);
            tv_mailreceiveddate = (TextView) itemView.findViewById(R.id.txt_mailreceiveddate);
            tv_reply_by = (TextView)itemView.findViewById(R.id.reply_by);

        }
    }

}
