package com.example.miraj.blakittest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miraj.blakittest.R;

import java.util.ArrayList;

public class InfoBlockFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_FIELDS = "fields";

    private String title;
    private ArrayList<Pair<String, String>> fields;

    public InfoBlockFragment() {}

    public static InfoBlockFragment newInstance(String title, ArrayList<Pair<String, String>> fields) {
        InfoBlockFragment fragment = new InfoBlockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_FIELDS, fields);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString(ARG_TITLE);
        fields = (ArrayList<Pair<String, String>>) getArguments().getSerializable(ARG_FIELDS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_block, container, false);

        if (title != null) {
            ((TextView) view.findViewById(R.id.titleText)).setText(title);
            view.findViewById(R.id.titleText).setVisibility(View.VISIBLE);
        }

        for (Pair<String, String> pair : fields)
            if (!pair.second.isEmpty())
                addFieldView((ViewGroup) view.findViewById(R.id.fieldsLayout), pair.first, pair.second);

        if (((ViewGroup) view.findViewById(R.id.fieldsLayout)).getChildCount() > 0)
            view.setVisibility(View.VISIBLE);

        return view;
    }

    protected void addFieldView(ViewGroup parentView, String key, String value) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.info_field, parentView, false);
        ((TextView) view.findViewById(R.id.keyText)).setText(key);
        ((TextView) view.findViewById(R.id.valueText)).setText(value);
        parentView.addView(view);
    }
}
