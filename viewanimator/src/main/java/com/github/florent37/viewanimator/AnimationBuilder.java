package com.github.florent37.viewanimator;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 * Modified by gzu-liyujiang on 24/01/2016.
 */
public class AnimationBuilder {
    private final ViewAnimator viewAnimator;
    private final View[] views;
    private final List<Animator> animatorList = new ArrayList<>();
    private boolean waitForHeight;
    private boolean nextValueWillBeDp = false;
    private Interpolator singleInterpolator = null;

    /**
     * Instantiates a new Animation builder.
     *
     * @param viewAnimator the view animator
     * @param views        the views
     */
    public AnimationBuilder(ViewAnimator viewAnimator, View... views) {
        this.viewAnimator = viewAnimator;
        this.views = views;
    }

    /**
     * Dp animation builder.
     *
     * @return the animation builder
     */
    public AnimationBuilder dp() {
        nextValueWillBeDp = true;
        return this;
    }

    /**
     * Add animation builder.
     *
     * @param animator the animator
     * @return the animation builder
     */
    protected AnimationBuilder add(Animator animator) {
        this.animatorList.add(animator);
        return this;
    }

    /**
     * To dp float.
     *
     * @param px the px
     * @return the float
     */
    protected float toDp(final float px) {
        return px / views[0].getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * To px float.
     *
     * @param dp the dp
     * @return the float
     */
    protected float toPx(final float dp) {
        return dp * views[0].getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * Get values float [ ].
     *
     * @param values the values
     * @return the float [ ]
     */
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

    /**
     * Property animation builder.
     *
     * @param propertyName the property name
     * @param values       the values
     * @return the animation builder
     */
    public AnimationBuilder property(String propertyName, float... values) {
        for (View view : views) {
            this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, getValues(values)));
        }
        return this;
    }

    /**
     * Translation y animation builder.
     *
     * @param y the y
     * @return the animation builder
     */
    public AnimationBuilder translationY(float... y) {
        return property("translationY", y);
    }

    /**
     * Translation x animation builder.
     *
     * @param x the x
     * @return the animation builder
     */
    public AnimationBuilder translationX(float... x) {
        return property("translationX", x);
    }

    /**
     * Alpha animation builder.
     *
     * @param alpha the alpha
     * @return the animation builder
     */
    public AnimationBuilder alpha(float... alpha) {
        return property("alpha", alpha);
    }

    /**
     * Scale x animation builder.
     *
     * @param scaleX the scale x
     * @return the animation builder
     */
    public AnimationBuilder scaleX(float... scaleX) {
        return property("scaleX", scaleX);
    }

    /**
     * Scale y animation builder.
     *
     * @param scaleY the scale y
     * @return the animation builder
     */
    public AnimationBuilder scaleY(float... scaleY) {
        return property("scaleY", scaleY);
    }

    /**
     * Scale animation builder.
     *
     * @param scale the scale
     * @return the animation builder
     */
    public AnimationBuilder scale(float... scale) {
        scaleX(scale);
        scaleY(scale);
        return this;
    }

    /**
     * Pivot x animation builder.
     *
     * @param pivotX the pivot x
     * @return the animation builder
     */
    public AnimationBuilder pivotX(float pivotX) {
        for (View view : views) {
            ViewCompat.setPivotX(view, pivotX);
        }
        return this;
    }

    /**
     * Pivot y animation builder.
     *
     * @param pivotY the pivot y
     * @return the animation builder
     */
    public AnimationBuilder pivotY(float pivotY) {
        for (View view : views) {
            ViewCompat.setPivotY(view, pivotY);
        }
        return this;
    }

    /**
     * Rotation x animation builder.
     *
     * @param pivotX the rotation x
     * @return the animation builder
     */
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

    /**
     * Rotation y animation builder.
     *
     * @param rotationY the rotation y
     * @return the animation builder
     */
    public AnimationBuilder rotationY(float... rotationY) {
        return property("rotationY", rotationY);
    }

    /**
     * Rotation animation builder.
     *
     * @param rotation the rotation
     * @return the animation builder
     */
    public AnimationBuilder rotation(float... rotation) {
        return property("rotation", rotation);
    }

