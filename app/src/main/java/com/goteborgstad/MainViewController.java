package com.goteborgstad;

import android.os.Bundle;

public class MainViewController extends android.app.Activity {

    private android.widget.RelativeLayout layout_content;
    public static com.goteborgstad.custom.views.AutofitTextViewWithFont txt_heading, btn_next, txt_sub_heading, txt_description, btn_bottom;
    public static android.widget.ImageView btn_back, logo, btn_right;
    public static android.support.v4.widget.DrawerLayout mDrawerLayout;
    public static android.widget.RelativeLayout layout_menu;
    public static android.widget.LinearLayout layout_top;

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (com.goteborgstad.custom.Global.fragmentBack) {
                getFragmentManager().popBackStack(com.goteborgstad.custom.Global.back_tag,
                        android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            //finish();
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.util.DisplayMetrics displaymetrics = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        com.goteborgstad.custom.Global.deviceHeight = displaymetrics.heightPixels;
        com.goteborgstad.custom.Global.deviceWidth = displaymetrics.widthPixels;

        com.goteborgstad.custom.Global.StoreFileToAppCache(com.goteborgstad.MainViewController.this, "" + com.goteborgstad.custom.Global.deviceHeight, "height");
        com.goteborgstad.custom.Global.StoreFileToAppCache(com.goteborgstad.MainViewController.this, "" + com.goteborgstad.custom.Global.deviceWidth, "width");

        if (com.goteborgstad.custom.Global.back_tag.equalsIgnoreCase("map")) {
            if (com.goteborgstad.custom.Global.reloadMapLocation) {
                com.goteborgstad.custom.Global.reloadMapLocation = false;
                com.goteborgstad.fragments.MapFragment.GetUserLocation(MainViewController.this);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_back = (android.widget.ImageView) findViewById(R.id.btn_back);

        layout_content = (android.widget.RelativeLayout) findViewById(R.id.layout_content);
        
        btn_bottom = (com.goteborgstad.custom.views.AutofitTextViewWithFont) findViewById(R.id.btn_bottom_main);
        txt_heading = (com.goteborgstad.custom.views.AutofitTextViewWithFont) findViewById(com.goteborgstad.R.id.txt_heading);
        btn_next = (com.goteborgstad.custom.views.AutofitTextViewWithFont) findViewById(com.goteborgstad.R.id.btn_next);
        logo = (android.widget.ImageView) findViewById(R.id.logo);

        btn_right = (android.widget.ImageView) findViewById(R.id.btn_right);

        layout_top = (android.widget.LinearLayout) findViewById(R.id.layout_top);
        txt_sub_heading = (com.goteborgstad.custom.views.AutofitTextViewWithFont) findViewById(com.goteborgstad.R.id.txt_sub_heading);
        txt_description = (com.goteborgstad.custom.views.AutofitTextViewWithFont) findViewById(com.goteborgstad.R.id.txt_description);

        android.graphics.Typeface font = android.graphics.Typeface.createFromAsset(getAssets(), com.goteborgstad.custom.Global.appFontNameBold);
        txt_sub_heading.setTypeface(font);
        txt_heading.setTypeface(font);

        layout_menu = (android.widget.RelativeLayout) findViewById(R.id.layout_menu);
        mDrawerLayout = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout);

        txt_heading.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        txt_heading.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        btn_next.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        btn_next.setMaxTextSize(com.goteborgstad.custom.Global.maxTextSize);

        txt_sub_heading.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        txt_sub_heading.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        txt_description.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        txt_description.setMaxTextSize(com.goteborgstad.custom.Global.maxTextSize);

        txt_heading.setVisibility(android.view.View.INVISIBLE);
        btn_next.setVisibility(android.view.View.INVISIBLE);
        btn_back.setVisibility(android.view.View.INVISIBLE);
        logo.setVisibility(android.view.View.GONE);
        btn_bottom.setVisibility(android.view.View.GONE);
        btn_right.setVisibility(android.view.View.INVISIBLE);

        mDrawerLayout.closeDrawer(layout_menu);
        mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        /*layout_content.getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // TODO Auto-generated method stub

                        try {

                            android.graphics.Rect r = new android.graphics.Rect();
                            layout_content.getWindowVisibleDisplayFrame(r);
                            int screenHeight = layout_content.getRootView().getHeight();
                            int heightDifference = screenHeight - (r.bottom - r.top);

                            android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
                                    android.widget.RelativeLayout.LayoutParams.FILL_PARENT,
                                    android.widget.RelativeLayout.LayoutParams.FILL_PARENT);

                            if (heightDifference > com.goteborgstad.custom.Global.dpToPx(26)) {
                                heightDifference = heightDifference - com.goteborgstad.custom.Global.dpToPx(80);
                                lp.topMargin = (-1)*com.goteborgstad.custom.Global.dpToPx(80);
                            }

                            lp.bottomMargin = heightDifference;
                            layout_content.setLayoutParams(lp);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                });*/

        CheckPermission();
    }

    public void CheckPermission() {
        int MyVersion = android.os.Build.VERSION.SDK_INT;
        if (MyVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
                ActivityCode();
            }
        } else {
            ActivityCode();
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = android.support.v4.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET, android.Manifest.permission.LOCATION_HARDWARE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INSTALL_LOCATION_PROVIDER
        }, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    //granted
                    ActivityCode();
                } else {
                    //not granted
                    CheckPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void ActivityCode() {

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        com.goteborgstad.fragments.MainFragment homeFrag = new com.goteborgstad.fragments.MainFragment();
        ft.replace(R.id.content_frame, homeFrag);
        ft.addToBackStack("");
        ft.commit();

    }
}
