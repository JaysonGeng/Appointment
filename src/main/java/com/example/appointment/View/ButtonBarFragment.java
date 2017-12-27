package com.example.appointment.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appointment.R;

/**
 * 重写Fragment的onCreateView（）方法，加载buttonbar_fragment动态布局
 */

public class ButtonBarFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_buttonbar,container,false);
        return view;
    }

}
