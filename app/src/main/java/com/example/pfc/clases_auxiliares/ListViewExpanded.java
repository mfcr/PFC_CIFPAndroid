package com.example.pfc.clases_auxiliares;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewExpanded extends ListView {
    public ListViewExpanded(Context context) {
        super(context);
    }
    public ListViewExpanded(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewExpanded(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 4, MeasureSpec.AT_MOST));
    }
}