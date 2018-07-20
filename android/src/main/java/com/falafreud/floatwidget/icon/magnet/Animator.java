package com.falafreud.floatwidget.icon.magnet;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.ref.WeakReference;

/**
 * A class that takes care of animating a view in a simple way.
 */
public class Animator
{
    protected WeakReference<View> viewRef;
    protected int animation;

    protected Animator(View view, int anim)
    {
        this.animation = anim;
        this.viewRef = new WeakReference<>(view);
    }

    protected void startAnimation()
    {
        startAnimation(null);
    }

    protected void startAnimation(@Nullable Animation.AnimationListener listener)
    {
        viewRef.get().clearAnimation();
        Animation anim = AnimationUtils.loadAnimation(viewRef.get().getContext(), animation);
        if (listener != null)
        {
            anim.setAnimationListener(listener);
        }
        anim.setFillAfter(true);
        viewRef.get().startAnimation(anim);
    }
}
