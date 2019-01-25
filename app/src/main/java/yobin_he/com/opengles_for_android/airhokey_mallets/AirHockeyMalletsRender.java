package yobin_he.com.opengles_for_android.airhokey_mallets;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.objects.Mallet;
import yobin_he.com.opengles_for_android.objects.Puck;
import yobin_he.com.opengles_for_android.objects.Table_Mallet;
import yobin_he.com.opengles_for_android.program.ColorMalletShaderProgram;
import yobin_he.com.opengles_for_android.program.TextureShaderProgram;
import yobin_he.com.opengles_for_android.utils.LoggerConfig;
import yobin_he.com.opengles_for_android.utils.MatrixHelper;
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

public class AirHockeyMalletsRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectMatrix = new float[16];
    private final float[] modelViewProjectMatrix = new float[16];

    private Table_Mallet table;
    private Mallet mallet;
    private Puck puck;

    private TextureShaderProgram textureProgram;
    private ColorMalletShaderProgram colorProgram;
    private int texture;


    public AirHockeyMalletsRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f,0f,0f,0f);

        table = new Table_Mallet();
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorMalletShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width / (float)height,1f,10f);
        Matrix.setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //将工程矩阵和视图矩阵相乘
        Matrix.multiplyMM(viewProjectMatrix,0,projectionMatrix,0,viewMatrix,0);


        //绘制桌子
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectMatrix,texture);
        table.bindData(textureProgram);
        table.draw();

        //绘制球拍
        positionObjectInScene(0f,mallet.height / 2f,-0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectMatrix,1f,0f,0f);
        mallet.bindData(colorProgram);
        mallet.draw();
////
        positionObjectInScene(0f,mallet.height / 2f,0.4f);
        colorProgram.setUniforms(modelViewProjectMatrix,0f,0f,1f);
        //绘制时候我们不必定义对象数据两次，我们只需要绘制再次相同的木槌，但是需要改变位置以及颜色
        mallet.draw();
//
//        //绘制冰球
        positionObjectInScene(0f,puck.height / 2f,0f);
        colorProgram.setUniforms(modelViewProjectMatrix,0.8f, 0.8f, 1f);
        puck.bindData(colorProgram);
        puck.draw();




    }

    private void positionTableInScene(){
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.rotateM(modelMatrix,0,-90f,1f,0f,0f); //绕x旋转负90度
        Matrix.multiplyMM(modelViewProjectMatrix,0,viewProjectMatrix,0,modelMatrix,0);
    }

    private void positionObjectInScene(float x,float y,float z){
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,x,y,z);
        Matrix.multiplyMM(modelViewProjectMatrix,0,viewProjectMatrix,0,modelMatrix,0);
    }
}
