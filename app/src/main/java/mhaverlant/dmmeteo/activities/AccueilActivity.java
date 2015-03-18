package mhaverlant.dmmeteo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.adapters.MeteoAdapter;
import mhaverlant.dmmeteo.models.FavoritesManager;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 13/03/15.
 */
public class AccueilActivity extends ActionBarActivity
{
    public static final String TAG = "AccueilActivity";
    public static final String kBUNDLE_CITY_LIST_KEY = "ListeMeteoVile";

    Set<Map.Entry<String, String>> MesFavoris;
    List<MeteoVille> ListeMeteoVille;
    List<MeteoVille> maBibliotheque;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);


        DecimalFormat mFormat= new DecimalFormat("00");
        mFormat.setRoundingMode(RoundingMode.DOWN);

        Calendar todayDate = Calendar.getInstance();
        this.date = todayDate.get(Calendar.YEAR)
                + "-" + mFormat.format(Double.valueOf(todayDate.get(Calendar.MONTH)+1))
                + "-" + mFormat.format(Double.valueOf(todayDate.get(Calendar.DAY_OF_MONTH)));


        RemplirMaBibliotheque();


        ListView myListView = (ListView) findViewById(R.id.mylistView);

        MeteoAdapter adaptater = new MeteoAdapter(this, maBibliotheque);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent monintent = new Intent(AccueilActivity.this, DetailActivity.class);
                MeteoVille meteoville = maBibliotheque.get(position);

                monintent.putExtra(DetailActivity.Nom, meteoville.getNom());
                monintent.putExtra(DetailActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);

                startActivity(monintent);
            }
        });

        myListView.setAdapter(adaptater);
        adaptater.notifyDataSetChanged();

    }

    public void RemplirMaBibliotheque()
    {
        maBibliotheque = new ArrayList<MeteoVille>();
        Intent intent = getIntent();
        ListeMeteoVille= (List<MeteoVille>) intent.getExtras().getSerializable(kBUNDLE_CITY_LIST_KEY);


        MesFavoris = FavoritesManager.getInstance().getCities().entrySet();
        Iterator<Map.Entry<String,String>> iterator = MesFavoris.iterator();

        if (MesFavoris.size()!=0){
            while(iterator.hasNext()){
                Map.Entry<String, String> element = iterator.next();
                String city = element.getKey();
                Log.i(TAG," Mes Favoris : "+city);
                for (MeteoVille meteoville : ListeMeteoVille){
                    if (city != null && meteoville.getNom().compareTo(city)==0 && meteoville.getDate().compareTo(this.date)==0){
                        maBibliotheque.add(meteoville);
                        Log.i(TAG,"Ma meteo ville est"+meteoville.getNom());
                        Log.i(TAG,"Ma date"+meteoville.getDate());
                    }
                }
            }
        }
        else{

            Toast.makeText(this, "Veuillez ajouter un Favori",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acceuil, menu);
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
                Intent monintent1 = new Intent(AccueilActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(AccueilActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(AccueilActivity.this, MaPositionActivity.class);
                monintent3.putExtra(MaPositionActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
