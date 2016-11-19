package com.bk.hica17.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by trung on 17/11/2016.
 */
public class SpaceItem extends RecyclerView.ItemDecoration {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    public static final int GRID = 3;
    public static final int VERTICAL_NORMAL = 4;

    private int space;
    private int type;

    public SpaceItem(int space, int type) {
        this.space = space;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        if (type == VERTICAL) {

            outRect.left = 0;
            outRect.right = 0;

            if (position == 0) {
                outRect.top = 0;
                outRect.bottom = space;

            } else {
                outRect.top = space;
                outRect.bottom = space;
            }
        } else if (type == VERTICAL_NORMAL) {

            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = space;
            if (position == 0) {
                outRect.top = 2 * space;
            } else {
                outRect.top = space;
            }
        }
    }
}
