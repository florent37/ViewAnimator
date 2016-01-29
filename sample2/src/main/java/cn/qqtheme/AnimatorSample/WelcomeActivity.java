package cn.qqtheme.AnimatorSample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

/**
 * 类描述
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * Email:liyujiang_tk@yeah.net
 * DateTime:2016/1/29 2:41
 * Builder:Android Studio
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View view = findViewById(R.id.welcome_heart);
        ViewAnimator.animate(view)
                .fall()
                .duration(3000)
                .fadeIn()
                .duration(3000)
                .interpolator(new DecelerateInterpolator())
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .start();
    }

}
