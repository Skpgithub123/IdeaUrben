package app.msupply.com.ideaurben;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AbsListView.LayoutParams;
import android.widget.Toast;

import app.msupply.com.ideaurben.Activity.App_User;
import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Adapter.ImageAdapter;

public class MainActivity extends AppCompatActivity {

    GridView gridview;
   public static   Typeface regular,bold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");

       /* gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Log.d("displayvaluesare","dfdsfdf");
                Toast.makeText(MainActivity.this,"displayvaluesare",Toast.LENGTH_LONG).show();


            }
        });*/
        /*tv_signup.setTypeface(bold);
        tv_forgot.setTypeface(bold);*/
    /*    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Log.d("clickactivity","8888888"+i);


            }
        });*/

      /*  gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                *//*Toast.makeText(HelloGridView.this, "" + position,
                        Toast.LENGTH_SHORT).show();*//*

                Log.d("clickactivity","8888888"+position);

                if (position == 1)
                {
                    Intent intent = new Intent(MainActivity.this, App_User.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(MainActivity.this, Report_Here.class);
                    startActivity(intent);
                }
            }
        });*/




    }
}
