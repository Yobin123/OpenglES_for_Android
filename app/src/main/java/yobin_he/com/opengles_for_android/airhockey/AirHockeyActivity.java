package yobin_he.com.opengles_for_android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import yobin_he.com.opengles_for_android.BaseActivity;
import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * 用于进行曲棍球游戏
 */
public class AirHockeyActivity extends BaseActivity {
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
            surfaceView.setRenderer(new AirHockeyRender());
            isRenderSet = true;
        }else {
            Toast.makeText(context,"your device cannot support opengl es 2.0",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
