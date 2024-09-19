package com.ljkj.lib_common.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 缩放
 */
public class ZoomableImageView extends AppCompatImageView {

    private static final int NONE = 0;
    private static final int DRAG = 1;

    private final float[] matrixValues = new float[9];
    private float scale = 1.0f; // Start with scale factor of 1.0
    private final PointF last = new PointF();
    private final PointF start = new PointF();
    private int mode = NONE;
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleGestureDetector;
    private final Matrix matrixV = new Matrix();

    public ZoomableImageView(Context context) {
        this(context, null);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                scale = scale > 1.0f ? 1.0f : 2.0f;
                float x = e.getX();
                float y = e.getY();
                matrixV.setScale(scale, scale, x, y);
                centerImage();
                setImageMatrix(matrixV);
                return true;
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                scale *= scaleFactor;
                scale = Math.max(0.5f, Math.min(scale, 3.0f));
                matrixV.setScale(scale, scale, detector.getFocusX(), detector.getFocusY());
                centerImage();
                setImageMatrix(matrixV);
                return true;
            }
        });
    }

    private void centerImage() {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        // Calculate scaled image dimensions
        float scaledWidth = imageWidth * scale;
        float scaledHeight = imageHeight * scale;

        float translateX = (viewWidth - scaledWidth) / 2;
        float translateY = (viewHeight - scaledHeight) / 2;

        matrixV.reset(); // Reset matrix before applying scale and translation
        matrixV.postScale(scale, scale);
        matrixV.postTranslate(translateX, translateY);
        setImageMatrix(matrixV);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetImage();
    }

    private void resetImage() {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        // Calculate scale factor to fit the image within the view
        float scaleX = viewWidth / imageWidth;
        float scaleY = viewHeight / imageHeight;
        scale = Math.min(scaleX, scaleY); // Use the smaller scale factor to ensure the image fits within the view

        matrixV.reset();
        matrixV.setScale(scale, scale);
        centerImage(); // Center the image after scaling
        setImageMatrix(matrixV);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        PointF currentPoint = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(currentPoint);
                start.set(last);
                mode = DRAG;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float dx = currentPoint.x - last.x;
                    float dy = currentPoint.y - last.y;
                    matrixV.postTranslate(dx, dy);
                    setImageMatrix(matrixV);
                    last.set(currentPoint.x, currentPoint.y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        return true;
    }
}
