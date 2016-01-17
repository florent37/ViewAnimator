package com.github.florent37.viewanimator;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 增加了一些具体的动画
 * <p>
 * Author: 李玉江[QQ:1032694760]
 * Date： 2016/1/18 1:44
 * Builder：IntelliJ IDEA
 * Link:https://github.com/daimajia/AndroidViewAnimations
 */
public class EnhancedAnimationBuilder extends AnimationBuilder {

    public EnhancedAnimationBuilder(ViewAnimator viewAnimator, View... views) {
        super(viewAnimator, views);
    }

    public EnhancedAnimationBuilder pivotX(float... pivotX) {
        ObjectAnimator.ofFloat(getView(), "pivotX", getValues(pivotX));
        return this;
    }

    public EnhancedAnimationBuilder pivotY(float... pivotY) {
        ObjectAnimator.ofFloat(getView(), "pivotY", getValues(pivotY));
        return this;
    }

    /**
     * 弹跳。
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
     * 滚动进来。只支持单个View
     */
    public AnimationBuilder rollIn() {
        View target = getView();
        return alpha(0, 1)
                .translationX(-(target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()), 0)
                .rotation(-120, 0);
    }

    /**
     * 滚动出去。只支持单个View
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
     * 直立起来。只支持单个View
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
     * 波浪。只支持单个View
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
     * 游移不定。只支持单个View
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
