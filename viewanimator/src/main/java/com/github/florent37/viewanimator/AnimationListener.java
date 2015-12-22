package com.github.florent37.viewanimator;

import android.view.View;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class AnimationListener {
    public static interface Start{
        void onStart();
    }

    public static interface Stop{
        void onStop();
    }

    public static interface Update<V extends View>{
        void update(V view, float value);
    }
}
