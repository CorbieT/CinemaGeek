package com.bignerdranch.android.cinemaquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;

import org.xmlpull.v1.XmlPullParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FaqFragment extends Fragment {

    @BindView(R.id.faq_text_view)
    TextView mFaqTextView;

    private Unbinder unbinder;

    public static FaqFragment newInstance() {
        return new FaqFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        parseFaq();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void parseFaq() {
        try {
            XmlPullParser parser = getResources().getXml(R.xml.faq);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("faq")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        mFaqTextView.setText(parser.getText());
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}