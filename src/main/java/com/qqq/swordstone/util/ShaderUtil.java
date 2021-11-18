package com.qqq.swordstone.util;



import com.qqq.swordstone.graphic.Shader;
import com.qqq.swordstone.graphic.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ShaderUtil {

    public static ShaderProgram createShaderProgram(String vertexShaderName, String fragmentShaderName) {
        Shader vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertexShaderName);
        Shader fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragmentShaderName);
        return createShaderProgram(vertexShader, fragmentShader);
    }

    public static ShaderProgram createShaderProgram(Shader vertexShader, Shader fragmentShader) {
        ShaderProgram program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.link();
        return program;
    }

}
