package com.csy.fight.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csy.fight.R;

import butterknife.ButterKnife;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class MessageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            setTitle();
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void setTitle() {
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.home_tab_message);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
    }
}
