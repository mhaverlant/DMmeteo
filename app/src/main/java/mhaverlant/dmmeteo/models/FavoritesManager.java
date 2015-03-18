package mhaverlant.dmmeteo.models;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by haverlantmatthias on 15/03/15.
 */

/*
    DESIGN PATTERN -> Singleton
 */
public class FavoritesManager
{
    private static FavoritesManager INSTANCE;
    private static final String kFAV_FILE_NAME = "com.mhaverlant.dmmeteo.fav_file_name.dat";
    private static final String TAG = "FavoritesManager";

    private HashMap<String,String> cities;

    private Context appContext;

    private FavoritesManager()
    {

    }

    public void init(Context context){
        if(appContext == null){
            appContext = context;
        }

        loadFavsFromDisk();
    }
    private void loadFavsFromDisk()
    {
        try {

            FileInputStream fis =  appContext.openFileInput(kFAV_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            cities = (HashMap<String,String>) ois.readObject();
            Log.i(TAG,"Les Favoris sont : "+cities);
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            cities = new HashMap<String,String>();
        }
    }


    public static FavoritesManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new FavoritesManager();
        }
        return INSTANCE;
    }

    public HashMap<String,String> getCities()
    {
        loadFavsFromDisk();
        return cities;
    }

    public void addCityAsFav(String city,String cityCoord)
    {
        cities.put(city,cityCoord);
        persistFavsToDisk();
    }
    public void removeCityFromFavs(String city)
    {
        cities.remove(city);
        persistFavsToDisk();
    }
    private void persistFavsToDisk()
    {
        /*
            TODOS : Save favs on disk
         */
        File favFile = appContext.getFileStreamPath(kFAV_FILE_NAME);
        try {
            if(favFile.exists() || favFile.createNewFile())
            {
                FileOutputStream fos = appContext.openFileOutput(kFAV_FILE_NAME, appContext.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(cities);
                oos.close();
                Log.i(TAG, "Favorite saved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
