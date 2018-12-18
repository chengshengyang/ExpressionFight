package com.csy.fight.main;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by chengshengyang on 2018/12/17.
 *
 * @author chengshengyang
 */
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private int countItem;
    private int lastVisibleItem;
    private boolean isScrolled = false;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    /**
     * 加载接口
     *
     * @param countItem 总数量
     * @param lastItem  最后显示的position
     */
    protected abstract void onLoading(int countItem, int lastItem);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //拖拽或者惯性滑动时isScrolled设置为true
        if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
            isScrolled = true;
        } else {
            isScrolled = false;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            countItem = staggeredGridLayoutManager.getItemCount();
            int[] lastVisibleItems = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(
                    new int[staggeredGridLayoutManager.getSpanCount()]
            );
            if (staggeredGridLayoutManager.getSpanCount() == 1) {
                lastVisibleItem = lastVisibleItems[0];
            } else if (staggeredGridLayoutManager.getSpanCount() == 2) {
                lastVisibleItem = Math.max(lastVisibleItems[0], lastVisibleItems[1]);
            } else if (staggeredGridLayoutManager.getSpanCount() == 3) {
                lastVisibleItem = Math.max(Math.max(lastVisibleItems[0], lastVisibleItems[1]), lastVisibleItems[2]);
            }
        }

        if (isScrolled && countItem != lastVisibleItem && lastVisibleItem == countItem - 1) {
            onLoading(countItem, lastVisibleItem);
        }
    }
}
