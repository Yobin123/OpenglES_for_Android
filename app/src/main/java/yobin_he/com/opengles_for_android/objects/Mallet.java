package yobin_he.com.opengles_for_android.objects;

import java.util.List;

import yobin_he.com.opengles_for_android.data.VertexArray;
import yobin_he.com.opengles_for_android.program.ColorMalletShaderProgram;
import yobin_he.com.opengles_for_android.program.ColorShaderProgram;
import yobin_he.com.opengles_for_android.utils.Geometry;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.objects
 * @fileName: Mallet
 * @Date : 2019/1/24  14:39
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Mallet {
    private static final  int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height,int numPointsAroundMallet) {
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createMallet(new Geometry.Point(0f,0f,0f),radius,height,numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    public void bindData(ColorMalletShaderProgram colorProgram){
        vertexArray.setVertexAttributePointer(0,
                colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT,0);
    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
