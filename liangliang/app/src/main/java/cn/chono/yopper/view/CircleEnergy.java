package cn.chono.yopper.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.chono.yopper.R;

/**
 * 自定义章节状态控制按钮view
 *
 * @author 杨金宇
 */
public class CircleEnergy extends View {

    public static final int STATUS_INIT = 1;//初始状态
    public static final int STATUS_WORKING = 2;//下载中
    public static final int STATUS_PAUSED = 3;//暂停中
    public static final int STATUS_PLAY = 4;//可播放

    private int mStatus = STATUS_INIT;

    private boolean mIsInitialized;

    private int startAngle = 120;
    private int sweepAngle = 300;

    private Paint mOutCiclePaint;//外圈画笔
    private Paint mProgressPaint;//进度画笔

    private float mCx, mCy;//圆心
    private float mRadius;//环半径
    private float mSweep;//百分比转换为角度
    private float mPaintWidth = 3;
    private RectF mOval;

    private Paint mInnerTextPaint;
    private int mInnerText = 0;
    private float mInnerTextHeight;//获取字体大小的高度,drawtext的过程中x坐标可以用center设置中心,但有y只能通过当前坐标+字体高度来设置
    private int mInnerTextSize = 60;

    private int mInnerTextColor;

    private Paint mEnergyPaint;
    private float energyFontHeight;
    private String mEnergyText = "能量";
    private int mEnergyTextSize = 10;

    private int mTextHeightSpace;

    private Paint mCircleBackgroudPaint;
    private int mCircleBackgroudColor;
    private boolean hasBackgroud;
//    private Paint mInnerPaint;//内部形状画笔
//    private int mInitColor;
//    private Path mDownloadPath;
//    private Path mPlayPath;
//    private Path mPausePath1;
//    private Path mPausePath2;
//    private int mPlayColor;
//    private int mDownloadColor;


    public CircleEnergy(Context context) {
        super(context);
    }

