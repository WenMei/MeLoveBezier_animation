package com.me.melovebezier_animation;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by wmei on 2017/3/29 0029.
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {


    private final PointF point2;
    private final PointF point1;

    public BezierEvaluator(PointF pointF1, PointF pointF2){
        this.point1 = pointF1;
        this.point2 = pointF2;
    }

    /**
     * fraction 百分比 0-1.0f,在该duration时间段里面任何时间点完成度
     *
     */
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF pointF = new PointF();
        pointF.x = startValue.x*(1-fraction)*(1-fraction)*(1-fraction)
                    +3*point1.x*fraction*(1-fraction)*(1-fraction)
                    +3*point2.x*fraction*fraction*(1-fraction)
                    +endValue.x*fraction*fraction*fraction;

        pointF.y = startValue.y*(1-fraction)*(1-fraction)*(1-fraction)
                +3*point1.y*fraction*(1-fraction)*(1-fraction)
                +3*point2.y*fraction*fraction*(1-fraction)
                +endValue.y*fraction*fraction*fraction;


        return pointF;
    }
}
