package mhaverlant.dmmeteo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.activities.DisplayFavorisActivity;
import mhaverlant.dmmeteo.models.FavoritesManager;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 16/03/15.
 */
public class FavorisAdapter extends BaseAdapter {
    List<String> biblio;
    // LayoutInflater aura pour mission de charger notre fichier XML
    LayoutInflater inflater;
    /**
     * Elle nous servira à mémoriser les éléments de la liste en mémoire pour
     * qu’à chaque rafraichissement l’écran ne scintille pas
     *
     * @author patrice
     */
    public class ViewHolder {
        TextView Nom;
        ImageView Image1;
        ImageButton removebutton;
    }
    public FavorisAdapter(Context context, List<String> objects) {
        inflater = LayoutInflater.from(context);
        this.biblio = objects;
    }
    /**
     * Génère la vue pour un objet
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String Ville = biblio.get(position);
        if (convertView == null) {
            Log.v("test", "convertView is null");
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ville_item,parent,false);
            holder.Nom = (TextView) convertView.findViewById(R.id.texteNom);

            // A modifier pour permettre de clicker sur image2...

            holder.removebutton = (ImageButton) convertView.findViewById(R.id.image2);
            holder.removebutton.setTag(Ville);

            holder.Image1 = (ImageView) convertView.findViewById(R.id.image1);
            convertView.setTag(holder);
        } else {
            Log.v("test", "convertView is not null");
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Nom.setText(Ville);
        holder.removebutton.setImageResource(R.drawable.poubelle);
        holder.Image1.setImageResource(R.drawable.ville);
        return convertView;
    }
    /**
     * Retourne le nombre d'éléments
     */


    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return biblio.size();
    }
    /**
     * Retourne l'item à la position
     */
    @Override
    public String getItem(int position) {
// TODO Auto-generated method stub
        return biblio.get(position);
    }
    /**
     * Retourne la position de l'item
     */
    @Override
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return position;
    }
}
