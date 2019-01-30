package yobin_he.com.opengles_for_android.objects;

import java.util.List;

import yobin_he.com.opengles_for_android.data.VertexArray;
import yobin_he.com.opengles_for_android.program.ColorMalletShaderProgram;
import yobin_he.com.opengles_for_android.utils.Geometry;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.objects
 * @fileName: Puck
 * @Date : 2019/1/24  14:32
 * @describe :冰球
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Puck {
    private static final  int POSITION_COMPONENT_COUNT = 3;
    public final float radius,height;
    private final List<ObjectBuilder.DrawCommand> drawList;

    private final VertexArray vertexArray;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        //构造的对象中包含vertexData(顶点数据)，drawList用于绘制
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0,0,0),radius,height),numPointsAroundPuck);

        this.radius = radius;
        this.height = height;
        vertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    /**
     * 绑定数据
     * @param colorProgram
     */
    public void bindData(ColorMalletShaderProgram colorProgram){
        vertexArray.setVertexAttributePointer(0,
                colorProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }

    /**
     * 绘制
     */
    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand: drawList){
            drawCommand.draw();
        }
    }
}
