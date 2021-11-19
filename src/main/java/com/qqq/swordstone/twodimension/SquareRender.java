package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.ShaderProgram;
import com.qqq.swordstone.graphic.Texture;
import com.qqq.swordstone.graphic.VertexArrayObject;
import com.qqq.swordstone.graphic.VertexBufferObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SquareRender {

    ShaderProgram shaderProgram;

    VertexArrayObject quadVAO;

    //2D方块基础大小和颜色
    float[] vertex = {
            0f, 1.0f, 0f, 1.0f,
            1.0f, 0f, 1.0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 1.0f, 0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0f, 1.0f, 0f
    };

    public SquareRender(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        initRenderData();
    }

    /**
     *
     * @param width 视窗宽
     * @param height 视窗高
     * @param position 方块在视窗中的位置
     * @param size 方块在视窗中的大小
     * @param rotate
     * @param color 颜色
     * @param texture 纹理
     */
    public void draw2D(int width, int height, Vector2f position, Vector2f size, float rotate, Vector3f color,Texture texture) {
        shaderProgram.use();

        //正交视角projection
        Matrix4f projection = new Matrix4f().ortho(0.0f, width, height, 0.0f, -1.0f, 1.0f);
        shaderProgram.setUniform1i("image", 0);
        shaderProgram.setUniformMatrix4fv("projection", projection);

        //方块位置和大小
        Matrix4f model = new Matrix4f();
        model.translate(new Vector3f(position, 0.0f));
        model.translate(new Vector3f(0.5f * size.x, 0.5f * size.y, 0.0f));
        model.rotate(rotate, new Vector3f(0.0f, 0.0f, 1.0f));
        model.translate(new Vector3f(-0.5f * size.x, -0.5f * size.y, 0.0f));
        model.scale(new Vector3f(size, 1.0f));
        shaderProgram.setUniformMatrix4fv("model", model);
        shaderProgram.setUniform3fv("spriteColor", color);

        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBindVertexArray(quadVAO.getID());
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

    }

    void initRenderData() {
        quadVAO = new VertexArrayObject();
        VertexBufferObject vbo = new VertexBufferObject();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer verBuf = stack.mallocFloat(4 * 6);
            verBuf.put(vertex);
            verBuf.flip();
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadData(GL_ARRAY_BUFFER, verBuf, GL_STATIC_DRAW);
        }

        quadVAO.bind();
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
