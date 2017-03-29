package com.me.melovebezier_animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by wmei on 2017/3/29 0029.
 */

public class LoveBezierRelativeLayout extends RelativeLayout {

    Drawable[] drawables = new Drawable[4];
    private Random random = new Random();
    //图片的宽高
    private int dHeight;
    private int dWidth;
    private RelativeLayout.LayoutParams params;
    //屏幕的宽高
    private int mWidth;
    private int mHeight;

    //加速 减速
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dec = new AccelerateInterpolator();
    private Interpolator accdec = new AccelerateInterpolator();
    private Interpolator[] interpolators;

    public LoveBezierRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public LoveBezierRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoveBezierRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        interpolators = new Interpolator[3];
        interpolators[0] = acc;
        interpolators[1] = dec;
        interpolators[2] = accdec;


        drawables[0] = getResources().getDrawable(R.mipmap.heart_blue);
        drawables[1] =getResources().getDrawable(R.mipmap.heart_green);
        drawables[2] = getResources().getDrawable(R.mipmap.heart_red);
        drawables[3] = getResources().getDrawable(R.mipmap.heart_y);

        //图片的宽度高度
        dWidth = drawables[0].getIntrinsicWidth();
        dHeight = drawables[0].getIntrinsicHeight();
        params = new LayoutParams(dWidth,dHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public  void addLove(){
        final ImageView iv = new ImageView(getContext());
        iv.setImageDrawable(drawables[random.nextInt(4)]);
        //添加图片 水平居中 底部
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(ALIGN_PARENT_BOTTOM);
        addView(iv,params);

        //添加动画：1.缩放 2.平移 3.渐变
        AnimatorSet set = getAnimator(iv);

        //添加监听 动画执行完后 移除iv
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(iv);
            }
        });

        //开启属性动画
        set.start();

    }

    private AnimatorSet getAnimator(ImageView iv) {
        //  1.缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv,"scaleX",0.4f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv,"scaleY",0.4f,1f);
        //  2.alpha
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv,"alpha",0.4f,1f);
        //三个动画同时执行
        AnimatorSet enterSet = new AnimatorSet();
        enterSet.setDuration(500);
        enterSet.playTogether(scaleX,scaleY,alpha);

        //3.贝赛尔动画（Evaluator估值器）
        ValueAnimator bezierAnimator = getBezierAnimator(iv);

        //综合所有属性动画的集合
        AnimatorSet set = new AnimatorSet();
        //按序列执行
        set.playSequentially(enterSet,bezierAnimator);
        set.setInterpolator(interpolators[random.nextInt(3)]);
        set.setTarget(iv);

        return set;
    }

    private ValueAnimator getBezierAnimator(final ImageView iv) {
        //贝赛尔曲线--准备四个关键坐标点（起始点p0，拐点p1，拐点p2，重点p3）
        PointF pointF0 = new PointF((mWidth-dWidth)/2,mHeight-dHeight);
        PointF pointF1 = getTogglePointF(0);
        PointF pointF2 = getTogglePointF(1);
        PointF pointF3 = new PointF(random.nextInt(mWidth),0);

        BezierEvaluator evaluator = new BezierEvaluator(pointF1,pointF2);
        //属性动画不仅仅可以改变view的属性，还可以改变自定义的一些属性（比如：PointF）
        ValueAnimator animator = ValueAnimator.ofObject(evaluator,pointF0,pointF3);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                //控制属性的变化
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                iv.setAlpha(1-animation.getAnimatedFraction());//1-0
            }
        });

        return animator;
    }

    private PointF getTogglePointF(int i) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(mWidth);
        if(i==0){//下面的p1
            pointF.y = random.nextInt(mHeight/2)+mHeight/2;//范围 h/2-h
        }else if(i==1){//上半部分的p2
            pointF.y = random.nextInt(mHeight/2);//范围 0-h/2
        }

        return pointF;
    }

}
