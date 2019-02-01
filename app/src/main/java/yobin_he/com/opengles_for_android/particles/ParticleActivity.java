package yobin_he.com.opengles_for_android.particles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import yobin_he.com.opengles_for_android.BaseActivity;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * 用于进行曲棍球游戏
 */
public class ParticleActivity extends BaseActivity {
    private Context context;
    private GLSurfaceView surfaceView;
    private boolean isRenderSet = false;
    private ParticleRender render;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        surfaceView = new GLSurfaceView(context);
        if(OpenglesHelper.isSupportsVersion(context,OpenglesHelper.OPENGL_VERSION)){
            surfaceView.setEGLContextClientVersion(2);
            render = new ParticleRender(context);
            surfaceView.setRenderer(render);
            isRenderSet = true;
        }else {
            Toast.makeText(context,"your device cannot support opengl es 2.0",Toast.LENGTH_SHORT).show();
            return;
        }


        setContentView(surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRenderSet){
            surfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isRenderSet){
            surfaceView.onPause();
        }
    }
}
