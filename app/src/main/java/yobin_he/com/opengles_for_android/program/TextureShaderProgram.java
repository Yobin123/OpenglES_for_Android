package yobin_he.com.opengles_for_android.program;

import android.content.Context;
import android.opengl.GLES10Ext;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL;

import yobin_he.com.opengles_for_android.R;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.program
 * @fileName: TextureShaderProgram
 * @Date : 2019/1/23  9:52
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class TextureShaderProgram extends ShaderProgram {
    //uniform locations
    private final int uMatrixLocation;
    private final  int uTextureUnitLocation;

    private final  int aPositionLocation;
    private final  int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader,R.raw.texture_fragment_shader);

        //为着色器程序检索uniform位置
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);

        //为着色器程序检索属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program,A_TEXTURE_COORDINATES);

    }

    public void setUniforms(float[] matrix,int textureId){
        //将matrix传递给着色器程序
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定纹理到相应单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);


        GLES20.glUniform1i(uTextureUnitLocation,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation(){
        return aTextureCoordinatesLocation;
    }
}
