package com.yfz.main;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 作者：游丰泽
 * 简介：仿刮刮乐的自绘view
 */
public class YFZScratchCardView extends View {
    private Context mContext;
    private Drawable mFrontDrawable;
    private Bitmap mFrontBitmap;
    private Drawable mBackGroundDrawable;
    private Canvas mCanvasFront;
    private RectF mRectF;
    private Paint mPaint;
    private double mDownX,mDownY;
    private int mDistanceX,mDistanceY;
    private int mScratchSize=5;
    public YFZScratchCardView(Context context) {
        super(context);
        init(context);
    }

    public YFZScratchCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YFZScratchCardView);
        mFrontDrawable=typedArray.getDrawable(R.styleable.YFZScratchCardView_yfz_frontDrawable);
        mBackGroundDrawable=typedArray.getDrawable(R.styleable.YFZScratchCardView_yfz_backGroundDrawable);
        mScratchSize=typedArray.getInteger(R.styleable.YFZScratchCardView_yfz_scratchSize,mScratchSize);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if( null == mFrontBitmap && null != mFrontDrawable){
            int width= MeasureSpec.getSize(widthMeasureSpec);
            int height= MeasureSpec.getSize(heightMeasureSpec);
            mFrontBitmap= drawableToBitmap(mFrontDrawable,width,height);
        }
    }

    private void init(Context context){
        mContext=context;
        mCanvasFront=new Canvas();
        mRectF=new RectF();
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        if(null != mBackGroundDrawable) {
            setBackground(mBackGroundDrawable);
        }

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.left=0;
        mRectF.top=0;
        mRectF.right=getWidth();
        mRectF.bottom=getHeight();
        canvas.drawBitmap(mFrontBitmap,0,0,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX=event.getX();
                mDownY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mDistanceX=(int)(event.getX()*1);
                mDistanceY=(int)(event.getY()*1);

                if(mDistanceX<getWidth() && mDistanceY<getHeight() ) {
                    mFrontBitmap.setPixel(mDistanceX >= 0 ? mDistanceX : 0, mDistanceY >= 0 ? mDistanceY : 0, Color.TRANSPARENT);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private Bitmap drawableToBitmap(Drawable drawable,int width,int height) {
        Bitmap bitmap = Bitmap.createBitmap(
                    width,
                    height,
                   drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(mCanvasFront);
        return bitmap;
    }
}
