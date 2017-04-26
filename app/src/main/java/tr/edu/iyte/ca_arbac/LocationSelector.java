package tr.edu.iyte.ca_arbac;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import java.io.IOException;
import java.util.List;

public class LocationSelector extends FragmentActivity implements OnMapClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Marker marker1;
    Marker marker2;
    Marker marker3;
    Circle circle;
    int counter;
    LatLng circleCenter;
    LatLng secondPoint;
    double circleRadius;
    private static final String TAG5 = "Test Message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.i(TAG5, "inside onCreate Method of LocationSelector");
        setContentView(R.layout.activity_location_selector);
        setUpMapIfNeeded();
        mMap.setOnMapClickListener(this);
       // Log.i(TAG5, "inside onCreate Method of LocationSelector1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void onSearch(View v){
        EditText locationName= (EditText) findViewById(R.id.locationName);
        String location=locationName.getText().toString();
        List<Address> addressList=null;
        if(location!=null || !location.equals("")){
            Geocoder geocoder= new Geocoder(this);
            try {
               addressList= geocoder.getFromLocationName(location,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=addressList.get(0);
            String locality=address.getLocality();
            LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());

            if(marker1!=null)
                marker1.remove();

            MarkerOptions markerOptions= new MarkerOptions()
                    .position(latLng)
                    .title(locality);
            marker1=mMap.addMarker(markerOptions);

           // Log.i(TAG5,"inside onSearch Method of LocationSelector");
           //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

    private void setUpMap() {
        marker1=mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
       mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
       // mMap.setMyLocationEnabled(true);
    }

    public void zoom(View v){
        if(v.getId()==R.id.zoomOut)
           mMap.animateCamera(CameraUpdateFactory.zoomOut());
       if(v.getId()==R.id.zoomIn)
           mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if(counter==0){
            circleCenter=latLng;
            if(marker2!=null)
                marker2.remove();

            MarkerOptions markerOptions= new MarkerOptions()
                    .position(latLng)
                    .title("center");
            marker2=mMap.addMarker(markerOptions);
           // Log.i(TAG5,"inside onMapClick Method of LocationSelector1");
        }
        else {
            secondPoint=latLng;
            Location center=new Location("center");
            center.setLatitude(circleCenter.latitude);
            center.setLongitude(circleCenter.longitude);

            Location point2= new Location("secondPoint");
            point2.setLatitude(secondPoint.latitude);
            point2.setLongitude(secondPoint.longitude);
            circleRadius=center.distanceTo(point2);

            if(marker3!=null)
                marker3.remove();

            MarkerOptions markerOptions= new MarkerOptions()
                    .position(latLng)
                    .title("secondPoint");
            marker3=mMap.addMarker(markerOptions);

            if(circle!=null)
                circle.remove();

            CircleOptions circleOptions= new CircleOptions()
                    .center(circleCenter)
                    .fillColor(0x330000FF)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(3)
                    .radius(circleRadius);
            circle=mMap.addCircle(circleOptions);
           // Log.i(TAG5, "inside onMapClick Method of LocationSelector2");
        }
        counter++;
    }

    public void returnSelectedLocation(View view){
        String locationContext= new String();
        locationContext= ContextAssigner.LOCATION +"="+circleCenter.latitude+";"+circleCenter.longitude+";"+
                secondPoint.latitude+";"+secondPoint.longitude;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ContextAssigner.LOCATION,locationContext);
        setResult(ContextAssigner.LOCATION_SELECTOR, returnIntent);
        finish();
      }
}
