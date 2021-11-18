package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.QqqWindow;
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

    SquareRender backgroud;

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
        glEnable(GL_DEPTH_TEST);

        ResourceManager.loadShader(Constant.resources + "vs/square.vs", Constant.resources + "fs/square.fs", "square");

        ResourceManager.loadTexture(Constant.resources + "textures/background.jpg", "background");
        ResourceManager.loadTexture(Constant.resources + "textures/awesomeface.png", "face");

        backgroud = new SquareRender(ResourceManager.getShader("square"));
    }

    public void show() {
        qqqWindow.showWindow();
    }

    public void render() {
        backgroud.draw2D(this.width,this.height,new Vector2f(0.0f, 0.0f), new Vector2f(this.width, this.height), 0.0f, new Vector3f(1),ResourceManager.getTexture("background"));
    }

    public void loop() {
        while (!qqqWindow.isClosing()) {
            glfwPollEvents();
            glViewport(0, 0, this.width, this.height);
            render();
            qqqWindow.update();
        }
        GL.setCapabilities(null);
    }


}
