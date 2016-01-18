package com.github.florent37.viewanimator;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class AnimationBuilder {

    private final ViewAnimator viewAnimator;
    private final View[] views;
    private final List<Animator> animatorList = new ArrayList<>();

    protected boolean waitForHeight;
    protected boolean nextValueWillBeDp = false;

    public AnimationBuilder(ViewAnimator viewAnimator, View...views) {
        this.viewAnimator = viewAnimator;
        this.views = views;
    }

    public AnimationBuilder dp() {
        nextValueWillBeDp = true;
        return this;
    }

    protected AnimationBuilder add(Animator animator) {
        this.animatorList.add(animator);
        return this;
    }

    protected float toDp(final float px) {
        return px / views[0].getContext().getResources().getDisplayMetrics().density;
    }

    protected float toPx(final float dp) {
        return dp * views[0].getContext().getResources().getDisplayMetrics().density;
    }

    protected float[] getValues(float...values) {
        if (!nextValueWillBeDp)
            return values;

        float[] pxValues = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            pxValues[i] = toPx(values[i]);
        }
        return pxValues;
    }

    public AnimationBuilder property(String propertyName, float... values) {
        for(View view : views) {
            this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, getValues(values)));
        }
        return this;
    }

    public AnimationBuilder translationY(float... y) {
        return property("translationY", y);
    }

    public AnimationBuilder translationX(float... x) {
        return property("translationX", x);
    }

    public AnimationBuilder alpha(float... alpha) {
        return property("alpha", alpha);
    }

    public AnimationBuilder scaleX(float... scaleX) {
        return property("scaleX", scaleX);
    }

    public AnimationBuilder scaleY(float... scaleY) {
        return property("scaleY", scaleY);
    }

    public AnimationBuilder scale(float... scale) {
        scaleX(scale);
        scaleY(scale);
        return this;
    }

    public AnimationBuilder pivotX(float pivotX) {
        for(View view : views) {
            ViewHelper.setPivotX(view, pivotX);
        }
        return this;
    }

    public AnimationBuilder pivotY(float pivotY) {
        for(View view : views) {
            ViewHelper.setPivotY(view, pivotY);
        }
        return this;
    }

    public AnimationBuilder pivotX(float... pivotX) {
        ObjectAnimator.ofFloat(getView(), "pivotX", getValues(pivotX));
        return this;
    }

    public AnimationBuilder pivotY(float... pivotY) {
        ObjectAnimator.ofFloat(getView(), "pivotY", getValues(pivotY));
        return this;
    }

    public AnimationBuilder rotationX(float... rotationX) {
        return property("rotationX", rotationX);
    }

    public AnimationBuilder rotationY(float... rotationY) {
        return property("rotationY", rotationY);
    }

    public AnimationBuilder rotation(float... rotation) {
        return property("rotation", rotation);
    }

    public AnimationBuilder backgroundColor(int... colors) {
        for(View view : views) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", colors);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            this.animatorList.add(objectAnimator);
        }
        return this;
    }

    public AnimationBuilder textColor(int... colors) {
        for(View view : views) {
            if (view instanceof TextView) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "textColor", colors);
                objectAnimator.setEvaluator(new ArgbEvaluator());
                this.animatorList.add(objectAnimator);
            }
        }
        return this;
    }

    public AnimationBuilder custom(final AnimationListener.Update update, float... values) {
        for(final View view : views) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(getValues((values)));
            if (update != null)
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override public void onAnimationUpdate(ValueAnimator animation) {
                        update.update(view, (Float) animation.getAnimatedValue());
                    }
                });
            add(valueAnimator);
        }
        return this;
    }

    public AnimationBuilder height(float... height) {
        return custom(new AnimationListener.Update() {
            @Override public void update(View view, float value) {
                view.getLayoutParams().height = (int) value;
                view.requestLayout();
            }
        }, height);
    }

    public AnimationBuilder width(float... width) {
        return custom(new AnimationListener.Update() {
            @Override public void update(View view, float value) {
                view.getLayoutParams().width = (int) value;
                view.requestLayout();
            }
        }, width);
    }

    public AnimationBuilder waitForHeight() {
        waitForHeight = true;
        return this;
    }

    protected List<Animator> createAnimators() {
        return animatorList;
    }

    //region Animate New View
    public AnimationBuilder andAnimate(View...views) {
        return viewAnimator.addAnimationBuilder(views);
    }

    public AnimationBuilder thenAnimate(View...views) {
        return viewAnimator.thenAnimate(views);
    }
    //endregion

    //region Back to ViewAnimator

    public ViewAnimator start() {
        return viewAnimator.start();
    }

    public ViewAnimator duration(long duration) {
        return viewAnimator.duration(duration);
    }

    public ViewAnimator startDelay(long startDelay) {
        return viewAnimator.startDelay(startDelay);
    }

    //region callbacks

    public ViewAnimator onStart(AnimationListener.Start start) {
        return viewAnimator.onStart(start);
    }

    public ViewAnimator onStop(AnimationListener.Stop stop) {
        return viewAnimator.onStop(stop);
    }

    public ViewAnimator onEnd(AnimationListener.Stop stop) {
        return onStop(stop);
    }

    //endregion

    //region interpolator

    public ViewAnimator interpolator(Interpolator interpolator) {
        return viewAnimator.interpolator(interpolator);
    }

    public ViewAnimator accelerate() {
        return viewAnimator.interpolator(new AccelerateInterpolator());
    }

    public ViewAnimator descelerate() {
        return viewAnimator.interpolator(new DecelerateInterpolator());
    }

    //endregion

    //endregion

    public View[] getViews() {
        return views;
    }

    public View getView() {
        return views[0];
    }

    public boolean isWaitForHeight() {
        return waitForHeight;
    }


    /**
     * 弹跳
     */
    public AnimationBuilder bounce() {
        return translationY(0, 0, -30, 0, -15, 0, 0);
    }

    /**
     * 弹跳进来
     */
    public AnimationBuilder bounceIn() {
        return alpha(0, 1, 1, 1)
                .scaleX(0.3f, 1.05f, 0.9f, 1)
                .scaleY(0.3f, 1.05f, 0.9f, 1);
    }

    /**
     * 淡入
     */
    public AnimationBuilder fadeIn() {
        return alpha(0, 0.25f, 0.5f, 0.75f, 1);
    }

    /**
     * 淡出
     */
    public AnimationBuilder fadeOut() {
        return alpha(1, 0.75f, 0.5f, 0.25f, 0);
    }

    /**
     * 闪现
     */
    public AnimationBuilder flash() {
        return alpha(1, 0, 1, 0, 1);
    }

    /**
     * 水平翻转
     */
    public AnimationBuilder flipHorizontal() {
        return rotationX(90, -15, 15, 0);
    }

    /**
     * 垂直翻转
     */
    public AnimationBuilder flipVertical() {
        return rotationY(90, -15, 15, 0);
    }

    /**
     * 脉搏
     */
    public AnimationBuilder pulse() {
        return scaleY(1, 1.1f, 1).scaleX(1, 1.1f, 1);
    }

    /**
     * 滚动进来。Only support single view
     */
    public AnimationBuilder rollIn() {
        View target = getView();
        return alpha(0, 1)
                .translationX(-(target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()), 0)
                .rotation(-120, 0);
    }

    /**
     * 滚动出去。Only support single view
     */
    public AnimationBuilder rollOut() {
        View target = getView();
        return alpha(1, 0)
                .translationX(0, target.getWidth())
                .rotation(0, 120);
    }

    /**
     * 扭转
     */
    public AnimationBuilder rubber() {
        return scaleX(1, 1.25f, 0.75f, 1.15f, 1).scaleY(1, 0.75f, 1.25f, 0.85f, 1);
    }

    /**
     * 抖动
     */
    public AnimationBuilder shake() {
        return translationX(0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
    }

    /**
     * 直立起来。Only support single view
     */
    public AnimationBuilder standUp() {
        View target = getView();
        float x = (target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()) / 2
                + target.getPaddingLeft();
        float y = target.getHeight() - target.getPaddingBottom();
        return pivotX(x, x, x, x, x)
                .pivotY(y, y, y, y, y)
                .rotationX(55, -30, 15, -15, 0);
    }

    /**
     * 摇摆
     */
    public AnimationBuilder swing() {
        return rotation(0, 10, -10, 6, -6, 3, -3, 0);
    }

    /**
     * Tada
     */
    public AnimationBuilder tada() {
        return scaleX(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1)
                .scaleY(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1)
                .rotation(0, -3, -3, 3, -3, 3, -3, 3, -3, 0);
    }

    /**
     * 波浪。Only support single view
     */
    public AnimationBuilder wave() {
        View target = getView();
        float x = (target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()) / 2
                + target.getPaddingLeft();
        float y = target.getHeight() - target.getPaddingBottom();
        return pivotX(x, x, x, x, x)
                .pivotY(y, y, y, y, y)
                .rotation(12, -12, 3, -3, 0);
    }

    /**
     * 游移不定。Only support single view
     */
    public AnimationBuilder wobble() {
        View target = getView();
        float width = target.getWidth();
        float one = (float) (width / 100.0);
        return translationX(0 * one, -25 * one, 20 * one, -15 * one, 10 * one, -5 * one, 0 * one, 0)
                .rotation(0, -5, 3, -3, 2, -1, 0);
    }

    /**
     * 缩放进入
     */
    public AnimationBuilder zoomIn() {
        return scaleX(0.45f, 1).scaleY(0.45f, 1).alpha(0, 1);
    }

    /**
     * 缩放出去
     */
    public AnimationBuilder zoomOut() {
        return scaleX(1, 0.3f, 0).scaleY(1, 0.3f, 0).alpha(1, 0, 0);
    }

}
