package com.car.bolang.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.bolang.R;


public class AnimAlertDialog extends AlertDialog {

    private TextView progressTv;
    private ImageView progressImg;
    private CharSequence content;
    //帧动画
    private AnimationDrawable animation;

    public AnimAlertDialog(Context context) {
        super(context, R.style.CustomAlertDialog);
    }
    public AnimAlertDialog(Context context, CharSequence content) {
        super(context, R.style.CustomAlertDialog);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_alert_dialog);

        //点击imageview外侧区域，动画不会消失
        setCanceledOnTouchOutside(false);

        progressImg = (ImageView) findViewById(R.id.anim_dialog_iv);
        progressTv = (TextView) findViewById(R.id.anim_dialog_tv);
        //加载动画资源
        animation = (AnimationDrawable) progressImg.getDrawable();
        if (!TextUtils.isEmpty(content)){
            progressTv.setText(content);
        }
    }

    /*
     * 在AlertDialog的 onStart() 生命周期里面执行开始动画
     */
    @Override
    protected void onStart() {
        super.onStart();
        animation.start();
    }

    /**
     * 在AlertDialog的onStop()生命周期里面执行停止动画
     */
    @Override
    protected void onStop() {
        super.onStop();
        animation.stop();
    }
    public  void setContent(CharSequence content){

    }

}
