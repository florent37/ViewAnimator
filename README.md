ViewAnimator
=======

#Usage

```java
ViewAnimator
       .animate(image)
           .translationY(1000, 0)
           .alpha(0,1)

       .animate(text)
           .translationY(-200, 0)

       .descelerate()
       .duration(2000)

       .onStart(() -> {})
       .onStop(() -> {})

       .start();
```

#Animation Queue

```java
ViewAnimator
       .queue(
               ViewAnimator.animate(image).alpha(0, 1).descelerate().duration(1000),
               ViewAnimator.animate(text).scale(0, 1).accelerate().duration(300)
       )
       .start();
```

#More

Using DP values
```java
ViewAnimator
       .animate(image)
           .dp().translationY(-200, 0)
       .start();
```

Animate Height/Width values
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

Community
--------

Looking for contributors, feel free to fork !

Credits
-------

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

License
--------

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
