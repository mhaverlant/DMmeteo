package mhaverlant.dmmeteo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.math.RoundingMode;
import java.security.Provider;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.adapters.MeteoAdapter;
import mhaverlant.dmmeteo.interfaces.WebServiceRequestorCallback;
import mhaverlant.dmmeteo.models.MeteoVille;
import mhaverlant.dmmeteo.tasks.WebServiceRequestor;

/**
 * Created by haverlantmatthias on 13/03/15.
 */
public class MaPositionActivity extends ActionBarActivity {

    public static final String TAG = "AccueilActivity";
    public static final String kBUNDLE_CITY_LIST_KEY = "ListeMeteoVille";

    private LocationManager lManager;
    private List<MeteoVille> ListeMeteoVille;
    private static final String preLoader = "http://www.infoclimat.fr/public-api/gfs/json?_ll=";
    private static final String postLoader = "&_auth=ABoEEw5wU3ECL1ptB3FVfFE5AjcBdwUiVChXNFw5Uy4Eb1Q1D28GYFE%2FA35TfFJkU34CYQswUmILYAJ6DH5RMABqBGgOZVM0Am1aPwcoVX5RfwJjASEFIlQwVzlcL1M4BG5ULg9uBmRROQN%2FU2JSZVNiAn0LK1JrC2wCZwxlUTIAYwRiDmRTNQJtWicHKFVkUWYCNwE%2BBTVUYVc3XGJTYwRiVDMPZQZjUTwDf1NmUmdTaAJnCzxSagtoAmMMflEtABoEEw5wU3ECL1ptB3FVfFE3AjwBag%3D%3D&_c=e0a28c0708e4309b36a9bfabf9763677";
    private Location location;
    private List<MeteoVille> Maposition;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Maposition = new ArrayList<MeteoVille>();
        setContentView(R.layout.activity_maposition);
        Intent intent =getIntent();
        this.ListeMeteoVille = (List<MeteoVille>) intent.getExtras().getSerializable(kBUNDLE_CITY_LIST_KEY);





        LocationManager loc= (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        List provider=loc.getAllProviders();



        loc.requestLocationUpdates((String) provider.get(0),0, 0, new LocationListener(){

            @Override
            public void onLocationChanged(Location locs) {
                Maposition = new ArrayList<MeteoVille>();
                lanceJSON("MaPosition",locs.getLatitude()+","+locs.getLongitude());
                 
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }





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

                        Log.i(TAG, "onSuccess triggered : " + meteoVilles);
                        Maposition.addAll(meteoVilles);

                        ListView myListView = (ListView) findViewById(R.id.malistView);
                        MeteoAdapter adaptater = new MeteoAdapter(getApplicationContext(), Maposition);

                        myListView.setAdapter(adaptater);
                        adaptater.notifyDataSetChanged();

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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
                Intent monintent1 = new Intent(MaPositionActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(MaPositionActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(MaPositionActivity.this, MaPositionActivity.class);
                monintent3.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
