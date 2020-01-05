package com.checkers.animation;

import android.view.ViewManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.checkers.view.FigureTag;

import java.util.function.Consumer;

public class MovingFigureAnimationListener implements Animation.AnimationListener {
    private final ImageButton from;
    private final ImageButton to;
    private final FigureTag fromTag;
    private final ImageView animationImage;
    private Consumer<Animation> onAnimationEnd;

    public MovingFigureAnimationListener(ImageButton from, ImageButton to, ImageView animationImage) {
        this.from = from;
        this.fromTag = (FigureTag) from.getTag();
        this.to = to;
        this.animationImage = animationImage;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        from.setImageResource(android.R.color.transparent);
        from.refreshDrawableState();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimationEnd.accept(animation);
        to.setTag(fromTag);
        ((ViewManager) animationImage.getParent()).removeView(animationImage);
        to.refreshDrawableState();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // no implementation
    }

    public void setOnAnimationEnd(Consumer<Animation> onAnimationFinished) {
        onAnimationEnd = onAnimationFinished;
    }
}
