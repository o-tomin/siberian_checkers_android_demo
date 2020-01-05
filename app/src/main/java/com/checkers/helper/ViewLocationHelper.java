package com.checkers.helper;

import android.graphics.Rect;
import android.view.View;
import android.view.Window;

public class ViewLocationHelper {
    private Window window;

    public ViewLocationHelper(Window window) {
        this.window = window;
    }

    public int[] getViewLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[1] -= (getTitleBarHeight(window) + getStatusBarHeight(window));
        return location;
    }

    private static int getTitleBarHeight(Window window) {
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    private static int getStatusBarHeight(Window window) {
        return window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }
}
