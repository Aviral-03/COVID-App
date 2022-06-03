package com.ab.rakshasutra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.rakshasutra.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotspotFragment extends Fragment {

    public HotspotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotspot, container, false);
    }
}
