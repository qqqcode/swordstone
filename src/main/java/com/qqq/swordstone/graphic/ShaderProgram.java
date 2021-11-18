package com.qqq.swordstone.graphic;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class ShaderProgram {
    /**
     * Stores the handle of the program.
     */
    private final int id;


    public int getId() {
        return id;
    }

    public ShaderProgram() {
        id = glCreateProgram();
    }

    public void attachShader(Shader shader) {
        glAttachShader(id, shader.getID());
    }

    public void bindFragmentDataLocation(int number, CharSequence name) {
        glBindFragDataLocation(id, number, name);
    }

    public void link() {
        glLinkProgram(id);

        checkStatus();
    }

    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    }

    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    }

    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    }

    public void pointVertexAttribute(int location, int size, int stride, int offset) {
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }

    public void pointVertexAttribute(CharSequence name, int size, int stride, int offset) {
        int location = getAttributeLocation(name);
        enableVertexAttribute(location);
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }

    public int getUniformLocation(CharSequence name) {
        int uniformLocation = glGetUniformLocation(id, name);
        return uniformLocation;
    }

    public void setUniform1i(int location, int value) {
        glUniform1i(location, value);
    }

    public void setUniform1i(CharSequence name ,int value) {
        int location = getUniformLocation(name);
        setUniform1i(location,value);
    }

    public void setUniform1iv(int location, IntBuffer intBuffer) {
        glUniform1iv(location,intBuffer);
    }

    public void setUniform1iv(CharSequence name , IntBuffer intBuffer) {
        int location = getUniformLocation(name);
        setUniform1iv(location,intBuffer);
    }

    public void setUniform1fv(int location, FloatBuffer value) {
        glUniform1fv(location, value);
    }

    public void setUniform1fv(CharSequence name , FloatBuffer floatBuffer) {
        int location = getUniformLocation(name);
        setUniform1fv(location,floatBuffer);
    }

    public void setUniform1f(int location, float value) {
        glUniform1f(location, value);
    }

    public void setUniform1f(CharSequence name ,float value) {
        int location = getUniformLocation(name);
        setUniform1f(location,value);
    }


    public void setUniform2fv(int location, Vector2f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2);
            value.get(buffer);
            glUniform2fv(location, buffer);
        }
    }

    public void setUniform2fv(CharSequence name, Vector2f value) {
        int uniformLocation = getUniformLocation(name);
        setUniform2fv(uniformLocation,value);
    }

    public void setUniform2fv(int location, FloatBuffer value) {
        glUniform2fv(location, value);
    }

    public void setUniform2fv(CharSequence name, FloatBuffer value) {
        int uniformLocation = getUniformLocation(name);
        setUniform2fv(uniformLocation,value);
    }

    public void setUniform3fv(int location, Vector3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3);
            value.get(buffer);
            glUniform3fv(location, buffer);
        }
    }

    public void setUniform3fv(CharSequence name, Vector3f value) {
        int uniformLocation = getUniformLocation(name);
        setUniform3fv(uniformLocation,value);
    }

    public void setNglUniform3fv(int location, int count, long value) {
        nglUniform3fv(location, count, value);
    }

    public void setNglUniform3fv(CharSequence name, int count, long value) {
        int uniformLocation = getUniformLocation(name);
        setNglUniform3fv(uniformLocation, count, value);
    }

    public void setUniform3fv(int location, FloatBuffer value) {
        glUniform3fv(location, value);
    }

    public void setUniform3fv(CharSequence name, FloatBuffer value) {
        int uniformLocation = getUniformLocation(name);
        glUniform3fv(uniformLocation, value);
    }

    public void setUniform4fv(int location, Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4);
            value.get(buffer);
            glUniform4fv(location, buffer);
        }
    }

    public void setUniform4fv(CharSequence name, Vector4f value) {
        int uniformLocation = getUniformLocation(name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4);
            value.get(buffer);
            glUniform4fv(uniformLocation, buffer);
        }
    }

    public void setUniformMatrix3fv(int location, Matrix3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3 * 3);
            value.get(buffer);
            glUniformMatrix3fv(location, false, buffer);
        }
    }

    public void setUniformMatrix3fv(CharSequence name, Matrix3f value) {
        int uniformLocation = getUniformLocation(name);
        setUniformMatrix3fv(uniformLocation,value);
    }

    public void setUniformMatrix3fv(int location,FloatBuffer value) {
        glUniformMatrix3fv(location, false, value);
    }

    public void setUniformMatrix3fv(CharSequence name,FloatBuffer value) {
        int uniformLocation = getUniformLocation(name);
        setUniformMatrix3fv(uniformLocation, value);
    }

    public void setUniformMatrix4fv(int location, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4 * 4);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setUniformMatrix4fv(CharSequence name, Matrix4f value) {
        int uniformLocation = getUniformLocation(name);
        setUniformMatrix4fv(uniformLocation,value);
    }

    public void setUniformMatrix4f(int location,FloatBuffer value) {
        glUniformMatrix4fv(location, false, value);
    }

    public void setUniformMatrix4f(CharSequence name,FloatBuffer value) {
        int uniformLocation = getUniformLocation(name);
        setUniformMatrix4f(uniformLocation, value);
    }


    /**
     * Use this shader program.
     */
    public void use() {
        glUseProgram(id);
    }

    /**
     * Checks if the program was linked successfully.
     */
    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }

    /**
     * Deletes the shader program.
     */
    public void delete() {
        glDeleteProgram(id);
    }
}
