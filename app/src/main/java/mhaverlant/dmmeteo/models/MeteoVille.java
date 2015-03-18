package mhaverlant.dmmeteo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import mhaverlant.dmmeteo.R;


/**
 * Created by haverlantmatthias on 14/03/15.
 */
public class MeteoVille implements Parcelable
{
    private String nom;
    private String date;
    private float Temperature;
    private float precipitation;
    private float vent;
    private String risquedeneige;


    public String getNom(){return this.nom;}
    public float getTemperature(){return this.Temperature;}
    public String getDate(){return this.date;}
    public float getPrecipitation(){return this.precipitation;}
    public float getVent(){return this.vent;}
    public String getRisquedeneige(){return this.risquedeneige;}

    public void setNom(String nom){this.nom=nom;}
    public void setDate(String date){this.date=date;}
    public void setTemperature(int Temperature){this.Temperature=Temperature;}
    public void setPrecipitation(int precipitation){this.precipitation=precipitation;}
    public void setVent(int vent){this.vent=vent;}
    public void setRisquedeneige(String risquedeneige){this.risquedeneige=risquedeneige;}

    public MeteoVille(String nom, String date, float Temperature, float precipitation, float vent, String risquedeneige){
        this.nom=nom;
        this.date=date;
        this.Temperature=Temperature;
        this.precipitation=precipitation;
        this.vent=vent;
        this.risquedeneige=risquedeneige;
    }

    public MeteoVille(Parcel in)
    {
        nom = in.readString();
        date= in.readString();
        Temperature = in.readFloat();
        precipitation = in.readFloat();
        vent = in.readFloat();
        risquedeneige = in.readString();
    }

    public String ConvertDay(){


        Calendar calendar = new GregorianCalendar(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5, 7))-1,Integer.parseInt(date.substring(8,10)));
        int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String dayString;
        switch (dayOfWeek) {
            case 1:
                dayString = "Lundi";
                break;
            case 2:
                dayString = "Mardi";
                break;
            case 3:
                dayString = "Mercredi";
                break;
            case 4:
                dayString = "Jeudi";
                break;
            case 5:
                dayString = "Vendredi";
                break;
            case 6:
                dayString = "Samedi";
                break;
            case 7:
                dayString = "Dimanche";
                break;
            default:
                dayString ="Jour invalide";
        }
        return dayString;


    }

    public String ConvertMonth(){
        int Month = Integer.parseInt(date.substring(5,7));
        String monthString;
        switch (Month) {
            case 1:  monthString = "Janvier";       break;
            case 2:  monthString = "Fevrier";      break;
            case 3:  monthString = "Mars";         break;
            case 4:  monthString = "Avril";         break;
            case 5:  monthString = "Mai";           break;
            case 6:  monthString = "Juin";          break;
            case 7:  monthString = "Juillet";          break;
            case 8:  monthString = "Aout";        break;
            case 9:  monthString = "Septembre";     break;
            case 10: monthString = "Octobre";       break;
            case 11: monthString = "Novembre";      break;
            case 12: monthString = "Decembre";      break;
            default: monthString = "Invalid month"; break;
        }
        return monthString;
    }

    public int getImage(){
        if (risquedeneige.compareTo("oui")==0){
            return R.drawable.neigeux;
        }
        else{
            if (precipitation>1){
                return R.drawable.pluvieux;
            }
            else{
                if (vent>20 && vent<=40){
                    return R.drawable.ventfaible;
                }
                else if (vent>40){
                    return R.drawable.venteux;
                }
                else{
                    if (Temperature> 15){
                        return R.drawable.beau;
                    }
                    else{
                        return R.drawable.beaunuageux;
                    }
                }
            }
        }
    }


    /*
        PARCELABLE
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(nom);
        dest.writeString(date);
        dest.writeFloat(Temperature);
        dest.writeFloat(precipitation);
        dest.writeFloat(vent);
        dest.writeString(risquedeneige);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public MeteoVille createFromParcel(Parcel in) {
                    return new MeteoVille(in);
                }

                public MeteoVille[] newArray(int size) {
                    return new MeteoVille[size];
                }
            };
}
