package yobin_he.com.opengles_for_android.airhockey_1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import yobin_he.com.opengles_for_android.BaseActivity;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * 用于进行曲棍球游戏
 */
public class AirHockeyActivity1 extends BaseActivity {
    private Context context;
    private GLSurfaceView surfaceView;
    private boolean isRenderSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        surfaceView = new GLSurfaceView(context);
        if(OpenglesHelper.isSupportsVersion(context,OpenglesHelper.OPENGL_VERSION)){
            surfaceView.setEGLContextClientVersion(2);
            surfaceView.setRenderer(new AirHockeyRender1(context));
            isRenderSet = true;
        }else {
            Toast.makeText(context,"your device cannot support opengl es 2.0",Toast.LENGTH_SHORT).show();
            return;
        }
        setContentView(surfaceView);
    }
}
