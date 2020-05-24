package com.example.a.sportsplanning.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a.sportsplanning.R;
import com.example.a.sportsplanning.evaluate.CurrentSituationActivity;

/**
 * 用户还未进行测评时的Fragment
 *   A simple {@link Fragment} subclass.
 */
public class EvaluateFragment extends Fragment {
    private Button btn;

    public EvaluateFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluate, container, false);
        btn=view.findViewById(R.id.start_evaluate);//实例化按钮控件
        //为按钮设置点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),CurrentSituationActivity.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
