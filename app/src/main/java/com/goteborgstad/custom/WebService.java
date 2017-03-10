package com.goteborgstad.custom;

public class WebService {

    private String backup_fileKey = "";
    private android.content.Context backup_con;
    private String backup_request = "";
    private java.io.File backup_uploadFile = null;

    public static String response = "";
    public static String message = "";

    private android.app.ProgressDialog customLoader;

    public static String domain_path =
            "http://www.avenyproduction.se/www/anmalhinder-app/services/";

    public static String base_url =
            domain_path + "webservices.php?request=";

    public static String fileUploadLink =
            domain_path + "webservices.php?";

    private myCallback mCallback;
    public static String responseJson = "";

    private String auth_username = "webservices";
    private String auth_password = "*w3BS3r^1Ces*";

    private String link = "";

    public interface myCallback {
        public void onServiceResponse(String responseJson);
    }

    public WebService(android.content.Context mContext, String request, myCallback MyCall) {

        backup_con = mContext;
        backup_request = request;
        mCallback = MyCall;

        response = "";
        message = "";

        /*if (request.contains("\"")) {
            request = request.replaceAll("\"", "%22");
        }

        if (request.contains(" ")) {
            request = request.replaceAll(" ", "%20");
        }*/

        try {
            request = java.net.URLEncoder.encode(request, "utf-8");
        } catch (Exception ee) {}

        link = base_url + request;

        customLoader = new android.app.ProgressDialog(backup_con, Global.dialog_theme);
        customLoader.setCanceledOnTouchOutside(false);
        customLoader.setCancelable(true);
        customLoader.setMessage("Var god vänta");
        customLoader.show();

        Global.ShowLog("link = " + link);

        responseJson = "";

        Runnable priceRunnable = new Runnable() {
            public void run() {

                try {

                    java.net.Authenticator.setDefault(new java.net.Authenticator() {
                        protected java.net.PasswordAuthentication getPasswordAuthentication() {
                            return new java.net.PasswordAuthentication(auth_username,
                                    auth_password.toCharArray());
                        }
                    });

                    java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(link)
                            .openConnection();
                    c.setUseCaches(false);
                    c.connect();
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(c
                            .getInputStream()));
                    String line = "";
                    StringBuffer str_buffer = new StringBuffer();

                    while ((line = in.readLine()) != null) {
                        str_buffer.append(line);
                    }

                    responseJson = str_buffer.toString();

                } catch (Exception e) {
                    // TODO: handle exception
                    responseJson = "";
                }

                android.os.Message msg = new android.os.Message();
                msg.what = 1;
                ResponseHandler.sendMessage(msg);
            }
        };
        Thread th = new Thread(null, priceRunnable, "thread");
        th.start();
    }

    private com.koushikdutta.async.future.Future<String> uploading;

    void CancelUpload() {
        uploading.cancel();
        uploading = null;
    }

    public WebService(android.content.Context mContext, String request,
                      myCallback MyCall, java.io.File uploadFile, String fileKey) {

        backup_con = mContext;
        backup_request = request;
        mCallback = MyCall;
        backup_uploadFile = uploadFile;
        backup_fileKey = fileKey;

        response = "";
        message = "";

        if (uploadFile == null) {
            new WebService(backup_con, backup_request, mCallback);
        } else {

            customLoader = new android.app.ProgressDialog(backup_con, Global.dialog_theme);
            customLoader.setCanceledOnTouchOutside(false);
            customLoader.setCancelable(true);
            customLoader.setMessage("Please Wait!");
            customLoader.setMax(100);
            customLoader.setIndeterminate(false);
            customLoader.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
            customLoader.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(android.content.DialogInterface dialog) {
                    CancelUpload();
                }
            });
            customLoader.setProgressNumberFormat("%1d KB / %2d KB");
            customLoader.show();

            responseJson = "";

            uploading = com.koushikdutta.ion.Ion.with(backup_con, fileUploadLink)
                    .setTimeout(2000000)
                    .addHeader("Authorization", "Basic "
                            + android.util.Base64.encodeToString((auth_username + ":"
                            + auth_password).getBytes(), android.util.Base64.NO_WRAP))
                    //.uploadProgressBar(progressBar)
                    .uploadProgressHandler(new com.koushikdutta.ion.ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            //txt_per.setText("" + progressBar.getProgress() + " %");

                            total = (int) total / 1024;
                            downloaded = (int) downloaded / 1024;

                            customLoader.setMax((int) total);
                            customLoader.setProgress((int) downloaded);
                        }
                    })
                    .setMultipartParameter("request", request)
                    .setMultipartFile(fileKey, uploadFile)
                    .setMultipartParameter("debug", "1")
                    .asString()
                    .setCallback(new com.koushikdutta.async.future.FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String s) {

                            responseJson = s;

                            android.os.Message msg = new android.os.Message();
                            msg.what = 1;
                            ResponseHandler.sendMessage(msg);
                        }
                    });
        }
    }

    public android.os.Handler ResponseHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    try {

                        customLoader.dismiss();
                        customLoader.dismiss();

                        Global.ShowLog("responseJson = " + responseJson);

                        if (responseJson.equalsIgnoreCase("")) {

                            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(backup_con, Global.dialog_theme);
                            dialog.setCancelable(true);
                            dialog.setMessage("Some error occurs");
                            dialog.setPositiveButton("Retry",
                                    new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(android.content.DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            new WebService(backup_con, backup_request, mCallback);
                                        }
                                    });
                            dialog.setNegativeButton("Cancel",
                                    new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(android.content.DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            dialog.show();

                        } else {
                            mCallback.onServiceResponse(responseJson);
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    super.handleMessage(msg);
            }
        }
    };
}
