package com.falafreud.floatwidget.icon.magnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.falafreud.floatwidget.R;
import com.falafreud.floatwidget.icon.backboard.Actor;
import com.falafreud.floatwidget.icon.backboard.MotionProperty;
import com.falafreud.floatwidget.icon.backboard.imitator.Imitator;
import com.falafreud.floatwidget.icon.backboard.imitator.InertialImitator;
import com.falafreud.floatwidget.icon.backboard.performer.Performer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class Magnet implements
        View.OnTouchListener,
        View.OnClickListener,
        ViewTreeObserver.OnGlobalLayoutListener,
        SpringListener {

    private static final String TAG = "FloatWidget";

    public static Builder<Magnet> newBuilder(Context context) {

        return new MagnetBuilder(context);
    }

    /**
     * This method is called by FloatIconService.
     *
     * @param count
     */
    public void onUnreadMessageReceived(int count) {

        try {
            TextView badgeTextView = (TextView) iconView.findViewById(R.id.badge_textview);
            Log.d(TAG, "Magnet onUnreadMessageReceived: reading badge");
            if (badgeTextView != null) {
                int counter = Integer.valueOf(badgeTextView.getText().toString());
                Log.d(TAG, "Magnet onUnreadMessageReceived: " + counter);

                if (counter == 99) {
                    badgeTextView.setText("+99");
                } else {
                    badgeTextView.setText(String.valueOf(counter + count));
                    badgeTextView.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Magnet onUnreadMessageReceived: " + e.toString());
        }
    }

    private static class MagnetBuilder extends Builder<Magnet> {

        MagnetBuilder(Context context) {

            super(Magnet.class, context);
        }

        @Override
        public Magnet build() {

            return super.build();
        }
    }

    /**
     * Builder class to create your {@link Magnet}
     */
    public static class Builder<T extends Magnet> {

        protected T magnet;

        /**
         * Used to instantiate your subclass of {@link Magnet}
         *
         * @param clazz your subclass
         */
        public Builder(Class<T> clazz, @NonNull Context context) {

            final Constructor<T> constructor;
            try {
                constructor = clazz.getDeclaredConstructor(Context.class);
                constructor.setAccessible(true);
                magnet = constructor.newInstance(context);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * The Icon must have a view, provide a view or a layout using {@link #setIconView(int)}
         *
         * @param iconView the view representing the icon
         */
        public Builder<T> setIconView(@NonNull View iconView) {

            magnet.iconView = iconView;
            return this;
        }

        /**
         * Use an xml layout to provide the button view
         *
         * @param iconViewRes the layout id of the icon
         */
        public Builder<T> setIconView(@LayoutRes int iconViewRes) {

            return setIconView(LayoutInflater.from(magnet.context).inflate(iconViewRes, null));
        }

        /**
         * whether your magnet sticks to the edge your screen when you release it
         */
        public Builder<T> setShouldStickToWall(boolean shouldStick) {

            magnet.shouldStickToWall = shouldStick;
            return this;
        }

        /**
         * Whether you can fling away your Magnet towards the bottom of the screen
         *
         * @deprecated use {@link #setShouldShowRemoveView(boolean)} instead
         */
        @Deprecated
        public Builder<T> setShouldFlingAway(boolean shouldFling) {

            return this;
        }

        /**
         * Callback for when the icon moves, is clicked, is flinging away, and destroyed
         */
        public Builder<T> setIconCallback(IconCallback callback) {

            magnet.iconCallback = callback;
            return this;
        }

        /**
         * Whether the remove icon should be shown
         */
        public Builder<T> setShouldShowRemoveView(boolean showRemoveView) {

            magnet.shouldShowRemoveView = showRemoveView;
            return this;
        }

        /**
         * Whether the remove icon should respond to touch movements
         */
        public Builder<T> setRemoveIconShouldBeResponsive(boolean shouldBeResponsive) {

            magnet.removeView.shouldBeResponsive = shouldBeResponsive;
            return this;
        }

        /**
         * You can set a custom remove icon or use the default one
         */
        public Builder<T> setRemoveIconResId(int removeIconResId) {

            magnet.removeView.setIconResId(removeIconResId);
            return this;
        }

        /**
         * You can set a custom remove icon shadow or use the default one
         */
        public Builder<T> setRemoveIconShadow(int shadow) {

            magnet.removeView.setShadowBG(shadow);
            return this;
        }

        /**
         * Set the initial coordinates of the magnet in pixels
         */
        public Builder<T> setInitialPosition(int x, int y) {

            magnet.initialX = x;
            magnet.initialY = y;
            return this;
        }

        /**
         * Set a custom width for the icon view in pixels. default is {@link
         * WindowManager.LayoutParams#WRAP_CONTENT}
         */
        public Builder<T> setIconWidth(int width) {

            magnet.iconWidth = width;
            return this;
        }

        /**
         * Set a custom height for the icon view in pixels. default is {@link
         * WindowManager.LayoutParams#WRAP_CONTENT}
         */
        public Builder<T> setIconHeight(int height) {

            magnet.iconHeight = height;
            return this;
        }

        /**
         * Set the percent of the view to be hidden when the magnet touches the wall. Default is {@code
         * 0.3f}.
         */
        public Builder<T> setHideFactor(float toHideFactor) {

            magnet.hideFactor = toHideFactor;
            return this;
        }

        /**
         * Set the configuration for the springs used by this magnet.
         */
        public Builder<T> withSpringConfig(@NonNull SpringConfig springConfig) {

            magnet.springConfig = springConfig;
            return this;
        }

        public T build() {

            if (magnet.iconView == null) {
                throw new NullPointerException("IconView is null!");
            }
            return magnet;
        }
    }

    protected static double distSq(double x1, double y1, double x2, double y2) {

        return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
    }

    public double getDistance(double x1, double y1, double x2, double y2) {

        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    protected View iconView;
    protected RemoveView removeView;
    protected WindowManager windowManager;
    protected WindowManager.LayoutParams layoutParams;
    protected Context context;
    protected IconCallback iconCallback;
    protected final BroadcastReceiver orientationChangeReceiver;

    protected SpringConfig springConfig;
    protected Spring xSpring, ySpring;
    protected Actor actor;
    protected MagnetImitator motionImitatorX;
    protected MagnetImitator motionImitatorY;
    protected WindowManagerPerformer xWindowManagerPerformer;
    protected WindowManagerPerformer yWindowManagerPerformer;
    protected float hideFactor = 0.0f;
    protected float xMinValue, xMaxValue;
    protected float yMinValue, yMaxValue;
    protected int iconWidth = -1, iconHeight = -1;
    protected int initialX = -1, initialY = -1;
    protected int[] iconPosition = new int[2];

    private float lastX = 0;
    private float lastY = 0;
    private double distance = 0;
    private ImageView iconImageView = null;
    private RelativeLayout.LayoutParams iconPressedLayoutParams;
    private RelativeLayout.LayoutParams iconUnPressedLayoutParams;
    private boolean isToMove = false;

    protected boolean shouldShowRemoveView = true;
    protected float goToWallVelocity;
    protected float flingVelocityMinimum;
    protected float restVelocity;
    protected boolean shouldStickToWall = true;
    protected long lastTouchDown;
    protected boolean isBeingDragged;
    protected boolean addedToWindow;
    protected boolean isFlinging;
    protected boolean isSnapping;
    protected boolean isGoingToWall;

    public Magnet(Context context) {

        this.context = context;
        this.orientationChangeReceiver = new OrientationChangeReceiver();
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.removeView = new RemoveView(context);
        this.goToWallVelocity = pxFromDp(700);
        this.flingVelocityMinimum = pxFromDp(400);
        this.restVelocity = pxFromDp(100);
        this.springConfig = SpringConfig.fromBouncinessAndSpeed(1, 20);
    }

    @Nullable
    private Resources getResource() throws Exception {

        try {
            if (this.context != null && this.context.getResources() != null) {
                return this.context.getResources();
            }
        } catch (Exception e) {
            Log.d(TAG, "Magnet getResource: " + e.toString());
            throw e;
        }
        return null;
    }

    @NonNull
    protected SpringSystem getSpringSystem() {

        return SpringSystem.create();
    }

    @NonNull
    protected SpringConfig getSpringConfig() {

        return springConfig;
    }

    protected Spring createXSpring(SpringSystem springSystem, SpringConfig config) {

        Spring spring = springSystem.createSpring();
        spring.setSpringConfig(config);
        spring.setRestSpeedThreshold(restVelocity);
        return spring;
    }

    protected Spring createYSpring(SpringSystem springSystem, SpringConfig config) {

        Spring spring = springSystem.createSpring();
        spring.setSpringConfig(config);
        spring.setRestSpeedThreshold(restVelocity);
        return spring;
    }

    protected void initializeMotionPhysics() {

        SpringConfig config = getSpringConfig();
        SpringSystem springSystem = getSpringSystem();
        this.xSpring = createXSpring(springSystem, config);
        this.ySpring = createYSpring(springSystem, config);
        this.motionImitatorX = new MagnetImitator(
                MotionProperty.X,
                Imitator.TRACK_DELTA,
                Imitator.FOLLOW_SPRING,
                0, 0);
        this.motionImitatorY = new MagnetImitator(
                MotionProperty.Y,
                Imitator.TRACK_DELTA,
                Imitator.FOLLOW_SPRING,
                0, 0);
        this.xWindowManagerPerformer = new WindowManagerPerformer(MotionProperty.X);
        this.yWindowManagerPerformer = new WindowManagerPerformer(MotionProperty.Y);
        this.actor = new Actor.Builder(springSystem, this.iconView).addMotion(this.xSpring, this.motionImitatorX, this.xWindowManagerPerformer)
                .addMotion(this.ySpring, this.motionImitatorY, this.yWindowManagerPerformer)
                .onTouchListener(this)
                .build();
        this.iconView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        prepareIconView();
    }

    private void prepareIconView() {

//        try {
//            Log.d(TAG, "Magnet prepareIconView");
//            this.iconImageView = this.iconView.findViewById(R.id.icon_imageview);
//            RelativeLayout.LayoutParams iconLayoutParams = (RelativeLayout.LayoutParams) this.iconImageView.getLayoutParams();
//
//            this.iconPressedLayoutParams = new  RelativeLayout.LayoutParams(iconLayoutParams);
//            this.iconUnPressedLayoutParams = new RelativeLayout.LayoutParams(iconLayoutParams);
//
//            this.iconPressedLayoutParams.height = (int) pxFromDp(55);
//            this.iconPressedLayoutParams.width = (int) pxFromDp(55);
//
//            this.iconUnPressedLayoutParams.height = (int) pxFromDp(60);
//            this.iconUnPressedLayoutParams.width = (int) pxFromDp(60);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    protected int getStatusBarHeight() {

        int result = 0;
        try {
            int resourceId = getResource().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResource().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Magnet getStatusBarHeight error: " + e.toString());
        }
        return result;
    }

    protected int getNavBarHeight() {

        int result = 0;
        try {
            int resourceId = getResource().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return getResource().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.toString();
            Log.d(TAG, "Magnet getNavBarHeight error: " + e.toString());
        }
        return result;
    }

    protected void addToWindow() {

        int overlayFlag = WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                this.iconWidth > 0 ? this.iconWidth : WindowManager.LayoutParams.WRAP_CONTENT,
                this.iconHeight > 0 ? this.iconHeight : WindowManager.LayoutParams.WRAP_CONTENT,
                overlayFlag, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSPARENT);
        params.gravity = Gravity.TOP | Gravity.START;
        this.windowManager.addView(this.iconView, this.layoutParams = params);
        this.addedToWindow = true;
    }

    protected float pxFromDp(float dp) {

        try {
            return dp * getResource().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Magnet pxFromDp: " + e.toString());
        }
        return Math.round(dp);
    }

    protected boolean iconOverlapsWithRemoveView() {

        if (this.removeView.isShowing()) {
            View firstView = this.removeView.buttonImage;
            View secondView = this.iconView;
            int[] firstPosition = new int[2];
            int[] secondPosition = new int[2];

            firstView.getLocationOnScreen(firstPosition);
            secondView.getLocationOnScreen(secondPosition);

            // Rect constructor parameters: left, top, right, bottom
            Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1], firstPosition[0] + firstView.getMeasuredWidth(), firstPosition[1] + firstView.getMeasuredHeight());
            Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1], secondPosition[0] + secondView.getMeasuredWidth(), secondPosition[1] + secondView.getMeasuredHeight());

            return rectFirstView.intersect(rectSecondView);
        }
        return false;
    }

    protected void showRemoveView() {

        if (removeView != null && shouldShowRemoveView && !removeView.isShowing()) {
            removeView.show();
        }
    }

    protected void hideRemoveView() {

        if (removeView != null && shouldShowRemoveView && removeView.isShowing()) {
            removeView.hide();
        }
    }

    protected void onOrientationChange() {

        iconView.getViewTreeObserver().addOnGlobalLayoutListener(Magnet.this);
    }

    /**
     * Show the Magnet i.e. add it to the Window
     */
    public void show() {

        addToWindow();
        iconView.setOnClickListener(this);
        initializeMotionPhysics();
        setPosition(0, initialY);
        xSpring.addListener(this);
        ySpring.addListener(this);
        context.registerReceiver(orientationChangeReceiver, new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
    }

    /**
     * Move the icon to the given position
     *
     * @param x       The x coordinate to move to in pixels
     * @param y       The y coordinate to move to in pixels
     * @param animate Whether to animate to the position. This param is deprecated and will be
     *                ignored
     * @deprecated use {@link #setPosition(int, int)}
     */
    @Deprecated
    public void setPosition(int x, int y, boolean animate) {

        setPosition(x, y);
    }

    /**
     * Move the icon to the given position
     *
     * @param x The x coordinate to move to in pixels
     * @param y The y coordinate to move to in pixels
     */
    public void setPosition(int x, int y) {

        actor.removeAllListeners();
        xSpring.setEndValue(x);
        ySpring.setEndValue(y);
        actor.addAllListeners();
    }

    /**
     * Update the icon view size after the magnet has been shown
     *
     * @param width  the width of the icon view in pixels
     * @param height the height of the icon view in pixels
     */
    public void setIconSize(int width, int height) {

        iconWidth = width;
        iconHeight = height;
        if (addedToWindow) {
            layoutParams.width = width;
            layoutParams.height = height;
            windowManager.updateViewLayout(iconView, layoutParams);
        }
    }

    /**
     * Move the magnet to the nearest wall
     * See {@link Builder#setShouldStickToWall(boolean)}
     */
    public void goToWall() {

        Log.d(TAG, "Magnet goToWall");
        try {
            if (shouldStickToWall && !isGoingToWall && isToMove) {
                isGoingToWall = true;
                iconView.getLocationOnScreen(iconPosition);
                boolean endX = iconPosition[0] > getResource().getDisplayMetrics().widthPixels / 2;
                float nearestXWall = endX ? xMaxValue : xMinValue;
                actor.removeAllListeners();
                xSpring.setVelocity(iconPosition[0] > nearestXWall ? -goToWallVelocity : goToWallVelocity);
                xSpring.setEndValue(nearestXWall);
                actor.addAllListeners();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSetBadgePosition(boolean position) {

        try {
            TextView badgeTextView = (TextView) iconView.findViewById(R.id.badge_textview);
            ImageView iconImageView = (ImageView) iconView.findViewById(R.id.icon_imageview);
            if (badgeTextView != null && iconImageView != null) {
                RelativeLayout.LayoutParams badgeLayoutParams = (RelativeLayout.LayoutParams) badgeTextView.getLayoutParams();
                RelativeLayout.LayoutParams iconLayoutParams = (RelativeLayout.LayoutParams) iconImageView.getLayoutParams();
                if (badgeLayoutParams != null) {
                    int margin = Math.round(pxFromDp(40.0f));
                    int iconMargin = Math.round(pxFromDp(10.0f));
                    if (position) {
                        // right
                        badgeLayoutParams.setMargins(0, badgeLayoutParams.topMargin, margin, badgeLayoutParams.bottomMargin);
                        iconLayoutParams.setMargins(iconMargin, iconLayoutParams.topMargin, 0, iconLayoutParams.bottomMargin);
                    } else {
                        // left
                        badgeLayoutParams.setMargins(margin, badgeLayoutParams.topMargin, 0, badgeLayoutParams.bottomMargin);
                        iconLayoutParams.setMargins(0, iconLayoutParams.topMargin, iconMargin, iconLayoutParams.bottomMargin);
                    }
                    this.setBadgePosition(badgeTextView, badgeLayoutParams, iconImageView, iconLayoutParams);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Magnet setBadgePosition: " + e.toString());
        }
    }

    private void setBadgePosition(
            final TextView badgeTextView,
            RelativeLayout.LayoutParams badgeLayoutParams,
            ImageView iconImageView,
            final RelativeLayout.LayoutParams iconLayoutParams) {

        badgeTextView.setLayoutParams(badgeLayoutParams);
        iconImageView.setLayoutParams(iconLayoutParams);
    }


    /**
     * Destroys the magnet - removes the view from the WindowManager and calls
     * {@link IconCallback#onIconDestroyed()}
     */
    public void destroy() {

        try {
            actor.removeAllListeners();
            xSpring.setAtRest();
            ySpring.setAtRest();
            if (windowManager != null && iconView != null) {
                windowManager.removeView(iconView);
            }
            context.unregisterReceiver(orientationChangeReceiver);
            if (removeView != null) {
                removeView.destroy();
            }
            if (iconCallback != null) {
                iconCallback.onIconDestroyed();
            }
            context = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onIconPressed(boolean isPressed) {

        try {
//            iconImageView.setLayoutParams(isPressed ? iconPressedLayoutParams : iconUnPressedLayoutParams);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ViewTreeObserver.OnGlobalLayoutListener

    @Override
    public void onGlobalLayout() {

        try {
            xMinValue = -iconView.getMeasuredWidth() * hideFactor;
            motionImitatorX.setMinValue(xMinValue);
            xMaxValue = getResource().getDisplayMetrics().widthPixels - ((1f - hideFactor) * iconView.getMeasuredWidth());
            motionImitatorX.setMaxValue(xMaxValue);
            yMinValue = getStatusBarHeight() - iconView.getMeasuredHeight() * hideFactor;
            motionImitatorY.setMinValue(yMinValue);
            yMaxValue = getResource().getDisplayMetrics().heightPixels - getNavBarHeight() + (hideFactor * iconView.getMeasuredHeight()) - pxFromDp(120);
            motionImitatorY.setMaxValue(yMaxValue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                iconView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                iconView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            goToWall();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Magnet onGlobalLayout error: " + e.toString());
        }
    }

    // View.OnTouchListener

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Magnet onTouch ACTION_DOWN");
            this.onIconPressed(true);
            this.isBeingDragged = true;
            this.isToMove = false;
            this.lastTouchDown = System.currentTimeMillis();
            this.lastX = event.getRawX();
            this.lastY = event.getRawY();
            this.distance = 0;
            return false;

        } else if (action == MotionEvent.ACTION_MOVE) {
            this.distance = getDistance(this.lastX, this.lastY, event.getRawX(), event.getRawY());
            Log.d(TAG, "Magnet onTouch ACTION_MOVE " + this.distance);
            if (!this.isToMove && this.distance > 100) {
                this.isToMove = true;
            }
            return this.isToMove;

        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            this.onIconPressed(false);
            this.isBeingDragged = false;
            this.distance = 0;
            this.lastX = 0;
            this.lastY = 0;
            this.isToMove = false;
            Log.d(TAG, "Magnet onTouch ACTION_UP");
            return true;
        }
        return false;
    }

    // View.OnClickListener

    @Override
    public void onClick(View view) {

        xSpring.setAtRest();
        ySpring.setAtRest();
        view.getLocationOnScreen(iconPosition);
        if (iconCallback != null) {
            iconCallback.onIconClick(view, iconPosition[0], iconPosition[1]);
        }
    }

    // SpringListener

    @Override
    public void onSpringUpdate(Spring spring) {

        this.iconView.getLocationOnScreen(this.iconPosition);
        if (this.iconCallback != null) {
            this.iconCallback.onMove(this.iconPosition[0], this.iconPosition[1]);
        }
        try {
            if (this.shouldStickToWall && getResource().getDisplayMetrics() != null) {
                boolean endX = this.iconPosition[0] > getResource().getDisplayMetrics().widthPixels / 2;
                this.handleSetBadgePosition(endX);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "onSpringUpdate: error: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onSpringUpdate: error: " + e.toString());
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) {


    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    class OrientationChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            onOrientationChange();
        }
    }

    protected class MagnetImitator extends InertialImitator {

        protected MagnetImitator(@NonNull MotionProperty property, int trackStrategy, int followStrategy, double minValue, double maxValue) {

            super(property, trackStrategy, followStrategy, minValue, maxValue);
        }

        protected boolean canSnap(float x, float y) {

            if (!removeView.isShowing()) {
                return false;
            }
            View view = removeView.buttonImage;
            int[] removeViewPosition = new int[2];
            view.getLocationOnScreen(removeViewPosition);
            double distSq = distSq(x, y, removeViewPosition[0] + view.getMeasuredWidth() / 2, removeViewPosition[1] + view.getMeasuredHeight() / 2);
            return distSq < Math.pow(1.5f * view.getMeasuredWidth(), 2);
        }

        @Override
        public void constrain(MotionEvent event) {

            super.constrain(event);
            showRemoveView();
            isSnapping = false;
            isFlinging = false;
        }

        @Override
        public void release(MotionEvent event) {

            super.release(event);
            if (
                    !isGoingToWall &&
                            !isSnapping &&
                            (Math.abs(ySpring.getVelocity()) >= flingVelocityMinimum || Math.abs(xSpring.getVelocity()) >= flingVelocityMinimum))
            {
                isFlinging = true;
            }
            if (!isFlinging && !isSnapping) {
                goToWall();
                hideRemoveView();
            } else if (isSnapping) {
                if (iconOverlapsWithRemoveView()) {
                    if (mProperty == MotionProperty.Y) {
                        isSnapping = false;
                        isFlinging = false;
                        if (iconCallback != null) {
                            iconCallback.onFlingAway();
                        }
                    }
                    hideRemoveView();
                }
            } else if (isFlinging) {
                hideRemoveView();
            }
        }

        @Override
        public void imitate(final View view, @NonNull final MotionEvent event) {

//            Log.d(TAG, "Magnet imitate");

            final float viewValue;
            if (mProperty == MotionProperty.X) {
                viewValue = layoutParams.x;
            } else if (mProperty == MotionProperty.Y) {
                viewValue = layoutParams.y;
            } else {
                return;
            }
            final float eventValue = mProperty.getValue(event);
            mOffset = mProperty.getOffset(view);
            if (event.getHistorySize() > 0) {
                final float historicalValue = mProperty.getOldestValue(event);
                imitate(viewValue + mOffset, eventValue, eventValue - historicalValue, event);
            } else {
                imitate(viewValue + mOffset, eventValue, 0, event);
            }
        }

        /**
         * This function make the icon follow the finger touch.
         */
        @Override
        public void mime(float offset, float value, float delta, float dt, MotionEvent event) {

            if (iconOverlapsWithRemoveView() && canSnap(event.getRawX(), event.getRawY())) {

                isSnapping = true;
                // snap to it - remember to compensate for translation
                int[] removeViewPosition = new int[2];
                removeView.button.getLocationOnScreen(removeViewPosition);
                switch (mProperty) {
                    case X:
                        try {
                            int midPoint = getResource().getDisplayMetrics().widthPixels / 2;
                            getSpring().setEndValue(midPoint - (iconView.getWidth() / 2));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "Magnet mime error: " + e.toString());
                        }
                        break;
                    case Y:
                        getSpring().setEndValue(removeViewPosition[1] - iconView.getHeight() / 2);
                        break;
                }
            } else {
                // follow finger
                isSnapping = false;
                super.mime(offset, value, delta, dt, event);
            }
        }
    }

    protected class WindowManagerPerformer extends Performer {

        protected final MotionProperty motionProperty;

        protected WindowManagerPerformer(MotionProperty motionProperty) {

            super(null, null);
            this.motionProperty = motionProperty;
        }

        @Override
        public void onSpringUpdate(@NonNull Spring spring) {

            try {
                double currentValue = spring.getCurrentValue();
                if (motionProperty == MotionProperty.X) {
                    layoutParams.x = (int) currentValue;
                    windowManager.updateViewLayout(iconView, layoutParams);
                } else if (motionProperty == MotionProperty.Y) {
                    layoutParams.y = (int) currentValue;
                    windowManager.updateViewLayout(iconView, layoutParams);
                } else {
                    return;
                }
                if (removeView.isShowing()) {
                    removeView.onMove(layoutParams.x, layoutParams.y);
                }
                if (isFlinging && !isBeingDragged && iconOverlapsWithRemoveView()) {
                    if (motionProperty == MotionProperty.Y) {
                        isSnapping = false;
                        isFlinging = false;
                        if (iconCallback != null) {
                            iconCallback.onFlingAway();
                        }
                    }
                    hideRemoveView();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {

            super.onSpringAtRest(spring);

            isGoingToWall = false;
            if (!isSnapping && !isBeingDragged) {
                hideRemoveView();
            }
        }
    }
}