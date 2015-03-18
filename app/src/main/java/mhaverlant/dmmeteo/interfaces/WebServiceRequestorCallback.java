package mhaverlant.dmmeteo.interfaces;

import java.util.List;

import mhaverlant.dmmeteo.models.MeteoVille;

/**
 * Created by haverlantmatthias on 15/03/15.
 */
public interface WebServiceRequestorCallback
{
    public void onSuccess(List<MeteoVille> meteoVilles);
    public void onError(Exception e);
}
