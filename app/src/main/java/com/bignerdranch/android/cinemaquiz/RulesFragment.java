package com.bignerdranch.android.cinemaquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

public class RulesFragment extends Fragment{

    private TextView mRulesTextView;

    public static RulesFragment newInstance(){
        return new RulesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rules_fragment, container, false);
        mRulesTextView = view.findViewById(R.id.rules_text_view);
        parseRules();
        return view;
    }

    private void parseRules(){
        try {
            XmlPullParser parser = getResources().getXml(R.xml.rules);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("rules")){
                    if(parser.next() == XmlPullParser.TEXT){
                        mRulesTextView.setText(parser.getText());
                    }
                }
                parser.next();
            }
        }catch (Throwable t){
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
