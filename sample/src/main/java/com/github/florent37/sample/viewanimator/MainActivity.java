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
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        findViewById(R.id.parallel).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                animateParallel();
            }
        });

        findViewById(R.id.sequentially).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                animateSequentially();
            }
        });
    }

    protected void animateParallel() {
        ViewAnimator
                .animate(image)
                    .dp().translationY(1000, 0)
                    .translationX(0)

                .animate(text)
                    .dp().translationY(-200, 0)
                    .textColor(Color.BLACK,Color.BLUE)

                .waitForHeight()
                .descelerate()
                .duration(2000)

                .onStart(new AnimationListener.Start() {
                    @Override public void onStart() {
                        ViewAnimator
                                .animate(text)
                                    .custom(new AnimationListener.Update<TextView>() {
                                        @Override public void update(TextView view, float value) {
                                            view.setText(String.format("%.02f",value));
                                        }
                                    }, 0, 1)

                                .animate(image)
                                    .rotation(360)

                                .duration(5000)
                                .start();
                    }
                })
                .onStop(new AnimationListener.Stop() {
                    @Override public void onStop() {
                        ViewAnimator
                                .animate(image)
                                    .dp().width(10, 1000)
                                .duration(3000)
                                .start();
                    }
                })

                .start();
    }

    protected void animateSequentially() {
        ViewAnimator
                .queue(
                        ViewAnimator.animate(image).alpha(0, 1).descelerate().duration(1000),
                        ViewAnimator.animate(text).scale(0, 1).accelerate().duration(300)
                )
                .start();
    }
}