package com.qqq.swordstone.listener;

import com.qqqopengl.graphic.Camera;
import com.qqqopengl.graphic.QqqWindow;

import static org.lwjgl.glfw.GLFW.*;

public class PollEvents {

    public static void useEvents (QqqWindow window,Camera camera,float detaTime, boolean firstMouse, float lastX, float lastY) {

    }

    public static void keyPress(QqqWindow window, Camera camera, float deltaTime) {
        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            window.close();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            camera.processKeyboard(Camera.CameraMovement.FORWARD, deltaTime);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            camera.processKeyboard(Camera.CameraMovement.BACKWARD, deltaTime);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            camera.processKeyboard(Camera.CameraMovement.LEFT, deltaTime);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            camera.processKeyboard(Camera.CameraMovement.RIGHT, deltaTime);
        }
    }

    public static void mousePress(QqqWindow qqqWindow, Camera camera, boolean firstMouse, float lastX, float lastY) {
        float xpos = com.qqqopengl.listener.MouseListener.getX();
        float ypos = com.qqqopengl.listener.MouseListener.getY();
        if (firstMouse) {
            lastX = qqqWindow.getWidth() / 2;
            lastY = qqqWindow.getHeight() / 2;
            firstMouse = false;
        }

        float xoffset = xpos - lastX;
        float yoffset = lastY - ypos;

        lastX = xpos;
        lastY = ypos;

        camera.processMouseMovement(xoffset, yoffset, true);
    }

    public static void scrollCallback(Camera camera) {
        float scrollX = com.qqqopengl.listener.MouseListener.getScrollX();
        float scrollY = com.qqqopengl.listener.MouseListener.getScrollY();
        camera.processMouseScroll(scrollY);
    }
}
