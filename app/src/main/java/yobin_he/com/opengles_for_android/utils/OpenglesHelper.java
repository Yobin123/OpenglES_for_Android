package yobin_he.com.opengles_for_android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;

import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetShaderInfoLog;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: OpenglesHelper
 * @Date : 2019/1/18  16:19
 * @describe ： opengles帮助类
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class OpenglesHelper {
    private static final String TAG = "OpenglesHelper";
    public static final int OPENGL_VERSION = 0x20000;


    /**
     * 判断是否支持版本
     *
     * @param context
     * @param version
     * @return
     */
    public static boolean isSupportsVersion(Context context, int version) {
        final boolean isSupport;
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
//        isSupport = configurationInfo.reqGlEsVersion >= OPENGL_VERSION; //不考虑模拟器等缺陷

        isSupport = configurationInfo.reqGlEsVersion >= version ||    //考虑模拟器的部分缺陷
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")));

        return isSupport;
    }

    /**
     * 编译顶点着色器对象
     *
     * @param shaderCode
     * @return
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 编译片元着色器对象
     *
     * @param shaderCode
     * @return
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }


    /**
     * 步骤：1.创建
     * 2.传入（上传）
     * 3.编译
     * 4.判断编译状态。
     *
     * @param type
     * @param shaderCode
     * @return
     */
    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader");
            }
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode); //传入着色器资源
        GLES20.glCompileShader(shaderObjectId); // 编译着色器
        //获取编译状态
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (LoggerConfig.ON) {
            // Print the shader info log to the Android log output.
            Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                    + glGetShaderInfoLog(shaderObjectId));
        }

        //验证编译状态
        if (compileStatus[0] == 0) {
            //如果失败，删除着色器对象
            GLES20.glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed.");
            }
            return 0;
        }
        return shaderObjectId;
    }

    /**
     * 链接顶点和片元着色器到openGl program中，返回openGL program 对象Id,否则链接失败
     * @param vertexShader
     * @param fragmentShader
     * @return
     */
    public static int linkProgram(int vertexShader,int fragmentShader){
        final int programObjectId = GLES20.glCreateProgram(); // 创建程序

        if (programObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new program");
            }
            return 0;
        }

        GLES20.glAttachShader(programObjectId,vertexShader);
        GLES20.glAttachShader(programObjectId,fragmentShader); //附上着色器

        GLES20.glLinkProgram(programObjectId); //联合着色器

        final  int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_LINK_STATUS,linkStatus,0); //检查链接是否成功
        if(LoggerConfig.ON){
            Log.v(TAG,"Results of linking program:\n" + glGetProgramInfoLog(programObjectId));
        }
        //验证链接状态
        if(linkStatus[0] == 0){
            //如果失败，删除program对象
            GLES20.glDeleteProgram(programObjectId);
            if(LoggerConfig.ON){
                Log.w(TAG,"linking of program failed");
            }
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        GLES20.glValidateProgram(programObjectId);
        final  int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_VALIDATE_STATUS,validateStatus,0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

}
