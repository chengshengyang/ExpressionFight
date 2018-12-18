package com.csy.fight.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csy.fight.R;
import com.csy.fight.main.IMainContract;
import com.csy.fight.main.OnLoadMoreListener;
import com.csy.fight.main.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class ExpressionFragment extends BaseFragment implements IMainContract.IView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MyAdapter myAdapter;
    private StaggeredGridLayoutManager recyclerViewLayoutManager;
    private Handler handler;
    private List<Integer> listData = new ArrayList<>();
    private int count = 0;

    /**
     * view必须持有presenter对象实例
     */
    protected IMainContract.IPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_expression, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            setTitle();
        }
    }

    @Override
    public void setTitle() {
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.home_tab_expression);
        }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setTitle();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        myAdapter = new MyAdapter(getActivity(), listData);
        handler = new Handler();
        recyclerViewLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void initEvent() {
        //下拉刷新的圆圈是否显示
        mSwipeRefreshLayout.setRefreshing(false);

        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light);

        //设置下拉时圆圈的背景颜色（这里设置成白色）
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        //设置下拉刷新时的操作
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //具体操作
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData("refresh");
                    }
                }, 3000);
            }
        });

        mRecyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData("loadMore");
                    }
                }, 3000);
            }
        });

        getData("reset");
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }

    private void getData(final String type) {
        if ("refresh".equals(type)) {
            listData.clear();
            count = 0;
            for (int i = 0; i < 20; i++) {
                count += 1;
                listData.add(count);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                count += 1;
                listData.add(count);
            }
        }

        myAdapter.notifyDataSetChanged();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if ("refresh".equals(type)) {
            Toast.makeText(getActivity(), "刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "加载完毕", Toast.LENGTH_SHORT).show();
        }
    }
}
