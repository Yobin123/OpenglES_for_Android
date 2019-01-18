package yobin_he.com.opengles_for_android.first_project;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.first_project
 * @fileName: FirstOpenGLRender
 * @Date : 2019/1/18  16:40
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class FirstOpenGLRender implements GLSurfaceView.Renderer {
    private Context context;
    public FirstOpenGLRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,0.0f,0.0f,0.0f); //设置清空屏幕用的颜色  rgba
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height); // 设置opengl 视图充满整个surfaceView
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //清空屏幕，并用之前调用glClearColor颜色填充整个屏幕
    }
}