    public CircleEnergy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleEnergy(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int dimen, int padding) {
        Resources rs = getResources();
//        mInitColor = rs.getColor(R.color.color_ffffff);
//        mDownloadColor = Color.BLUE;
//        mPlayColor = Color.RED;
        mPaintWidth = dp2px(getContext(),mPaintWidth);
        float width = (float) ((dimen - padding * 2) / 2.0);
        mCx = mCy = (float) (dimen / 2.0);
        mOutCiclePaint = new Paint();
        mOutCiclePaint.setStyle(Paint.Style.STROKE);
        mOutCiclePaint.setAntiAlias(true);
//        float strokeWidth = (float) (7 * width / 30.0);
        mOutCiclePaint.setStrokeWidth(mPaintWidth);
        mOutCiclePaint.setColor(getResources().getColor(R.color.color_ffd5ce));
        mRadius = (float) (width - mPaintWidth * 2);

        float offset = mPaintWidth / 2;
        mOval = new RectF(mCx - width + offset, mCy - width + offset, mCx + width - offset, mCy + width - offset);
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(getResources().getColor(R.color.color_ff7462));
        mProgressPaint.setStrokeWidth(mPaintWidth);

        mInnerTextPaint = new Paint();
        mInnerTextPaint.setColor(mInnerTextColor);
        mInnerTextPaint.setAntiAlias(true);
        mInnerTextPaint.setTextSize(dp2px(getContext(), mInnerTextSize));
        mInnerTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = mInnerTextPaint.getFontMetrics();
        mInnerTextHeight = metrics.bottom - metrics.top;
        mInnerTextHeight = mInnerTextHeight / 2 - metrics.bottom;

        mEnergyPaint = new Paint();
        mEnergyPaint.setColor(mInnerTextColor);
        mEnergyPaint.setAntiAlias(true);
        mEnergyPaint.setTextSize(dp2px(getContext(), mEnergyTextSize));
        mEnergyPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metricsEnergy = mEnergyPaint.getFontMetrics();
        energyFontHeight = metricsEnergy.bottom - metricsEnergy.top;
        energyFontHeight = energyFontHeight / 2 - metricsEnergy.bottom;

        mCircleBackgroudPaint = new Paint();
        mCircleBackgroudPaint.setColor(mCircleBackgroudColor);

//        mDownloadPath = new Path();
//        mDownloadPath.moveTo((float)(mCx-width/6.0), (float)(mCy-11*width/30.0));
//        mDownloadPath.lineTo((float)(mCx+width/6.0), (float)(mCy-11*width/30.0));
//        mDownloadPath.lineTo((float)(mCx+width/6.0), mCy);
//        mDownloadPath.lineTo((float)(mCx+11*width/30.0), mCy);
//        mDownloadPath.lineTo(mCx, (float)(mCy+16*width/30.0));
//        mDownloadPath.lineTo((float)(mCx-11*width/30.0), mCy);
//        mDownloadPath.lineTo((float)(mCx-width/6.0), mCy);
//        mDownloadPath.lineTo((float)(mCx-width/6.0), (float)(mCy-11*width/30.0));
//
//        mPlayPath = new Path();
//        mPlayPath.moveTo((float)(mCx-7*width/30.0), (float)(mCy-13*width/30.0));
//        mPlayPath.lineTo((float)(mCx+width/2.0), mCy);
//        mPlayPath.lineTo((float)(mCx-7*width/30.0), (float)(mCy+13*width/30.0));
//
//        mPausePath1 = new Path();
//        mPausePath1.moveTo((float)(mCx-width/3.0), (float)(mCy-11*width/30.0));
//        mPausePath1.lineTo((float)(mCx-width/10.0), (float)(mCy-11*width/30.0));
//        mPausePath1.lineTo((float)(mCx-width/10.0), (float)(mCy+11*width/30.0));
//        mPausePath1.lineTo((float)(mCx-width/3.0), (float)(mCy+11*width/30.0));
//        mPausePath2 = new Path();
//        mPausePath2.moveTo((float)(mCx+width/3.0), (float)(mCy-11*width/30.0));
//        mPausePath2.lineTo((float)(mCx+width/10.0), (float)(mCy-11*width/30.0));
//        mPausePath2.lineTo((float)(mCx+width/10.0), (float)(mCy+11*width/30.0));
//        mPausePath2.lineTo((float)(mCx+width/3.0), (float)(mCy+11*width/30.0));


//        mInnerPaint = new Paint();
//        mInnerPaint.setAntiAlias(true);
//        mInnerPaint.setStyle(Paint.Style.FILL);


        mIsInitialized = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsInitialized) {
            init(getWidth(), getPaddingLeft());
        }
        canvas.drawColor(Color.TRANSPARENT);
        if (mStatus == STATUS_INIT) {
//            mOutCiclePaint.setColor(mInitColor);
//            mInnerPaint.setColor(mInitColor);
//            canvas.drawCircle(mCx, mCy, mRadius, mOutCiclePaint);
//            canvas.drawPath(mDownloadPath, mInnerPaint);
        } else if (mStatus == STATUS_WORKING) {
//            mInnerPaint.setColor(mDownloadColor);
//            canvas.drawPath(mDownloadPath, mInnerPaint);
//            mOutCiclePaint.setColor(mPlayColor);
//            canvas.drawCircle(mCx, mCy, mRadius, mOutCiclePaint);//底部为圆
            if(hasBackgroud){
                canvas.drawCircle(mCx, mCy, mRadius,  mCircleBackgroudPaint);//背景
            }
            canvas.drawArc(mOval, startAngle, sweepAngle, false, mOutCiclePaint);//底部为圆弧
            canvas.drawArc(mOval, startAngle, mSweep, false, mProgressPaint);
            canvas.drawText(mInnerText + "", mCx, mCy + mInnerTextHeight / 2 - mTextHeightSpace, mInnerTextPaint);
            canvas.drawText(mEnergyText, mCx, mCy + mInnerTextHeight + energyFontHeight / 2 + mTextHeightSpace, mEnergyPaint);
        } else if (mStatus == STATUS_PAUSED) {
//            mInnerPaint.setColor(mInitColor);
//            canvas.drawPath(mPausePath1, mInnerPaint);
//            canvas.drawPath(mPausePath2, mInnerPaint);
//            mOutCiclePaint.setColor(mInitColor);
//            canvas.drawCircle(mCx, mCy, mRadius, mOutCiclePaint);
//            canvas.drawArc(mOval, -90, mSweep, false, mProgressPaint);
        } else if (mStatus == STATUS_PLAY) {
//            mOutCiclePaint.setColor(mPlayColor);
//            mInnerPaint.setColor(mPlayColor);
//            canvas.drawCircle(mCx, mCy, mRadius, mOutCiclePaint);
//            canvas.drawPath(mPlayPath, mInnerPaint);
        }

    }

    public void setStartAngleAndSweepAngle(int start, int sweep) {
        startAngle = start;
        sweepAngle = sweep;
    }

    public void setStatus(int status) {
        mStatus = status;
        invalidate();
    }

    public void setProgress(float progress) {
        mSweep = progress * sweepAngle;
        invalidate();
    }

    public void setSweepAngle(int angle) {
        sweepAngle = angle;
    }

    public void setStartAngle(int angle) {
        startAngle = angle;
    }

    public void setInnerText(int energy) {
        mInnerText = energy;
    }

    public void setEnergyText(String s) {
        mEnergyText = s;
    }

    public void setInnerTextSize(int size) {
        mInnerTextSize = size;
    }

    public void setEnergyTextSize(int size) {
        mEnergyTextSize = size;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setPaintWidth(float width){
        mPaintWidth = width;
    }

    public void setTextHeightSpace(int f){
        mTextHeightSpace = f;
    }

    public void setCircleBackgroudColor(int color){
        hasBackgroud = true;
        mCircleBackgroudColor = color;
    }

    public void setInnerTextColor(int color){
        mInnerTextColor = color;
    }

    public static int dp2px(Context c,float dp){
        System.out.println("density:"+c.getResources().getDisplayMetrics().density);
        return (int)(dp * (c.getResources().getDisplayMetrics().density));
    }
    public static int px2dp(Context c,float px){
        return (int)(px / c.getResources().getDisplayMetrics().density);
    }
}