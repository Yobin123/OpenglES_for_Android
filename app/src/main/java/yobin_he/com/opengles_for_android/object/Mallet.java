package yobin_he.com.opengles_for_android.object;

import android.opengl.GLES20;

import yobin_he.com.opengles_for_android.Constants;
import yobin_he.com.opengles_for_android.data.VertexArray;
import yobin_he.com.opengles_for_android.program.ColorShaderProgram;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.object
 * @fileName: Mallet
 * @Date : 2019/1/23  11:07
 * @describe : 木槌数据
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Mallet {
    private static int POSITION_COMPONENT_COUNT = 2;
    private static int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);

        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2);
    }
}
