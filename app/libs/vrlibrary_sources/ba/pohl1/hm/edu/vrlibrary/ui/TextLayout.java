package ba.pohl1.hm.edu.vrlibrary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Pohl on 07.02.2016.
 */
public class TextLayout extends LinearLayout {

    private static final int DEFAULT_PADDING = 5;
    private static final String DEFAULT_TEXT = "";

    private final TextView textView;
    private int padding;

    public TextLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.DKGRAY);
        addView(textView);
        initDefaultValues();
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(final String text) {
        textView.setText(text);
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(14.0f);

        final Rect bounds = canvas.getClipBounds();
        final int x = bounds.left + getPadding();
        final int y = bounds.top;
        canvas.drawText(getText(), x, y, paint);
    }

    protected void initDefaultValues() {
        setPadding(DEFAULT_PADDING);
        setText(DEFAULT_TEXT);
    }
}
