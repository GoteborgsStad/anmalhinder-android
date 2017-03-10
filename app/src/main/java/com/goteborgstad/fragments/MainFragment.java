package com.goteborgstad.fragments;

public class MainFragment extends android.app.Fragment {

    private android.widget.ImageView img_title_1;
    private com.goteborgstad.custom.views.AutofitTextViewWithFont txt_title_1;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_main, container, false);

        com.goteborgstad.custom.Global.controllerName = "MainFragment";
        com.goteborgstad.custom.Global.fragmentBack = false;
        com.goteborgstad.custom.Global.back_tag = "main";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);
        //com.goteborgstad.MainViewController.txt_heading.setText("Göteborgs\nStad");

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER | android.view.Gravity.LEFT);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "Göteborgs\nStad", "", android.graphics.Color.parseColor("#0749A1"));

        com.goteborgstad.MainViewController.btn_back.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_back.setImageResource(com.goteborgstad.R.drawable.icon_back_arrow);
        com.goteborgstad.MainViewController.btn_back.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {


            }
        });

        com.goteborgstad.MainViewController.btn_next.setVisibility(android.view.View.INVISIBLE);
        com.goteborgstad.MainViewController.btn_next.setText("Nästa");

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(android.graphics.Color.parseColor("#333333"));
        com.goteborgstad.MainViewController.txt_description.setTextColor(android.graphics.Color.parseColor("#333333"));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#e5e5e5"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpta hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Här anmäler du bristande användbarhet i publika lokaler och offentlig miljö, exempelvis bibliotek eller café.\n" +
                "Välj typ av hinder i listan och ange plats. Foto och kontaktuppgift är valfritt.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("INFO");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("INFO");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.back_tag = "webview";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.WebviewFragment homeFrag = new com.goteborgstad.fragments.WebviewFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                ft.commit();
            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        img_title_1 = (android.widget.ImageView) v.findViewById(com.goteborgstad.R.id.img_title_1);
        txt_title_1 = (com.goteborgstad.custom.views.AutofitTextViewWithFont) v.findViewById(com.goteborgstad.R.id.txt_title_1);

        android.graphics.Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(),
                com.goteborgstad.custom.Global.appFontNameBold);
        txt_title_1.setTypeface(font);

        txt_title_1.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
        txt_title_1.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

        txt_title_1.setText("Anmäl hinder");

        txt_title_1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                ListFragment.isClicked = false;

                com.goteborgstad.custom.Global.back_tag = "list";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ListFragment homeFrag = new com.goteborgstad.fragments.ListFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                ft.commit();
            }
        });

        img_title_1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                ListFragment.isClicked = false;

                com.goteborgstad.custom.Global.back_tag = "list";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.ListFragment homeFrag = new com.goteborgstad.fragments.ListFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                ft.commit();
            }
        });

        return v;
    }
}
