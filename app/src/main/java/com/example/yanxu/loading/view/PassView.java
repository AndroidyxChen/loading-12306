package com.example.yanxu.loading.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.yanxu.loading.R;


/**
 * Created by chen on 2018/12/12.
 * 仿PC端12306的刷新loading动画的自定义view
 */

public class PassView extends View {

    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mWidth, mHeight;
    private float mRadius = 0f;
    private ValueAnimator animator;
    private int theCircle = -1;//当前哪个圆球变化

    public PassView(Context context) {
        this(context, null);
    }

    public PassView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //初始化
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PassView);
        if (typedArray != null) {
            mColor = typedArray.getColor(R.styleable.PassView_pass_color, Color.RED);
            mRadius = typedArray.getDimension(R.styleable.PassView_pass_radius, 0);
            typedArray.recycle();
        }
        mWidth = (int) (2 * mRadius);
        mHeight = (int) (2 * mRadius);
    }

    /**
     * 重写onMeasure方法，解决wrap_content问题，因为：
     * 直接继承View的自定义控件需要重写onMeasure方法并设置wrap_content时的自身大小，否则在布局中使用wrap_content就相当于使用match_parent
     * 这里的view默认宽高为了方便直接指定为小圆的直径
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    /**
     * 重写onDraw方法实现自己的想要的效果，同时处理padding问题，因为：
     * 绘制的时候需要考虑到View四周的空白，即padding，否则计算会有偏差
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();
        int width = getWidth() - pLeft - pRight;
        int height = getHeight() - pTop - pBottom;
        int mHLength = 150;//大圆的半径
        mPaint.setColor(mColor);
        mPaint.setTextSize(50);
        //1、动画开启前，theCircle的初始值为-1，所以初始化时只走canvas.drawCircle()方法，即所有位置均为蓝色
        //2、动画开启后，theCircle=0时不改变颜色，即所有位置均为蓝色
        //3、继续，theCircle=1、i=2时，第一个位置依旧为蓝色，第二个及剩余其他位置为灰色
        //4、继续，theCircle=2，i=3时，第一、二个位置依旧为蓝色，第三个及剩余其他位置为灰色
        //5、......
        //6、theCircle=11,i=12时，第一到十一个位置全部为蓝色,剩余的第十二个为灰色
        //7、直到，theCircle=0,循环继续
        //注：思路重点为，Paint使用的是同一个，所以在重新setColor()前的颜色是一样的，setColor()后的颜色是另一样的
        for (int i = 1; i <= 12; i++) {
            if (theCircle + 1 == i && theCircle != 0) {
                mPaint.setColor(getResources().getColor(R.color.colorPassGray));
            }
            canvas.drawCircle(width / 2 + (float) Math.sin(Math.PI * (i - 1) / 6) * mHLength,
                    height / 2 - (float) Math.cos(Math.PI * (i - 1) / 6) * mHLength, mRadius, mPaint);
            //Math.PI即为π，等于180度
        }
    }

    //开启动画
    public void start() {
        if (animator != null)
            animator.cancel();
        animator = ValueAnimator.ofInt(0, 12);
        animator.setDuration(2000);//动画时长
        animator.setRepeatMode(ValueAnimator.RESTART);//动画的重复次数
        animator.setRepeatCount(ValueAnimator.INFINITE);//动画的重复模式
        animator.setInterpolator(new LinearInterpolator());//线性插值器：匀速动画
        //监听动画过程
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前动画的进度值，此处的theCircle值为（0、1、2...11、0）
                theCircle = ((int) animation.getAnimatedValue() % 12);
                invalidate();
            }
        });
        animator.start();
    }

}
