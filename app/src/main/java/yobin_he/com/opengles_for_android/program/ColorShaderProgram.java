package yobin_he.com.opengles_for_android.program;

import android.content.Context;
import android.opengl.GLES20;

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

public class ColorShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private  final  int aPositionLocation;
    private final  int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.ortho_vertex_shader, R.raw.ortho_fragment_shader);
        //uniform所在位置
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        //属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix){
        //传入matrix
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

}
