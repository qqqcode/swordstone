package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.Renderer;
import com.qqq.swordstone.graphic.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GunObject {

    int clip;

    GameObject holder;

    List<BulletObject> bullets;

    Texture texture;

    float lastTime;

    float speed;

    GunObject(GameObject holder, Texture texture) {
        this.clip = 30;
        this.holder = holder;
        this.bullets = new LinkedList<>();
        this.texture = texture;
        this.lastTime = (float) glfwGetTime();
        this.speed = 1000f;
    }

    public void shot(float time) {
        float v = (float) glfwGetTime();
        if (v - this.lastTime <= time) {
            return;
        }
        this.lastTime = v;
        Vector2f position = new Vector2f(holder.getPosition().x(), holder.getPosition().y());
        BulletObject bullet = new BulletObject(this.texture, position, new Vector2f(50.0f), new Vector3f(1.0f), 0f);
        GameObject.Direct direct = holder.getDirect();
        bullet.setDirect(direct);
        bullets.add(bullet);
    }

    public void shot() {
        shot(0.5f);
    }

    public void drawBullets(Renderer renderer) {
        if (bullets.size() <= 0) {
            return;
        }
        for (BulletObject bullet : bullets) {
            bullet.drawSquare(renderer);
        }
    }

    public void update(float deltaTime) {
        if (bullets.size() <= 0) {
            return;
        }
        Iterator<BulletObject> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            BulletObject bullet = iterator.next();
            bullet.move(this.speed, deltaTime);
            if (bullet.isDestroy()) {
                iterator.remove();
            }
            if (bullet.getPosition().x + bullet.getSize().x < 0 || bullet.getPosition().y + bullet.getSize().y < 0) {
                iterator.remove();
            }
        }
    }

    public boolean collision(GameObject gameObject) {
        if (bullets.size() <= 0) {
            return false;
        }
        Iterator<BulletObject> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            BulletObject next = iterator.next();
            if (next.isCollision(gameObject)) {
                next.setDestroy(true);
                return true;
            }
        }
        return false;
    }
}
