package app.msupply.com.ideaurben.Commonclass;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import app.msupply.com.ideaurben.R;

/**
 * Created by Zest Developer on 7/28/2017.
 */

public class CommonMethods {

    public Context context;
         public CommonMethods(Context c)
         {
             this.context = c;
         }

         public  void  displayToast(String message)
         {
             Toast.makeText(context,message,Toast.LENGTH_LONG).show();
         }

    public void showErrorMessage(String title,String message) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                new ContextThemeWrapper(context, R.style.popup_theme));

        // dlgAlert.setTitle(title);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


}
