package com.qqq.swordstone.graphic;

import static org.lwjgl.opengl.GL30.*;

public class VertexArrayObject {

    private final int id;

    public VertexArrayObject() {
        id = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public static void unbind() {
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(id);
    }

    public int getID() {
        return id;
    }

}
