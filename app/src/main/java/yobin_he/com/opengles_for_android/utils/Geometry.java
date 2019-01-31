package yobin_he.com.opengles_for_android.utils;

import java.util.Calendar;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: Geometry
 * @Date : 2019/1/23  16:53
 * @describe : 几何形，建立基本的圆形属性，如：圆心，半径
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Geometry {

    public static class Point { //点
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * 在y轴上偏移，因为构成三维坐标是x轴是左右偏移，y轴坐标上下偏移，z轴用于做远近处理（从近屏幕到远屏幕），
         *
         * @param distance
         * @return
         */
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }
    }

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length() {
            return (float) Math.sqrt((double) (x * x) + (double) (y * y) + (double) (z * z));
        }

        public Vector crossProduct(Vector other) {
            return new Vector((y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x));
        }

        public float dotProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector scale(float f) {
            return new Vector(x * f, y * f, z * f);
        }
    }

    /**
     * 射线
     */
    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    /**
     * 圆
     */
    public static class Circle { //圆
        public final Point center; //圆心
        public final float radius; //半径

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        /**
         * 圆的放大缩小
         *
         * @param scale
         * @return
         */
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    /**
     * 圆柱
     */
    public static class Cylinder {
        public final Point center; //圆形
        public final float radius; //半径
        public final float height; //高度。

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    public static class Sphere {
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    public static class Plane {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius; //中心点到线的距离。如果小于球体的半径说明两者相交
    }

    public static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.point, point); //第一个点

        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point); //第二个点

        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }


    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);


        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

}
