/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ba.pohl1.hm.edu.vrlibrary.ui.hud;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import ba.pohl1.hm.edu.vrlibrary.util.UIUtils;

/**
 * Contains two sub-views to provide a simple stereo HUD.
 */
public class CardboardOverlayView extends LinearLayout {

    private static final long DEFAULT_DURATION = 5000L;

    protected final CardboardOverlayEyeView leftView;
    protected final CardboardOverlayEyeView rightView;
    private AlphaAnimation textFadeAnimation;

    public CardboardOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        leftView = new CardboardOverlayEyeView(context, attrs);
        leftView.setLayoutParams(params);
        addView(leftView);

        rightView = new CardboardOverlayEyeView(context, attrs);
        rightView.setLayoutParams(params);
        addView(rightView);

        // Set some reasonable defaults.
        setDepthOffset(0.0005f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);

        textFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
    }

    public void show3DToast(String message) {
        show3DToast(message, DEFAULT_DURATION);
    }

    public void show3DToast(final String message, final long duration) {
        if(Looper.getMainLooper().equals(Looper.myLooper())) {
            // UI thread
            doShow3DToast(message, duration);
        } else {
            // Non UI thread
            UIUtils.runInUIThread(new Runnable() {
                @Override
                public void run() {
                    doShow3DToast(message, duration);
                }
            });
        }
    }

    private void doShow3DToast(final String message, final long duration) {
        setText(message);
        setTextAlpha(1f);
        textFadeAnimation.setDuration(duration);
        textFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setTextAlpha(0f);
            }
        });
        leftView.startTextAnimation(textFadeAnimation);
        rightView.startTextAnimation(textFadeAnimation);
    }

    private void setDepthOffset(float offset) {
        leftView.setOffset(offset);
        rightView.setOffset(-offset);
    }

    private void setText(String text) {
        leftView.setText(text);
        rightView.setText(text);
    }

    private void setTextAlpha(float alpha) {
        leftView.setTextViewAlpha(alpha);
        rightView.setTextViewAlpha(alpha);
    }

    private void setColor(int color) {
        leftView.setColor(color);
        rightView.setColor(color);
    }

    private abstract class EndAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }
}
