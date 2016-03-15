package metral.julien.channelmessaging.Activity.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import metral.julien.channelmessaging.R;

public class GPSActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Boolean isUpdating;
    protected Location mLastLocation;
    private double mLatitude = 0;
    private double mLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        mGoogleApiClient = new GoogleApiClient
                .Builder(GPSActivity.this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build()
        ;
        mGoogleApiClient.connect();
        isUpdating = false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(!isUpdating){
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);

                mLocationRequest.setFastestInterval(5000);

                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();
                }

                isUpdating = true;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        isUpdating = false;
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        isUpdating = false;
        super.onResume();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        isUpdating = false;
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        isUpdating = false;
        mLastLocation = location;
        mLongitude = location.getLongitude();
        mLatitude = location.getLatitude();
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
