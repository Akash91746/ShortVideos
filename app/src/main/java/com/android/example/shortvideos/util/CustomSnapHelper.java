package com.android.example.shortvideos.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class CustomSnapHelper extends PagerSnapHelper {

    private RecyclerView mRecyclerView;

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        Timber.d("Running find target snap position");
        LinearLayoutManager layout = (LinearLayoutManager) layoutManager;

        Timber.d("First last completely visible item position = %s", layout.findLastCompletelyVisibleItemPosition());
        Timber.d("First completely visible item position = %s", layout.findFirstCompletelyVisibleItemPosition());
        Timber.d("First visible item position = %s", layout.findFirstVisibleItemPosition());


        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {

        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
    }
}
