package cn.qqtheme.AnimatorSample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends Activity {
    private static final String SVG_PATH = "m 182.89072,369.33798 0,0 c 0,0 -5.74786,-42.19512 -11.46215,-52.19512 -5.71428,-10 -7.14286,-14.28572 -12.85714,-20 -5.71429,-5.71429 -8.57143,-11.42857 -15.71429,-15.71429 -7.14285,-4.28571 -17.14285,-8.57143 -17.14285,-8.57143 0,0 -12.85715,-8.57143 -20,-12.85714 C 98.571429,255.71429 90,252.85714 84.285714,248.57143 78.571429,244.28571 67.142857,230 67.142857,230 c 0,0 -7.142857,-5.71429 -8.571428,-14.28571 -1.428572,-8.57143 -5.714286,-22.85715 -5.714286,-22.85715 l -1.428572,-17.14285 0,-18.57143 L 70,140 85.714286,137.14286 114.28571,138.57143 137.14286,150 160,167.14286 l 15.71429,5.71428 12.85714,2.85715 15.71428,1.42857 10,0 c 0,0 10,-1.42857 17.14286,-5.71429 C 238.57143,167.14286 245.71429,160 245.71429,160 l 21.42857,-15.71429 c 0,0 4.28571,-5.71428 10,-5.71428 5.71428,0 14.28571,0 18.57143,0 4.28571,0 15.71428,2.85714 15.71428,2.85714 l 11.42857,14.28572 c 0,0 4.28572,10 4.28572,18.57142 0,8.57143 -8.57143,21.42858 -12.85715,28.57143 -4.28571,7.14286 -18.57142,20 -18.57142,20 l -17.14286,10 c 0,0 -8.57143,-5.71428 -17.14286,11.42857 -8.57143,17.14286 -20,28.57143 -20,28.57143 L 230,294.28571 215.71429,312.85714 c 0,0 -4.28572,4.28572 -8.57143,15.71429 -4.28572,11.42857 -24.25214,40.76655 -24.25214,40.76655 l 0,0 c 0,0 0,41.25 0,41.25 0,0 2.89071,-29.41202 0,0 C 180,440 171.42857,448.57143 171.42857,461.42857 c 0,12.85714 14.28572,-4.28571 -4.28571,30 -18.57143,34.28572 -15.71429,25.71429 -27.14286,41.42857 -11.42857,15.71429 -27.14286,20 -27.14286,20 l -29.999997,-15.71428 -20,-41.42857 1.428571,-38.57143 c 0,0 -2.857143,-8.57143 5.714286,-11.42857 8.571429,-2.85715 25.714286,-10 25.714286,-10 l 31.428574,-1.42858 c 0,0 18.57143,2.85715 28.57143,2.85715 10,0 32.85714,1.42857 40,0 7.14285,-1.42857 30.92643,-26.55488 30.92643,-26.55488 0,0 33.35928,-4.87369 37.64499,-6.30227 4.28572,-1.42857 20,-21.42857 20,-21.42857 L 300,351.42857 300,340 l -5.71429,-18.57143 -18.57142,-10 -24.28572,5.71429 L 232.85714,340 c 0,0 -6.21642,29.33798 -6.21642,29.33798 l -43.75,41.25 c 0,0 -15.68072,-27.98344 0,0 15.68071,27.98345 28.53785,56.55488 28.53785,56.55488 0,0 22.85714,10 32.85714,17.14285 10,7.14286 8.57143,5.71429 12.85715,14.28572 4.28571,8.57143 5.71428,24.28571 5.71428,37.14286 0,12.85714 -12.85714,28.57142 -12.85714,28.57142 0,0 -34.28571,8.57143 -47.14286,5.71429 -12.85714,-2.85714 -35.71428,-14.28571 -40,-22.85714 -4.28571,-8.57143 -18.57143,-51.42857 -18.57143,-60 0,-8.57143 1.42858,-40 1.42858,-40 l -4.28572,-42.85715 c 0,0 5.71429,4.28572 -5.71428,-11.42857 C 124.28571,377.14286 118.57143,368.57143 112.85714,364.28571 107.14286,360 82.857143,342.85714 75.714286,341.42857 68.571429,340 47.142857,340 47.142857,340 c 0,0 -8.571428,-4.28571 -8.571428,11.42857 0,15.71429 0,30 7.142857,37.14286 7.142857,7.14286 22.857143,17.14286 22.857143,17.14286 0,0 18.571428,2.85714 28.571428,1.42857 10.000003,-1.42857 34.285713,-8.57143 34.285713,-8.57143 l 51.46215,12.01655 0,0 0,-41.25";
    private static final int[] START_POINT = new int[]{300, 270};
    private static final int[] BOTTOM_POINT = new int[]{300, 400};
    private static final int[] LEFT_CONTROL_POINT = new int[]{450, 200};
    private static final int[] RIGHT_CONTROL_POINT = new int[]{150, 200};
    private ImageView imageView, imageView4Path, imageView4Svg, imageView4Follower, imageView4Bubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.animation_image);
        imageView4Path = (ImageView) findViewById(R.id.animation_path);
        imageView4Svg = (ImageView) findViewById(R.id.animation_svg_path);
        imageView4Follower = (ImageView) findViewById(R.id.animation_snow);
        imageView4Bubble = (ImageView) findViewById(R.id.animation_heart);
        onFollower(null);
        onBubble(null);
    }

    public void onDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Test animation");
        builder.setMessage("Animator for dialog");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        ViewAnimator.animate(view, dialog.getWindow().getDecorView())
                .slideBottomIn()
                .interpolator(new DecelerateInterpolator())
                .start();
    }


    public void onShake(View view) {
        ViewAnimator.animate(view, imageView)
                .shake()
                .interpolator(new LinearInterpolator())
                .start();
    }

    public void onBounce(View view) {
        ViewAnimator.animate(view, imageView)
                .bounce()
                .interpolator(new BounceInterpolator())
                .start();
    }

    public void onBounceIn(View view) {
        ViewAnimator.animate(view, imageView)
                .bounceIn()
                .interpolator(new BounceInterpolator())
                .start();
    }

    public void onFlash(View view) {
        ViewAnimator.animate(view, imageView)
                .flash()
                .repeatCount(1)
                .start();
    }

    public void onFlipHorizontal(View view) {
        ViewAnimator.animate(view, imageView)
                .flipHorizontal()
                .start();
    }

    public void onFlipVertical(View view) {
        ViewAnimator.animate(view, imageView)
                .flipVertical()
                .start();
    }

    public void onWave(View view) {
        ViewAnimator.animate(view, imageView)
                .wave()
                .duration(5000)
                .start();
    }

    public void onTada(View view) {
        ViewAnimator.animate(view, imageView)
                .tada()
                .start();
    }

    public void onPulse(View view) {
        ViewAnimator.animate(view, imageView)
                .pulse()
                .start();
    }

    public void onStandUp(View view) {
        ViewAnimator.animate(view, imageView)
                .standUp()
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    public void onSwing(View view) {
        ViewAnimator.animate(view, imageView)
                .swing()
                .start();
    }

    public void onWobble(View view) {
        ViewAnimator.animate(view, imageView)
                .wobble()
                .start();
    }

    public void onFall(View view) {
        ViewAnimator.animate(view, imageView)
                .fall()
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    public void onNewsPaper(View view) {
        ViewAnimator.animate(view, imageView)
                .newsPaper()
                .start();
    }

    public void onSlit(View view) {
        ViewAnimator.animate(view, imageView)
                .slit()
                .start();
    }

    public void onSlideLeftIn(View view) {
        ViewAnimator.animate(view, imageView)
                .slideLeftIn()
                .start();
    }

    public void onSlideRightIn(View view) {
        ViewAnimator.animate(view, imageView)
                .slideRightIn()
                .start();
    }

    public void onSlideTopIn(View view) {
        ViewAnimator.animate(view, imageView)
                .slideTopIn()
                .start();
    }

    public void onRubber(View view) {
        ViewAnimator.animate(view, imageView)
                .rubber()
                .start();
    }

    public void onZoomIn(View view) {
        ViewAnimator.animate(view, imageView)
                .zoomIn()
                .start();
    }

    public void onRollIn(View view) {
        ViewAnimator.animate(view, imageView)
                .rollIn()
                .thenAnimate(imageView)
                .rotation(5, 10, 30, 80)
                .duration(3000)
                .start();
    }

    public void onFadeIn(View view) {
        ViewAnimator.animate(view, imageView)
                .fadeIn()
                .start();
    }

    public void onPath(View view) {
        Path path = new Path();
        path.moveTo(START_POINT[0], START_POINT[1]);
        path.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], BOTTOM_POINT[0], BOTTOM_POINT[1]);
        path.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], START_POINT[0], START_POINT[1]);
        path.close();
        ViewAnimator.animate(imageView4Path)
                .repeatMode(ViewAnimator.RESTART)
                .repeatCount(ViewAnimator.INFINITE)
                .path(path)
                .start();
    }

    public void onSvgPath(View view) {
        final int[] imgLoc = new int[2];
        //save location
        imageView4Svg.getLocationOnScreen(imgLoc);
        ViewAnimator.animate(imageView4Svg)
                .svgPath(SVG_PATH)
                .repeatMode(ViewAnimator.REVERSE)
                .repeatCount(2)
                .duration(15000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        //restore location
                        ViewHelper.setX(imageView4Svg, imgLoc[0]);
                        ViewHelper.setY(imageView4Svg, imgLoc[1]);
                    }
                })
                .start();
    }

    public void onFollower(View view) {
        DisplayMetrics metrics = ScreenUtils.displayMetrics(this);
        ViewAnimator.animate(imageView4Follower)
                .path(PathUtils.createFollower(metrics.widthPixels, metrics.heightPixels))
                .duration(5000)
                .repeatCount(ViewAnimator.INFINITE)
                .start();
    }

    public void onBubble(View view) {
        DisplayMetrics metrics = ScreenUtils.displayMetrics(this);
        ViewAnimator.animate(imageView4Bubble)
                .path(PathUtils.createBubble(metrics.heightPixels, metrics.widthPixels / 2, metrics.heightPixels))
                .duration(5000)
                .repeatCount(ViewAnimator.INFINITE)
                .start();
    }

}
