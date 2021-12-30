package com.qqq.swordstone.graphic;

import org.joml.Vector4f;

public class Animation {

    private int startFrame;

    private int endFrame;

    private int textureRow;

    private int textureColumn;

    private Texture texture;

    public Animation(int startFrame, int endFrame, int textureRow, int textureColumn, Texture texture) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.textureRow = textureRow;
        this.textureColumn = textureColumn;
        this.texture = texture;
    }

    public Vector4f getCurrentFrame(int currentFrame) {
        currentFrame = currentFrame + startFrame;

        //当前帧所在位置
        int row = currentFrame / textureColumn;
        int column = currentFrame % textureColumn;

        //每一帧的宽和长
        float eachWidth = (1/ (float)textureColumn);
        float eachHeight = (1/(float)textureRow);

        return new Vector4f(column * eachWidth,row * eachHeight,(column + 1)  * eachWidth,(row +1) * eachHeight);
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public int getTextureRow() {
        return textureRow;
    }

    public void setTextureRow(int textureRow) {
        this.textureRow = textureRow;
    }

    public int getTextureColumn() {
        return textureColumn;
    }

    public void setTextureColumn(int textureColumn) {
        this.textureColumn = textureColumn;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
