package com.goteborgstad.fragments;

import static android.content.Context.LOCATION_SERVICE;
import static com.goteborgstad.R.id.map;
import static com.goteborgstad.custom.Global.back_tag;

public class MapFragment extends android.app.Fragment implements android.widget.AdapterView.OnItemClickListener {

    public static android.widget.AutoCompleteTextView autoCompView;

    public static android.widget.ImageView btn_search;
    public static android.widget.RelativeLayout layout_map_parent;
    public static com.google.android.gms.maps.MapView mMapView = null;
    public static com.google.android.gms.maps.GoogleMap googleMap = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_map, container, false);

        com.goteborgstad.custom.Global.controllerName = "MapFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        back_tag = "map";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "STEG 3 AV 5", "PLATS FÖR HINDER", android.graphics.Color.parseColor("#4D548F"));

        com.goteborgstad.MainViewController.btn_back.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_back.setImageResource(com.goteborgstad.R.drawable.icon_back_arrow);
        com.goteborgstad.MainViewController.btn_back.setContentDescription("Tillbaka");
        com.goteborgstad.MainViewController.btn_back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), view);

                getFragmentManager().popBackStack(back_tag,
                        android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        com.goteborgstad.MainViewController.btn_next.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_next.setText("Nästa");

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.txt_description.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#4D548F"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Visa på karta eller skriv en adress var hindret finns. Detta är nödvändigt för att kunna skicka anmälan.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                com.goteborgstad.custom.Global.selectedBitmap = CreateBitmapFromLayout(layout_map_parent);

                SaveBitmap();

            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        btn_search = (android.widget.ImageView) v.findViewById(com.goteborgstad.R.id.btn_search);

        layout_map_parent = (android.widget.RelativeLayout) v.findViewById(com.goteborgstad.R.id.layout_map_parent);
        mMapView = (com.google.android.gms.maps.MapView) v.findViewById(map);
        autoCompView = (android.widget.AutoCompleteTextView) v.findViewById(com.goteborgstad.R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), com.goteborgstad.R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        locationManager = (android.location.LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);

        if (!enabled) {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity(), com.goteborgstad.custom.Global.dialog_theme);
            dialog.setMessage("Sätt på platstjänsten i inställningar för att kunna hämta aktuell position!");
            dialog.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    dialog.dismiss();

                    com.goteborgstad.custom.Global.reloadMapLocation = true;

                    android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Avbryt", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    dialog.dismiss();

                    ReadFileData();
                }
            });
            dialog.show();
        }

        LoadGoogleMap(savedInstanceState);

        autoCompView.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click

                    autoCompView.setContentDescription("Search");

                    com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), autoCompView);

                    com.goteborgstad.custom.Global.ShowLog("location = " + autoCompView.getText().toString());

                    GetLocationFromName(autoCompView.getText().toString());

                    return true;
                }
                return false;
            }
        });

        btn_search.setContentDescription("Search");
        btn_search.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), btn_search);

                com.goteborgstad.custom.Global.ShowLog("location = " + autoCompView.getText().toString());

                GetLocationFromName(autoCompView.getText().toString());
            }
        });

        return v;
    }

    public void LoadGoogleMap(final android.os.Bundle savedInstanceState) {
        try {

            if (back_tag.equalsIgnoreCase("map")) {
                mMapView.onCreate(savedInstanceState);

                mMapView.onResume();
            }

            Runnable myRun = new Runnable() {
                @Override
                public void run() {
                    if (back_tag.equalsIgnoreCase("map")) {
                        try {
                            com.google.android.gms.maps.MapsInitializer.initialize(
                                    getActivity().getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    android.os.Message msg = new android.os.Message();
                    msg.what = 1;
                    MyHandler.sendMessage(msg);
                }
            };
            Thread th = new Thread(null, myRun, "thread");
            th.start();
        } catch (Exception ee) { }
    }

    public android.os.Handler MyHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:

                    mMapView.setContentDescription("Google Map");

                    if (back_tag.equalsIgnoreCase("map")) {
                        mMapView.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
                            @Override
                            public void onMapReady(com.google.android.gms.maps.GoogleMap gMap) {
                                googleMap = gMap;
                                googleMap.setContentDescription("Google Map");

                                try {
                                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                                    googleMap.getUiSettings().setMapToolbarEnabled(true);
                                    googleMap.getUiSettings().setTiltGesturesEnabled(true);
                                    googleMap.getUiSettings().setScrollGesturesEnabled(true);

                                } catch (Exception ee) {}

                                locationManager = (android.location.LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                                boolean enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
                                if (enabled) {
                                    ReadFileData();
                                }

                                googleMap.setOnMarkerDragListener(new com.google.android.gms.maps.GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(com.google.android.gms.maps.model.Marker arg0) {
                                        // TODO Auto-generated method stub
                                        com.goteborgstad.custom.Global.ShowLog("onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                                        com.goteborgstad.custom.Global.user_latitude = "" + arg0.getPosition().latitude;
                                        com.goteborgstad.custom.Global.user_longitude = "" + arg0.getPosition().longitude;
                                    }

                                    @SuppressWarnings("unchecked")
                                    @Override
                                    public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker arg0) {
                                        // TODO Auto-generated method stub
                                        com.goteborgstad.custom.Global.ShowLog("onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                                        //com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), com.goteborgstad.MainViewController.btn_bottom);

                                        com.goteborgstad.custom.Global.user_latitude = "" + arg0.getPosition().latitude;
                                        com.goteborgstad.custom.Global.user_longitude = "" + arg0.getPosition().longitude;

                                        googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(arg0.getPosition()));

                                        String locationName = getCompleteAddressString(getActivity(), arg0.getPosition().latitude, arg0.getPosition().longitude);
                                        autoCompView.setContentDescription(locationName);
                                        autoCompView.setText(locationName);

                                        StoreData(getActivity());
                                    }

                                    @Override
                                    public void onMarkerDrag(com.google.android.gms.maps.model.Marker arg0) {
                                        // TODO Auto-generated method stub
                                        com.goteborgstad.custom.Global.ShowLog("onMarkerDrag...");
                                    }
                                });
                            }
                        });
                    }

                    super.handleMessage(msg);
            }
        }
    };

    public void ReadFileData() {
        try {

            String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
            com.goteborgstad.custom.Global.ShowLog("sendmail = " + sendmail);

            if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
                GetUserLocation(getActivity());
            } else {
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(sendmail);

                    String location = obj.getString("location");
                    String address = obj.getString("address");

                    boolean isLocationExist = false;
                    if (location.contains(",")) {
                        isLocationExist = true;
                        String[] coordinates = location.split(",");
                        com.goteborgstad.custom.Global.user_latitude = coordinates[0];
                        com.goteborgstad.custom.Global.user_longitude = coordinates[1];
                    } else {
                        GetUserLocation(getActivity());
                    }

                    if (isLocationExist) {
                        if (address.equalsIgnoreCase("")) {
                            address = getCompleteAddressString(getActivity(), new Double(com.goteborgstad.custom.Global.user_latitude),
                                    new Double(com.goteborgstad.custom.Global.user_longitude));
                        }
                    }

                    autoCompView.setText(address);

                    com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(
                            new Double(com.goteborgstad.custom.Global.user_latitude), new Double(com.goteborgstad.custom.Global.user_longitude));

                    UpdateLocation(true, latLng);

                } catch (Exception ee) {
                    GetUserLocation(getActivity());
                }
            }

        } catch (Exception ee) {}
    }

    public static String getCompleteAddressString(android.content.Context mContext, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        android.location.Geocoder geocoder = new android.location.Geocoder(mContext, java.util.Locale.getDefault());
        try {
            java.util.List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                strAdd = strReturnedAddress.toString();
                if (strAdd.length() > 3) {
                    strAdd = strAdd.substring(0,strAdd.length()-2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strAdd;
    }

    public static void UpdateLocation(boolean isZoom, com.google.android.gms.maps.model.LatLng latLng) {

        googleMap.clear();

        com.google.android.gms.maps.model.MarkerOptions marker = new com.google.android.gms.maps.model.MarkerOptions().position(
                latLng);

        //marker.icon(pin_open);

        marker.draggable(true);

        googleMap.addMarker(marker);

        if (isZoom) {
            com.google.android.gms.maps.model.CameraPosition cameraPosition = new com.google.android.gms.maps.model.CameraPosition.Builder()
                    .target(latLng).zoom(17).build();

            googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    // get current location
    public static  android.location.LocationManager locationManager;
    public static  android.location.LocationListener locationListener;
    public static  android.app.ProgressDialog customLoader;
    public static android.content.Context GlobalContext = null;

    public static void GetUserLocation(android.content.Context mContext) {

        GlobalContext = mContext;

        com.goteborgstad.custom.Global.ShowLog("GetUserLocation");

        locationManager = (android.location.LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        if (enabled) {

            customLoader = new android.app.ProgressDialog(mContext, com.goteborgstad.custom.Global.dialog_theme);
            customLoader.setCanceledOnTouchOutside(false);
            customLoader.setCancelable(true);
            customLoader.setMessage("Var god vänta. Hämtar aktuell position");
            customLoader.show();

            android.location.Criteria criteriaForLocationService = new android.location.Criteria();
            criteriaForLocationService.setAccuracy(android.location.Criteria.ACCURACY_COARSE);
            criteriaForLocationService.setAltitudeRequired(true);
            criteriaForLocationService.setVerticalAccuracy(android.location.Criteria.ACCURACY_HIGH);
            criteriaForLocationService.setSpeedRequired(true);
            criteriaForLocationService.setSpeedAccuracy(android.location.Criteria.ACCURACY_HIGH);
            criteriaForLocationService.setBearingAccuracy(android.location.Criteria.ACCURACY_HIGH);
            criteriaForLocationService.setBearingRequired(true);
            criteriaForLocationService.setHorizontalAccuracy(android.location.Criteria.ACCURACY_HIGH);

            locationListener = new android.location.LocationListener() {

                public void onLocationChanged(android.location.Location location) {
                    // Called when a new location is found by the network
                    // location provider.

                    com.goteborgstad.custom.Global.ShowLog("GetUserLocation-onLocationChanged");

                    com.goteborgstad.custom.Global.user_longitude = "" + location.getLongitude();
                    com.goteborgstad.custom.Global.user_latitude = "" + location.getLatitude();

                    android.os.Message msg = new android.os.Message();
                    msg.what = 1;
                    MyHandlerUserLocation.sendMessage(msg);
                }

                public void onStatusChanged(String provider, int status,
                                            android.os.Bundle extras) {
                    //Toast.makeText(getApplicationContext(), "GPS StatusChanged", Toast.LENGTH_LONG).show();
                    com.goteborgstad.custom.Global.ShowLog("GetUserLocation-onStatusChanged");

                    com.goteborgstad.custom.Global.user_longitude = "0.0000";
                    com.goteborgstad.custom.Global.user_latitude = "0.0000";

                    android.os.Message msg = new android.os.Message();
                    msg.what = 1;
                    MyHandlerUserLocation.sendMessage(msg);
                }

                public void onProviderEnabled(String provider) {
                    //Toast.makeText(getApplicationContext(), "GPS ProviderEnabled", Toast.LENGTH_LONG).show();
                    com.goteborgstad.custom.Global.ShowLog("GetUserLocation-onProviderEnabled");

                    com.goteborgstad.custom.Global.user_longitude = "0.0000";
                    com.goteborgstad.custom.Global.user_latitude = "0.0000";

                    android.os.Message msg = new android.os.Message();
                    msg.what = 1;
                    MyHandlerUserLocation.sendMessage(msg);
                }

                public void onProviderDisabled(String provider) {

                    com.goteborgstad.custom.Global.ShowLog("GetUserLocation-onProviderDisabled");

                    com.goteborgstad.custom.Global.user_longitude = "0.0000";
                    com.goteborgstad.custom.Global.user_latitude = "0.0000";

                    android.os.Message msg = new android.os.Message();
                    msg.what = 1;
                    MyHandlerUserLocation.sendMessage(msg);
                }
            };

            if (android.support.v4.app.ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    android.content.pm.PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(mContext,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestSingleUpdate(criteriaForLocationService, locationListener, android.os.Looper.getMainLooper());
            //locationManager.requestSingleUpdate(android.location.LocationManager.GPS_PROVIDER, locationListener, android.os.Looper.getMainLooper());
        }
    }

    public static void removeLocationListner() {

        try {

            if (android.support.v4.app.ActivityCompat.checkSelfPermission(GlobalContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED
                    && android.support.v4.app.ActivityCompat.checkSelfPermission(GlobalContext,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);

        } catch (Exception ee) {}
    }

    public static android.os.Handler MyHandlerUserLocation = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    try {
                        customLoader.dismiss();
                        customLoader.hide();
                    } catch (Exception ee) {}

                    try {

                        removeLocationListner();

                        com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(
                                new Double(com.goteborgstad.custom.Global.user_latitude), new Double(com.goteborgstad.custom.Global.user_longitude));

                        autoCompView.setText(getCompleteAddressString(GlobalContext, new Double(com.goteborgstad.custom.Global.user_latitude),
                                new Double(com.goteborgstad.custom.Global.user_longitude)));

                        UpdateLocation(true, latLng);

                        StoreData(GlobalContext);

                    } catch (Exception ee) {
                    }
                    super.handleMessage(msg);
            }
        }
    };

    public android.graphics.Bitmap CreateBitmapFromLayout(android.widget.RelativeLayout drawingCanvas) {

        android.graphics.Bitmap overAllDrawing = null;
        try {
            drawingCanvas.measure(android.view.View.MeasureSpec.makeMeasureSpec(
                    drawingCanvas.getLayoutParams().width, android.view.View.MeasureSpec.EXACTLY),
                    android.view.View.MeasureSpec.makeMeasureSpec(drawingCanvas.getLayoutParams().height,
                            android.view.View.MeasureSpec.EXACTLY));
            overAllDrawing = android.graphics.Bitmap.createBitmap(drawingCanvas.getWidth(), drawingCanvas.getHeight(),
                    android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(overAllDrawing);
            drawingCanvas.draw(canvas);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return overAllDrawing;
    }

    public void SaveBitmap() {

        StoreData(getActivity());

        back_tag = "image";

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        com.goteborgstad.fragments.ImageFragment homeFrag = new com.goteborgstad.fragments.ImageFragment();
        ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
        ft.addToBackStack(back_tag);
        ft.commit();
    }

    // google places
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyA5AFVgh3vwICeAbzW37_iDRInONb6udUM";

    private android.location.Geocoder geocoder;
    private java.util.List<android.location.Address> addresses;

    public void onItemClick(android.widget.AdapterView adapterView, android.view.View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);
        autoCompView.setContentDescription(str);
        autoCompView.setText(str);

        com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), view);

        //android.widget.Toast.makeText(getActivity(), str, android.widget.Toast.LENGTH_SHORT).show();

        //com.goteborgstad.custom.Global.ShowLog("str = " + str);

        //GetLocationFromName(str);
    }

    public void GetLocationFromName(String city_name) {
        try {

            com.goteborgstad.custom.Global.ShowLog("city_name = " + city_name);

            geocoder = new android.location.Geocoder(getActivity(), java.util.Locale.getDefault());

            addresses = geocoder.getFromLocationName(city_name, 1);

            com.goteborgstad.custom.Global.ShowLog("addresses = " + addresses.size());

            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                com.goteborgstad.custom.Global.user_longitude = "" + longitude;
                com.goteborgstad.custom.Global.user_latitude = "" + latitude;

                com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(
                        new Double(com.goteborgstad.custom.Global.user_latitude), new Double(com.goteborgstad.custom.Global.user_longitude));

                UpdateLocation(true, latLng);

                StoreData(getActivity());

            } else {
                if (city_name.contains(",")) {
                    String temp[] = city_name.split(",");
                    if (temp.length > 1) {
                        city_name = "";
                        for (int a = 0; a < temp.length-1; a++) {
                            city_name += temp[a] + ",";
                        }
                        city_name = city_name.substring(0, city_name.length()-1);
                    } else {
                        city_name = city_name.substring(0, city_name.indexOf(",")-1);
                    }

                    GetLocationFromName(city_name);
                }
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static java.util.ArrayList autocomplete(String input) {
        java.util.ArrayList resultList = null;

        java.net.HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //sb.append("&components=country:gr");
            sb.append("&sensor=true");
            sb.append("&input=" + java.net.URLEncoder.encode(input, "utf8"));

            java.net.URL url = new java.net.URL(sb.toString());
            conn = (java.net.HttpURLConnection) url.openConnection();
            java.io.InputStreamReader in = new java.io.InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (java.net.MalformedURLException e) {
            com.goteborgstad.custom.Global.ShowLog("Error processing Places API URL = " + e);
            return resultList;
        } catch (java.io.IOException e) {
            com.goteborgstad.custom.Global.ShowLog("Error connecting to Places API = " + e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            org.json.JSONObject jsonObj = new org.json.JSONObject(jsonResults.toString());
            org.json.JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new java.util.ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (org.json.JSONException e) {
            com.goteborgstad.custom.Global.ShowLog("Cannot process JSON results = " + e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends android.widget.ArrayAdapter implements android.widget.Filterable {
        private java.util.ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(android.content.Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static void StoreData(android.content.Context mContext) {
        String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(mContext, "sendmail");
        if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
            android.widget.Toast.makeText(mContext, "First select any category", android.widget.Toast.LENGTH_SHORT).show();
        } else {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                obj.put("address", autoCompView.getText().toString());
                obj.put("location", com.goteborgstad.custom.Global.user_latitude + "," + com.goteborgstad.custom.Global.user_longitude);

                com.goteborgstad.custom.Global.StoreFileToAppCache(mContext, obj.toString(), "sendmail");

            } catch (Exception ee) {}
        }
    }
}
