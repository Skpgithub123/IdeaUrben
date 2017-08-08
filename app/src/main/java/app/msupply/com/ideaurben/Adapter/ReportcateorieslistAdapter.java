package app.msupply.com.ideaurben.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Commonclass.Report_BeanClass;
import app.msupply.com.ideaurben.Commonclass.Report_TypeBeanclass;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;

/**
 * Created by Zest Developer on 8/5/2017.
 */

public class ReportcateorieslistAdapter extends RecyclerView.Adapter<ReportcateorieslistAdapter.MyViewHolder>{

    ArrayList<Report_TypeBeanclass> arrayList = new ArrayList<>();

    Context context;
   // CardView lltypes_imagadapter;
    public ReportcateorieslistAdapter(Context context, ArrayList<Report_TypeBeanclass> arrayList)
    {

        this.context = context;
        this.arrayList = arrayList;
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

        public TextView tt_id,tt_tittle;




        public MyViewHolder(View itemView) {
            super(itemView);

            tt_id = (TextView) itemView.findViewById(R.id.tv_idoftypes);
            tt_tittle = (TextView) itemView.findViewById(R.id.typesofreports);

           // lltypes_imagadapter  = (CardView) itemView.findViewById(R.id.lltypes_imagadapter);

            tt_tittle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            switch (view.getId())
            {
                case R.id.typesofreports:
                {


                    int pos = getAdapterPosition();

                    Log.d("databaseids","valuesprintln"+arrayList.get(pos).getId()+ arrayList.get(pos).getTittle());
                    Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, Report_Here.class);
                    intent.putExtra("passid",arrayList.get(pos).getId());
                    context.startActivity(intent);

                    break;

                }
            }


        }
    }



}
