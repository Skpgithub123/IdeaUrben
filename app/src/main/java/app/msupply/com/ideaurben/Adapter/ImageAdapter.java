package app.msupply.com.ideaurben.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.msupply.com.ideaurben.Activity.App_User;
import app.msupply.com.ideaurben.Activity.Bullentin_Board;
import app.msupply.com.ideaurben.Activity.FeedBack;
import app.msupply.com.ideaurben.Activity.RegistartionActivity;
import app.msupply.com.ideaurben.Activity.ReportCategories_list;
import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Activity.Setting_Activity;
import app.msupply.com.ideaurben.MainActivity;
import app.msupply.com.ideaurben.R;
import android.widget.AbsListView.LayoutParams;

/**
 * Created by Zest Developer on 7/30/2017.
 */

public class ImageAdapter extends BaseAdapter {

    public Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
     //   ImageView imageView;

        ViewHolder_dialog viewHolder_dialog;
        LayoutInflater inflter=   (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflter.inflate(R.layout.grid_images, null);
            viewHolder_dialog = new ViewHolder_dialog();


            viewHolder_dialog.images_grids = (ImageView) convertView.findViewById(R.id.images_grids);
            viewHolder_dialog.text_images = (TextView) convertView.findViewById(R.id.text_images);
            viewHolder_dialog.ll_imagadapter= (LinearLayout) convertView.findViewById(R.id.ll_imagadapter);
            viewHolder_dialog.text_images.setTypeface(MainActivity.bold);

            //  convertView.setLayoutParams(new GridView.LayoutParams(250, 250));
             viewHolder_dialog.images_grids.setScaleType(ImageView.ScaleType.FIT_XY);
             convertView.setPadding(4,4,4,4);

            convertView.setTag(viewHolder_dialog);
            // if it's not recycled, initialize some attributes
          /*  imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);*/
        } else {
   //         imageView = (ImageView) convertView;
            viewHolder_dialog = (ViewHolder_dialog) convertView.getTag();
        }

        viewHolder_dialog.images_grids.setImageResource(mThumbIds[position]);

        viewHolder_dialog.ll_imagadapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 1)
                {
                    Intent intent = new Intent(mContext, App_User.class);
                    mContext.startActivity(intent);
                }else if (position== 5)
                {
                    Intent intent = new Intent(mContext, Setting_Activity.class);
                    mContext.startActivity(intent);
                }else if (position == 0)
                {
                    Intent intent = new Intent(mContext, ReportCategories_list.class);
                    mContext.startActivity(intent);
                }
                else if (position == 2)
                {
                    Intent intent = new Intent(mContext, Bullentin_Board.class);
                    mContext.startActivity(intent);
                }
                else if (position == 3)
                {
                    Intent intent = new Intent(mContext, FeedBack.class);
                    mContext.startActivity(intent);
                }

            }
        });


        if (position == 0)
        {
            viewHolder_dialog.text_images.setText("Report");
        }
        else if (position == 1)
        {
            viewHolder_dialog.text_images.setText("App User");
        }else if(position == 2)
        {
            viewHolder_dialog.text_images.setText("Bulletin Board");
        }else if (position == 3)
        {
            viewHolder_dialog.text_images.setText("FeedBack");
        }else if (position == 4)
        {
            viewHolder_dialog.text_images.setText("Share It");
        }else if (position == 5)
        {
            viewHolder_dialog.text_images.setText("Profile");
        }
        return convertView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.reports, R.drawable.add_user,
            R.drawable.board, R.drawable.ic_feedback_black_48dp,
            R.drawable.shareit, R.drawable.settings,
    };

    public  class ViewHolder_dialog
    {
     public   ImageView images_grids ;
        TextView text_images ;
        LinearLayout ll_imagadapter;

    }

}
