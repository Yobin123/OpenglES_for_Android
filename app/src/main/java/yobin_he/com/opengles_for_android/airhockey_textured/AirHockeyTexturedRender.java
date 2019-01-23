package yobin_he.com.opengles_for_android.airhockey_textured;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.object.Mallet;
import yobin_he.com.opengles_for_android.object.Table;
import yobin_he.com.opengles_for_android.program.ColorShaderProgram;
import yobin_he.com.opengles_for_android.program.TextureShaderProgram;
import yobin_he.com.opengles_for_android.utils.LoggerConfig;
import yobin_he.com.opengles_for_android.utils.MatrixHelper;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;
import yobin_he.com.opengles_for_android.utils.TextResourceReader;
import yobin_he.com.opengles_for_android.utils.TextureHelper;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.airhockey
 * @fileName: AirHockeyRender
 * @Date : 2019/1/18  17:26
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 * 步骤：1.创建字节缓存
 * 2.获取着色器资源
 * 3.编译顶点和片元着色器
 * 4.链接程序并使用
 */

public class AirHockeyTexturedRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;


    public AirHockeyTexturedRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        table = new Table();
        mallet = new Mallet();

        textureProgram = new TextureShaderProgram(context);

        colorProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height); // 设置视图

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0); //设置成单位矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f); //沿着z轴进行平移
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f); //沿着x轴进行旋转

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //draw the table
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        //draw the mallet
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();

    }
}
