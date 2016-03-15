package metral.julien.channelmessaging.Activity.Map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import metral.julien.channelmessaging.Model.Message;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User user;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (User) getIntent().getSerializableExtra("User");
            message = (Message) getIntent().getSerializableExtra("Message");
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double latitude = (message.getLatitude() != null) ? message.getLatitude() : 0;
        double longitude = (message.getLongitude() != null) ? message.getLongitude() : 0;

        LatLng latLng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(latLng).title(message.getUsername()));
        CameraPosition cameraPosition = new CameraPosition(latLng,14,0,0);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }
}
