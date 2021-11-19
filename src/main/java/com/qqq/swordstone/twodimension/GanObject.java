package com.qqq.swordstone.twodimension;

import com.qqq.swordstone.graphic.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Stack;

public class GanObject {

    int clip;

    Stack<GameObject> bullets;

    GameObject holder;

    GanObject(GameObject holder) {
        this.clip = 30;
        this.bullets = new Stack<>();
        this.holder = holder;
    }

    public void loadClip(Texture texture) {
        int size = this.bullets.size();
        while (size < this.clip) {
            bullets.push(new BulletObject(texture, holder.getPosition(), new Vector2f(10f), new Vector3f(1), 0));
        }
    }

    public GameObject shot() {
        GameObject pop = bullets.pop();
        return pop;
    }
}
