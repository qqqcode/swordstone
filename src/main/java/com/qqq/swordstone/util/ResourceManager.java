package com.qqq.swordstone.util;



import com.qqq.swordstone.graphic.FontTexture;
import com.qqq.swordstone.graphic.ShaderProgram;
import com.qqq.swordstone.graphic.Texture;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    public static Map<String, ShaderProgram> shaders = new HashMap<>();
    public static Map<String, Texture> textures = new HashMap<>();
    public static Map<String, FontTexture> fontTextures = new HashMap<>();

    public static ShaderProgram loadShader(String vertexShaderName, String fragmentShaderName, String name) {
        ShaderProgram shaderProgram = ShaderUtil.createShaderProgram(vertexShaderName, fragmentShaderName);
        shaders.put(name,shaderProgram);
        return shaderProgram;
    }
    public static ShaderProgram getShader(String name){
        ShaderProgram shader = shaders.get(name);
        if (shader ==null) {
            throw new RuntimeException("不存在");
        }
        return shader;
    }

    public static Texture loadTexture(String path,String name){
        Texture texture = Texture.loadTexture(path);
        textures.put(name,texture);
        return texture;
    }
    public static Texture getTexture(String name) {
        Texture texture = textures.get(name);
        if (texture ==null) {
            throw new RuntimeException("不存在");
        }
        return texture;
    }

    public static FontTexture loadFontTexture(CharSequence path,String name) {
        FontTexture fontTexture = null;
        if (path != null && path != "") {
            try {
                fontTexture = new FontTexture(path,16);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        } else {
            fontTexture = new FontTexture();
        }
        fontTextures.put(name,fontTexture);
        return fontTexture;
    }

    public static FontTexture getFontTexture(String name) {
        return fontTextures.get(name);
    }

    public static void clear(){
        for (ShaderProgram value : shaders.values()) {
            value.delete();
        }

        for (Texture value : textures.values()) {
            value.delete();
        }

        shaders.clear();
        textures.clear();
        fontTextures.clear();
    }
    private ResourceManager() {}
}
