package mhaverlant.dmmeteo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.adapters.MeteoDetailAdapter;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 15/03/15.
 */
public class DetailActivity extends ActionBarActivity {
    public static final String TAG = "AccueilActivity";
    public static final String kBUNDLE_CITY_LIST_KEY = "ListeMeteoVille";
    public static final String Nom = "Nom";

    private Set<String> MesFavoris;
    private List<MeteoVille> ListeMeteoVille;
    private List<MeteoVille> maBibliotheque;
    private String nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        this.ListeMeteoVille= (List<MeteoVille>) intent.getExtras().getSerializable(kBUNDLE_CITY_LIST_KEY);
        this.nom = intent.getExtras().getString(Nom);



        RemplirMaBibliotheque();


        ListView myListView = (ListView) findViewById(R.id.listeView);

        MeteoDetailAdapter adaptater = new MeteoDetailAdapter(this, maBibliotheque);
        myListView.setAdapter(adaptater);
        adaptater.notifyDataSetChanged();
    }

    public void RemplirMaBibliotheque(){
        maBibliotheque = new ArrayList<MeteoVille>();

        for (MeteoVille villemeteo:ListeMeteoVille){
            if (villemeteo.getNom().compareTo(nom)==0){
                maBibliotheque.add(villemeteo);
            }
        }


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
                Intent monintent1 = new Intent(DetailActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(DetailActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(DetailActivity.this, MaPositionActivity.class);
                monintent3.putExtra(MaPositionActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
