package Interface;

import com.ab.rakshasutra.MyLatLng;

import java.util.List;

public interface IOnLoadLocationListener {
    void onLoadLocationSuccess(List <MyLatLng> latLngs);
    void onLoadLocationFailed (String message);
}
