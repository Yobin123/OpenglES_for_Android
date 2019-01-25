package yobin_he.com.opengles_for_android.program;

import android.content.Context;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL;

import yobin_he.com.opengles_for_android.R;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.program
 * @fileName: ColorShaderProgram
 * @Date : 2019/1/23  11:11
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ColorMalletShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uColorLocation;

    private  final  int aPositionLocation;
    public ColorMalletShaderProgram(Context context) {
        super(context, R.raw.mallet_vertex_shader, R.raw.mallet_fragment_shader);
        //uniform所在位置
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uColorLocation = GLES20.glGetUniformLocation(program,U_COLOR);
        //属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

    }


    public void setUniforms(float[] matrix,float r,float g,float b){
        //传入matrix
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform4f(uColorLocation,r,g,b,1f);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }


}
