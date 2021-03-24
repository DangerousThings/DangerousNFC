package com.dangerousthings.nfc.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

public class VerticalTextView extends androidx.appcompat.widget.AppCompatTextView
{
    private int _width;
    private int _height;
    private final Rect _bounds = new Rect();

    public VerticalTextView(@NonNull Context context)
    {
        super(context);
    }

    public VerticalTextView(@NonNull Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public VerticalTextView(@NonNull Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        _height = getMeasuredWidth();
        _width = getMeasuredHeight();
        setMeasuredDimension(_width, _height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.save();
        canvas.translate(_width, _height);
        canvas.rotate(-90);
        TextPaint paint = getPaint();
        paint.setColor(getTextColors().getDefaultColor());
        String text = text();
        paint.getTextBounds(text, 0, text.length(), _bounds);
        canvas.drawText(text, getCompoundPaddingLeft(), (float)(_bounds.height() - _width) / 2, paint);
        canvas.restore();
    }

    private String text()
    {
        return super.getText().toString();
    }
}
