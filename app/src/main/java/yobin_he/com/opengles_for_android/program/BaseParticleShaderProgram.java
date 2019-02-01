package yobin_he.com.opengles_for_android.program;

import android.content.Context;
import android.opengl.GLES20;

import yobin_he.com.opengles_for_android.utils.OpenglesHelper;
import yobin_he.com.opengles_for_android.utils.TextResourceReader;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.program
 * @fileName: ParticleShaderProgram
 * @Date : 2019/1/31  13:38
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class BaseParticleShaderProgram {
    //uniform constants
    protected static final  String U_MATRIX = "u_Matrix";
    protected static final  String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TIME = "u_Time";

    //attribute constants
    protected static final  String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected  static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final  String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";


    //着色程序
    protected final  int program;
    protected BaseParticleShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){
        // Compile the shaders and link the program.
        program = OpenglesHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }
    public void useProgram(){
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program);
    }

}
