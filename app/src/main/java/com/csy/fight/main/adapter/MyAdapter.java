package com.csy.fight.main.adapter;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csy.fight.R;

import java.util.List;

/**
 * Created by chengshengyang on 2018/12/17.
 *
 * @author chengshengyang
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CONTENT = 0;//正常内容
    private final static int TYPE_FOOTER = 1;//下拉刷新

    private Context context;
    private List<Integer> listData;

    public MyAdapter(Context context, List<Integer> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listData.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_expression_footer, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.view_expression_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {

        } else {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.textView.setText("第" + position + "行");
        }
    }


    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        private ContentLoadingProgressBar progressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pb_progress);
        }
    }
}
