package com.qqq.swordstone.graphic;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public enum CameraMovement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    private Vector3f position;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;
    private Vector3f front;

    private float yaw;
    private float pitch;

    private float movementSpeed;
    private float mouseSensitivity;
    private float zoom;

    class Builder {
        private Vector3f position;
        private Vector3f up;
        private Vector3f right;
        private Vector3f worldUp;
        private Vector3f front;

        private float yaw;
        private float pitch;

        private float movementSpeed;
        private float mouseSensitivity;
        private float zoom;

        Builder() {
            this.position = new Vector3f(0.0f, 0.0f, 0.0f);
            this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
            this.front = new Vector3f(0.0f, 0.0f, -1.0f);
            this.up = new Vector3f(0.0f, 1.0f, 0.0f);

            this.yaw = -90.0f;
            this.pitch = 0.0f;

            this.movementSpeed = 2.5f;
            this.mouseSensitivity = 0.1f;
            this.zoom = 45.0f;
        }

        Builder position(Vector3f position) {
            this.position = position;
            return this;
        }

        Builder worldUp(Vector3f worldUp) {
            this.worldUp = worldUp;
            return this;
        }

        Builder front(Vector3f front) {
            this.front = front;
            return this;
        }

        Builder up(Vector3f up) {
            this.up = up;
            return this;
        }

        Builder yaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        Builder movementSpeed(float movementSpeed) {
            this.movementSpeed = movementSpeed;
            return this;
        }

        Builder mouseSensitivity(float mouseSensitivity) {
            this.mouseSensitivity = mouseSensitivity;
            return this;
        }

        Builder zoom(float zoom) {
            this.zoom = zoom;
            return this;
        }

        Camera build() {
            Camera camera = new Camera();
            camera.position = this.position;
            camera.up = this.up;
            camera.right = this.right;
            camera.worldUp = this.worldUp;
            camera.front = this.front;
            camera.yaw = this.yaw;
            camera.pitch = this.pitch;
            camera.movementSpeed = this.movementSpeed;
            camera.mouseSensitivity = this.mouseSensitivity;
            camera.zoom = this.zoom;
            camera.updateCameraVectors();
            return camera;
        }
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, position.add(this.front.x, this.front.y, this.front.z, new Vector3f()), this.up);
    }

    public void processKeyboard(CameraMovement direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        if (direction == CameraMovement.FORWARD)
            position.add(this.front.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.BACKWARD)
            position.sub(this.front.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.LEFT)
            position.sub(this.right.mul(velocity, new Vector3f()));
        if (direction == CameraMovement.RIGHT)
            position.add(this.right.mul(velocity, new Vector3f()));
    }

    public void processMouseMovement(float xoffset, float yoffset, boolean constrainPitch) {
        xoffset *= mouseSensitivity;
        yoffset *= mouseSensitivity;

        yaw += xoffset;
        pitch += yoffset;

        if (constrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }
        updateCameraVectors();
    }

    public void processMouseScroll(float yoffset) {
        zoom -= yoffset;
        if (zoom < 1.0f)
            zoom = 1.0f;
        if (zoom > 45.0f)
            zoom = 45.0f;
    }

    void updateCameraVectors() {
        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
        front.y = (float) Math.sin(Math.toRadians(this.pitch));
        front.z = (float) (Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
        front.normalize(this.front);
        this.right = this.front.cross(this.worldUp, new Vector3f()).normalize(new Vector3f());
        this.up = this.right.cross(this.front, new Vector3f()).normalize(new Vector3f());
    }

    public float getZoom() {
        return zoom;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        return front;
    }
}
