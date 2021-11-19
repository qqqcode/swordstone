package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.QqqWindow;
import com.qqq.swordstone.graphic.ShaderProgram;
import com.qqq.swordstone.listener.KeyListener;
import com.qqq.swordstone.util.Constant;
import com.qqq.swordstone.util.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TwoDimensionWindow {

    QqqWindow qqqWindow;
    private int width = 800;
    private int height = 600;

    float deltaTime = 0.0f;
    float lastFrame = 0.0f;


    SquareRender squareRender;
    PostProcessor effects;

    GameObject fire;


    TwoDimensionWindow(int width, int height) {
        this.width = 800;
        this.height = 600;
        init();
    }

    void init() {
        GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //窗口创建
        qqqWindow = new QqqWindow("qqq", this.width, this.height, true);
        //debugProc = GLUtil.setupDebugMessageCallback();
        qqqWindow.setKeyCallback(KeyListener::keyCallback);
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //资源加载
        //着色器
        ResourceManager.loadShader("vs/square.vs", "fs/square.fs", "square");
        ResourceManager.loadShader("vs/post_processing.vert", "fs/post_processing.frag", "postprocessing");

        //2d纹理
        ResourceManager.loadTexture("textures/background.jpg", "background");
        ResourceManager.loadTexture("textures/awesomeface.png", "face");
        ResourceManager.loadTexture("textures/img_4.png", "fire");

        //绘制器
        squareRender = new SquareRender(ResourceManager.getShader("square"));
        //特效绘制
        effects = new PostProcessor(ResourceManager.getShader("postprocessing"), this.width, this.height);

        //game对象
        fire = new GameObject(ResourceManager.getTexture("fire"), new Vector2f(this.width / 2, this.height / 2), new Vector2f(50.0f, 70.0f), new Vector3f(1), 0f);
    }

    public void show() {
        qqqWindow.showWindow();
    }

    public void update(float deltaTime) {
        float speed = 100.0f;
        float x = 0.0f;
        float y = 0.0f;
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            y = -1.0f;
            effects.shake = true;
        } else {
            effects.shake = false;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            effects.chaos = true;
            x = -1.0f;
        } else {
            effects.chaos = false;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            y = 1.0f;
            effects.confuse = true;
        } else {
            effects.confuse = false;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            x = 1.0f;
        }
        fire.move(x, y, speed, deltaTime);
    }

    public void render() {

        effects.beginRender();

        ShaderProgram square = ResourceManager.getShader("square");
        square.use();
        square.setUniform1f("texturePos", 0f);
        square.setUniform1f("textureXClip", 1.0f);
        squareRender.draw2D(this.width, this.height, new Vector2f(0.0f, 0.0f), new Vector2f(this.width, this.height), 0.0f, new Vector3f(1), ResourceManager.getTexture("background"));


        float currentFrame = (float) glfwGetTime();
        currentFrame = currentFrame * 10;
        int c = (int) currentFrame;
        c = c % 12;
        square.use();
        float clip = 12f;
        square.setUniform1f("texturePos", c / clip);
        square.setUniform1f("textureXClip", clip);
        fire.drawSquare(squareRender, this.width, this.height);

        effects.endRender();
        effects.render((float) glfwGetTime());
    }

    public void loop() {
        while (!qqqWindow.isClosing()) {

            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            glfwPollEvents();
            glViewport(0, 0, this.width, this.height);

            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            update(deltaTime);
            render();
            qqqWindow.update();
        }
        GL.setCapabilities(null);
    }


}
