package com.goteborgstad.fragments;

import static com.goteborgstad.R.id.map;
import static com.goteborgstad.custom.Global.back_tag;
import static com.goteborgstad.custom.Global.dialog_theme;

public class SummeryFragment extends android.app.Fragment {

    private com.google.android.gms.maps.MapView mMapView = null;
    private com.google.android.gms.maps.GoogleMap googleMap = null;

    private com.goteborgstad.custom.WebService mWebService;
    private com.goteborgstad.custom.WebService.myCallback mCallback;
    private android.widget.ImageView img_user_pic;
    
    private com.goteborgstad.custom.views.AutofitTextViewWithFont txt_title_1, txt_title_4;
    private android.widget.RelativeLayout layout_click1, layout_click2, layout_click3, layout_click4;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public SummeryFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_summery, container, false);

        com.goteborgstad.custom.Global.controllerName = "SummeryFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "summery";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "SAMMANFATTNING", "Detta har du fyllt i. ", android.graphics.Color.parseColor("#0076BC"));

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

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(getActivity().getResources().getColor(com.goteborgstad.R.color.header_text_color_line1));
        com.goteborgstad.MainViewController.txt_description.setTextColor(getActivity().getResources().getColor(com.goteborgstad.R.color.header_text_color_line1));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#e5e5e5"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Du kan ändra svaren om du vill.\n" +
                "Skicka sedan in anmälan.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("SKICKA");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("SKICKA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                try {

                    String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                    com.goteborgstad.custom.Global.ShowLog("sendmail = " + sendmail);

                    /*org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                    String address = obj.getString("address");
                    address = address.replaceAll("", "");
                    obj.put("address", address);
                    sendmail = obj.toString();*/

                    java.io.File root = new java.io.File(android.os.Environment.getExternalStorageDirectory(),
                            getString(com.goteborgstad.R.string.app_name));
                    if (!root.exists()) {
                        root.mkdir();
                    }
                    java.io.File resFolder = new java.io.File(root, ".res");
                    if (!resFolder.exists()) {
                        resFolder.mkdir();
                    }
                    java.io.File userImage = new java.io.File(resFolder, "user.jpg");
                    com.goteborgstad.custom.Global.ShowLog("userImage = " + userImage.exists());

                    if (com.goteborgstad.custom.Global.CheckInternetConnectivity(getActivity())) {
                        if (userImage.exists()) {
                            mWebService = new com.goteborgstad.custom.WebService(getActivity(), sendmail, mCallback, userImage, "image");
                        } else {
                            mWebService = new com.goteborgstad.custom.WebService(getActivity(), sendmail, mCallback);
                        }
                    }

                } catch (Exception ee) {}
            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        layout_click1 = (android.widget.RelativeLayout) v.findViewById(com.goteborgstad.R.id.layout_click1);
        layout_click2 = (android.widget.RelativeLayout) v.findViewById(com.goteborgstad.R.id.layout_click2);
        layout_click3 = (android.widget.RelativeLayout) v.findViewById(com.goteborgstad.R.id.layout_click3);
        layout_click4 = (android.widget.RelativeLayout) v.findViewById(com.goteborgstad.R.id.layout_click4);

        txt_title_1 = (com.goteborgstad.custom.views.AutofitTextViewWithFont) v.findViewById(com.goteborgstad.R.id.txt_title_1);
        txt_title_4 = (com.goteborgstad.custom.views.AutofitTextViewWithFont) v.findViewById(com.goteborgstad.R.id.txt_title_4);
        img_user_pic = (android.widget.ImageView) v.findViewById(com.goteborgstad.R.id.img_user_pic);
        mMapView = (com.google.android.gms.maps.MapView) v.findViewById(map);

        txt_title_1.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        txt_title_4.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);

        txt_title_1.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);
        txt_title_4.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        mCallback = new com.goteborgstad.custom.WebService.myCallback() {
            @Override
            public void onServiceResponse(String responseJson) {

                com.goteborgstad.custom.Global.ShowLog("responseJson = " + responseJson);

                try {
                    org.json.JSONObject mainObj = new org.json.JSONObject(responseJson);
                    com.goteborgstad.custom.WebService.response = mainObj.getString("response");
                    com.goteborgstad.custom.WebService.message = mainObj.getString("message");

                    if (com.goteborgstad.custom.WebService.response.equalsIgnoreCase("success")) {

                        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity(), dialog_theme);
                        dialog.setCancelable(false);
                        dialog.setMessage(com.goteborgstad.custom.WebService.message);
                        dialog.setPositiveButton("OK",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(android.content.DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        java.io.File root = new java.io.File(android.os.Environment.getExternalStorageDirectory(),
                                                getString(com.goteborgstad.R.string.app_name));
                                        if (!root.exists()) {
                                            root.mkdir();
                                        }
                                        java.io.File resFolder = new java.io.File(root, ".res");
                                        if (!resFolder.exists()) {
                                            resFolder.mkdir();
                                        }

                                        DeleteRecursive(resFolder);

                                        String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                                        if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

                                        } else {
                                            try {
                                                org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                                                String name = obj.getString("name");
                                                String email = obj.getString("email");
                                                String phone = obj.getString("phone");

                                                obj = new org.json.JSONObject();
                                                obj.put("name", name);
                                                obj.put("email", email);
                                                obj.put("phone", phone);

                                                com.goteborgstad.custom.Global.StoreFileToAppCache(getActivity(), obj.toString(), "sendmail");

                                            } catch (Exception ee) {}
                                        }

                                        android.content.Intent in = new android.content.Intent();
                                        in.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        in.setClass(getActivity(), com.goteborgstad.MainViewController.class);
                                        startActivity(in);

                                        getActivity().finish();
                                    }
                                });
                        dialog.show();

                    } else {
                        com.goteborgstad.custom.Global.ServiceResponseError(getActivity(), "", com.goteborgstad.custom.WebService.message);
                    }

                } catch (Exception ee) {}
            }
        };
        
        try {
            String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
            com.goteborgstad.custom.Global.ShowLog("sendmail = " + sendmail);
            if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

            } else {
                org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                String category = obj.getString("category");
                String subcategory = obj.getString("subcategory");
                //String description = obj.getString("description");
                String address = obj.getString("address");
                //String location = obj.getString("location");
                String name = obj.getString("name");
                //String email = obj.getString("email");
                //String phone = obj.getString("phone");

                String cat_name = "";
                if (subcategory.equalsIgnoreCase("")) {
                    cat_name = category;
                } else {
                    cat_name = subcategory;
                }
                txt_title_1.setText(cat_name);

                if (name.equalsIgnoreCase("")) {
                    name = "KONTAKTUPPGIFTER";
                }
                txt_title_4.setText(name);

                layout_click1.setContentDescription(cat_name);
                layout_click2.setContentDescription(address);
                layout_click3.setContentDescription("Lägg till bild");
                layout_click4.setContentDescription(name);
            }

        } catch (Exception ee) {}

        java.io.File root = new java.io.File(android.os.Environment.getExternalStorageDirectory(),
                getString(com.goteborgstad.R.string.app_name));
        if (!root.exists()) {
            root.mkdir();
        }
        java.io.File resFolder = new java.io.File(root, ".res");
        if (!resFolder.exists()) {
            resFolder.mkdir();
        }
        java.io.File userImage = new java.io.File(resFolder, "user.jpg");
        com.goteborgstad.custom.Global.ShowLog("userImage = " + userImage.exists());

        if (userImage.exists()) {
            img_user_pic.setImageBitmap(GetBitmap(userImage));
        } else {
            img_user_pic.setImageBitmap(null);
        }

        LoadGoogleMap(savedInstanceState);

        layout_click1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                back_tag = "list";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ListFragment homeFrag = new com.goteborgstad.fragments.ListFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(back_tag);
                ft.commit();

            }
        });

        layout_click2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                back_tag = "map";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.MapFragment homeFrag = new com.goteborgstad.fragments.MapFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(back_tag);
                ft.commit();
            }
        });

        layout_click3.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                back_tag = "image";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ImageFragment homeFrag = new com.goteborgstad.fragments.ImageFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(back_tag);
                ft.commit();
            }
        });

        layout_click4.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                back_tag = "contact";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ContactFragment homeFrag = new com.goteborgstad.fragments.ContactFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(back_tag);
                ft.commit();
            }
        });

        return v;
    }

    void DeleteRecursive(java.io.File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (java.io.File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);
        fileOrDirectory.delete();
    }

    public android.graphics.Bitmap GetBitmap(java.io.File img_file) {

        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inTempStorage = new byte[16*1024];
        options.inPurgeable = true;

        android.graphics.Bitmap btmp = android.graphics.BitmapFactory.decodeFile(img_file.getAbsolutePath(), options);

        android.media.ExifInterface exif;
        try {

            exif = new android.media.ExifInterface(img_file.getAbsolutePath());
            int exifOrientation = exif
                    .getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION,
                            android.media.ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = btmp.getWidth();
                int h = btmp.getHeight();

                // Setting pre rotate
                android.graphics.Matrix mtx = new android.graphics.Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                btmp = android.graphics.Bitmap.createBitmap(btmp, 0, 0, w, h, mtx, false);
            }

        } catch (Exception e) {

        }
        return btmp;
    }

    public void LoadGoogleMap(final android.os.Bundle savedInstanceState) {
        try {

            if (back_tag.equalsIgnoreCase("summery")) {
                mMapView.onCreate(savedInstanceState);

                mMapView.onResume();
            }

            Runnable myRun = new Runnable() {
                @Override
                public void run() {
                    if (back_tag.equalsIgnoreCase("summery")) {
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

                    if (back_tag.equalsIgnoreCase("summery")) {
                        mMapView.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
                            @Override
                            public void onMapReady(com.google.android.gms.maps.GoogleMap gMap) {
                                googleMap = gMap;

                                try {
                                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                                    googleMap.getUiSettings().setTiltGesturesEnabled(false);
                                    googleMap.getUiSettings().setScrollGesturesEnabled(false);

                                } catch (Exception ee) {}

                                try {

                                    String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                                    if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

                                    } else {
                                        try {
                                            org.json.JSONObject obj = new org.json.JSONObject(sendmail);

                                            String location = obj.getString("location");

                                            if (location.contains(",")) {
                                                String[] coordinates = location.split(",");
                                                com.goteborgstad.custom.Global.user_latitude = coordinates[0];
                                                com.goteborgstad.custom.Global.user_longitude = coordinates[1];
                                            } else {
                                                com.goteborgstad.custom.Global.user_latitude = "0.0000";
                                                com.goteborgstad.custom.Global.user_longitude = "0.0000";
                                            }

                                            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(
                                                    new Double(com.goteborgstad.custom.Global.user_latitude), new Double(com.goteborgstad.custom.Global.user_longitude));

                                            UpdateLocation(true, latLng);

                                        } catch (Exception ee) {}
                                    }

                                } catch (Exception ee) {}

                            }
                        });
                    }

                    super.handleMessage(msg);
            }
        }
    };

    public void UpdateLocation(boolean isZoom, com.google.android.gms.maps.model.LatLng latLng) {

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
}
