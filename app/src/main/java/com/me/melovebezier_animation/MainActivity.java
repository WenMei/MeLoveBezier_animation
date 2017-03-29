package com.me.melovebezier_animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoveBezierRelativeLayout loveBezierRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loveBezierRelativeLayout= (LoveBezierRelativeLayout) findViewById(R.id.bezier);
    }

    public void start(View view){
        //低耦合高类聚
        loveBezierRelativeLayout.addLove();
    }

}
