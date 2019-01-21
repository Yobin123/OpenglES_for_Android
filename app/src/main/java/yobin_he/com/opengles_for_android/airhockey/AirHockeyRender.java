package yobin_he.com.opengles_for_android.airhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.utils.LoggerConfig;
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
 *       4.
 *
 */

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static final String TAG = "AirHockeyRender";
    private static final String U_COLOR = "u_Color";
    private static final  String A_POSITION = "a_Position";
    private static int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;


    public AirHockeyRender(Context context) {
        this.context = context;

        /*
		float[] tableVertices = {
			0f,  0f,
			0f, 14f,
			9f, 14f,
			9f,  0f
		};
         */
        /*
		float[] tableVerticesWithTriangles = {
			// Triangle 1
			0f,  0f,
			9f, 14f,
			0f, 14f,

			// Triangle 2
			0f,  0f,
			9f,  0f,
			9f, 14f
			// Next block for formatting purposes
			9f, 14f,
			, // Comma here for formatting purposes

			// Line 1
			0f,  7f,
			9f,  7f,

			// Mallets
			4.5f,  2f,
			4.5f, 12f
		};
         */

        //以逆时针的顺序排列顶点，这是卷曲顺序
        float[] tableVertices = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f,  0.5f,
                -0.5f,  0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f,  0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f,  0.25f
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
        vertexData.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);

        //获取着色器资源。
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);

        //创建顶点着色器和片元着色器
        int vertexShader = OpenglesHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = OpenglesHelper.compileFragmentShader(fragmentShaderSource);

        program = OpenglesHelper.linkProgram(vertexShader,fragmentShader);
        if(LoggerConfig.ON){
            OpenglesHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);

        uColorLocation = GLES20.glGetUniformLocation(program,U_COLOR);  //其中uniform或者attribute跟raw中对应的一致。
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

        //关联属性与顶点数据坐标
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,0,vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height); // 设置视图
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        //绘制桌面
        GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

        //绘制中间分割线
        GLES20.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        //绘制第一个球
        GLES20.glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        //绘制第二个球
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }
}
