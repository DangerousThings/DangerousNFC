package com.dangerousthings.nfc.controls;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener
{
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context)
    {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeRight()
    {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            boolean result = false;
            try
            {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY))
                {
                    if (Math.abs(diffX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if(diffX > 0)
                        {
                            onSwipeRight();
                            result = true;
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return result;
        }
    }
}
