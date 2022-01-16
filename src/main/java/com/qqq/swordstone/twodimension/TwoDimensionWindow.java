package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.*;
import com.qqq.swordstone.listener.KeyListener;
import com.qqq.swordstone.util.IOUtil;
import com.qqq.swordstone.util.ResourceManager;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TwoDimensionWindow {

    enum State {
        run,
        stop,
        end
    }

    State state = State.stop;
    QqqWindow qqqWindow;
    private int width = 800;
    private int height = 600;
    int level = 1;

    float deltaTime = 0.0f;
    float lastFrame = 0.0f;
    float stopTime = 2f;

    Vector3f cameraPos = new Vector3f(0f);
    private int worldWidth = 2000;
    private int worldHeight = 2000;
    int success = 0;//0 not show ;1 show success;2 show failed


    PostProcessor effects;
    float shakeTime = 0.0f;
    Renderer renderer;

    GameObject fire;
    List<GameObject> enemyList;

    List<GameObject> wallList;

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
        ResourceManager.loadTexture("textures/img.png","wall");
        ResourceManager.loadTexture("textures/img_2.png","wood");
        ResourceManager.loadTexture("textures/fire.png","fire");

        ResourceManager.loadFontTexture("", "defaultFont");

        //特效
        effects = new PostProcessor(ResourceManager.getShader("postprocessing"), this.width, this.height);

        //渲染器
        renderer = new Renderer();
        renderer.init();

        //game对象
        fire = new GameObject(ResourceManager.getTexture("fire"), new Vector2f(this.width / 2, this.height / 2), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f);
        fire.addAnimation(1, 0, 64, 8, 8);

        gun = new GunObject(fire, ResourceManager.getTexture("face"));

        enemyList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            enemyList.add(new GameObject(ResourceManager.getTexture("face"), new Vector2f((float) Math.random() * this.height, 0f), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f, GameObject.Direct.down));
        }

        wallList = new ArrayList<>();
        String file = "levels/one_levels.lvl";
        try ( BufferedReader buffer = IOUtil.getBufferedReader(file)){
            String s = "";
            while ((s = buffer.readLine()) != null) {
                String[] pos = s.split(" ");
                for (String num : pos) {
                    String[] split = num.split(",");
                    float x = Float.valueOf(split[0]);
                    float y = Float.valueOf(split[1]);
                    wallList.add(new GameObject(ResourceManager.getTexture("wall"), new Vector2f(x,y), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void reload() {
        state = State.run;
        success = 0;
        //game对象
        enemyList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            enemyList.add(new GameObject(ResourceManager.getTexture("face"), new Vector2f((float) Math.random() * this.height, 0f), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f, GameObject.Direct.down));
        }

        wallList = new ArrayList<>();
        String file = "levels/two_levels.lvl";
        try ( BufferedReader buffer = IOUtil.getBufferedReader(file)){
            String s = "";
            while ((s = buffer.readLine()) != null) {
                String[] pos = s.split(" ");
                for (String num : pos) {
                    String[] split = num.split(",");
                    float x = Float.valueOf(split[0]);
                    float y = Float.valueOf(split[1]);
                    wallList.add(new GameObject(ResourceManager.getTexture("wall"), new Vector2f(x,y), new Vector2f(80.0f, 80.0f), new Vector3f(1), 0f));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        qqqWindow.showWindow();
        state = State.run;
    }

    public void update(float deltaTime) {

        if (shakeTime > 0.0f) {
            shakeTime -= deltaTime;
            if (shakeTime <= 0.0f)
                effects.shake = false;
        }

        if (state == State.end) {
            return;
        }
        if (state == State.stop) {
            stopTime = stopTime - deltaTime;
            if (stopTime <=0 ) {
                state = State.run;
                level = level + 1;
                reload();
                stopTime = 2f;
            }
            if (stopTime > 0) {
                return;
            }
        }




        float speed = 100.0f;
        float x = 0.0f;
        float y = 0.0f;
        GameObject.Direct direct = fire.getDirect();
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            y = -1.0f;
            //effects.shake = true;
            direct = GameObject.Direct.up;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            x = -1.0f;
            direct = GameObject.Direct.left;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            y = 1.0f;
            direct = GameObject.Direct.down;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            x = 1.0f;
            direct = GameObject.Direct.right;
        }

        //cameraPos playerPos
        Vector2f position = new Vector2f(fire.getPosition());
        position.x = position.x + fire.getSize().x / 2;
        position.y = position.y + fire.getSize().y / 2;
        if ((position.x + x < 200f && cameraPos.x > 0)
                || (position.x + x > this.width - 200f && cameraPos.x + this.width + 200 < this.worldWidth)) {
            cameraPos.x = cameraPos.x + x;
            x = 0;

        }
        if ((position.y + y < 200f && cameraPos.y > 0)
                || (position.y + y > this.height - 200f && cameraPos.y + this.height + 200 < this.worldHeight)) {
            cameraPos.y = cameraPos.y + y;
            y = 0;
        }

//        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
//            gun.shot();
//        }
        gun.shot();
        fire.move(x, y, speed, deltaTime, direct);

        gun.update(deltaTime);

        Iterator<GameObject> iterator = enemyList.iterator();
        while (iterator.hasNext()) {
            GameObject next = iterator.next();
            if (gun.collision(next, new Vector3f(0 - cameraPos.x,0 - cameraPos.y,0f))) {
                effects.shake = true;
                shakeTime = 0.1f;
                iterator.remove();
            } else {
                next.move(10f, deltaTime);
            }
        }
        Iterator<GameObject> wallIterator = wallList.iterator();
        while (wallIterator.hasNext()) {
            GameObject next = wallIterator.next();
            if (gun.collision(next,new Vector3f(0 - cameraPos.x,0 - cameraPos.y,0f))) {
                effects.shake = true;
                shakeTime = 0.5f;
            }
        }

        if (enemyList.isEmpty() && level == 2) {
            state = State.end;
            success = 1;
        }
        if (enemyList.isEmpty() && level < 2) {
            state = State.stop;
            success = 1;
        }
    }

    public void render() {

        FontTexture defaultFont = ResourceManager.getFontTexture("defaultFont");

        effects.beginRender();

        Texture background = ResourceManager.getTexture("background");
        this.worldHeight = background.getHeight();
        this.worldWidth = background.getWidth();
        Texture.drawTexture(background,renderer,new Vector3f(0 - this.cameraPos.x, 0 - this.cameraPos.y, 0f),new Vector3f(background.getWidth(), background.getHeight(), 1.0f));
        if (success == 1){
            defaultFont.drawText(renderer,"Success",new Vector3f(this.width/2 -175,this.height/2 - 25,0f),new Vector3f(50),Color.RED);
        }


        for (GameObject gameObject : wallList) {
            Vector2f position = new Vector2f(gameObject.getPosition());
            gameObject.drawSquare(renderer, new Vector3f(position.x - cameraPos.x, position.y - cameraPos.y, 0f));
            gameObject.drawFont(defaultFont, renderer, position.x + "" + position.y, new Vector3f(position.x - cameraPos.x, position.y - cameraPos.y, 0f), new Vector3f(20f, 20f, 1f), Color.WHITE);
        }

        fire.drawAnimation(renderer, 1);

        gun.drawBullets(renderer);

        for (GameObject gameObject : this.enemyList) {
            Vector2f position = new Vector2f(gameObject.getPosition());
            gameObject.drawSquare(renderer, new Vector3f(position.x - cameraPos.x, position.y - cameraPos.y, 0f));
            gameObject.drawFont(defaultFont, renderer, position.x + "" + position.y, new Vector3f(position.x - cameraPos.x, position.y - cameraPos.y, 0f), new Vector3f(20f, 20f, 1f), Color.WHITE);
        }

        fire.drawFont(defaultFont, renderer, fire.getPosition().x + "," + fire.getPosition().y, new Vector3f(20f, 20f, 1f), Color.BLACK);

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
