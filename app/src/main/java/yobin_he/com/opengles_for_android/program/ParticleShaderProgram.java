package yobin_he.com.opengles_for_android.program;

import android.content.Context;
import android.opengl.GLES20;

import yobin_he.com.opengles_for_android.R;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.program
 * @fileName: ParticleShaderProgram
 * @Date : 2019/1/31  13:42
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ParticleShaderProgram extends BaseParticleShaderProgram {
    //uniform locations
    private final int uMatrixLocation;
    private final int uTimeLocation;
    private final int uTextureUhitLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;




    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);

        //为着色器程序检索uniform位置
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = GLES20.glGetUniformLocation(program, U_TIME);
        uTextureUhitLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);

        //为着色器程序提取属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = GLES20.glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = GLES20.glGetAttribLocation(program, A_PARTICLE_START_TIME);
    }

    public void setUniforms(float[] matrix ,float elapsedTime,int textureId){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform1f(uTimeLocation,elapsedTime);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glUniform1i(uMatrixLocation,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }

    public int getDirectionVectorLocation(){
        return aDirectionVectorLocation;
    }

    public int getParticleStartTimeLocation(){
        return aParticleStartTimeLocation;
    }
}
