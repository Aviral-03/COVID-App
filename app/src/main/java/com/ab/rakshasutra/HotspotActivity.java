package com.ab.rakshasutra;

import Interface.IOnLoadLocationListener;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.ab.rakshasutra.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HotspotActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private View mapView;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker currentUser;
    private List<LatLng> dangerousArea;
    private List<LatLng> greenArea;
    private List<LatLng> orangeArea;
    private IOnLoadLocationListener listener;
    private DatabaseReference myCity;
    private Location lastLocation;
    private GeoQuery geoQuery;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    private View nbottomSheet;
    private View ipmbottomSheet;
    private View feverbottomSheet;
    private View cisrbottomSheet;
    private View kakatiyabottomsheet;
    private BottomSheetBehavior ipmbottomSheetBehavior;
    private BottomSheetBehavior nbottomSheetBehavior;
    private BottomSheetBehavior feverbottomSheetBehavior;
    private BottomSheetBehavior cisrbottomSheetBehavior;
    private BottomSheetBehavior kakatiyabottomSheetBehavior;

    //Auto Complete
    SearchView searchView;
    //Markers List
    private static final LatLng OSMANIA = new LatLng(17.3823887, 78.478957);
    private static final LatLng NIZAM = new LatLng(17.421824, 78.452093);
    private static final LatLng IPM = new LatLng(17.394044, 78.490268);
    private static final LatLng CISR = new LatLng(17.421011, 78.541035);
    private static final LatLng Fever = new LatLng(17.395899, 78.502433);
    private static final LatLng Kakatiya = new LatLng(18.0013286, 79.5889363);
    //Marker Name
    private Marker mOsmania;
    private Marker mNizam;
    private Marker mIPM;
    private Marker mCISR;
    private Marker mFever;
    private Marker mKakatiya;


    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields;
    private static final float DEFAULT_ZOOM = 15f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot);
        Window w = getWindow();


        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        searchView = findViewById(R.id.input_search);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQuery("", false);


        Button button = findViewById(R.id.hospitals);
        //Button Bottom Sheet
        //Call Button
        ImageButton cbtn = (ImageButton) findViewById(R.id.call);
        ImageButton btn1 = (ImageButton) findViewById(R.id.call_nizam);
        ImageButton btn2 = (ImageButton) findViewById(R.id.call_osmania);
        ImageButton btn3 = (ImageButton) findViewById(R.id.call_fever);
        ImageButton btn4 = (ImageButton) findViewById(R.id.call_cisr);
        ImageButton btn5 = (ImageButton) findViewById(R.id.call_kaktiya);

        //Button Direction
        Button dbtn1 = findViewById(R.id.dbutton_nizam);
        Button dbtn2 = findViewById(R.id.dbutton_osmania);
        Button dbtn3 = findViewById(R.id.dbutton_ipm);
        Button dbtn4 = findViewById(R.id.dbutton_fever);
        Button dbtn5 = findViewById(R.id.dbutton_cisr);
        Button dbtn6 = findViewById(R.id.dbutton_kaktiya);


        //Bottom Sheet Behaviour
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        nbottomSheet = findViewById(R.id.nbottom_sheet);
        nbottomSheetBehavior = BottomSheetBehavior.from(nbottomSheet);
        nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        ipmbottomSheet = findViewById(R.id.ipmbottom_sheet);
        ipmbottomSheetBehavior = BottomSheetBehavior.from(ipmbottomSheet);
        ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        feverbottomSheet = findViewById(R.id.feverbottom_sheet);
        feverbottomSheetBehavior = BottomSheetBehavior.from(feverbottomSheet);
        feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        cisrbottomSheet = findViewById(R.id.cisrbottom_sheet);
        cisrbottomSheetBehavior = BottomSheetBehavior.from(cisrbottomSheet);
        cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        kakatiyabottomsheet = findViewById(R.id.kakatiyabottom_sheet);
        kakatiyabottomSheetBehavior = BottomSheetBehavior.from(kakatiyabottomsheet);
        kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        //Button Direction
        dbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Nizam's+Institute+Of+Medical+Sciences"));
                startActivity(intent);
            }
        });
        dbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Osmania+Medical+College"));
                startActivity(intent);
            }
        });
        dbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Institute+of+Preventive+Medicine"));
                startActivity(intent);
            }
        });
        dbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Government+Fever+Hospital"));
                startActivity(intent);
            }
        });
        dbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=CSIR+-+Centre+for+Cellular+and+Molecular+Biology"));
                startActivity(intent);
            }
        });
        dbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Kakatiya+Medical+College"));
                startActivity(intent);
            }
        });

        //Button OnClick Listner
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 1075"));
                startActivity(intent);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 040 2348 9000"));
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:  040 2465 3992"));
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:  040 2766 7844"));
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:  040 2716 0222"));
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:  0870 244 6355"));
                startActivity(intent);
            }
        });


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HotspotActivity.this);


                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(HotspotActivity.this);
                        mapView = mapFragment.getView();


                        settingGeoFire();
                        initArea();
                        greenArea();
                        orangeArea();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(HotspotActivity.this, "You must enable permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(HotspotActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.hotspot);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.resource:
                        startActivity(new Intent(getApplicationContext()
                                , activity_resource.class));
                        Toast.makeText(HotspotActivity.this, "Resources", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext()
                                , News.class));
                        Toast.makeText(HotspotActivity.this, "News", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                }
                Toast.makeText(HotspotActivity.this, "Hotspots", Toast.LENGTH_SHORT).show();
                return false;

            }
        });
    }


    private void orangeArea() {
        orangeArea = new ArrayList<>();
        //orangeArea.add(new LatLng(17.238316, 78.473104));
    }

    private void greenArea() {
        greenArea = new ArrayList<>();
        //greenArea.add(new LatLng(17.237934, 78.431797));
    }

    private void initArea() {
        dangerousArea = new ArrayList<>();
        //dangerousArea.add(new LatLng(17.247645, 78.476128));
        //Rachakonda
        dangerousArea.add(new LatLng(17.2528469, 78.5517662));
        dangerousArea.add(new LatLng(17.26845, 78.4281462));
        dangerousArea.add(new LatLng(17.2695621, 78.4586005));
        dangerousArea.add(new LatLng(17.2012997, 78.4588623));
        dangerousArea.add(new LatLng(17.3678877, 78.5399503));
        dangerousArea.add(new LatLng(17.3361855, 78.4880277));
        dangerousArea.add(new LatLng(17.3364749, 78.5578841));
        dangerousArea.add(new LatLng(17.4246167, 78.5378457));
        dangerousArea.add(new LatLng(17.2889077, 78.4529768));
        dangerousArea.add(new LatLng(17.3730568, 78.5427906));
        dangerousArea.add(new LatLng(17.439662, 78.4248873));
        dangerousArea.add(new LatLng(17.4652354, 78.5561961));
        dangerousArea.add(new LatLng(17.46596, 78.5507003));
        //Cyberbad
        dangerousArea.add(new LatLng(17.4841752, 78.3994189));
        dangerousArea.add(new LatLng(17.4213885, 78.3320616));
        dangerousArea.add(new LatLng(17.4506873, 78.3812971));
        dangerousArea.add(new LatLng(17.4716712, 78.3611983));
        dangerousArea.add(new LatLng(17.4810243, 78.3570437));
        dangerousArea.add(new LatLng(17.4942247, 78.3494065));
        dangerousArea.add(new LatLng(17.4927775, 78.3422159));
        dangerousArea.add(new LatLng(17.4981477, 78.3752126));
        dangerousArea.add(new LatLng(17.4923557, 78.38329));
        dangerousArea.add(new LatLng(17.4242849, 78.3523327));
        dangerousArea.add(new LatLng(17.3369778, 78.4408007));
        dangerousArea.add(new LatLng(17.3310553, 78.4374632));
        dangerousArea.add(new LatLng(17.3455929, 78.4207472));
        dangerousArea.add(new LatLng(17.3536092, 78.4263912));
        dangerousArea.add(new LatLng(17.5166048, 78.4248547));
        dangerousArea.add(new LatLng(17.5126426, 78.3994834));
        dangerousArea.add(new LatLng(17.5181379, 78.4330241));
        dangerousArea.add(new LatLng(17.510543, 78.4495372));
        dangerousArea.add(new LatLng(17.5301594, 78.4137597));
        dangerousArea.add(new LatLng(17.5016133, 78.4603938));
    }


    private void settingGeoFire() {
        myLocationRef = FirebaseDatabase.getInstance().getReference("UserLocation");
        geoFire = new GeoFire(myLocationRef);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap != null) {

                    geoFire.setLocation("You", new GeoLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .alpha(0f)
                                    .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                            locationResult.getLastLocation().getLongitude()))
                                    .title(""));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUser.getPosition(), 16.0f));
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                    .target(currentUser.getPosition())
                                    .zoom(16)
                                    .bearing(0)
                                    .tilt(30)
                                    .build()));
                        }
                    });
                }

            }
        };

    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Marker
        mMap = googleMap;
        mOsmania = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.3823887, 78.478957))
                .title("Osmania Medical college")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
        mNizam = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.421824, 78.452093))
                .title("Nizam Institute")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
        mIPM = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.394044, 78.490268))
                .title("Institute of Preventive Medicine")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
        mCISR = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.421011, 78.541035))
                .title("CISR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
        mFever = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(17.395899, 78.502433))
                .title("Government Fever Hospital")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
        mKakatiya = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(18.0013286, 79.5889363))
                .title("Kakatiya Medical College")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(mOsmania)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    marker.showInfoWindow();
                    updateBottomSheetContent(marker);
                } else if (marker.equals(mNizam)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    marker.showInfoWindow();
                    updatenBottomSheetContent(marker);
                } else if (marker.equals(mIPM)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    marker.showInfoWindow();
                    updateipmBottomSheetContent(marker);
                } else if (marker.equals(mCISR)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    marker.showInfoWindow();
                    updatecisrBottomSheetContent(marker);
                } else if (marker.equals(mFever)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    marker.showInfoWindow();
                    updatefeverBottomSheetContent(marker);
                } else if (marker.equals(mKakatiya)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    updatekakatiyaBottomSheetContent(marker);
                }
                return true;
            }

            private void updatekakatiyaBottomSheetContent(Marker mKakatiya) {
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            private void updatecisrBottomSheetContent(Marker mCISR) {
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            private void updatefeverBottomSheetContent(Marker mFever) {
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            private void updateipmBottomSheetContent(Marker mIpm) {
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            private void updatenBottomSheetContent(Marker mNizam) {
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            private void updateBottomSheetContent(Marker mOsmania) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ipmbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                feverbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                nbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cisrbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                kakatiyabottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

        });

        mMap.isBuildingsEnabled();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 600);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(HotspotActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HotspotActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }


        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationCompass = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationCompass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 260, 30, 0);
        }
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View maptoolbar = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("4"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) maptoolbar.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(30, 1300, 0, 0);
        }

        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        for (LatLng latLng : dangerousArea) {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(1000)
                    .strokeColor(Color.RED)
                    .fillColor(0x22FF0000)
                    .strokeWidth(5.0f)
            );

            geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 1.0f);
            geoQuery.addGeoQueryEventListener(HotspotActivity.this);

        }
        for (LatLng latLng1 : greenArea) {
            mMap.addCircle(new CircleOptions().center(latLng1)
                    .radius(1000)
                    .strokeColor(Color.GREEN)
                    .fillColor(0x22008000)
                    .strokeWidth(5.0f)
            );

            geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng1.latitude, latLng1.longitude), 1.0f);
            geoQuery.addGeoQueryEventListener(HotspotActivity.this);

        }
        for (LatLng latLng2 : orangeArea) {
            mMap.addCircle(new CircleOptions().center(latLng2)
                    .radius(300)
                    .strokeColor(ResourcesCompat.getColor(getResources(), R.color.quantum_orange, null))
                    .fillColor(0x22ED7014)
                    .strokeWidth(5.0f)

            );


            geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng2.latitude, latLng2.longitude), 0.3f);
            geoQuery.addGeoQueryEventListener(HotspotActivity.this);
        }
    }

    public void onZoom (View view)
    {
        if (view.getId() == R.id.hospitals){
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(17.380891, 78.484799), 12.0f));

        }
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("Alert!",String.format("%s entered the Red Hotspot Area", key));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("Alert!",String.format("%s left the Red Hotspot Area ", key));
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) { }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String title, String content) {
        Toast.makeText(this, ""+content, Toast.LENGTH_SHORT).show();

        String NOTIFICATION_CHANNEL_ID = "RAKSHASUTRA_MULTIPLE_LOCATATION";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(),notification);
    }


}


