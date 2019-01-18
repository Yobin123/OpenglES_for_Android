package yobin_he.com.opengles_for_android.first_project;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import yobin_he.com.opengles_for_android.BaseActivity;
import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * 第一个opengles程序
 * 1.新建openglsufaceview实例
 * 2.检查系统是否支持openGles 2.0
 * 3.设置渲染器
 * 4.同步处理生命周期
 */
public class FirstOpenGlesActivity extends BaseActivity {
    private Context mContext;
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        glSurfaceView = new GLSurfaceView(mContext);

        if(OpenglesHelper.isSupportsVersion(mContext,OpenglesHelper.OPENGL_VERSION)){ // 检查是否支持Opengles 2.0
            glSurfaceView.setEGLContextClientVersion(2); //请求一个opengl es 2.0兼容环
            glSurfaceView.setRenderer(new FirstOpenGLRender(mContext));  //设置渲染器
            renderSet = true; // 渲染器标志设置标志位
        }else {
            Toast.makeText(mContext,"This Devices not support OpenGl es 2.0",Toast.LENGTH_SHORT).show();
            return;
        }

        setContentView(glSurfaceView); // 添加view

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(null != glSurfaceView){
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != glSurfaceView){
            glSurfaceView.onPause();
        }
    }
}
