package yobin_he.com.opengles_for_android.airhockey;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.airhockey
 * @fileName: AirHockeyRender
 * @Date : 2019/1/18  17:26
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class AirHockeyRender implements GLSurfaceView.Renderer {
    private static int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;

    public AirHockeyRender() {
        //以逆时针的顺序排列顶点，这是卷曲顺序
        float[] tableVertices = {
                //三角形1 坐标
                0f,0f,
                9f,14f,
                0f,14f,
                //三角形2 坐标
                0f,0f,
                9f,0f,
                9f,14f,

                //中间线
                0f,7f,
                9f,7f,

                //Mallets
                4.5f,2f,
                4.5f,12f
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


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