    /**
     * Background color animation builder.
     *
     * @param colors the colors
     * @return the animation builder
     */
    public AnimationBuilder backgroundColor(int... colors) {
        for (View view : views) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", colors);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            this.animatorList.add(objectAnimator);
        }
        return this;
    }

    /**
     * Text color animation builder.
     *
     * @param colors the colors
     * @return the animation builder
     */
    public AnimationBuilder textColor(int... colors) {
        for (View view : views) {
            if (view instanceof TextView) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "textColor", colors);
                objectAnimator.setEvaluator(new ArgbEvaluator());
                this.animatorList.add(objectAnimator);
            }
        }
        return this;
    }

    /**
     * Custom animation builder.
     *
     * @param update the update
     * @param values the values
     * @return the animation builder
     */
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

    /**
     * Height animation builder.
     *
     * @param height the height
     * @return the animation builder
     */
    public AnimationBuilder height(float... height) {
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
                view.getLayoutParams().height = (int) value;
                view.requestLayout();
            }
        }, height);
    }

    /**
     * Width animation builder.
     *
     * @param width the width
     * @return the animation builder
     */
    public AnimationBuilder width(float... width) {
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
                view.getLayoutParams().width = (int) value;
                view.requestLayout();
            }
        }, width);
    }

    /**
     * Wait for height animation builder.
     *
     * @return the animation builder
     */
    public AnimationBuilder waitForHeight() {
        waitForHeight = true;
        return this;
    }

    /**
     * Create animators list.
     *
     * @return the list
     */
    protected List<Animator> createAnimators() {
        return animatorList;
    }

    /**
     * And animate animation builder.
     *
     * @param views the views
     * @return the animation builder
     */
    public AnimationBuilder andAnimate(View... views) {
        return viewAnimator.addAnimationBuilder(views);
    }

    /**
     * Then animate animation builder.
     *
     * @param views the views
     * @return the animation builder
     */
    public AnimationBuilder thenAnimate(View... views) {
        return viewAnimator.thenAnimate(views);
    }

    /**
     * Duration view animator.
     *
     * @param duration the duration
     * @return the animation builder
     */
    public AnimationBuilder duration(long duration) {
        viewAnimator.duration(duration);
        return this;
    }

    /**
     * Start delay view animator.
     *
     * @param startDelay the start delay
     * @return the animation builder
     */
    public AnimationBuilder startDelay(long startDelay) {
        viewAnimator.startDelay(startDelay);
        return this;
    }

    /**
     * Repeat count of animation.
     *
     * @param repeatCount the repeat count
     * @return the animation builder
     */
    public AnimationBuilder repeatCount(@IntRange(from = -1) int repeatCount) {
        viewAnimator.repeatCount(repeatCount);
        return this;
    }

    /**
     * Repeat mode view animation.
     *
     * @param repeatMode the repeat mode
     * @return the animation builder
     */
    public AnimationBuilder repeatMode(@ViewAnimator.RepeatMode int repeatMode) {
        viewAnimator.repeatMode(repeatMode);
        return this;
    }

    /**
     * On start view animator.
     *
     * @param startListener the start listener
     * @return the animation builder
     */
    public AnimationBuilder onStart(AnimationListener.Start startListener) {
        viewAnimator.onStart(startListener);
        return this;
    }

    /**
     * On stop view animator.
     *
     * @param stopListener the stop listener
     * @return the animation builder
     */
    public AnimationBuilder onStop(AnimationListener.Stop stopListener) {
        viewAnimator.onStop(stopListener);
        return this;
    }

    /**
     * Interpolator view animator.
     *
     * @param interpolator the interpolator
     * @return the animation builder
     */
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

    /**
     * Get views view [ ].
     *
     * @return the view [ ]
     */
    public View[] getViews() {
        return views;
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public View getView() {
        return views[0];
    }

    /**
     * Is wait for height boolean.
     *
     * @return the boolean
     */
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

    /**
     * Only support single view
     *
     * @return the animation builder
     */
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

    /**
     * @return the animation builder
     */
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

    /**
     * Tada
     *
     * @return the animation builder
     */
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

    /**
     * 大转盘。以下几个特效参见：https://github.com/sd6352051/NiftyDialogEffects
     *
     * @return the animation builder
     */
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

    public AnimationBuilder slideLeft() {
        translationX(-300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideRight() {
        translationX(300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideTop() {
        translationY(-300, 0);
        alpha(0, 1);
        return this;
    }

    public AnimationBuilder slideBottom() {
        translationY(300, 0);
        alpha(0, 1);
        return this;
    }

    /**
     * 按指定路径运动
     *
     * @param path the path
     * @return the animation builder
     * @link http://blog.csdn.net/tianjian4592/article/details/47067161
     */
    public AnimationBuilder path(Path path) {
        if (path == null) {
            return this;
        }
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        return custom(new AnimationListener.Update() {
            @Override
            public void update(View view, float value) {
                float[] currentPosition = new float[2];// 当前点坐标
                pathMeasure.getPosTan(value, currentPosition, null);
                final float x = currentPosition[0];
                final float y = currentPosition[1];
                ViewCompat.setX(view, x);
                ViewCompat.setY(view, y);
                Log.d(null, "path: value=" + value + ", x=" + x + ", y=" + y);
            }
        }, 0, pathMeasure.getLength());
    }

    /**
     * Svg path animation, path like as:
     * &lt;path d="M250 150 L150 350 L350 350 Z" /&gt;
     *
     * @param dAttributeOfPath the d attribute of &lt;path&gt; from the XML
     * @return the view animation
     * @link http://www.w3school.com.cn/svg/svg_path.asp
     */
    public AnimationBuilder svgPath(String dAttributeOfPath) {
        return path(SvgPathParser.tryParsePath(dAttributeOfPath));
    }

}
