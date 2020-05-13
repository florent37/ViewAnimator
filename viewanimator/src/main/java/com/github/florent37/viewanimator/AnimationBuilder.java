package com.github.florent37.viewanimator;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Some effects thanks https://github.com/daimajia/AndroidViewAnimations
 * and https://github.com/sd6352051/NiftyDialogEffects
 * <p>
 * Created by florentchampigny on 22/12/2015.
 * Modified by gzu-liyujiang on 24/01/2016.
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class AnimationBuilder {
    private final ViewAnimator viewAnimator;
    private final View[] views;
    private final List<Animator> animatorList = new ArrayList<>();
    private boolean waitForHeight;
    private boolean nextValueWillBeDp = false;
    private Interpolator singleInterpolator = null;

    public AnimationBuilder(ViewAnimator viewAnimator, View... views) {
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

    protected float[] getValues(float... values) {
        if (!nextValueWillBeDp) {
            return values;
        }
        float[] pxValues = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            pxValues[i] = toPx(values[i]);
        }
        return pxValues;
    }

    public AnimationBuilder property(String propertyName, float... values) {
        for (View view : views) {
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
        for (View view : views) {
            view.setPivotX(pivotX);
        }
        return this;
    }

    public AnimationBuilder pivotY(float pivotY) {
        for (View view : views) {
            view.setPivotY(pivotY);
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
        for (View view : views) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", colors);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            this.animatorList.add(objectAnimator);
        }
        return this;
    }

    public AnimationBuilder textColor(int... colors) {
        for (View view : views) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(textView, "textColor", colors);
                objectAnimator.setEvaluator(new ArgbEvaluator());
                this.animatorList.add(objectAnimator);
            }
        }
        return this;
    }

    public AnimationBuilder custom(final AnimationListener.Update update, float... values) {
        for (final View view : views) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(getValues(values));
            if (update != null)
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //noinspection unchecked
                        update.update(view, (Float) animation.getAnimatedValue());
                    }
                });
            add(valueAnimator);
        }
        return this;
    }

    public AnimationBuilder height(float... height) {
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
                view.getLayoutParams().height = (int) value;
                view.requestLayout();
            }
        }, height);
    }

    public AnimationBuilder width(float... width) {
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
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

    public AnimationBuilder andAnimate(View... views) {
        return viewAnimator.addAnimationBuilder(views);
    }

    public AnimationBuilder thenAnimate(View... views) {
        return viewAnimator.thenAnimate(views);
    }

    public AnimationBuilder duration(@IntRange(from = 1) long duration) {
        viewAnimator.duration(duration);
        return this;
    }

    public AnimationBuilder startDelay(@IntRange(from = 0) long startDelay) {
        viewAnimator.startDelay(startDelay);
        return this;
    }

    public AnimationBuilder repeatCount(@IntRange(from = -1) int repeatCount) {
        viewAnimator.repeatCount(repeatCount);
        return this;
    }

    public AnimationBuilder repeatMode(@ViewAnimator.RepeatMode int repeatMode) {
        viewAnimator.repeatMode(repeatMode);
        return this;
    }

    public AnimationBuilder onStart(AnimationListener.Start startListener) {
        viewAnimator.onStart(startListener);
        return this;
    }

    public AnimationBuilder onStop(AnimationListener.Stop stopListener) {
        viewAnimator.onStop(stopListener);
        return this;
    }

    public AnimationBuilder interpolator(Interpolator interpolator) {
        viewAnimator.interpolator(interpolator);
        return this;
    }

    public AnimationBuilder singleInterpolator(Interpolator interpolator) {
        singleInterpolator = interpolator;
        return this;
    }

    public Interpolator getSingleInterpolator() {
        return singleInterpolator;
    }

    public ViewAnimator accelerate() {
        return viewAnimator.interpolator(new AccelerateInterpolator());
    }

    public ViewAnimator decelerate() {
        return viewAnimator.interpolator(new DecelerateInterpolator());
    }

    /**
     * Start.
     */
    public ViewAnimator start() {
        viewAnimator.start();
        return viewAnimator;
    }

    public View[] getViews() {
        return views;
    }

    public View getView() {
        return views[0];
    }

    public boolean isWaitForHeight() {
        return waitForHeight;
    }

    public AnimationBuilder bounce() {
        return translationY(0, 0, -30, 0, -15, 0, 0);
    }

    public AnimationBuilder bounceIn() {
        alpha(0, 1, 1, 1);
        scaleX(0.3f, 1.05f, 0.9f, 1);
        scaleY(0.3f, 1.05f, 0.9f, 1);
        return this;
    }

    public AnimationBuilder bounceOut() {
        scaleY(1, 0.9f, 1.05f, 0.3f);
        scaleX(1, 0.9f, 1.05f, 0.3f);
        alpha(1, 1, 1, 0);
        return this;
    }

    public AnimationBuilder fadeIn() {
        return alpha(0, 0.25f, 0.5f, 0.75f, 1);
    }

    public AnimationBuilder fadeOut() {
        return alpha(1, 0.75f, 0.5f, 0.25f, 0);
    }

    public AnimationBuilder flash() {
        return alpha(1, 0, 1, 0, 1);
    }

    public AnimationBuilder flipHorizontal() {
        return rotationX(90, -15, 15, 0);
    }

    public AnimationBuilder flipVertical() {
        return rotationY(90, -15, 15, 0);
    }

    public AnimationBuilder pulse() {
        scaleY(1, 1.1f, 1);
        scaleX(1, 1.1f, 1);
        return this;
    }

    public AnimationBuilder rollIn() {
        for (View view : views) {
            alpha(0, 1);
            translationX(-(view.getWidth() - view.getPaddingLeft() - view.getPaddingRight()), 0);
            rotation(-120, 0);
        }
        return this;
    }

    public AnimationBuilder rollOut() {
        for (View view : views) {
            alpha(1, 0);
            translationX(0, view.getWidth());
            rotation(0, 120);
        }
        return this;
    }

    public AnimationBuilder rubber() {
        scaleX(1, 1.25f, 0.75f, 1.15f, 1);
        scaleY(1, 0.75f, 1.25f, 0.85f, 1);
        return this;
    }

    public AnimationBuilder shake() {
        translationX(0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        interpolator(new CycleInterpolator(5));
        return this;
    }

    public AnimationBuilder standUp() {
        for (View view : views) {
            float x = (view.getWidth() - view.getPaddingLeft() - view.getPaddingRight()) / 2
                    + view.getPaddingLeft();
            float y = view.getHeight() - view.getPaddingBottom();
            pivotX(x, x, x, x, x);
            pivotY(y, y, y, y, y);
            rotationX(55, -30, 15, -15, 0);
        }
        return this;
    }

    public AnimationBuilder swing() {
        return rotation(0, 10, -10, 6, -6, 3, -3, 0);
    }

    public AnimationBuilder tada() {
        scaleX(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        scaleY(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        rotation(0, -3, -3, 3, -3, 3, -3, 3, -3, 0);
        return this;
    }

    public AnimationBuilder wave() {
        for (View view : views) {
            float x = (view.getWidth() - view.getPaddingLeft() - view.getPaddingRight()) / 2
                    + view.getPaddingLeft();
            float y = view.getHeight() - view.getPaddingBottom();
            rotation(12, -12, 3, -3, 0);
            pivotX(x, x, x, x, x);
            pivotY(y, y, y, y, y);
        }
        return this;
    }

    public AnimationBuilder wobble() {
        for (View view : views) {
            float width = view.getWidth();
            float one = (float) (width / 100.0);
            translationX(0 * one, -25 * one, 20 * one, -15 * one, 10 * one, -5 * one, 0 * one, 0);
            rotation(0, -5, 3, -3, 2, -1, 0);
        }
        return this;
    }

    public AnimationBuilder zoomIn() {
        scaleX(0.45f, 1);
        scaleY(0.45f, 1);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder zoomOut() {
        scaleX(1, 0.3f, 0);
        scaleY(1, 0.3f, 0);
        alpha(1, 0, 0);
        return this;
    }

    public AnimationBuilder fall() {
        rotation(1080, 720, 360, 0);
        return this;
    }

    public AnimationBuilder newsPaper() {
        alpha(0, 1);
        scaleX(0.1f, 0.5f, 1);
        scaleY(0.1f, 0.5f, 1);
        return this;
    }

    public AnimationBuilder slit() {
        rotationY(90, 88, 88, 45, 0);
        alpha(0, 0.4f, 0.8f, 1);
        scaleX(0, 0.5f, 0.9f, 0.9f, 1);
        scaleY(0, 0.5f, 0.9f, 0.9f, 1);
        return this;
    }

    public AnimationBuilder slideLeftIn() {
        translationX(-300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideRightIn() {
        translationX(300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideTopIn() {
        translationY(-300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideBottomIn() {
        translationY(300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder path(Path path) {
        if (path == null) {
            return this;
        }
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
                if (view == null) {
                    return;
                }
                float[] currentPosition = new float[2];
                pathMeasure.getPosTan(value, currentPosition, null);
                final float x = currentPosition[0];
                final float y = currentPosition[1];
                view.setX(x);
                view.setY(y);
                Log.d(null, "path: value=" + value + ", x=" + x + ", y=" + y);
            }
        }, 0, pathMeasure.getLength());
    }

}
