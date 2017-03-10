package com.goteborgstad.fragments;

import static com.goteborgstad.custom.Global.anmal;
import static com.goteborgstad.custom.Global.anmalBottomArrows;

public class ListFragment extends android.app.Fragment {

    public static boolean isClicked = false;
    private android.widget.LinearLayout list_items;
    private android.view.LayoutInflater infater_parent, infater_child;
    private android.widget.ScrollView my_scroll;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.goteborgstad.custom.Global.ReleaseMemoryOnDestory();
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState) {

        android.view.View v = inflater.inflate(com.goteborgstad.R.layout.fragment_listing, container, false);

        com.goteborgstad.custom.Global.controllerName = "ListFragment";
        com.goteborgstad.custom.Global.fragmentBack = true;
        com.goteborgstad.custom.Global.back_tag = "list";

        com.goteborgstad.MainViewController.mDrawerLayout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        com.goteborgstad.MainViewController.logo.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_heading.setVisibility(android.view.View.VISIBLE);
        //com.goteborgstad.MainViewController.txt_heading.setText("ANMÄL HINDER");
        //com.goteborgstad.MainViewController.txt_heading.setTextColor(android.graphics.Color.parseColor("#0749A1"));

        com.goteborgstad.MainViewController.txt_heading.setGravity(android.view.Gravity.CENTER);
        com.goteborgstad.custom.Global.UpdateHeadingText(getActivity(), "STEG 1 AV 5", "VÄLJ HINDER", android.graphics.Color.parseColor("#0749A1"));

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

        com.goteborgstad.MainViewController.btn_bottom.setVisibility(android.view.View.VISIBLE);
        com.goteborgstad.MainViewController.btn_bottom.setText("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setContentDescription("NÄSTA");
        com.goteborgstad.MainViewController.btn_bottom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
                if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
                    android.widget.Toast.makeText(getActivity(), "Det är obligatoriskt att välja kategori!", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        org.json.JSONObject obj = new org.json.JSONObject(sendmail);
                        if (obj.toString().contains("category")) {
                            String category = obj.getString("category");
                            if (!category.equalsIgnoreCase("")) {
                                com.goteborgstad.custom.Global.back_tag = "input_message";

                                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                com.goteborgstad.fragments.InputMessageFragment homeFrag = new com.goteborgstad.fragments.InputMessageFragment();
                                ft.replace(com.goteborgstad.R.id.content_frame, homeFrag);
                                ft.addToBackStack(com.goteborgstad.custom.Global.back_tag);
                                ft.commit();
                            }
                        }
                    } catch (Exception ee) {}
                }
            }
        });

        com.goteborgstad.MainViewController.txt_sub_heading.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.txt_description.setTextColor(android.graphics.Color.parseColor("#ffffff"));
        com.goteborgstad.MainViewController.layout_top.setBackgroundColor(android.graphics.Color.parseColor("#0749A1"));
        com.goteborgstad.MainViewController.layout_top.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setVisibility(android.view.View.GONE);
        com.goteborgstad.MainViewController.txt_description.setVisibility(android.view.View.VISIBLE);

        com.goteborgstad.MainViewController.txt_sub_heading.setText("Enkelt avhjälpt hinder");
        com.goteborgstad.MainViewController.txt_description.setText("Välj typ av hinder i listan.\n" +
                "Detta är obligatoriskt.");

        com.goteborgstad.MainViewController.btn_right.setVisibility(android.view.View.GONE);

        my_scroll = (android.widget.ScrollView) v.findViewById(com.goteborgstad.R.id.my_scroll);
        list_items = (android.widget.LinearLayout) v.findViewById(com.goteborgstad.R.id.list_items);

        infater_parent = (android.view.LayoutInflater) getActivity().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        infater_child = (android.view.LayoutInflater) getActivity().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

        LoadItems();

        return v;
    }

    public String readFromAssets(android.content.Context context, String filename) throws java.io.IOException {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public void LoadItems() {

        org.json.JSONObject parentJsonMatched = new org.json.JSONObject();
        org.json.JSONObject childJsonMatched = new org.json.JSONObject();

        android.graphics.Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), com.goteborgstad.custom.Global.appFontNameBold);

        anmal = new java.util.ArrayList<com.goteborgstad.custom.hinderArray>();
        try {
            String jsonData = readFromAssets(getActivity(), "json_data.txt");
            org.json.JSONArray mainArr = new org.json.JSONArray(jsonData);
            boolean isArrow = false;
            for (int a = 0; a < mainArr.length(); a++) {
                org.json.JSONObject obj = new org.json.JSONObject(mainArr.get(a).toString());
                com.goteborgstad.custom.hinderArray item = new com.goteborgstad.custom.hinderArray();
                item.title = obj.getString("title");
                item.sub_cats = new java.util.ArrayList<>();
                org.json.JSONArray subCatArr = new org.json.JSONArray(obj.getString("sub_cats"));

                item.isArrow = isArrow;
                item.isDetailsOn = false;
                if (isArrow) {
                    isArrow = false;
                }
                if (subCatArr.length() > 0) {
                    isArrow = true;
                }
                for (int b = 0; b < subCatArr.length(); b++) {
                    item.sub_cats.add(subCatArr.get(b).toString());
                }
                anmal.add(item);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        com.goteborgstad.custom.Global.ShowLog("total_anmal = " + anmal.size());

        String category = "";
        String subcategory = "";

        String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
        com.goteborgstad.custom.Global.ShowLog("sendmail = " + sendmail);

        try {
            org.json.JSONObject obj = new org.json.JSONObject();
            if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
                obj = new org.json.JSONObject();
            } else {
                obj = new org.json.JSONObject(sendmail);
            }

            category = obj.getString("category");
            subcategory = obj.getString("subcategory");

        } catch (Exception ee) {}

        com.goteborgstad.custom.Global.ShowLog("category = " + category);
        com.goteborgstad.custom.Global.ShowLog("subcategory = " + subcategory);

        anmalBottomArrows = new java.util.ArrayList<android.widget.ImageView>();
        com.goteborgstad.custom.Global.anmalTopArrows = new java.util.ArrayList<android.widget.ImageView>();
        com.goteborgstad.custom.Global.anmalSubCats = new java.util.ArrayList<android.widget.LinearLayout>();
        com.goteborgstad.custom.Global.anmalTitles = new java.util.ArrayList<com.goteborgstad.custom.hinderTitleArray>();

        list_items.removeAllViews();

        for (int a = 0; a < anmal.size(); a++) {
            android.view.View v = infater_parent.inflate(com.goteborgstad.R.layout.row_parent, null);
            android.widget.ImageView img_arrow = (android.widget.ImageView) v.findViewById(com.goteborgstad.R.id.img_arrow);
            android.widget.ImageView img_arrow_bottom = (android.widget.ImageView) v.findViewById(com.goteborgstad.R.id.img_arrow_bottom);
            com.goteborgstad.custom.views.AutofitTextViewWithFont txt_title = (com.goteborgstad.custom.views.AutofitTextViewWithFont)
                    v.findViewById(com.goteborgstad.R.id.txt_title);
            android.widget.LinearLayout layout_child_items = (android.widget.LinearLayout) v.findViewById(com.goteborgstad.R.id.layout_child_items);

            if (a % 2 == 0) {
                img_arrow_bottom.setImageResource(com.goteborgstad.R.drawable.list_item_top_arrow_white);
                img_arrow.setImageResource(com.goteborgstad.R.drawable.list_divider_arrow_white);
                txt_title.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_gray);
            } else {
                img_arrow_bottom.setImageResource(com.goteborgstad.R.drawable.list_item_top_arrow_gray);
                img_arrow.setImageResource(com.goteborgstad.R.drawable.list_divider_arrow_gray);
                txt_title.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_white);
            }

            img_arrow_bottom.setVisibility(android.view.View.INVISIBLE);
            anmalBottomArrows.add(img_arrow_bottom);

            if (anmal.get(a).isArrow) {
                img_arrow.setVisibility(android.view.View.VISIBLE);
            } else {
                img_arrow.setVisibility(android.view.View.INVISIBLE);
            }

            com.goteborgstad.custom.Global.anmalTopArrows.add(img_arrow);

            txt_title.setTypeface(font);

            txt_title.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
            txt_title.setMaxTextSize(com.goteborgstad.custom.Global.maxHeadingTextSize);

            txt_title.setText(anmal.get(a).title.toUpperCase());
            try {
                org.json.JSONObject obj = new org.json.JSONObject();
                obj.put("parent", a);
                obj.put("child", 0);
                obj.put("isParent", true);

                txt_title.setTag(obj.toString());

                if (category.equalsIgnoreCase(anmal.get(a).title)) {
                    parentJsonMatched = obj;
                }

            } catch (Exception ee) {
            }

            txt_title.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {

                    isClicked = true;

                    try {

                        org.json.JSONObject obj = new org.json.JSONObject(v.getTag().toString());

                        SelectedCategory(obj);

                        ParentRowClick(obj);

                    } catch (Exception ee) {
                    }
                }
            });

            com.goteborgstad.custom.hinderTitleArray item = new com.goteborgstad.custom.hinderTitleArray();
            item.title = txt_title;
            item.sub_title = new java.util.ArrayList<>();

            boolean isOpenChild = false;

            layout_child_items.removeAllViews();
            for (int b = 0; b < anmal.get(a).sub_cats.size(); b++) {
                android.view.View v_inner = infater_child.inflate(com.goteborgstad.R.layout.row_child, null);
                com.goteborgstad.custom.views.AutofitTextViewWithFont txt_title_inner = (com.goteborgstad.custom.views.AutofitTextViewWithFont)
                        v_inner.findViewById(com.goteborgstad.R.id.txt_title);

                if (b % 2 == 0) {
                    txt_title_inner.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_gray);
                } else {
                    txt_title_inner.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_white);
                }

                txt_title_inner.setMinTextSize(com.goteborgstad.custom.Global.minTextSize);
                txt_title_inner.setMaxTextSize(com.goteborgstad.custom.Global.maxTextSize);

                String innerTitle = anmal.get(a).sub_cats.get(b);

                txt_title_inner.setText(innerTitle.toUpperCase());
                try {
                    org.json.JSONObject obj = new org.json.JSONObject();
                    obj.put("parent", a);
                    obj.put("child", b);
                    obj.put("isParent", false);

                    txt_title_inner.setTag(obj.toString());

                    if (!innerTitle.equalsIgnoreCase("")) {
                        if (subcategory.equalsIgnoreCase(innerTitle)) {
                            isOpenChild = true;
                            childJsonMatched = obj;
                        }
                    }

                } catch (Exception ee) {
                }

                txt_title_inner.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {

                        isClicked = true;

                        try {
                            org.json.JSONObject obj = new org.json.JSONObject(v.getTag().toString());
                            SelectedCategory(obj);
                        } catch (Exception ee) {
                        }
                    }
                });

                item.sub_title.add(txt_title_inner);

                layout_child_items.addView(v_inner);
            }

            if (isOpenChild) {
                layout_child_items.setVisibility(android.view.View.VISIBLE);
            } else {
                layout_child_items.setVisibility(android.view.View.GONE);
            }

            com.goteborgstad.custom.Global.anmalTitles.add(item);

            com.goteborgstad.custom.Global.anmalSubCats.add(layout_child_items);

            list_items.addView(v);
        }

        if (parentJsonMatched.toString().contains("child")) {
            SelectedCategory(parentJsonMatched);
            ParentRowClick(parentJsonMatched);
        }

        if (childJsonMatched.toString().contains("child")) {
            SelectedCategory(childJsonMatched);
        }
    }

    public void SelectedCategory(org.json.JSONObject obj) {

        for (int a = 0; a < com.goteborgstad.custom.Global.anmalTitles.size(); a++) {

            if (a % 2 == 0) {
                com.goteborgstad.custom.Global.anmalTitles.get(a).title.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_gray);
            } else {
                com.goteborgstad.custom.Global.anmalTitles.get(a).title.setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_white);
            }

            com.goteborgstad.custom.Global.anmalTitles.get(a).title.setTextColor(android.graphics.Color.parseColor("#333333"));

            boolean isGrayStart = false;
            if (a % 2 == 0) {
                isGrayStart = true;
            }

            for (int b = 0; b < com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.size(); b++) {
                if (isGrayStart) {
                    if (b % 2 == 0) {
                        com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.get(b).setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_white);
                    } else {
                        com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.get(b).setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_gray);
                    }
                } else {
                    if (b % 2 == 0) {
                        com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.get(b).setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_gray);
                    } else {
                        com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.get(b).setBackgroundResource(com.goteborgstad.R.drawable.list_row_bg_white);
                    }
                }

                com.goteborgstad.custom.Global.anmalTitles.get(a).sub_title.get(b).setTextColor(android.graphics.Color.parseColor("#333333"));
            }
        }

        try {
            boolean isParent = obj.getBoolean("isParent");
            final int parent = obj.getInt("parent");
            int child = obj.getInt("child");

            String category = "";
            String subcategory = "";

            if (isParent) {

                category = com.goteborgstad.custom.Global.anmalTitles.get(parent).title.getText().toString();

                com.goteborgstad.custom.Global.anmalTitles.get(parent).title.setBackgroundResource(com.goteborgstad.R.drawable.img_row_selected);
                com.goteborgstad.custom.Global.anmalTitles.get(parent).title.setTextColor(android.graphics.Color.parseColor("#ffffff"));

                if (com.goteborgstad.custom.Global.anmalTitles.get(parent).sub_title.size() == 0) {

                    /*com.goteborgstad.custom.Global.anmalTitles.get(parent).title.setBackgroundResource(com.goteborgstad.R.drawable.img_row_selected);
                    com.goteborgstad.custom.Global.anmalTitles.get(parent).title.setTextColor(android.graphics.Color.parseColor("#ffffff"));*/

                } else {

                    my_scroll.post(new Runnable() {
                        public void run() {
                            my_scroll.setScrollY(parent);
                        }
                    });

                    for (int b = 0; b < com.goteborgstad.custom.Global.anmalTitles.size(); b++) {
                        if (b != parent) {
                            CloseOthersSubCats(b);
                        }
                    }
                    boolean isChildVisible = anmal.get(parent).isDetailsOn;
                    if (isChildVisible) {
                        // up arrow_hide
                        com.goteborgstad.custom.Global.anmalBottomArrows.get(parent).setVisibility(android.view.View.INVISIBLE);
                    } else {
                        // up arrow_show
                        com.goteborgstad.custom.Global.anmalBottomArrows.get(parent).setVisibility(android.view.View.VISIBLE);
                    }
                }
            } else {

                /*text = com.goteborgstad.custom.Global.anmalTitles.get(parent).title.getText().toString()
                        + " - " + com.goteborgstad.custom.Global.anmalTitles.get(parent).sub_title.get(child).getText().toString();*/

                category = com.goteborgstad.custom.Global.anmalTitles.get(parent).title.getText().toString();
                subcategory = com.goteborgstad.custom.Global.anmalTitles.get(parent).sub_title.get(child).getText().toString();

                com.goteborgstad.custom.Global.anmalTitles.get(parent).sub_title.get(child).setBackgroundResource(com.goteborgstad.R.drawable.img_row_selected);
                com.goteborgstad.custom.Global.anmalTitles.get(parent).sub_title.get(child).setTextColor(android.graphics.Color.parseColor("#ffffff"));
            }

            if (isClicked) {
                isClicked = false;
                UpdateButtonText(category, subcategory);
            }

        } catch (Exception ee) {
        }
    }

    public void ParentRowClick(org.json.JSONObject obj) {
        try {
            int index = Integer.parseInt(obj.getString("parent"));
            int next_index = index + 1;
            if (anmal.get(index).sub_cats.size() > 0) {
                boolean isChildVisible = anmal.get(index).isDetailsOn;

                if (isChildVisible) {
                    com.goteborgstad.custom.Global.anmalSubCats.get(index).setVisibility(android.view.View.GONE);
                    anmal.get(index).isDetailsOn = false;

                    if (next_index < anmal.size()) {
                        com.goteborgstad.custom.Global.anmalTopArrows.get(next_index).setVisibility(android.view.View.VISIBLE);
                    }

                } else {
                    com.goteborgstad.custom.Global.anmalSubCats.get(index).setVisibility(android.view.View.VISIBLE);
                    anmal.get(index).isDetailsOn = true;

                    if (next_index < anmal.size()) {
                        com.goteborgstad.custom.Global.anmalTopArrows.get(next_index).setVisibility(android.view.View.INVISIBLE);
                    }
                }
            }
        } catch (Exception ee) {}
    }

    public void CloseOthersSubCats(int index) {
        int next_index = index + 1;
        if (anmal.get(index).sub_cats.size() > 0) {
            com.goteborgstad.custom.Global.anmalSubCats.get(index).setVisibility(android.view.View.GONE);
            anmal.get(index).isDetailsOn = false;

            if (next_index < anmal.size()) {
                com.goteborgstad.custom.Global.anmalTopArrows.get(next_index).setVisibility(android.view.View.VISIBLE);
            }
        }
    }

    public void UpdateButtonText(String category, String subcategory) {
        
        try {
            String sendmail = com.goteborgstad.custom.Global.ReadFileFromAppCache(getActivity(), "sendmail");
            com.goteborgstad.custom.Global.ShowLog("UpdateButtonText-sendmail = " + sendmail);
            org.json.JSONObject obj = new org.json.JSONObject();

            if (sendmail.equalsIgnoreCase("") || sendmail.equalsIgnoreCase("null")) {
                obj = new org.json.JSONObject();
            } else {
                obj = new org.json.JSONObject(sendmail);
            }

            obj.put("action", "sendmail");
            obj.put("category", category);
            obj.put("subcategory", subcategory);

            com.goteborgstad.custom.Global.ShowLog("sendmail = " + obj.toString());

            com.goteborgstad.custom.Global.StoreFileToAppCache(getActivity(), obj.toString(), "sendmail");

        } catch (Exception ee) {}
    }
}
