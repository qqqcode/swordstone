package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.*;
import com.qqq.swordstone.util.Util;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameObject {

    private Texture texture;

    private Vector2f position;

    private Vector2f size;

    private Vector3f color;

    private float rotate;

    private Map<Integer, Animation> animationMap;

    public GameObject(Texture texture, Vector2f position, Vector2f size, Vector3f color, float rotate) {
        this.texture = texture;
        this.position = position;
        this.size = size;
        this.color = color;
        this.rotate = rotate;
    }

    public void drawSquare(Renderer renderer) {
        texture.bind();
        renderer.begin();
        renderer.setModel(new Vector3f(this.position,1.0f),new Vector3f(this.size,1.0f));
        renderer.drawTextureRegion(0.0f,0.0f,1.0f,1.0f,0.0f,0.0f,1.0f,1.0f,Color.WHITE);
        renderer.end();
    }

    public void drawSquare(Renderer renderer,int eachWidth,int eachHeight,int start) {
        texture.bind();
        renderer.begin();
        renderer.setModel(new Vector3f(this.position,1.0f),new Vector3f(this.size,1.0f));

        int row = start / 10;
        int column = start % 10;

        float width = (1/ (float)eachWidth);
        float heigth = (1/(float)eachHeight);

        float s1 = column * width;
        float t1 = row * heigth;
        float s2 = s1 + width;
        float t2 = t1 + heigth;

        renderer.drawTextureRegion(0.0f,0.0f,1.0f,1.0f,s1,t1,s2,t2,Color.WHITE);
        renderer.end();
    }

    public void drawAnimation(Renderer renderer,int type) {
        int v = (int)(glfwGetTime() * 10);

        texture.bind();
        renderer.begin();
        renderer.setModel(new Vector3f(this.position,1.0f),new Vector3f(this.size,1.0f));

        if (animationMap == null) return;
        Animation animation = animationMap.get(type);
        if (animation == null) return;

        int countFrames = animation.getEndFrame() - animation.getStartFrame();
        int currentFrame = v % countFrames;
        Vector4f currentFrame1 = animation.getCurrentFrame(currentFrame);
        renderer.drawTextureRegion(0.0f,0.0f,1.0f,1.0f,currentFrame1.x,currentFrame1.y,currentFrame1.z,currentFrame1.w,Color.WHITE);
        renderer.end();
    }

    public void drawFont(FontTexture fontTexture, Renderer renderer, CharSequence text) {
        this.drawFont(fontTexture, renderer, text, new Vector3f(50f), Color.WHITE);
    }

    public void drawFont(FontTexture fontTexture, Renderer renderer, CharSequence text, Vector3f size, Color color) {
        fontTexture.drawText(renderer, text, new Vector3f(this.position.x, this.position.y, 1.0f), size, color);
    }

    public void move(float x, float y, float speed, float deltaTime) {
        this.position.set(this.position.x() + x * speed * deltaTime, this.position.y() + y * speed * deltaTime);
    }

    public boolean isCollision(GameObject gameObject) {
        Vector2f position = gameObject.getPosition();
        Vector2f size = gameObject.getSize();

        float v = size.x/2.0f;//Util.gouGuf(size.x()/2.0f, size.y()/2.0f);
        float v1 = this.size.x/2.0f;//Util.gouGuf(this.size.x()/2.0f, this.size.y()/2.0f);

        float a = position.x() + size.x()/2.0f - (this.position.x() + this.size.x()/2.0f);
        float b = position.y() + size.y()/2.0f - (this.position.y() + this.size.y()/2.0f);
        float v2 = Util.gouGuf(a,b);

        if (v + v1 > v2) {
            return true;
        }
        return false;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public void addAnimation(int type,int startFrame, int endFrame, int textureRow, int textureColumn) {
        if (animationMap == null ) {
            animationMap = new HashMap<>();
        }
        animationMap.put(type,new Animation(startFrame,endFrame,textureRow,textureColumn,this.texture));
    }
}
