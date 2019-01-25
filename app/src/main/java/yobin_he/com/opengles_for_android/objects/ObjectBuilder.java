package yobin_he.com.opengles_for_android.objects;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import yobin_he.com.opengles_for_android.utils.Geometry;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.objects
 * @fileName: ObjectBuilder
 * @Date : 2019/1/24  10:54
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ObjectBuilder {
    private static final  int FLOATS_PER_VERTEX = 3;
    private  final  float[] vertexData;
    private  int offset = 0;
    private final List<DrawCommand> drawList = new ArrayList<>();

    public ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    static class GenerateData{
        final float[] vertexData;
        final List<DrawCommand> drawList;

        public GenerateData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    /**
     *  生成冰球
     * @param puck
     * @param numPoints
     * @return
     */
    static GenerateData createPuck(Geometry.Cylinder puck,int numPoints){
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size); //size为点的个数
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height / 2f),puck.radius);
        builder.appendCircle(puckTop,numPoints);
        builder.appendOpenCylinder(puck,numPoints);
        return builder.build();
    }

    /**
     * 构造木槌
     * @param center
     * @param radius
     * @param height
     * @param numPoints
     * @return
     */
    static GenerateData createMallet(Geometry.Point center,float radius,float height,int numPoints){
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);
        //first,generate the mallet base;
        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight),radius);

        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f),radius,baseHeight);

        builder.appendCircle(baseCircle,numPoints);
        builder.appendOpenCylinder(baseCylinder,numPoints);
        //生成木槌把手
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Geometry.Circle handleCircle = new Geometry.Circle(center.translateY(height * 0.5f),handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(handleCircle.
                center.translateY(-handleHeight / 2f),handleRadius,handleHeight);
        builder.appendCircle(handleCircle,numPoints);
        builder.appendOpenCylinder(handleCylinder,numPoints);
        return builder.build();
    }


    /**
     * 圆柱体顶部顶点数量
     * @param numPoints
     * @return
     */
    private static int sizeOfCircleInVertices(int numPoints){
        return 1 + (numPoints + 1);
    }

    /**
     * 圆柱体侧面顶点的数量
     * @param numPoints
     * @return
     */
    private static int sizeOfOpenCylinderInVertices(int numPoints){
        return (numPoints + 1) * 2;
    }



    static interface DrawCommand{
        void  draw();
    }


    private void appendCircle(Geometry.Circle circle ,int numPoints){
        final  int startVertex = offset / FLOATS_PER_VERTEX;
        final  int numVertices = sizeOfCircleInVertices(numPoints);

        //中心点
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;


        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians =  (( (float)i / (float)numPoints)
                    * (float) Math.PI * 2f); //将点数平均分配到360弧度，得到间隔弧度
            vertexData[offset++] = circle.center.x + circle.radius * (float) Math.cos(angleInRadians); //该点的x坐标
            vertexData[offset++] = circle.center.y; //该点在y轴坐标
            vertexData[offset++] = circle.center.z + circle.radius * (float)Math.sin(angleInRadians); //该点的z轴坐标
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,startVertex,numVertices); //绘制圆弧
            }
        });
    }

    private void appendOpenCylinder(Geometry.Cylinder cylinder, final int numPoints){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final  int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        //围绕中心生成条带，<=是用来完成条带，因为我们想生成的点在起始角度的两倍
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadius = ((float) i /(float)numPoints) * ((float)Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius * (float)Math.cos(angleInRadius);
            float zPosition = cylinder.center.z + cylinder.radius * (float)Math.sin(angleInRadius);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,startVertex,numVertices);
            }
        });

    }

    private GenerateData build(){
        return new GenerateData(vertexData,drawList);
    }



}
