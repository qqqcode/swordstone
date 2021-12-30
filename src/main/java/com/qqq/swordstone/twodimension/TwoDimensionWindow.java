package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.*;
import com.qqq.swordstone.listener.KeyListener;
import com.qqq.swordstone.util.ResourceManager;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TwoDimensionWindow {
    QqqWindow qqqWindow;
    private int width = 800;
    private int height = 600;

    float deltaTime = 0.0f;
    float lastFrame = 0.0f;


    PostProcessor effects;
    Renderer renderer;

    GameObject fire;
    List<GameObject> enemyList;

    GunObject gun;

    TwoDimensionWindow(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    void init() {
        //窗口创建
        qqqWindow = new QqqWindow("qqq", this.width, this.height, true);
        //debugProc = GLUtil.setupDebugMessageCallback();
        //qqqWindow.setKeyCallback(KeyListener::keyCallback);

        //资源加载
        //着色器
        ResourceManager.loadShader("vs/square.vs", "fs/square.fs", "square");
        ResourceManager.loadShader("vs/post_processing.vert", "fs/post_processing.frag", "postprocessing");

        //2d纹理
        ResourceManager.loadTexture("textures/background.jpg", "background");
        ResourceManager.loadTexture("textures/awesomeface.png", "face");
        ResourceManager.loadTexture("textures/img_4.png", "fire");
        ResourceManager.loadTexture("textures/newImage.png", "box");

        ResourceManager.loadFontTexture("", "defaultFont");

        //特效
        effects = new PostProcessor(ResourceManager.getShader("postprocessing"), this.width, this.height);

        //渲染器
        renderer = new Renderer();
        renderer.init();

        //game对象
        fire = new GameObject(ResourceManager.getTexture("box"), new Vector2f(this.width / 2, this.height / 2), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f);
        fire.addAnimation(1,0,59,6,10);

        gun = new GunObject(fire, ResourceManager.getTexture("face"));

        enemyList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            enemyList.add(new GameObject(ResourceManager.getTexture("face"), new Vector2f((float) Math.random() * this.height, 0f), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f));
        }
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
            //effects.shake = true;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            x = -1.0f;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            y = 1.0f;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            x = 1.0f;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            gun.shot();
        }

        fire.move(x, y, speed, deltaTime);

        gun.update(deltaTime);

        Iterator<GameObject> iterator = enemyList.iterator();
        while (iterator.hasNext()) {
            GameObject next = iterator.next();
            if (gun.collision(next) ) {
                iterator.remove();
            }
        }
    }

    public void render() {

        FontTexture defaultFont = ResourceManager.getFontTexture("defaultFont");
        effects.beginRender();

        ResourceManager.getTexture("background").bind();
        renderer.begin();
        renderer.setModel(new Vector3f(0.0f),new Vector3f(this.width,this.height,1.0f));
        renderer.drawTextureRegion(0.0f,0.0f,1.0f,1.0f,0.0f,0.0f,1.0f,1.0f,Color.WHITE);
        renderer.end();

        fire.drawAnimation(renderer,1);

        gun.drawBullets(renderer);

        for (GameObject gameObject : this.enemyList) {
            gameObject.drawSquare(renderer);
            gameObject.drawFont(defaultFont,renderer,gameObject.getPosition().x+""+gameObject.getPosition().y,new Vector3f(20f,20f,1f),Color.WHITE);
        }

        fire.drawFont(defaultFont,renderer,fire.getPosition().x +","+ fire.getPosition().y ,new Vector3f(20f,20f,1f),Color.BLACK);

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
