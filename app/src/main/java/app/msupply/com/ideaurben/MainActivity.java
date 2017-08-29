package app.msupply.com.ideaurben;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AbsListView.LayoutParams;
import android.widget.Toast;

import app.msupply.com.ideaurben.Activity.App_User;
import app.msupply.com.ideaurben.Activity.LoginActivity;
import app.msupply.com.ideaurben.Activity.Report_Here;
import app.msupply.com.ideaurben.Adapter.ImageAdapter;

public class MainActivity extends AppCompatActivity {

    GridView gridview;
   public static   Typeface regular,bold;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Bold.ttf");
        regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lato-Regular.ttf");



        /* toolbar = (Toolbar) findViewById(R.id.toolbarmenu);
        this.setSupportActionBar(toolbar);

       // toolbar.setTitle("Report Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report Search");
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/

        toolbar = (Toolbar) findViewById(R.id.toolbars);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        sp = getSharedPreferences("splogin", 0);
        editor = sp.edit();



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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logoutmenu, menu);

        MenuItem logouts = menu.findItem(R.id.logout);
        logouts.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //searchView.setQueryHint("Search Date Here.");

        logouts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                editor.clear().commit();
                editor.putString("logout","null").commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                return false;
            }
        });

        // searchView.setQueryHint(Html.fromHtml("<font color = #000>" + "Search Date Here."+ "</font>"));
        // searchView.setHintTextColor(mRes.getColor(android.R.color.white));
        // searchView.setBackgroundColor(Color.BLACK);
        //  toolbar.setBackgroundColor(Color.WHITE);
       // search(searchView);
        //   searchView.setOnQueryTextListener(this);

        return true;


    }


}
