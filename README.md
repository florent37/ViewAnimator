ViewAnimator
=======

[![API](https://img.shields.io/badge/API-11%2B-green.svg)](https://github.com/florent37/ViewAnimator/tree/master)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ViewAnimator-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2942)

A fluent Android animation library !


<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

[![png](https://raw.githubusercontent.com/florent37/ViewAnimator/master/montain_small.jpg)](https://github.com/florent37/ViewAnimator)

# Usage

Animate multiple view from one method

```java
ViewAnimator
       .animate(image)
            .translationY(-1000, 0)
            .alpha(0,1)
       .andAnimate(text)
            .dp().translationX(-20, 0)
            .decelerate()
            .duration(2000)
       .thenAnimate(image)
            .scale(1f, 0.5f, 1f)
            .accelerate()
            .duration(1000)
       .start();
       
```

[![gif](https://j.gifs.com/ERlBzW.gif)](https://youtu.be/ZHw8MfOM1Eg)

Without ViewAnimator

```java
AnimatorSet animatorSet = new AnimatorSet();
animatorSet.playTogether(
  ObjectAnimator.ofFloat(image,"translationY",-1000,0),
  ObjectAnimator.ofFloat(image,"alpha",0,1),
  ObjectAnimator.ofFloat(text,"translationX",-200,0)
);
animatorSet.setInterpolator(new DescelerateInterpolator());
animatorSet.setDuration(2000);
animatorSet.addListener(new AnimatorListenerAdapter(){
    @Override public void onAnimationEnd(Animator animation) {

      AnimatorSet animatorSet2 = new AnimatorSet();
      animatorSet2.playTogether(
          ObjectAnimator.ofFloat(image,"scaleX", 1f, 0.5f, 1f),
          ObjectAnimator.ofFloat(image,"scaleY", 1f, 0.5f, 1f)
      );
      animatorSet2.setInterpolator(new AccelerateInterpolator());
      animatorSet2.setDuration(1000);
      animatorSet2.start();

    }
});
animatorSet.start();
```

# More

[![gif](https://j.gifs.com/XD6R4V.gif)](https://youtu.be/Qlj40Y6ChSM)

Add same animation on multiples view
```java
ViewAnimator
       .animate(image,text)
       .scale(0,1)
       .start();
```

Add listeners
```java
ViewAnimator
       .animate(image)
       .scale(0,1)
       .onStart(() -> {})
       .onStop(() -> {})
       .start();

```

Use DP values
```java
ViewAnimator
       .animate(image)
       .dp().translationY(-200, 0)
       .start();
```

Animate Height / Width
```java
ViewAnimator
       .animate(view)
       .waitForHeight() //wait until a ViewTreeObserver notification
       .dp().width(100,200)
       .dp().height(50,100)
       .start();
```

Color animations
```java
ViewAnimator
       .animate(view)
       .textColor(Color.BLACK,Color.GREEN)
       .backgroundColor(Color.WHITE,Color.BLACK)
       .start();
```

Rotation animations
```java
ViewAnimator
       .animate(view)
       .rotation(360)
       .start();
```

Custom animations
```java
ViewAnimator
       .animate(text)
       .custom(new AnimationListener.Update<TextView>() {
            @Override public void update(TextView view, float value) {
                  view.setText(String.format("%.02f",value));
            }
        }, 0, 1)
       .start();
```

Cancel animations
```java
ViewAnimator viewAnimator = ViewAnimator
       .animate(view)
       .rotation(360)
       .start();

viewAnimator.cancel();
```

Enhanced animations (Thanks [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations) and [NiftyDialogEffects](https://github.com/sd6352051/NiftyDialogEffects) )   
```java
.shake().interpolator(new LinearInterpolator());
.bounceIn().interpolator(new BounceInterpolator());
.flash().repeatCount(4);
.flipHorizontal();
.wave().duration(5000);
.tada();
.pulse();
.standUp();
.swing();
.wobble();
```
...
![Preview](/EnhancedAnimations.gif)

Path animations ( Inspiration from http://blog.csdn.net/tianjian4592/article/details/47067161 )   
```java
    final int[] START_POINT = new int[]{ 300, 270 };
    final int[] BOTTOM_POINT = new int[]{ 300, 400 };
    final int[] LEFT_CONTROL_POINT = new int[]{ 450, 200 };
    final int[] RIGHT_CONTROL_POINT = new int[]{ 150, 200 };
    Path path = new Path();
    path.moveTo(START_POINT[0], START_POINT[1]);
    path.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], BOTTOM_POINT[0], BOTTOM_POINT[1]);
    path.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], START_POINT[0], START_POINT[1]);
    path.close();
    ViewAnimator.animate(view).path(path).repeatCount(-1).start();
```

SVG path animations (See simple 2, read http://www.w3school.com.cn/svg/svg_path.asp)
```html
<svg width="100%" height="100%">
    <path d="M..."  />
</svg>
```
```java
final String SVG_PATH = "M...";
ViewAnimator.animate(view).path(SvgPathParser.tryParsePath(SVG_PATH)).repeatCount(-1).start();
```

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

Add into your **build.gradle**

[![Download](https://api.bintray.com/packages/florent37/maven/ViewAnimator/images/download.svg)](https://bintray.com/florent37/maven/ViewAnimator/_latestVersion)

```groovy
compile 'com.github.florent37:viewanimator:1.0.5'
```

# Community

Looking for contributors, feel free to fork !

# Credits

Author: Florent Champigny   
Contributor: [李玉江(liyujiang)](https://github.com/gzu-liyujiang/ViewAnimator)   

Fiches Plateau Moto : [https://www.fiches-plateau-moto.fr/](https://www.fiches-plateau-moto.fr/)

<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://fr.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>

# License

    Copyright 2015 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
