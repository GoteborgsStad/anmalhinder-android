package com.goteborgstad.fragments;


import static com.goteborgstad.custom.Global.HideKeyBoard;
import static com.goteborgstad.custom.Global.ReleaseMemoryOnDestory;
import static com.goteborgstad.custom.Global.back_tag;

public class WebviewFragment extends android.app.Fragment {

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ReleaseMemoryOnDestory();
    }

    private android.webkit.WebView my_webview;
    private android.widget.ProgressBar loadingProgressBar;

    public WebviewFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_webview, container, false);

        com.goteborgstad.custom.Global.controllerName = "WebviewFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "webview";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "INFO", "", android.graphics.Color.parseColor("#0076BC"));

        com.goteborgstad.MainViewController.btn_back.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_back.setImageResource(com.goteborgstad.R.drawable.icon_back_arrow);
        com.goteborgstad.MainViewController.btn_back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                HideKeyBoard(getActivity(), view);

                getFragmentManager().popBackStack(back_tag,
                        android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        com.goteborgstad.MainViewController.btn_next.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_next.setText("Nästa");

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(getActivity().getResources().getColor(com.goteborgstad.R.color.header_text_color_line1));
        com.goteborgstad.MainViewController.txt_description.setTextColor(getActivity().getResources().getColor(com.goteborgstad.R.color.header_text_color_line1));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#e5e5e5"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.GONE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("SKICKA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                HideKeyBoard(getActivity(), v);

            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_right.setContentDescription("Stäng fönster");
        com.goteborgstad.MainViewController.btn_right.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                HideKeyBoard(getActivity(), view);

                getFragmentManager().popBackStack(back_tag,
                        android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        my_webview = (android.webkit.WebView) v.findViewById(com.goteborgstad.R.id.webview_browser);
        loadingProgressBar = (android.widget.ProgressBar) v.findViewById(com.goteborgstad.R.id.progressbar_Horizontal);

        my_webview.setLayerType(android.webkit.WebView.LAYER_TYPE_SOFTWARE, null);
        my_webview.getSettings().setJavaScriptEnabled(true);
        my_webview.getSettings().setPluginState(android.webkit.WebSettings.PluginState.ON);
        my_webview.getSettings().setAllowContentAccess(true);
        my_webview.getSettings().setDomStorageEnabled(true);
        my_webview.getSettings().setAppCacheEnabled(true);
        my_webview.setWebViewClient(new myWebViewClient());
        my_webview.getSettings().setAllowFileAccess(true);
        my_webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        my_webview.getSettings().setEnableSmoothTransition(true);
        my_webview.getSettings().setGeolocationEnabled(true);
        my_webview.getSettings().setLoadsImagesAutomatically(true);
        my_webview.getSettings().setLoadWithOverviewMode(true);
        my_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        my_webview.getSettings().setDefaultTextEncodingName("utf-8");
        my_webview.requestFocus(android.view.View.FOCUS_DOWN);
        my_webview.getSettings().setBuiltInZoomControls(false);
        my_webview.getSettings().setDisplayZoomControls(false);
        my_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        my_webview.getSettings().setSupportZoom(false);
        my_webview.getSettings().setUseWideViewPort(false);
        my_webview.requestFocusFromTouch();

        my_webview.loadUrl("file:///android_asset/info.html");

        my_webview.setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                loadingProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    loadingProgressBar.setVisibility(android.view.View.GONE);
                } else {
                    loadingProgressBar.setVisibility(android.view.View.VISIBLE);
                }
            }
        });

        return v;
    }

    private class myWebViewClient extends android.webkit.WebViewClient {
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

            if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url));
                startActivity(intent);

                my_webview.goBack();

            } else {
                //view.loadUrl(url);

                android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url));
                startActivity(intent);
            }

            return true;
        }
    }
}
