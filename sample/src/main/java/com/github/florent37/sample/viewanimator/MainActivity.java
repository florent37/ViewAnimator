package com.github.florent37.sample.viewanimator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    ImageView mountain;
    TextView text;
    TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        mountain = (ImageView) findViewById(R.id.mountain);
        text = (TextView) findViewById(R.id.text);
        percent = (TextView) findViewById(R.id.percent);

        findViewById(R.id.parallel).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                animateParallel();
            }
        });

        findViewById(R.id.mountain).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                simpleAnimation();
            }
        });

        findViewById(R.id.sequentially).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                animateSequentially();
            }
        });

        final View btnWave = findViewById(R.id.wave);
        btnWave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ViewAnimator.animate(btnWave).wave().duration(3000).start();
            }
        });

        final View btnShake = findViewById(R.id.shake);
        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ViewAnimator.animate(btnShake).shake().duration(3000).start();
            }
        });
    }

    protected void simpleAnimation(){
        ViewAnimator
                .animate(mountain)
                    .translationY(-1000, 0)
                    .alpha(0,1)
                .andAnimate(text)
                    .translationX(-200, 0)
                .descelerate()
                .duration(2000)

                .thenAnimate(mountain)
                    .scale(1f,0.5f,1f)
                .accelerate()
                .duration(1000)

                .start();
    }

    protected void animateParallel() {
        ViewAnimator
                .animate(mountain,image)
                    .dp().translationY(-1000, 0)
                    .alpha(0,1)

                .andAnimate(percent)
                    .scale(0,1)

                .andAnimate(text)
                    .dp().translationY(1000, 0)
                    .textColor(Color.BLACK, Color.WHITE)
                    .backgroundColor(Color.WHITE, Color.BLACK)

                .waitForHeight()
                .descelerate()
                .duration(2000)

                .thenAnimate(percent)
                    .custom(new AnimationListener.Update<TextView>() {
                        @Override public void update(TextView view, float value) {
                            view.setText(String.format("%.02f%%", value));
                        }
                    }, 0, 1)

                .andAnimate(image)
                    .rotation(0, 360)

                .duration(5000)

                .start();
    }

    protected void animateSequentially() {
        ViewAnimator
                .animate(image)
                    .dp().width(100f,150f)
                    .alpha(1,0.1f)
                    .descelerate()
                    .duration(800)
                .thenAnimate(image)
                    .dp().width(150f,100f)
                    .alpha(0.1f,1f)
                    .accelerate()
                    .duration(1200)
                .start();
    }
}
