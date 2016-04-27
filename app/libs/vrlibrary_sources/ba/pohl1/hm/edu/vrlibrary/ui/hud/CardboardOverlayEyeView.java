package ba.pohl1.hm.edu.vrlibrary.ui.hud;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple view group containing some horizontally centered text underneath a horizontally
 * centered image.
 * <p>
 * <p>This is a helper class for CardboardOverlayView.
 * <p>
 * Created by Pohl on 23.04.2016.
 */
public class CardboardOverlayEyeView extends ViewGroup {

    private static final int PADDING = 7;
    private final ImageView imageView;
    private final TextView textView;
    private float offset;

    public CardboardOverlayEyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageView = new ImageView(context, attrs);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setScaleX(0.25f);
        imageView.setScaleY(0.25f);
        imageView.setAdjustViewBounds(true);  // Preserve aspect ratio.
        addView(imageView);

        textView = new TextView(context, attrs);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.DKGRAY);
        addView(textView);
    }

    public void startTextAnimation(final Animation animation) {
        textView.startAnimation(animation);
    }

    public void setColor(int color) {
        imageView.setColorFilter(color);
        textView.setTextColor(color);
    }

    public void setImage(final int res) {
        imageView.setImageResource(res);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextViewAlpha(float alpha) {
        textView.setAlpha(alpha);
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Width and height of this ViewGroup.
        final int width = right - left - 2 * PADDING;
        final int height = bottom - top;

        // The size of the image, given as a fraction of the dimension as a ViewGroup.
        // We multiply both width and heading with this number to compute the image's bounding
        // box. Inside the box, the image is the horizontally and vertically centered.
        final float imageSize = 0.1f;

        // The fraction of this ViewGroup's height by which we shift the image off the
        // ViewGroup's center. Positive values shift downwards, negative values shift upwards.
        final float verticalImageOffset = -0.07f;

        // Vertical position of the text, specified in fractions of this ViewGroup's height.
        final float verticalTextPos = 0.52f;

        // Layout ImageView
        float adjustedOffset = offset;
        // If the half screen width is bigger than 1000 pixels, that means it's a big screen
        // phone and we need to use a different offset value.
        if(width > 1000) {
            adjustedOffset = 3.8f * offset;
        }
        float imageMargin = (1.0f - imageSize) / 2.0f;
        float leftMargin = (int) (width * (imageMargin + adjustedOffset));
        //float topMargin = (int) (height * (imageMargin + verticalImageOffset));
        float topMargin = height / 2 - imageView.getHeight() / 2;
        imageView.layout(
                (int) leftMargin, (int) topMargin,
                (int) (leftMargin + width * imageSize), (int) (topMargin + height * imageSize));

        // Layout TextView
        leftMargin = adjustedOffset * width;
        topMargin = height * verticalTextPos;
        textView.layout(
                (int) leftMargin + PADDING, (int) topMargin,
                (int) (leftMargin + width - PADDING), (int) (topMargin + height * (1.0f - verticalTextPos)));
    }
}
