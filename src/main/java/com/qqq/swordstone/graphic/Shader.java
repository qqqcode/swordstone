package com.qqq.swordstone.graphic;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int id;

    public Shader(int type) {
        id = glCreateShader(type);
    }

    public void source(CharSequence source) {
        glShaderSource(id, source);
    }

    public void compile() {
        glCompileShader(id);

        checkStatus();
    }

    private void checkStatus() {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }

    public void delete() {
        glDeleteShader(id);
    }

    public int getID() {
        return id;
    }

    public static Shader createShader(int type, CharSequence source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    public static Shader loadShader(int type, String path) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        CharSequence source = builder.toString();

        return createShader(type, source);
    }
}
