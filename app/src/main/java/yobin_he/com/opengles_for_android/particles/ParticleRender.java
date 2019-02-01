package yobin_he.com.opengles_for_android.particles;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.opengles_for_android.R;
import yobin_he.com.opengles_for_android.objects.particle.ParticleShooter;
import yobin_he.com.opengles_for_android.objects.particle.ParticleSystem;
import yobin_he.com.opengles_for_android.program.ParticleShaderProgram;
import yobin_he.com.opengles_for_android.utils.Geometry;
import yobin_he.com.opengles_for_android.utils.MatrixHelper;
import yobin_he.com.opengles_for_android.utils.TextureHelper;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.airhockey
 * @fileName: AirHockeyRender
 * @Date : 2019/1/18  17:26
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 * 步骤：1.创建字节缓存
 * 2.获取着色器资源
 * 3.编译顶点和片元着色器
 * 4.链接程序并使用
 */

public class ParticleRender implements GLSurfaceView.Renderer {
    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final  float[] viewMatrix = new float[16];
    private final  float[] viewProjectionMatrix = new float[16];

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter greenParticleShooter;
    private ParticleShooter blueParticleShooter;
    private long globalStartTime;

    private int texture;



    public ParticleRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE);

        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        final Geometry.Vector particleDirection = new Geometry.Vector(0f,0.5f,0f);

        final float angleVarianceInDegrees = 5f;
        final float speedVariance = 1f;

        redParticleShooter = new ParticleShooter(
                new Geometry.Point(-1f,0f,0f),
                particleDirection,
                Color.rgb(255,50,5),
                angleVarianceInDegrees,
                speedVariance
        );

        greenParticleShooter = new ParticleShooter(
                new Geometry.Point(0f,0f,0f),
                particleDirection,
                Color.rgb(25,255,25),
                angleVarianceInDegrees,
                speedVariance
        );
//
        blueParticleShooter = new ParticleShooter(
                new Geometry.Point(1f,0f,0f),
                particleDirection,
                Color.rgb(5,50,255),
                angleVarianceInDegrees,
                speedVariance
        );


        texture = TextureHelper.loadTexture(context, R.drawable.particle_texture);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width / (float)height,1f,10f);
        Matrix.setIdentityM(viewMatrix,0);
        Matrix.translateM(viewMatrix,0,0f,-1.5f,-5f);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除渲染器表面
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        redParticleShooter.addParticles(particleSystem,currentTime,5);
        greenParticleShooter.addParticles(particleSystem,currentTime,5);
        blueParticleShooter.addParticles(particleSystem,currentTime,5);

        particleProgram.useProgram();
        particleProgram.setUniforms(viewProjectionMatrix,currentTime,texture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }
}
