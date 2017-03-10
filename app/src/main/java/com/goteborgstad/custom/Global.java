package com.goteborgstad.custom;

@SuppressWarnings("ResourceType")
public class Global {

    public static String appFontName = "";//"ufonts.com_gotham-light.ttf";
    public static String appFontNameBold = "Gotham-Bold_0.ttf";

    public static int minTextSize = 8;
    public static int maxHeadingTextSize = 22;
    public static int maxTextSize = 16;

    public static String controllerName = "";
    public static boolean fragmentBack = true;
    public static String back_tag = "";

    public static int deviceWidth = 720;
    public static int deviceHeight = 1280;

    public static int dialog_theme =
            android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
            //android.app.AlertDialog.THEME_HOLO_DARK;
            //R.style.AppTheme;

    public static void ShowLog(String message) {
        //android.util.Log.d(controllerName, controllerName + "-" + message);
    }

    public static void ShowLog(String controllerName, String message) {
        //android.util.Log.d(controllerName, controllerName + "-" + message);
    }

    public static void HideKeyBoard(android.content.Context mContext, android.view.View clickedView) {
        try {
            clickedView.setContentDescription("");
        } catch (Exception ee) {ee.printStackTrace();}
        try {
            android.view.inputmethod.InputMethodManager inputManager = (android.view.inputmethod.InputMethodManager)
                    mContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(clickedView.getWindowToken(),
                    android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static boolean CheckInternetConnectivity(android.content.Context con) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager)
                con.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && (cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected())) {
            return true;
        } else {
            NetworkError(con);
            return false;
        }
    }

    public static void NetworkError(android.content.Context mContext) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext, dialog_theme);
        dialog.setCancelable(false);
        dialog.setMessage("Error in connection...");
        dialog.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public static void ServiceResponseError(android.content.Context mContext, String title, String message) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext, dialog_theme);
        dialog.setCancelable(true);
        if (!title.equalsIgnoreCase("")) {
            dialog.setTitle(title);
        }
        dialog.setMessage(message);
        dialog.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public static String ReadFileFromAppCache(android.content.Context context, String file_name) {
        String output = "";
        int ch;
        java.io.File f = new java.io.File(context.getFilesDir() + "/" + file_name);
        StringBuffer strContent = new StringBuffer("");
        java.io.FileInputStream fin = null;

        try {
            fin = new java.io.FileInputStream(f);

            while ((ch = fin.read()) != -1)
                strContent.append((char) ch);

            fin.close();
        } catch (java.io.FileNotFoundException e) {
            output = "null";
        } catch (java.io.IOException ioe) {
        }
        try {
            output = java.net.URLDecoder.decode(strContent.toString(), "UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
        }

        return output;
    }

    public static void StoreFileToAppCache(android.content.Context context, String data, String file_name) {

        try {
            String encodedValue = "";
            try {
                encodedValue = java.net.URLEncoder.encode(data, "UTF-8");
            } catch (java.io.UnsupportedEncodingException uee) {
            }
            java.io.File f = new java.io.File(context.getFilesDir() + "/" + file_name);

            java.io.FileOutputStream fop = new java.io.FileOutputStream(f, false);

            if (f.exists()) {
                fop.write(encodedValue.toString().getBytes());
                fop.flush();
                fop.close();
            } else
                System.out.println("This file is not exist");
        } catch (Exception e) {

        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * android.content.res.Resources.getSystem().getDisplayMetrics().density);
    }

    public static void ReleaseMemoryOnDestory() {
        try {
            System.gc();
        } catch (Exception ee) {
        }

        try {
            Runtime.getRuntime().gc();
        } catch (Exception ee) {
        }
    }

    public static void ReleaseMemoryOnBackButton() {
        try {
            System.gc();
        } catch (Exception ee) {
        }

        try {
            Runtime.getRuntime().gc();
        } catch (Exception ee) {
        }
    }

    public static android.graphics.Bitmap selectedBitmap = null;

    public static void RecycleBitmap(android.graphics.Bitmap bitmap) {
        try {
            if (bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static java.util.ArrayList<com.goteborgstad.custom.hinderArray> anmal = new java.util.ArrayList<hinderArray>();
    public static java.util.ArrayList<android.widget.LinearLayout> anmalSubCats = new java.util.ArrayList<android.widget.LinearLayout>();
    public static java.util.ArrayList<android.widget.ImageView> anmalTopArrows = new java.util.ArrayList<android.widget.ImageView>();
    public static java.util.ArrayList<com.goteborgstad.custom.hinderTitleArray> anmalTitles = new java.util.ArrayList<hinderTitleArray>();

    public static java.util.ArrayList<android.widget.ImageView> anmalBottomArrows = new java.util.ArrayList<android.widget.ImageView>();

    public static void UpdateHeadingText(android.content.Context mContext, String text1, String text2, int SecondTextColor) {

        if (!text2.equalsIgnoreCase("")) {

            String heading = text1 + "\n" + text2;

            android.text.SpannableString text = new android.text.SpannableString(heading);

            text.setSpan(new android.text.style.ForegroundColorSpan(mContext.getResources().getColor(com.goteborgstad.R.color.header_text_color_line1)),
                    0, text1.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            text.setSpan(new android.text.style.ForegroundColorSpan(SecondTextColor),
                    text1.length(), heading.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            com.goteborgstad.MainViewController.txt_heading.setText(text);

        } else {

            android.text.SpannableString text = new android.text.SpannableString(text1);

            text.setSpan(new android.text.style.ForegroundColorSpan(mContext.getResources().getColor(com.goteborgstad.R.color.header_text_color_line1)),
                    0, text1.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            com.goteborgstad.MainViewController.txt_heading.setText(text);
        }

        com.goteborgstad.MainViewController.txt_heading.setTextSize(mContext.getResources().getDimension(com.goteborgstad.R.dimen.text_size_title));
    }

    public static String user_latitude = "";
    public static String user_longitude = "";

    public static boolean reloadMapLocation = false;
}
