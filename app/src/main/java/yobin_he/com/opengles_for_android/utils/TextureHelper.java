package yobin_he.com.opengles_for_android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

import static android.opengl.GLUtils.texImage2D;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: TextureHelper
 * @Date : 2019/1/22  17:12
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class TextureHelper {
   private final static String TAG = "TextureHelper";

    /**
     * 加载纹理id
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(Context context,int resourceId){
        final  int[] textureObjectIds = new int[1];
        //生成纹理id
        GLES20.glGenTextures(1,textureObjectIds,0);
        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not generate a new OpenGL texture Object");
            }
            return 0;
        }
        //进行解码
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId,options);
        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG,"Resource ID" + resourceId + "could not be decoded");
            }
            GLES20.glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }
        //绑定纹理对象id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);

        //设置默认的纹理过滤参数
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        //加载纹理到OpenGL并返回其ID
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();//释放bitmap
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D); //生成贴图
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);//解除纹理绑定，防止其他纹理调用意外改变这个纹理
        return textureObjectIds[0];
    }

}
