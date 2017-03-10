package com.goteborgstad.fragments;

public class InputMessageFragment extends android.app.Fragment {

    private android.widget.EditText txt_message;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public InputMessageFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_input_message, container, false);

        com.goteborgstad.custom.Global.controllerName = "InputMessageFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "input_message";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "STEG 2 AV 5", "BESKRIV FELET", android.graphics.Color.parseColor("#0749A1"));

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
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#0749A1"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Beskriv på vilket sätt det hindrar dig.\nDetta är valfritt.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                String desc = txt_message.getText().toString();

                org.json.JSONObject obj = new org.json.JSONObject();
                String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

                } else {
                    try {
                        obj = new org.json.JSONObject(sendmail);
                        obj.put("desc", desc);
                    } catch (Exception ee) {}
                }

                com.goteborgstad.custom.Global.StoreFileToAppCache(getActivity(), obj.toString(), "sendmail");

                com.goteborgstad.custom.Global.back_tag = "map";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.MapFragment homeFrag = new com.goteborgstad.fragments.MapFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                ft.commit();
            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        txt_message = (android.widget.EditText) v.findViewById(com.goteborgstad.R.id.txt_input_message);
        txt_message.setHint("Beskriv hindret");

        String desc = "";
        String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
        com.goteborgstad.custom.Global.ShowLog("sendmail = " + sendmail);

        if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
            desc = "";
        } else {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                desc = obj.getString("desc");
            } catch (Exception ee) {}
        }
        txt_message.setText(desc);

        return v;
    }
}
