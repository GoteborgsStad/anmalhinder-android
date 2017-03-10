package com.goteborgstad.fragments;

public class ImageFragment extends android.app.Fragment {

    private com.goteborgstad.custom.views.AutofitTextViewWithFont btn_camera, btn_gallery;
    private boolean isCamera = false;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_image, container, false);

        com.goteborgstad.custom.Global.controllerName = "ImageFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "image";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "STEG 4 AV 5", "LÄGG TILL BILD", android.graphics.Color.parseColor("#2D6D70"));

        com.goteborgstad.MainViewController.btn_back.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_back.setImageResource(com.goteborgstad.R.drawable.icon_back_arrow);
        com.goteborgstad.MainViewController.btn_back.setContentDescription("Tillbaka");
        com.goteborgstad.MainViewController.btn_back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), view);

                getFragmentManager().popBackStack(com.goteborgstad.custom.Global.back_tag,
                        android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        com.goteborgstad.MainViewController.btn_next.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_next.setText("Nästa");

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.txt_description.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#2D6D70"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Ta en bild på hindret eller välj en befintlig bild. Detta är valfritt.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                com.goteborgstad.custom.Global.back_tag = "contact";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ContactFragment homeFrag = new com.goteborgstad.fragments.ContactFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                ft.commit();

            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        btn_camera = (com.goteborgstad.custom.views.AutofitTextViewWithFont) v.findViewById(com.goteborgstad.R.id.btn_camera);
        btn_gallery = (com.goteborgstad.custom.views.AutofitTextViewWithFont) v.findViewById(com.goteborgstad.R.id.btn_gallery);

        btn_camera.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        btn_camera.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        btn_gallery.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        btn_gallery.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        btn_camera.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                isCamera = true;
                CheckPermission();
            }
        });

        btn_gallery.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                isCamera = false;
                CheckPermission();
            }
        });

        return v;
    }

    public void CheckPermission() {
        try {
            int MyVersion = android.os.Build.VERSION.SDK_INT;
            if (MyVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (!checkIfAlreadyhavePermission()) {
                    requestForSpecificPermission();
                } else {
                    AfterPermission();
                }
            } else {
                AfterPermission();
            }
        } catch (Exception ee) { AfterPermission(); }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = android.support.v4.content.ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        if (result == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    //granted
                    AfterPermission();
                }
                break;
            default:
                onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private android.net.Uri imageUri = null;
    private int main_view_width_height = 350;

    public void AfterPermission() {
        if (isCamera) {

            android.content.Intent intent = new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1111);

        } else {
            choiceAvatarFromGallery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        com.goteborgstad.custom.Global.ShowLog("requestCode = " + requestCode);

        //onActivityResult(requestCode, resultCode, data);

        if (resultCode == android.app.Activity.RESULT_OK) {
            if (requestCode == 2222) {
                imageUri = data.getData();
                String[] filePathColumn = { android.provider.MediaStore.Images.Media.DATA };

                android.database.Cursor cursor = getActivity().getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                com.goteborgstad.custom.Global.ShowLog("filePath = " + filePath);

                com.goteborgstad.custom.Global.selectedBitmap = GetBitmap(new java.io.File(filePath));

                int o_width = com.goteborgstad.custom.Global.selectedBitmap.getWidth();
                int o_height = com.goteborgstad.custom.Global.selectedBitmap.getHeight();

                com.goteborgstad.custom.Global.ShowLog("o_width = " + o_width);
                com.goteborgstad.custom.Global.ShowLog("o_height = " + o_height);

                SaveFile();
            }
            if (requestCode == 1111) {
                com.goteborgstad.custom.Global.selectedBitmap = (android.graphics.Bitmap) data.getExtras().get("data");
                int o_width = com.goteborgstad.custom.Global.selectedBitmap.getWidth();
                int o_height = com.goteborgstad.custom.Global.selectedBitmap.getHeight();

                com.goteborgstad.custom.Global.ShowLog("o_width = " + o_width);
                com.goteborgstad.custom.Global.ShowLog("o_height = " + o_height);

                if (o_width > o_height) {
                    if (o_width >= main_view_width_height) {
                        float temp_w = (float)o_width/(float)main_view_width_height;
                        int scales_width = (int) main_view_width_height;
                        int scales_height = (int) Math.round(o_height/temp_w);
                        com.goteborgstad.custom.Global.selectedBitmap = android.graphics.Bitmap.createScaledBitmap(com.goteborgstad.custom.Global.selectedBitmap, scales_width, scales_height, false);
                    }
                } else {
                    if (o_height >= main_view_width_height) {
                        float temp_h = (float)o_height/(float)main_view_width_height;
                        int scales_width = (int) Math.round(o_width/temp_h);
                        int scales_height = (int) main_view_width_height;
                        com.goteborgstad.custom.Global.selectedBitmap = android.graphics.Bitmap.createScaledBitmap(com.goteborgstad.custom.Global.selectedBitmap, scales_width, scales_height, false);
                    }
                }

                o_width = com.goteborgstad.custom.Global.selectedBitmap.getWidth();
                o_height = com.goteborgstad.custom.Global.selectedBitmap.getHeight();

                com.goteborgstad.custom.Global.ShowLog("o_width = " + o_width);
                com.goteborgstad.custom.Global.ShowLog("o_height = " + o_height);

                SaveFile();
            }
        }
    }

    public void choiceAvatarFromGallery() {

        imageUri = null;

        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2222);
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

        int o_width = btmp.getWidth();
        int o_height = btmp.getHeight();
        if (o_width > o_height) {
            if (o_width >= main_view_width_height) {
                float temp_w = (float)o_width/(float)main_view_width_height;
                int scales_width = (int) main_view_width_height;
                int scales_height = (int) Math.round(o_height/temp_w);
                btmp = android.graphics.Bitmap.createScaledBitmap(btmp, scales_width, scales_height, false);
            }
        } else {
            if (o_height >= main_view_width_height) {
                float temp_h = (float)o_height/(float)main_view_width_height;
                int scales_width = (int) Math.round(o_width/temp_h);
                int scales_height = (int) main_view_width_height;
                btmp = android.graphics.Bitmap.createScaledBitmap(btmp, scales_width, scales_height, false);
            }
        }
        return btmp;
    }

    public void SaveFile() {

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

        try {
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            com.goteborgstad.custom.Global.selectedBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, bytes);
            userImage.createNewFile();
            java.io.FileOutputStream fo = new java.io.FileOutputStream(userImage);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        com.goteborgstad.custom.Global.RecycleBitmap(com.goteborgstad.custom.Global.selectedBitmap);
    }
}
