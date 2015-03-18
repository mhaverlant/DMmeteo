package mhaverlant.dmmeteo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mhaverlant.dmmeteo.R;
import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 13/03/15.
 */
public class MeteoAdapter extends BaseAdapter {
    List<MeteoVille> biblio;
    // LayoutInflater aura pour mission de charger notre fichier XML
    LayoutInflater inflater;
    /**
     * Elle nous servira à mémoriser les éléments de la liste en mémoire pour
     * qu’à chaque rafraichissement l’écran ne scintille pas
     *
     * @author patrice
     */
    private class ViewHolder {
        TextView Nom;
        TextView Temperature;
        ImageView Image;
    }
    public MeteoAdapter(Context context, List<MeteoVille> objects) {
        inflater = LayoutInflater.from(context);
        this.biblio = objects;
    }
    /**
     * Génère la vue pour un objet
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            Log.v("test", "convertView is null");
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.meteoville_item, null);
            holder.Nom = (TextView) convertView.findViewById(R.id.txtNom);
            holder.Temperature = (TextView) convertView
                    .findViewById(R.id.txtTemperature);
            holder.Image = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            Log.v("test", "convertView is not null");
            holder = (ViewHolder) convertView.getTag();
        }
        MeteoVille meteoville = biblio.get(position);
        holder.Nom.setText(meteoville.getNom());
        holder.Temperature.setText(" "+meteoville.getTemperature()+"° C");
        holder.Image.setImageResource(meteoville.getImage());
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
    public MeteoVille getItem(int position) {
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

