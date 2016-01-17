package com.github.florent37.viewanimator;

import android.view.View;

/**
 * 类描述
 * <p>
 * Author: 李玉江[QQ:1032694760]
 * Date： 2016/1/18 1:51
 * Builder：IntelliJ IDEA
 */
public class EnhancedViewAnimator extends ViewAnimator {

    public static EnhancedAnimationBuilder animate(View... view) {
        EnhancedViewAnimator viewAnimator = new EnhancedViewAnimator();
        return viewAnimator.addAnimationBuilder(view);
    }

    @Override
    public EnhancedAnimationBuilder thenAnimate(View... views) {
        EnhancedViewAnimator nextViewAnimator = new EnhancedViewAnimator();
        this.next = nextViewAnimator;
        nextViewAnimator.prev = this;
        return nextViewAnimator.addAnimationBuilder(views);
    }

    @Override
    public EnhancedAnimationBuilder addAnimationBuilder(View... views) {
        EnhancedAnimationBuilder animationBuilder = new EnhancedAnimationBuilder(this, views);
        animationList.add(animationBuilder);
        return animationBuilder;
    }

}
