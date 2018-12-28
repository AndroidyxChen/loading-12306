# loading-12306
仿PC端12306的刷新loading的自定义view

效果图：
![实现后的效果图](https://img-blog.csdnimg.cn/20181227201226623.gif)
```
核心代码及实现逻辑如下：
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
        --------------------------------------------------------------------------------------------------------- 
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
```
--------------------- 
###### 本demo已发表在本人的CSDN上，详细内容可到CSDN查看
###### 作者：不二程序员 
###### 来源：CSDN 
###### 原文：https://blog.csdn.net/Cyx77520/article/details/85303981 
--------------------- 
