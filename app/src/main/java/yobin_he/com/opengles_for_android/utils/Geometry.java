package yobin_he.com.opengles_for_android.utils;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: Geometry
 * @Date : 2019/1/23  16:53
 * @describe : 几何形
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Geometry {

    public static class Point{
       public final float x , y ,z;
       public Point(float x,float y,float z){
           this.x = x;
           this.y = y;
           this.z = z;
       }

        /**
         * 在y轴上偏移
         * @param distance
         * @return
         */
       public Point translateY(float distance){
           return new Point(x,y + distance,z);
       }
    }

    /**
     *圆
     */
    public static class Circle{
        public final Point center; //圆心
        public final  float radius; //半径
        public Circle(Point center,float radius){
            this.center = center;
            this.radius = radius;
        }

        /**
         * 圆的放大缩小
         * @param scale
         * @return
         */
        public Circle scale(float scale){
            return new Circle(center,radius * scale);
        }
    }

    /**
     *
     */
    public static class Cylinder{
        public final   Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }
}
