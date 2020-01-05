package com.checkers.animation;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.checkers.helper.ViewLocationHelper;
import com.checkers.view.FigureTag;

import java.util.function.Consumer;

public class AnimatedStep {
    private final ViewLocationHelper viewLocationHelper;
    private final RelativeLayout rootLayout;
    private final Consumer<Animation> onAnimationFinished;

    public AnimatedStep(RelativeLayout layout,
                        ViewLocationHelper viewLocationHelper,
                        Consumer<Animation> onAnimationFinished) {
        this.viewLocationHelper = viewLocationHelper;
        this.rootLayout = layout;
        this.onAnimationFinished = onAnimationFinished;
    }

    public void moveFigure(ImageButton from, ImageButton to) {
        FigureTag fromTag = (FigureTag) from.getTag();
        if (fromTag != null) {
            int[] fromLocation = viewLocationHelper.getViewLocationOnScreen(from);
            int[] toLocation = viewLocationHelper.getViewLocationOnScreen(to);
            ImageView toAnimate = placeImage(
                    fromLocation[0],
                    fromLocation[1],
                    from.getHeight(),
                    from.getWidth(),
                    fromTag.getDrawableId());
            int xDelta = toLocation[0] - fromLocation[0];
            int yDelta = toLocation[1] - fromLocation[1];
            TranslateAnimation animation = new TranslateAnimation(0, xDelta, 0, yDelta);
            animation.setDuration(500);
            animation.setFillAfter(false);
            MovingFigureAnimationListener animationListener = new MovingFigureAnimationListener(from, to, toAnimate);
            animationListener.setOnAnimationEnd(onAnimationFinished);
            animation.setAnimationListener(animationListener);
            toAnimate.startAnimation(animation);
        }
    }

    private ImageView placeImage(int x, int y, int height, int width, int drawableId) {
        //RelativeLayout relativeLayout = findViewById(R.id.root);
        ImageView image = new ImageView(rootLayout.getContext());
        image.setBackground(rootLayout.getContext().getDrawable(drawableId));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        rootLayout.addView(image, layoutParams);
        return image;
    }
}
