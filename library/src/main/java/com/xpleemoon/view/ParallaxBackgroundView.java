package com.xpleemoon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * It is support horizontal or vertical parallax.</br>
 * Horizontal and vertical parallax are set via {@link #setParallaxOrientation(int)}(attr {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxOrientation}).
 * Parallax background is set via {@link #setParallaxBackgroundResource(int)}(attr {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxBackground}) or {@link #setParallaxBackground(Drawable)} and {@link #setParallaxPercent(float) percent}.</br>
 * It has 2 modes are used for scaling:
 * <ul>
 * <li>{@link #MODE_PRE_SCALE} uses more memory but scrolls slightly smoother</li>
 * <li>{@link #MODE_POST_SCALE} uses less memory but is more CPU-intensive</li>
 * </ul>
 * {@link #MODE_POST_SCALE} is default mode.
 * <p/>
 * In addition, these modes are referenced <a href="http://stackoverflow.com/questions/17207612/how-to-have-a-wider-image-scrolling-in-the-background?answertab=votes#tab-top">rupps</a>.
 * Thanks <a href="http://stackoverflow.com/questions/17207612/how-to-have-a-wider-image-scrolling-in-the-background?answertab=votes#tab-top">rupps</a>.
 *
 * @author xpleemoon
 */
public class ParallaxBackgroundView extends View {
    /**
     * uses more memory but scrolls slightly smoother
     */
    public static final int MODE_PRE_SCALE = 0;
    /**
     * uses less memory but is more CPU-intensive
     */
    public static final int MODE_POST_SCALE = 1;

    @IntDef({MODE_PRE_SCALE, MODE_POST_SCALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ParallaxMode {
    }

    public static final int PARALLAX_HORIZONTAL = 0;
    public static final int PARALLAX_VERTICAL = 1;

    @IntDef({PARALLAX_HORIZONTAL, PARALLAX_VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ParallaxOrientationMode {
    }

    private Bitmap mParallaxBackground = null;
    /**
     * Current progress 0~100
     */
    private float mParallaxPercent = 0;
    private boolean isParallax = true;
    @ParallaxMode
    private int mParallaxMode = MODE_POST_SCALE;
    private int mCurrentFactorWidth;
    private int mCurrentFactorHeight;
    private float mCurrentFactorMultiplier;
    private Rect mSrcRect;
    private Rect mDestRect;
    @ParallaxOrientationMode
    private int mParallaxOrientation;

    public ParallaxBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxBackgroundView);
        isParallax = typedArray.getBoolean(R.styleable.ParallaxBackgroundView_isParallax, true);
        //noinspection WrongConstant
        mParallaxMode = typedArray.getInt(R.styleable.ParallaxBackgroundView_parallaxMode, MODE_POST_SCALE);
        //noinspection WrongConstant
        mParallaxOrientation = typedArray.getInt(R.styleable.ParallaxBackgroundView_parallaxOrientation, PARALLAX_HORIZONTAL);
        int parallaxBackgroundResId = typedArray.getResourceId(R.styleable.ParallaxBackgroundView_parallaxBackground, 0);
        if (parallaxBackgroundResId > 0) {
            setParallaxBackgroundResource(parallaxBackgroundResId);
        }
        typedArray.recycle();
    }

    public ParallaxBackgroundView(Context context) {
        super(context);
    }

    /**
     * Whether Parallax is active or not
     *
     * @return
     */
    public boolean isParallax() {
        return isParallax;
    }

    /**
     * Enables or disables parallax mode
     * attr ref {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_isParallax}
     *
     * @param isParallax
     */
    public void setParallax(boolean isParallax) {
        this.isParallax = isParallax;
    }

    @ParallaxMode
    public int getparallaxMode() {
        return mParallaxMode;
    }

    /**
     * After set the parallax mode, be sure to <b>set background</b>
     * <ul>
     * <li>{@link #MODE_PRE_SCALE} uses more memory but scrolls slightly smoother</li>
     * <li>{@link #MODE_POST_SCALE} uses less memory but is more CPU-intensive</li>
     * </ul>
     * attr ref {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxMode}
     *
     * @param mode
     */
    public void setParallaxMode(@ParallaxMode int mode) {
        mParallaxMode = mode;
        if (mParallaxBackground != null) {
            mParallaxBackground = null;
        }
    }

    @ParallaxOrientationMode
    public int getParallaxOrientation() {
        return mParallaxOrientation;
    }

    /**
     * attr ref {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxOrientation}
     *
     * @param parallaxOrientation
     */
    public void setParallaxOrientation(@ParallaxOrientationMode int parallaxOrientation) {
        this.mParallaxOrientation = parallaxOrientation;
    }

    /**
     * Seth the percent of the parallax scroll. 0 Means totally left, 100 means totally right.
     *
     * @param percent
     */
    public void setParallaxPercent(float percent) {
        if (percent > 100) {
            percent = 100;
        } else if (percent < 0) {
            percent = 0;
        }
        if (percent == mParallaxPercent) {
            return;
        }
        mParallaxPercent = percent;
        invalidate();
    }

    public Bitmap getParallaxBackground() {
        return mParallaxBackground;
    }

    /**
     * attr ref {@link com.xpleemoon.view.R.styleable#ParallaxBackgroundView_parallaxBackground}
     */
    public void setParallaxBackgroundResource(@DrawableRes int resid) {
        Drawable background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = getResources().getDrawable(resid, getContext().getTheme());
        } else {
            //noinspection deprecation
            background = getResources().getDrawable(resid);
        }
        setParallaxBackground(background);
    }

    public void setParallaxBackground(Drawable background) {
        if (!isParallax || !(background instanceof BitmapDrawable)) {
            if (mParallaxBackground != null) {
                mParallaxBackground = null;
            }
            return;
        }

        final Bitmap original = ((BitmapDrawable) background).getBitmap();
        post(new Runnable() { // make sure getWidth() and getHeight() are valid
            @Override
            public void run() {
                switch (mParallaxMode) {
                    case MODE_POST_SCALE:
                        drawPostScale(original);
                        break;

                    case MODE_PRE_SCALE:
                        drawPreScale(original);
                        break;
                    default:
                        throw new IllegalStateException("parallax mode undefined");
                }
            }
        });
    }

    private void drawPreScale(Bitmap original) {
        Canvas canvas;
        Rect srcRect;
        if (mParallaxOrientation == PARALLAX_HORIZONTAL) {
            float factor = calculateFactor(original.getWidth(), MODE_PRE_SCALE);
            mCurrentFactorMultiplier = (factor - 1) * getWidth() / 100;

            mParallaxBackground = Bitmap.createBitmap((int) (getWidth() * factor), getHeight(), Bitmap.Config.RGB_565);
            canvas = new Canvas(mParallaxBackground);

            int adjustment = 0;
            // we crop the original image up and down, as it has been expanded to FACTOR
            // you can play with the Adjustement value to crop top, center or bottom.
            // I only use center so its hardcoded.
            float scaledBitmapFinalHeight = original.getHeight() * mParallaxBackground.getWidth() / original.getWidth();
            if (scaledBitmapFinalHeight > mParallaxBackground.getHeight()) {
                // as expected, we have to crop up&down to maintain aspect ratio
                adjustment = (int) (scaledBitmapFinalHeight - mParallaxBackground.getHeight()) / 4;
            }

            srcRect = new Rect(0, adjustment, original.getWidth(), original.getHeight() - adjustment);
        } else {
            float factor = calculateFactor(original.getHeight(), MODE_PRE_SCALE);
            mCurrentFactorMultiplier = (factor - 1) * getHeight() / 100;

            mParallaxBackground = Bitmap.createBitmap(getWidth(), (int) (getHeight() * factor), Bitmap.Config.RGB_565);
            canvas = new Canvas(mParallaxBackground);

            int adjustment = 0;
            // we crop the original image up and down, as it has been expanded to FACTOR
            // you can play with the Adjustement value to crop left, center or right.
            // I only use center so its hardcoded.
            float scaledBitmapFinalWidth = original.getWidth() * mParallaxBackground.getHeight() / original.getHeight();
            if (scaledBitmapFinalWidth > mParallaxBackground.getWidth()) {
                // as expected, we have to crop up&down to maintain aspect ratio
                adjustment = (int) (scaledBitmapFinalWidth - mParallaxBackground.getWidth()) / 4;
            }

            srcRect = new Rect(0, adjustment, original.getWidth() - adjustment, original.getHeight());
        }
        Rect destRect = new Rect(0, 0, mParallaxBackground.getWidth(), mParallaxBackground.getHeight());
        canvas.drawBitmap(original, srcRect, destRect, null);

        invalidate();
    }

    private void drawPostScale(Bitmap original) {
        mParallaxBackground = original;
        float factor = calculateFactor(mParallaxOrientation == PARALLAX_HORIZONTAL
                ? mParallaxBackground.getWidth()
                : mParallaxBackground.getHeight(), MODE_POST_SCALE);
        mCurrentFactorWidth = (int) (mParallaxBackground.getWidth() / factor);
        mCurrentFactorHeight = (int) (mParallaxBackground.getHeight() / factor);
        if (mParallaxOrientation == PARALLAX_HORIZONTAL) {
            mCurrentFactorMultiplier = (factor - 1) * mCurrentFactorWidth / 100;
            mSrcRect = new Rect(0, 0, mCurrentFactorWidth, mParallaxBackground.getHeight());
        } else {
            mCurrentFactorMultiplier = (factor - 1) * mCurrentFactorHeight / 100;
            mSrcRect = new Rect(0, 0, mParallaxBackground.getWidth(), mCurrentFactorHeight);
        }
        mDestRect = new Rect(0, 0, getWidth(), getHeight());

        invalidate();
    }

    /**
     * calculate the scaling factor of {@link #mParallaxBackground image}
     *
     * @param length
     * @param mode
     * @return
     */
    private float calculateFactor(int length, @ParallaxMode int mode) {
        float basicFactor = 1.5f; // How much a image will be scaled
        switch (mode) {
            case MODE_PRE_SCALE:
                int compareLength = mParallaxOrientation == PARALLAX_HORIZONTAL ? getWidth() : getHeight();
                if (length <= compareLength) {
                    return calculateFactor((int) (length * basicFactor), MODE_PRE_SCALE);
                }

                float factor = length / compareLength;
                if (factor < basicFactor) {
                    return calculateFactor((int) (length * basicFactor / factor), MODE_PRE_SCALE);
                }
                return factor;
            case MODE_POST_SCALE:
                return basicFactor;
            default:
                throw new IllegalStateException("parallax mode undefined");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isParallax && mParallaxBackground != null) {
            int oxb = (int) (mCurrentFactorMultiplier * mParallaxPercent);
            switch (mParallaxMode) {
                case MODE_PRE_SCALE:
                    // You currently cannot enable hardware acceleration at the view level.
                    // View layers have other functions besides disabling hardware acceleration.
                    // See https://developer.android.com/guide/topics/graphics/hardware-accel.html#layers
                    setLayerType(LAYER_TYPE_SOFTWARE, null);
                    if (mParallaxOrientation == PARALLAX_HORIZONTAL) {
                        canvas.drawBitmap(mParallaxBackground, -oxb, 0, null);
                    } else {
                        canvas.drawBitmap(mParallaxBackground, 0, -oxb, null);
                    }
                    setLayerType(LAYER_TYPE_NONE, null);
                    break;
                case MODE_POST_SCALE:
                    if (mParallaxOrientation == PARALLAX_HORIZONTAL) {
                        mSrcRect.left = oxb;
                        mSrcRect.right = mCurrentFactorWidth + oxb;
                    } else {
                        mSrcRect.top = oxb;
                        mSrcRect.bottom = mCurrentFactorHeight + oxb;
                    }
                    setLayerType(LAYER_TYPE_SOFTWARE, null);
                    canvas.drawBitmap(mParallaxBackground, mSrcRect, mDestRect, null);
                    setLayerType(LAYER_TYPE_NONE, null);
                    break;
                default:
                    throw new IllegalStateException("parallax mode undefined");
            }
        } else {
            super.onDraw(canvas);
        }
    }
}
