package com.github.florent37.sample.viewanimator;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.Locale;

public class ViewAnimatorMainActivity extends AppCompatActivity {
    ImageView image;
    ImageView mountain;
    TextView text;
    TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_animator_activity_main);
        image = (ImageView) findViewById(R.id.image);
        mountain = (ImageView) findViewById(R.id.mountain);
        text = (TextView) findViewById(R.id.text);
        percent = (TextView) findViewById(R.id.percent);

        findViewById(R.id.parallel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateParallel();
            }
        });

        findViewById(R.id.mountain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleAnimation();
            }
        });

        findViewById(R.id.sequentially).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSequentially();
            }
        });

    }

    protected void simpleAnimation() {
        ViewAnimator.animate(mountain)
                .translationY(-1000, 0)
                .alpha(0, 1)
                .andAnimate(text)
                .translationX(-200, 0)
                .interpolator(new DecelerateInterpolator())
                .duration(2000)

                .thenAnimate(mountain)
                .scale(1f, 0.5f, 1f)
                .interpolator(new AccelerateInterpolator())
                .duration(1000)

                .start();
    }

    protected void animateParallel() {
        final ViewAnimator viewAnimator = ViewAnimator.animate(mountain, image)
                .dp().translationY(-1000, 0)
                .alpha(0, 1)
                .singleInterpolator(new OvershootInterpolator())

                .andAnimate(percent)
                .scale(0, 1)

                .andAnimate(text)
                .textColor(Color.BLACK, Color.WHITE)
                .backgroundColor(Color.WHITE, Color.BLACK)

                .waitForHeight()
                .singleInterpolator(new AccelerateDecelerateInterpolator())
                .duration(2000)

                .thenAnimate(percent)
                .custom(new AnimationListener.Update<TextView>() {
                    @Override
                    public void update(TextView view, float value) {
                        view.setText(String.format(Locale.US, "%.02f%%", value));
                    }
                }, 0, 1)

                .andAnimate(image)
                .rotation(0, 360)

                .duration(5000)

                .start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewAnimator.cancel();
                Toast.makeText(getBaseContext(), "animator canceled", Toast.LENGTH_SHORT).show();
            }
        }, 4000);
    }

    protected void animateSequentially() {
        ViewAnimator.animate(image)
                .dp().width(100f, 150f)
                .alpha(1, 0.1f)
                .interpolator(new DecelerateInterpolator())
                .duration(800)
                .thenAnimate(image)
                .dp().width(150f, 100f)
                .alpha(0.1f, 1f)
                .interpolator(new AccelerateInterpolator())
                .duration(1200)
                .start();
    }
}
