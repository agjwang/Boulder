package entities;

import org.joml.Vector3f;

import models.TexturedModel;
import renderEngine.Window;
import terrains.Terrain;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {

	private static final float RUN_SPEED = 1.0f;
	private static final float TURN_SPEED = 2.0f;
	
	private static final float GRAVITY = -0.02f;
	private static final float JUMP_POWER = 0.5f;
	
	private float currentSpeed = 0.0f;
	private float currentTurnSpeed = 0.0f;
	private float currentUpwardsSpeed = 0.0f;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Window window, Terrain terrain) {
		checkInputs(window);
		super.changeRotation(0.0f, currentTurnSpeed, 0.0f);
		float dx = (float) (currentSpeed * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (currentSpeed * Math.cos(Math.toRadians(super.getRotY())));
		currentUpwardsSpeed += GRAVITY;
		float dy = currentUpwardsSpeed;
		super.changePosition(dx, dy, dz);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			isInAir = false;
			super.getPosition().y = terrainHeight;
			currentUpwardsSpeed = 0.0f;
		}
	}
	
	public void checkInputs(Window window) {
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			currentSpeed = RUN_SPEED;
		} else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			currentSpeed = -RUN_SPEED;
		} else {
			currentSpeed = 0.0f;
		}
		
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			currentTurnSpeed = TURN_SPEED;
		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			currentTurnSpeed = -TURN_SPEED;
		} else {
			currentTurnSpeed = 0.0f;
		}
		
		if (window.isKeyPressed(GLFW_KEY_SPACE) && !isInAir) {
			currentUpwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
}
