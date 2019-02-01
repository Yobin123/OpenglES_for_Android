package yobin_he.com.opengles_for_android.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import yobin_he.com.opengles_for_android.Constants;
import yobin_he.com.opengles_for_android.utils.OpenglesHelper;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.data
 * @fileName: VertexArray
 * @Date : 2019/1/22  17:57
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class VertexArray {
    private final FloatBuffer floatBuffer;
    public VertexArray(float[] vertexData){
        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttributePointer(int dataOffset ,int attributeLocation,int componentCount,int stride){
        floatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,false,stride,floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

    public void  updateBuffer(float[] vertexData,int start,int count){
        floatBuffer.position(start);
        floatBuffer.put(vertexData,start,count);
        floatBuffer.position(0);
    }


}
