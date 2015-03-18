package mhaverlant.dmmeteo.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.interfaces.WebServiceRequestorCallback;
import mhaverlant.dmmeteo.activities.MainActivity;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 15/03/15.
 */
public class WebServiceRequestor extends AsyncTask<String, Void, String>
{
    private static final String TAG = "WebServiceRequestor";

    private String URL;
    private List<NameValuePair> parameters;
    private String date, cityName;

    List<String> datesToFetch = new ArrayList<>();

    private WebServiceRequestorCallback callback;

    public WebServiceRequestor(String Url, String cityName, List<NameValuePair> params, WebServiceRequestorCallback callback)
    {
        this.URL = Url;
        this.parameters = params;
        this.callback = callback;
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
            callback.onError(e);
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result)
    {
        String date = MainActivity.getStringifiedDate(0);
        String tomorrowDate = MainActivity.getStringifiedDate(1);
        String postTomorrow = MainActivity.getStringifiedDate(2);

        datesToFetch.add(date);
        datesToFetch.add(tomorrowDate);
        datesToFetch.add(postTomorrow);

        DecimalFormat df=new DecimalFormat("#.##");

        //R.integer.kDATA_PER_CITY

        try {
            JSONObject theObject = new JSONObject(result);
            List<MeteoVille> meteoVilles = new ArrayList<MeteoVille>();



            for ( int i=0;i<datesToFetch.size();i++)
            {
                JSONObject cityData = (JSONObject) theObject.get(datesToFetch.get(i)+" 15:00:00");
                Log.i(TAG,"Taille a fetcher"+datesToFetch.get(0)+" "+datesToFetch.get(1)+" "+datesToFetch.get(2));

                        meteoVilles.add(new MeteoVille(cityName,
                                                        datesToFetch.get(i),
                                ((float)((int)((Float.parseFloat(cityData.getJSONObject("temperature").getString("sol"))-273)*10)))/10,
                                ((float)((int)( Float.parseFloat(cityData.getString("pluie"))*10)))/10,
                                ((float)((int)( Float.parseFloat(cityData.getJSONObject("vent_moyen").getString("10m"))*10)))/10,
                                                        cityData.getString("risque_neige")
                                                        ));




            }
            callback.onSuccess(meteoVilles);
        } catch (Exception e){
            callback.onError(e);
        }
        super.onPostExecute(result);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Void... values) {

    }
}
