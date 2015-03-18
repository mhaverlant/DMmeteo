package mhaverlant.dmmeteo.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mhaverlant.dmmeteo.R;

import mhaverlant.dmmeteo.interfaces.WebServiceRequestorCallback;
import mhaverlant.dmmeteo.models.FavoritesManager;
import mhaverlant.dmmeteo.models.MeteoVille;

import mhaverlant.dmmeteo.tasks.WebServiceRequestor;

/**
 * Created by haverlantmatthias on 16/03/15.
 */
public class FavorisActivity extends ActionBarActivity {
    private String TAG = "Favoris Activity";
    private List<MeteoVille> ListeMeteoVille;
    private static final String kBUNDLE_CITY_LIST_KEY="ListeMeteoVille";
    final String[] listevilles = {"Paris","Marseille","Lille","Bordeau"};
    private String coordonate;
    private String ville;
    private EditText autoComplete;
    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        this.ListeMeteoVille = (List<MeteoVille>) intent.getExtras().getSerializable(kBUNDLE_CITY_LIST_KEY);
        setContentView(R.layout.activity_favoris);

        ListView lv = (ListView) findViewById(R.id.list_view);
        autoComplete = (EditText) findViewById(R.id.inputSearch);

        adapter = new ArrayAdapter<String>(this, R.layout.item_view, R.id.city, listevilles);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view,
                                                              int position, long id) {
                                          lanceJSON(listevilles[position]);
                                      }
                                  });
            lv.setAdapter(adapter);

            autoComplete.addTextChangedListener(new

            TextWatcher() {

                @Override
                public void onTextChanged (CharSequence cs,int arg1, int arg2, int arg3){
                    // When user changed the Text
                    FavorisActivity.this.adapter.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged (CharSequence arg0,int arg1, int arg2,
                int arg3){
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged (Editable arg0){
                    // TODO Auto-generated method stub
                }
            }

            );

        }


    public void lanceJSON(final String city){
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        /*
            TextView txt = (TextView) findViewById(R.id.output);
            txt.setText("En Cours de Chargement...");
         */

        WebServiceCity webServicecity =new WebServiceCity("http://api.openweathermap.org/data/2.5/weather?q="+city+",fr", city, params);


        webServicecity.execute();

    }


    public class WebServiceCity extends AsyncTask<String, Void, String> {
        private static final String TAG = "WebServiceCity";

        private String URL;
        private List<NameValuePair> parameters;
        private String cityName;


        public WebServiceCity(String Url, String cityName, List<NameValuePair> params)
        {
            this.URL = Url;
            this.parameters = params;
            this.cityName = cityName;
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(URL);
                if (parameters != null)
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (Exception e)
            {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {

            //R.integer.kDATA_PER_CITY
            HashMap<String,String> Favoris= FavoritesManager.getInstance().getCities();
            String city =null;
            try {
                JSONObject Object = new JSONObject(result);
                JSONObject monObject= (JSONObject) Object.get("coord");

                coordonate = monObject.getString("lon")+","+monObject.getString("lat");
                FavoritesManager.getInstance().addCityAsFav(cityName,monObject.getString("lat")+","+monObject.getString("lon"));
                Intent monintent4 = new Intent(FavorisActivity.this, MainActivity.class);
                startActivity(monintent4);
                } catch (JSONException e1) {
                e1.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favoris, menu);


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
                Intent monintent1 = new Intent(FavorisActivity.this, AccueilActivity.class);
                monintent1.putExtra(AccueilActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent1);
                return true;
            case R.id.Favoris:
                Intent monintent2 = new Intent(FavorisActivity.this, DisplayFavorisActivity.class);
                monintent2.putExtra(DisplayFavorisActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent2);
                return true;
            case R.id.MaPosition:
                Intent monintent3 = new Intent(FavorisActivity.this, MaPositionActivity.class);
                monintent3.putExtra(MaPositionActivity.kBUNDLE_CITY_LIST_KEY, (Serializable) ListeMeteoVille);
                startActivity(monintent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
