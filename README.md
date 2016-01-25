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

Enhanced animations (Thanks [AndroidViewAnimators](https://github.com/daimajia/AndroidViewAnimators),[NiftyDialogEffects](https://github.com/sd6352051/NiftyDialogEffects))   
![screenshots](/EnhancedAnimations.gif)   
```java
                int random = new Random().nextInt(10);
                final ViewGroup viewGroup = (ViewGroup) v.getParent();
                final ImageView imageView = new ImageView(v.getContext());
                imageView.setImageResource(R.mipmap.ic_launcher);
                viewGroup.addView(imageView);
                AnimationBuilder builder;
                switch (random) {
                    case 0:
                        builder = ViewAnimator.animate(imageView).shake().interpolator(new LinearInterpolator());
                        break;
                    case 1:
                        builder = ViewAnimator.animate(imageView).bounceIn().interpolator(new BounceInterpolator());
                        break;
                    case 2:
                        builder = ViewAnimator.animate(imageView).flash().repeatCount(1);
                        break;
                    case 3:
                        builder = ViewAnimator.animate(imageView).flipHorizontal();
                        break;
                    case 4:
                        builder = ViewAnimator.animate(imageView).wave().duration(5000);
                        break;
                    case 5:
                        builder = ViewAnimator.animate(imageView).tada();
                        break;
                    case 6:
                        builder = ViewAnimator.animate(imageView).pulse();
                        break;
                    case 7:
                        builder = ViewAnimator.animate(imageView).standUp();
                        break;
                    case 8:
                        builder = ViewAnimator.animate(imageView).swing();
                        break;
                    default:
                        builder = ViewAnimator.animate(imageView).wobble();
                        break;
                }
                builder.onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        viewGroup.removeView(imageView);
                    }
                }).start();
```

Path animations (Read http://blog.csdn.net/tianjian4592/article/details/47067161)   
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

SVG path animations (Read http://www.w3school.com.cn/svg/svg_path.asp)   
```html
<svg width="100%" height="100%">
    <path
        d="M 42.266949,70.444915 C 87.351695,30.995763 104.25847,28.177966 104.25847,28.177966 l 87.3517,36.631356 8.45339,14.088983 L 166.25,104.25847 50.720339,140.88983 c 0,0 -45.0847458,180.33898 -39.449153,194.42797 5.635594,14.08898 67.627119,183.15678 67.627119,183.15678 l 16.90678,81.7161 c 0,0 98.622885,19.72457 115.529665,22.54237 16.90678,2.8178 70.44491,-22.54237 78.8983,-33.81356 8.45339,-11.27118 76.08051,-107.07627 33.81356,-126.80085 -42.26695,-19.72457 -132.43644,-56.35593 -132.43644,-56.35593 0,0 -33.81356,-73.26271 -19.72458,-73.26271 14.08899,0 132.43644,73.26271 138.07204,33.81356 5.63559,-39.44915 19.72457,-169.0678 19.72457,-169.0678 0,0 28.17797,-25.36017 -28.17796,-19.72457 -56.35593,5.63559 -95.80509,11.27118 -95.80509,11.27118 l 42.26695,-87.35169 8.45339,-28.177968";
    />
</svg>
```
```java
    final String SVG_PATH = "M 42.266949,70.444915 C 87.351695,30.995763 104.25847,28.177966 104.25847,28.177966 l 87.3517,36.631356 8.45339,14.088983 L 166.25,104.25847 50.720339,140.88983 c 0,0 -45.0847458,180.33898 -39.449153,194.42797 5.635594,14.08898 67.627119,183.15678 67.627119,183.15678 l 16.90678,81.7161 c 0,0 98.622885,19.72457 115.529665,22.54237 16.90678,2.8178 70.44491,-22.54237 78.8983,-33.81356 8.45339,-11.27118 76.08051,-107.07627 33.81356,-126.80085 -42.26695,-19.72457 -132.43644,-56.35593 -132.43644,-56.35593 0,0 -33.81356,-73.26271 -19.72458,-73.26271 14.08899,0 132.43644,73.26271 138.07204,33.81356 5.63559,-39.44915 19.72457,-169.0678 19.72457,-169.0678 0,0 28.17797,-25.36017 -28.17796,-19.72457 -56.35593,5.63559 -95.80509,11.27118 -95.80509,11.27118 l 42.26695,-87.35169 8.45339,-28.177968";
    ViewAnimator.animate(view).svgPath(SVG_PATH).repeatCount(1).start();
```

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
Contributor: [李玉江(liyujiang)](https://github.com/gzu-liyujiang/ViewAnimator)   

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
