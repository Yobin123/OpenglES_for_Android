package yobin_he.com.opengles_for_android.objects.particle;

import android.graphics.Color;
import android.opengl.GLES20;

import yobin_he.com.opengles_for_android.Constants;
import yobin_he.com.opengles_for_android.data.VertexArray;
import yobin_he.com.opengles_for_android.program.ParticleShaderProgram;
import yobin_he.com.opengles_for_android.utils.Geometry;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.objects
 * @fileName: ParticleSystem
 * @Date : 2019/1/31  14:02
 * @describe : 粒子系统
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ParticleSystem {
    private static final  int POSITION_COMPONENT_COUNT = 3;
    private static final  int COLOR_COMPONENT_COUNT = 3;
    private static final  int VECTOR_COMPONENT_COUNT = 3;
    private static final  int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    private static final  int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT
            +VECTOR_COMPONENT_COUNT
            +PARTICLE_START_TIME_COMPONENT_COUNT;

    //跨度
    private static final  int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

    private final  float[] particles;
    private final VertexArray vertexArray;
    private final  int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }


    public void  addParticle(Geometry.Point position, int color, Geometry.Vector direction,float particleStartTime){
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;

        int currentOffset = particleOffset;
        nextParticle++;

        if(currentParticleCount < maxParticleCount){
            currentParticleCount++;
        }

        if(nextParticle == maxParticleCount){
            nextParticle = 0;
        }

        //点坐标分量
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        //颜色分量
        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;

        //向量分量
        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;

        //粒子创建时间
        particles[currentOffset++] = particleStartTime;

        vertexArray.updateBuffer(particles,particleOffset,TOTAL_COMPONENT_COUNT);
    }

    /**
     * 绑定数据
     * @param particleShaderProgram
     */
    public void bindData(ParticleShaderProgram particleShaderProgram){
        int dataOffset = 0;
        vertexArray.setVertexAttributePointer(dataOffset,
                particleShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                particleShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                particleShaderProgram.getDirectionVectorLocation(),
                VECTOR_COMPONENT_COUNT,STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                particleShaderProgram.getParticleStartTimeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT,STRIDE);
    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS,0, currentParticleCount);
    }

}
