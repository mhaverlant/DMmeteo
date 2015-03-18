package mhaverlant.dmmeteo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.adapters.FavorisAdapter;
import mhaverlant.dmmeteo.adapters.MeteoDetailAdapter;
import mhaverlant.dmmeteo.models.FavoritesManager;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 13/03/15.
 */
public class DisplayFavorisActivity extends ActionBarActivity
{
    public static final String TAG = "FavorisActivity";
    public static final String kBUNDLE_CITY_LIST_KEY = "ListeMeteoVille";

    private List<String> MesFavoris;
    private List<MeteoVille> ListeMeteoVille;
    private List<String> maBibliotheque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayfavoris);


        Intent intent = getIntent();
        this.ListeMeteoVille= (List<MeteoVille>) intent.getExtras().getSerializable(kBUNDLE_CITY_LIST_KEY);

        RemplirMaBibliotheque();


        final ListView myListView = (ListView) findViewById(R.id.listeView1);


        Button btn1 = (Button)new Button(this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monIntent2 = new Intent(DisplayFavorisActivity.this,FavorisActivity.class);
                monIntent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monIntent2);
            }
        });
        btn1.setText("Ajouter un Favori");
        myListView.addFooterView(btn1);
        FavorisAdapter adaptater = new FavorisAdapter(this, maBibliotheque);
        myListView.setAdapter(adaptater);
        adaptater.notifyDataSetChanged();

    }

    public void removeAtomPayOnClickHandler(View v) {
        final String cityToDelete = (String)v.getTag();


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Suppression de Favori");

        // set dialog message
        alertDialogBuilder
                .setMessage("Voulez vous supprimer le Favori?")
                .setCancelable(false)
                .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        FavoritesManager.getInstance().removeCityFromFavs(cityToDelete);
                        Intent monintent4 = new Intent(DisplayFavorisActivity.this, DisplayFavorisActivity.class);
                        monintent4.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                        startActivity(monintent4);
                        // current activity

                    }
                })
                .setNegativeButton("Non",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void RemplirMaBibliotheque(){
        maBibliotheque = new ArrayList<String>();
        for(String city:FavoritesManager.getInstance().getCities().keySet()){
            maBibliotheque.add(city);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_displayfavoris, menu);


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
                Intent monintent1 = new Intent(DisplayFavorisActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(DisplayFavorisActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(DisplayFavorisActivity.this, MaPositionActivity.class);
                monintent3.putExtra(MaPositionActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
