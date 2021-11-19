package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameObject {

    private Texture texture;

    private Vector2f position;

    private Vector2f size;

    private Vector3f color;

    private float rotate;

    public GameObject(Texture texture, Vector2f position, Vector2f size, Vector3f color, float rotate) {
        this.texture = texture;
        this.position = position;
        this.size = size;
        this.color = color;
        this.rotate = rotate;
    }

    public void drawSquare(SquareRender render, int windowWidth, int windowHeight) {
        render.draw2D(windowWidth, windowHeight, this.position, this.size, this.rotate, this.color, this.texture);
    }

    public void move(float x, float y, float speed, float deltaTime) {
        this.position.set(this.position.x() + x * speed * deltaTime, this.position.y() + y * speed * deltaTime);
    }
}
