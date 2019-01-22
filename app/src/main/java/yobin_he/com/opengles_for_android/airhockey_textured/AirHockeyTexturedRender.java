package yobin_he.com.opengles_for_android.airhockey_textured;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.utils.LoggerConfig;
import yobin_he.com.opengles_for_android.utils.MatrixHelper;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;
import yobin_he.com.opengles_for_android.utils.TextResourceReader;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.airhockey
 * @fileName: AirHockeyRender
 * @Date : 2019/1/18  17:26
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 *  步骤：1.创建字节缓存
 *       2.获取着色器资源
 *       3.编译顶点和片元着色器
 *       4.链接程序并使用
 */

public class AirHockeyTexturedRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private static final String A_COLOR = "a_Color";
    private static final  String A_POSITION = "a_Position";
    private static final  String U_MATRIX = "u_Matrix";


    private static int POSITION_COMPONENT_COUNT = 2; //两个代表一个位置坐标
    private static int COLOR_COMPONENT_COUNT = 3; //三个数值代表一个颜色坐标
    private static final int BYTES_PER_FLOAT = 4; //一个float数据占用4个字节。
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;//跨度。用于区分颜色或者坐标分量。

    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private int aPositionLocation; //保存顶点位置
    private int aColorLocation; //保存颜色位置
    private int uMatrixLocation; // 用于保存矩阵uniform的位置

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];





    public AirHockeyTexturedRender(Context context) {
        this.context = context;


    //增加 z 和 w 分量使之更加像3d
//        float[] tableVertices = {
//                //坐标顺序
//                // x,y,z,w,R,G,B,A
//                0f,       0f,   0f,  1.5f,   1f,   1f,   1f,
//                -0.5f, -0.8f,   0f,    1f, 0.7f, 0.7f, 0.7f,
//                0.5f,  -0.8f,   0f,    1f, 0.7f, 0.7f, 0.7f,
//                0.5f,   0.8f,   0f,    2f, 0.7f, 0.7f, 0.7f,
//                -0.5f,  0.8f,   0f,    2f, 0.7f, 0.7f, 0.7f,
//                -0.5f, -0.8f,   0f,    1f, 0.7f, 0.7f, 0.7f,
//
//                // Line 1
//                -0.5f,    0f,   0f,  1.5f,   1f,   0f,   0f,
//                0.5f,     0f,   0f,  1.5f,   1f,   0f,   0f,
//
//                // Mallets
//                0f,    -0.4f,   0f, 1.25f,    0f,  0f,   1f,
//                0f,     0.4f,   0f, 1.75f,    1f,  0f,   0f
//        };


        float[] tableVertices = {
                // Order of coordinates: X, Y, R, G, B
                // Triangle Fan
                0f,     0f,    1f,    1f,    1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f,  0.4f, 1f, 0f, 0f
        };

        /**
         *OpenGL作为本地系统库直接运行在硬件；没有虚拟机，也没有垃圾回收或内存压缩
         * 从java调用本地代码技术
         * 1.JNI
         * 2.利用特殊集合类，可以分配本地内存块，并且把java数据复制到本地保存，本地内存可以被本地环境存取，
         * 不受垃圾回收器管控。
         */
        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT) //java浮点数有32位即每个浮点数占用四个字节
                .order(ByteOrder.nativeOrder()) //告诉字节缓冲区按照本地字节序组织内容（不同的平台有不同的排序）
                .asFloatBuffer();
        vertexData.put(tableVertices);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);

        //获取着色器资源。
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.ortho_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.ortho_fragment_shader);

        //创建顶点着色器和片元着色器
        int vertexShader = OpenglesHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = OpenglesHelper.compileFragmentShader(fragmentShaderSource);

        program = OpenglesHelper.linkProgram(vertexShader,fragmentShader);
        if(LoggerConfig.ON){
            OpenglesHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);

        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);  //其中uniform或者attribute跟raw中对应的一致。
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_COLOR_LOCATION.(由变量vertexData指定，绑定数据到顶点属性在颜色分量的位置)
        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height); // 设置视图

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width / (float) height,1f,10f);

        Matrix.setIdentityM(modelMatrix,0); //设置成单位矩阵
        Matrix.translateM(modelMatrix,0,0f,0f,-2.5f); //沿着z轴进行平移
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f); //沿着x轴进行旋转

        final  float[] temp = new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //给着色器传递正交投影
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);

//        //绘制桌面

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);

        //绘制中间分割线

        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        //绘制第一个球

        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);


        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }
}
