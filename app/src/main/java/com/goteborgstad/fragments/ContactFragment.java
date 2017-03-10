package com.goteborgstad.fragments;

import static com.goteborgstad.custom.Global.back_tag;

public class ContactFragment extends android.app.Fragment {

    private android.widget.EditText txt_name, txt_email, txt_phone;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public ContactFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_contact, container, false);

        com.goteborgstad.custom.Global.controllerName = "ContactFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "contact";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "STEG 5 AV 5", "KONTAKTUPPGIFTER", android.graphics.Color.parseColor("#B50000"));

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
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#B50000"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Det är frivilligt att lämna kontaktuppgifter men nödvändigt om du vill få återkoppling.");

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setTextColor(android.graphics.Color.WHITE);
        com.goteborgstad.MainViewController.btn_bottom.setText("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                com.goteborgstad.custom.Global.HideKeyBoard(getActivity(), v);

                String name = txt_name.getText().toString();
                String email = txt_email.getText().toString();
                String phone = txt_phone.getText().toString();


                try {

                    String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                    if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

                    } else {
                        org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                        obj.put("name", name);
                        obj.put("email", email);
                        obj.put("phone", phone);

                        com.goteborgstad.custom.Global.StoreFileToAppCache(getActivity(), obj.toString(), "sendmail");
                    }

                } catch (Exception ee) {}

                com.goteborgstad.custom.Global.back_tag = "summery";

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                com.goteborgstad.fragments.SummeryFragment homeFrag = new com.goteborgstad.fragments.SummeryFragment();
                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                ft.addToBackStack(back_tag);
                ft.commit();
            }
        });

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        txt_name = (android.widget.EditText) v.findViewById(com.goteborgstad.R.id.txt_name);
        txt_email = (android.widget.EditText) v.findViewById(com.goteborgstad.R.id.txt_email);
        txt_phone = (android.widget.EditText) v.findViewById(com.goteborgstad.R.id.txt_phone);

        String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
        if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {

        } else {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                txt_name.setText(obj.getString("name"));
                txt_email.setText(obj.getString("email"));
                txt_phone.setText(obj.getString("phone"));

            } catch (Exception ee) {}
        }

        return v;
    }
}
