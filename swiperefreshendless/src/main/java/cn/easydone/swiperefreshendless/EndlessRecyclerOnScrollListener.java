package cn.easydone.swiperefreshendless;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import static android.support.v7.widget.RecyclerView.*;

public abstract class EndlessRecyclerOnScrollListener extends
        OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class
            .getSimpleName();

    private int previousTotal = 0;
    private boolean loading = true;
    int lastCompletelyVisiableItemPosition, visibleItemCount, totalItemCount;

    private int currentPage = 1;

    private LayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener(
            LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        if(mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager  = (LinearLayoutManager) mLayoutManager;
            lastCompletelyVisiableItemPosition = layoutManager .findLastCompletelyVisibleItemPosition();
        } else if(mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager  = (StaggeredGridLayoutManager) mLayoutManager;
            int[] lastPositions = layoutManager .findLastCompletelyVisibleItemPositions(new int[layoutManager .getSpanCount()]);
            lastCompletelyVisiableItemPosition = getMaxPosition(lastPositions);
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (visibleItemCount > 0)
                && (lastCompletelyVisiableItemPosition >= totalItemCount - 1)) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    /**
     * 将分页变量归零
     */
    public void reset() {
        previousTotal = 0;
        currentPage = 0;
        lastCompletelyVisiableItemPosition = 0;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    public abstract void onLoadMore(int currentPage);
}