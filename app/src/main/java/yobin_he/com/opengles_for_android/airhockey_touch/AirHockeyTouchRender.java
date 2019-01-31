package yobin_he.com.opengles_for_android.airhockey_touch;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.widget.Button;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.objects.Mallet;
import yobin_he.com.opengles_for_android.objects.Puck;
import yobin_he.com.opengles_for_android.objects.Table_Mallet;
import yobin_he.com.opengles_for_android.program.ColorMalletShaderProgram;
import yobin_he.com.opengles_for_android.program.TextureShaderProgram;
import yobin_he.com.opengles_for_android.utils.Geometry;
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

public class AirHockeyTouchRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16]; //模型矩阵

    private final float[] viewMatrix = new float[16];  //视图矩阵
    private final float[] viewProjectMatrix = new float[16];
    private final float[] modelViewProjectMatrix = new float[16];

    private final float[] invertedViewProjectionMatrix = new float[16];

    private Table_Mallet table;
    private Mallet mallet;
    private Puck puck;

    private TextureShaderProgram textureProgram;
    private ColorMalletShaderProgram colorProgram;
    private int texture;

    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;
    private Geometry.Point previousBlueMalletPosition;

    private Geometry.Point puckPosition;
    private Geometry.Vector puckVector;

    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float farBound = -0.8f;
    private final float nearBound = 0.8f;


    public AirHockeyTouchRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        table = new Table_Mallet();
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        blueMalletPosition = new Geometry.Point(0f,mallet.height / 2f,0.4f);
        puckPosition = new Geometry.Point(0f,puck.height / 2f,0f);
        puckVector = new Geometry.Vector(0f,0f,0f);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorMalletShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);//投影矩阵。
        Matrix.setLookAtM(viewMatrix, //目标数组，以便能存储图形矩阵
                0, //偏移值
                0f, 1.2f, 2.2f, //眼睛所在位置
                0f, 0f, 0f,//物体位置
                0f, 1f, 0f); //头指向的地方，upY值为1，意味头直接指向上方。

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        puckPosition = puckPosition.translate(puckVector);
        if(puckPosition.x < leftBound + puck.radius || puckPosition.x > rightBound - puck.radius){
            puckVector = new Geometry.Vector(-puckVector.x,puckVector.y,puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }

        if(puckPosition.z < farBound + puck.radius || puckPosition.z > nearBound - puck.radius){
            puckVector = new Geometry.Vector(puckVector.x,puckVector.y,-puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }

        puckPosition = new Geometry.Point(
                clamp(puckPosition.x, leftBound + puck.radius,rightBound - puck.radius)
                ,puckPosition.y,
                clamp(puckPosition.z,farBound + puck.radius,nearBound - puck.radius));

        puckVector = puckVector.scale(0.99f);

        //将工程矩阵和视图矩阵相乘
        Matrix.multiplyMM(viewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.invertM(invertedViewProjectionMatrix,0,viewProjectMatrix,0);  //反转矩阵

        //绘制桌子
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        //绘制球拍
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectMatrix, 1f, 0f, 0f);
        mallet.bindData(colorProgram);
        mallet.draw();
////
        positionObjectInScene(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z);
        colorProgram.setUniforms(modelViewProjectMatrix, 0f, 0f, 1f);
        //绘制时候我们不必定义对象数据两次，我们只需要绘制再次相同的木槌，但是需要改变位置以及颜色
        mallet.draw();
//
//        //绘制冰球
        positionObjectInScene(puckPosition.x,puckPosition.y,puckPosition.z);
        colorProgram.setUniforms(modelViewProjectMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorProgram);
        puck.draw();


    }

    private void positionTableInScene() { //modelViewProjectMatrix = viewProjectMatrix * modelMatrix;
        Matrix.setIdentityM(modelMatrix, 0);//生成单位矩阵
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f); //绕x旋转负90度
        Matrix.multiplyMM(modelViewProjectMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);
    }


    /**
     * 按压操作
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

        Geometry.Sphere malletBoundingSphere = new Geometry
                .Sphere(new Geometry.Point(blueMalletPosition.x,
                blueMalletPosition.y,
                blueMalletPosition.z),
                mallet.height / 2f);

        malletPressed = Geometry.intersects(malletBoundingSphere, ray);

    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    /**
     * 进行拖拽操作
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if (malletPressed) {
            Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0, 0, 0), new Geometry.Vector(0, 1, 0));

            Geometry.Point touchPoint = Geometry.intersectionPoint(ray, plane);
            previousBlueMalletPosition = blueMalletPosition;

            blueMalletPosition = new Geometry.Point(
                    clamp(touchPoint.x, leftBound + mallet.radius,
                            rightBound - mallet.radius),
                    mallet.height / 2f,
                    clamp(touchPoint.z, 0f + mallet.radius, nearBound - mallet.radius)
            );

            float distance = Geometry.vectorBetween(blueMalletPosition, puckPosition).length();

            if (distance < (puck.radius + mallet.radius)) {
                puckVector = Geometry.vectorBetween(previousBlueMalletPosition, blueMalletPosition);
            }
        }
    }

    private float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(value, min));
    }


    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        Matrix.multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        Matrix.multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
    }
}
