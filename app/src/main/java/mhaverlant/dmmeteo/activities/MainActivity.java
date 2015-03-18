package mhaverlant.dmmeteo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.interfaces.WebServiceRequestorCallback;
import mhaverlant.dmmeteo.models.FavoritesManager;
import mhaverlant.dmmeteo.models.MeteoVille;
import mhaverlant.dmmeteo.tasks.WebServiceRequestor;


public class MainActivity extends ActionBarActivity {
    List<String> Favoris;
    private HashMap<String,String> listevilles;
    private String localisation;
    private static final String preLoader = "http://www.infoclimat.fr/public-api/gfs/json?_ll=";
    private static final String postLoader = "&_auth=ABoEEw5wU3ECL1ptB3FVfFE5AjcBdwUiVChXNFw5Uy4Eb1Q1D28GYFE%2FA35TfFJkU34CYQswUmILYAJ6DH5RMABqBGgOZVM0Am1aPwcoVX5RfwJjASEFIlQwVzlcL1M4BG5ULg9uBmRROQN%2FU2JSZVNiAn0LK1JrC2wCZwxlUTIAYwRiDmRTNQJtWicHKFVkUWYCNwE%2BBTVUYVc3XGJTYwRiVDMPZQZjUTwDf1NmUmdTaAJnCzxSagtoAmMMflEtABoEEw5wU3ECL1ptB3FVfFE3AjwBag%3D%3D&_c=e0a28c0708e4309b36a9bfabf9763677";
    private static final String TAG = "MainActivity";
    private List<MeteoVille> ListeMeteoVille;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        FavoritesManager.getInstance().init(getApplicationContext());

        int i=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar= (ProgressBar) findViewById(R.id.pbAsyncBackgroundTraitement);
        progressBar.setMax(100);
        progressBar.setProgress(0);


        this.ListeMeteoVille = new ArrayList<MeteoVille>();


        initListeVille();

        Set<String> cities = listevilles.keySet();
        Log.i(TAG,"Ma listevilles est pour le main:"+listevilles.get("Lille"));
        // TODO : Launch ProgressBar
        for (String city : cities)

        {
            lanceJSON(city, listevilles.get(city));
            i++;
            progressBar.setProgress(i*100/cities.size());
        }

        launchAccueilIfNeeded();
    }

    public static String getStringifiedDate(int dayOffset)
    {
        DecimalFormat mFormat= new DecimalFormat("00");
        mFormat.setRoundingMode(RoundingMode.DOWN);

        Calendar todayDate = Calendar.getInstance();
        Calendar monCalendar = new GregorianCalendar();
        todayDate.add(monCalendar.DAY_OF_YEAR, dayOffset);

        return todayDate.get(Calendar.YEAR)
                + "-" + mFormat.format(Double.valueOf(todayDate.get(Calendar.MONTH)+1))
                + "-" + mFormat.format(Double.valueOf(todayDate.get(Calendar.DAY_OF_MONTH)));
    }

    private void launchAccueilIfNeeded()
    {
        if(ListeMeteoVille.size() >= listevilles.keySet().size() * getResources().getInteger(R.integer.kDATA_PER_CITY ) && listevilles.size()>0)
        {
            // TODO : Stop ProgressBar
            Log.i(TAG, "Done loading data, launching Accueil");
            Intent accueil = new Intent(MainActivity.this, AccueilActivity.class);
            accueil.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
            startActivity(accueil);
        }
    }

    private void initListeVille(){

        this.listevilles = FavoritesManager.getInstance().getCities();

        if (listevilles.size()==0){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Veuillez Ajouter un Favori");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Veuillez ajouter un Favori")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            Intent monintent4 = new Intent(MainActivity.this, FavorisActivity.class);
                            monintent4.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                            startActivity(monintent4);

                        }
                    });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
    }
    /*private void getlocalisation(){
        this.localisation =
    }*/

    private void lanceJSON(final String city, String localite){
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        /*
            TextView txt = (TextView) findViewById(R.id.output);
            txt.setText("En Cours de Chargement...");
         */

        WebServiceRequestor webServiceRequestor =new WebServiceRequestor(preLoader+localite+postLoader, city, params,
            new WebServiceRequestorCallback()
            {
                @Override
                public void onSuccess(List<MeteoVille> meteoVilles)
                {
                    progressBar.setProgress(progressBar.getProgress()+1/listevilles.size()*100);
                    Log.i(TAG, "onSuccess triggered : "+meteoVilles);
                    ListeMeteoVille.addAll(meteoVilles);

                    launchAccueilIfNeeded();
                }
                @Override
                public void onError(Exception e)
                {
                    Log.e(TAG, "Exception while getting infos about "+city+" : "+e);
                }
            });
        webServiceRequestor.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.Acceuil:
                Intent monintent1 = new Intent(MainActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(MainActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(MainActivity.this, MaPositionActivity.class);
                monintent3.putExtra(MaPositionActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
