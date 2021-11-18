package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.QqqWindow;
import com.qqq.swordstone.graphic.ShaderProgram;
import com.qqq.swordstone.util.Constant;
import com.qqq.swordstone.util.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TwoDimensionWindow {

    QqqWindow qqqWindow;
    private int width = 800;
    private int height = 600;

    float deltaTime = 0.0f;
    float lastFrame = 0.0f;


    SquareRender backgroud;

    SquareRender fire;

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
        qqqWindow = new QqqWindow("qqq", this.width, this.height, true);
        //debugProc = GLUtil.setupDebugMessageCallback();
        glClearColor(0f, 0f, 0f, 1f);

        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ResourceManager.loadShader(Constant.resources + "vs/square.vs", Constant.resources + "fs/square.fs", "square");

        ResourceManager.loadTexture(Constant.resources + "textures/background.jpg", "background");
        ResourceManager.loadTexture(Constant.resources + "textures/awesomeface.png", "face");
        ResourceManager.loadTexture(Constant.resources + "textures/img_4.png", "fire");

        backgroud = new SquareRender(ResourceManager.getShader("square"));

        fire = new SquareRender(ResourceManager.getShader("square"));
    }

    public void show() {
        qqqWindow.showWindow();
    }

    public void update(float deltaTime) {

    }

    public void render() {
        float currentFrame = (float) glfwGetTime();
        currentFrame = currentFrame * 10;
        int c = (int) currentFrame;
        c = c % 12;

        ShaderProgram square = ResourceManager.getShader("square");
        square.use();
        square.setUniform1f("texturePos",0f);
        square.setUniform1f("textureXClip",1.0f);
        backgroud.draw2D(this.width, this.height, new Vector2f(0.0f, 0.0f), new Vector2f(this.width, this.height), 0.0f, new Vector3f(1), ResourceManager.getTexture("background"));


        square.use();
        float clip = 12f;
        square.setUniform1f("texturePos",c/clip);
        square.setUniform1f("textureXClip",clip);
        fire.draw2D(this.width,this.height,new Vector2f(this.width/2,this.height/2),new Vector2f(50.0f,70.0f),0f,new Vector3f(1.0f),ResourceManager.getTexture("fire"));
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
