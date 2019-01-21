package yobin_he.com.opengles_for_android.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: TextResourceReader
 * @Date : 2019/1/21  9:42
 * @describe : 原文件读取
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class TextResourceReader {

    /**
     * 读取 资源文件
     * @param context
     * @param resourceId
     * @return
     */
    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }
        } catch (IOException e) {
           throw  new RuntimeException("Could not open resource " + resourceId,e);
        } catch (Resources.NotFoundException nfe){
            throw new RuntimeException("Resource not found:" + resourceId,nfe);

        }
        return body.toString();
    }


}
