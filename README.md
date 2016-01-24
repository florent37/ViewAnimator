ViewAnimator
=======

[![API](https://img.shields.io/badge/API-9%2B-green.svg)](https://github.com/florent37/ViewAnimator/tree/master)
[![API](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/florent37/ViewAnimator/tree/API14+)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ViewAnimator-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2942)

A fluent Android animation library !

[![png](https://raw.githubusercontent.com/florent37/ViewAnimator/master/montain_small.jpg)](https://github.com/florent37/ViewAnimator)

#Usage

Animate multiple view from one method

```java
ViewAnimator
       .animate(image)
           .translationY(-1000, 0)
           .alpha(0,1)
       .andAnimate(text)
           .dp().translationX(-20, 0)
       .descelerate()
       .duration(2000)

       .thenAnimate(image)
            .scale(1f,0.5f,1f)
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
          ObjectAnimator.ofFloat(image,"scaleX",1f,0.5f,1f),
          ObjectAnimator.ofFloat(image,"scaleY",1f,0.5f,1f)
      );
      animatorSet2.setInterpolator(new AccelerateInterpolator());
      animatorSet2.setDuration(1000);
      animatorSet2.start();

    }
});
animatorSet.start();
```

#More

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

Enhanced animations (Thanks https://github.com/daimajia/AndroidViewAnimations)   
```java
ViewAnimator.animate(view).wave().duration(3000).start();
```
```java
ViewAnimator.animate(view).shake().duration(3000).start();
```
...   
![Preview](/EnhancedAnimations.gif)

#Download

Add into your **build.gradle**

[![Download](https://api.bintray.com/packages/florent37/maven/ViewAnimator/images/download.svg)](https://bintray.com/florent37/maven/ViewAnimator/_latestVersion)

```groovy
compile 'com.github.florent37:viewanimator:1.0.0@aar'
compile 'com.nineoldandroids:library:2.4.0'
```

#Community

Looking for contributors, feel free to fork !

#Dependencies

[NineOldAndroid](nineoldandroids.com) : Android library for using the Honeycomb (Android 3.0) animation API on all versions of the platform back to 1.0!


#Credits

Author: Florent Champigny

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/profile/view?id=297860624">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>

#License

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
