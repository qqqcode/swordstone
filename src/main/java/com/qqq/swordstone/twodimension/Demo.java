package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.*;
import com.qqq.swordstone.listener.KeyListener;
import com.qqq.swordstone.util.ResourceManager;
import com.qqq.swordstone.util.ShaderUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class Demo {

    static VertexArrayObject vao;

    static VertexBufferObject vbo;

    static ShaderProgram program;

    static int numVertices = 0;

    static FloatBuffer vertices;

    public static void main(String[] args) {

        GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //窗口创建
        QqqWindow qqqWindow = new QqqWindow("qqq", 800, 600, true);
        //debugProc = GLUtil.setupDebugMessageCallback();
        qqqWindow.setKeyCallback(KeyListener::keyCallback);
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glfwPollEvents();
        glViewport(0, 0, 800, 600);

        FontTexture fontTexture = new FontTexture();
        vertices = MemoryUtil.memAllocFloat(4096);

        vao = new VertexArrayObject();
        vbo = new VertexBufferObject();
        program = ShaderUtil.createShaderProgram("vs/default.vs","fs/default.fs");

        Texture texture = Texture.loadTexture("textures/awesomeface.png");

        program.use();
        /* Set texture uniform */
        int uniTex = program.getUniformLocation("texImage");
        program.setUniform1i(uniTex, 0);

        /* Set model matrix to identity matrix */
        Matrix4f model = new Matrix4f();
//        model.translate(new Vector3f(0.5f * 800 - 40, 0.5f * 600 - 40, 0.0f));
        model.scale(new Vector3f(50,50, 1.0f));
        int uniModel = program.getUniformLocation("model");
        program.setUniformMatrix4fv(uniModel, model);

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = program.getUniformLocation("view");
        program.setUniformMatrix4fv(uniView, view);

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = new Matrix4f().ortho(0.0f, 800, 600, 0.0f, -1.0f, 1.0f);
        int uniProjection = program.getUniformLocation("projection");
        program.setUniformMatrix4fv(uniProjection, projection);

        vao = new VertexArrayObject();
        Renderer renderer = new Renderer();
        renderer.init();
        while (!qqqWindow.isClosing()) {
            float x = 0.0f;
            float y = 0.0f;
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            CharSequence text = "DEMO";
            fontTexture.drawText(renderer,text,new Vector3f(0.0f),new Vector3f(50f,50f,1.0f),Color.WHITE);
            qqqWindow.update();
        }
        GL.setCapabilities(null);
    }


}
