package yobin_he.com.opengles_for_android.airhockey_touch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import yobin_he.com.opengles_for_android.BaseActivity;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * 用于进行曲棍球游戏
 */
public class AirHockeyTouchActivity extends BaseActivity {
    private Context context;
    private GLSurfaceView surfaceView;
    private boolean isRenderSet = false;
    private AirHockeyTouchRender render;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        surfaceView = new GLSurfaceView(context);
        if(OpenglesHelper.isSupportsVersion(context,OpenglesHelper.OPENGL_VERSION)){
            surfaceView.setEGLContextClientVersion(2);
            render = new AirHockeyTouchRender(context);
            surfaceView.setRenderer(render);
            isRenderSet = true;
        }else {
            Toast.makeText(context,"your device cannot support opengl es 2.0",Toast.LENGTH_SHORT).show();
            return;
        }

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event != null){
                    final float normalizedX = (event.getX() / (float)v.getWidth()) * 2 -1; // x的移动距离
                    final  float normalizedY = -((event.getY() / (float)v.getHeight()) * 2 - 1); //Y的移动距离
                    if(event.getAction() == MotionEvent.ACTION_DOWN){

                        surfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                render.handleTouchPress(normalizedX,normalizedY);
                            }
                        });
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        surfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                render.handleTouchDrag(normalizedX,normalizedY);
                            }
                        });
                    }
                    return true;
                }else {
                    return false;
                }
            }
        });

        setContentView(surfaceView);
    }

}
