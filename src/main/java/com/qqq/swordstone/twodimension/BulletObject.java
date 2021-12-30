package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BulletObject extends GameObject {

    private boolean destroy;

    private Vector2f shotPos;

    private float range;

    public BulletObject(Texture texture, Vector2f position, Vector2f size, Vector3f color, float rotate) {
        super(texture, position, size, color, rotate);
        this.range = 1000.0f;
        this.shotPos = new Vector2f(position.x(),position.y());
        this.destroy = false;
    }

    public void move(float x, float y, float speed, float deltaTime) {
        super.move(x, y, speed, deltaTime);
        Vector2f position = this.getPosition();
        float x2 = this.shotPos.x();
        float y2 = this.shotPos.y();
        float x1 = position.x();
        float y1 = position.y();
        double sqrt = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if (sqrt > range) {
            this.destroy = true;
        }
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }
}
