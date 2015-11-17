package norakomi.sovietposterart;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import norakomi.sovietposterart.data.DataManager;
import norakomi.sovietposterart.helpers.DataLoadingSubject;

/**
 * A scroll listener for RecyclerView to load more items as you approach the end.
 *
 * Adapted from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    // The minimum number of items remaining before we should loading more.
    private static final int VISIBLE_THRESHOLD = 5;

    private final GridLayoutManager layoutManager;
    private final DataManager dataManager;

    public InfiniteScrollListener(GridLayoutManager layoutManager, DataManager dataManager) {
        this.layoutManager = layoutManager;
        this.dataManager = dataManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (!dataManager.isDataLoading() &&
                (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

}
