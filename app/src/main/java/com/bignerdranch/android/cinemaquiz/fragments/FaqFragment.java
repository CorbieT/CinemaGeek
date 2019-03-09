package com.bignerdranch.android.cinemaquiz.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;

import org.xmlpull.v1.XmlPullParser;

public class FaqFragment extends Fragment{

    private TextView mFaqTextView;

    public static FaqFragment newInstance(){
        return new FaqFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_fragment, container, false);
        mFaqTextView = view.findViewById(R.id.faq_text_view);
        parseFaq();
        return view;
    }

    private void parseFaq(){
        try {
            XmlPullParser parser = getResources().getXml(R.xml.faq);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("faq")){
                    if (parser.next() == XmlPullParser.TEXT){
                        mFaqTextView.setText(parser.getText());
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
