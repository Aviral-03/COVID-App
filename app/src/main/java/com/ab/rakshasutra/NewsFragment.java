package com.ab.rakshasutra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.rakshasutra.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class NewsFragment extends Fragment {

    private TextView totalCountrecover, totalCountconfirm, totalCountactive;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_news, container,false);

        //Call View
        totalCountconfirm = root.findViewById(R.id.totalCountconfirm);
        totalCountrecover = root.findViewById(R.id.totalCountrecover);
        totalCountactive = root.findViewById(R.id.totalCountactive);
        // call volley
        getData();

        return root;
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://corona.lmao.ninja/v2/countries/India?yesterday=false&strict=true";

        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    totalCountrecover.setText(jsonObject.getString("recovered"));
                    totalCountconfirm.setText(jsonObject.getString("cases"));
                    totalCountactive.setText(jsonObject.getString("active"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
    });

        queue.add(stringRequest);

    }
}
