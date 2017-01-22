package entities;

import org.joml.Vector3f;

import renderEngine.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
	
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private float pitch = 20.0f;
	private float yaw = 0.0f;
	private float roll = 0.0f;
	private float yOffset = 5.0f;
	
	private float distanceFromPlayer = 50.0f;
	private float angleAroundPlayer = 180.0f;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move(Window window) {
		updateCameraParams(window);
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		yaw = -(player.getRotY() + angleAroundPlayer);
		
		position.y = player.getPosition().y + verticalDistance + yOffset;
		
		float theta = player.getRotY() + angleAroundPlayer;
		float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x + xOffset;
		position.z = player.getPosition().z + zOffset;
	}
	
	private void updateCameraParams(Window window) {
		window.updateMousePos();
		calculateZoom(window);
		calculatePitch(window);
		calculateAngleAroundPlayer(window);
	}
	
	private void calculateZoom(Window window) {
		float zoomLevel = (float) window.getScrollY() * 1.5f;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer < 20.0f)
			distanceFromPlayer = 20.0f;
		if (distanceFromPlayer > 800.0f)
			distanceFromPlayer = 800.0f;
	}
	
	private void calculatePitch(Window window) {
		if (window.isRightClick()) {
			float pitchChange = (float) window.getDY() * 0.1f;
			pitch += pitchChange;
			if (pitch < 5.0f)
				pitch = 5.0f;
			if (pitch > 90.0f)
				pitch = 90.0f;
		}
	}
	
	private void calculateAngleAroundPlayer(Window window) {
		if (window.isLeftClick()) {
			float dx = (float) window.getDX();
			float angleChange = (float) dx * 0.1f;
			angleAroundPlayer -= angleChange;
		}
	}
}
