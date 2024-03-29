package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entities.Camera;
import renderEngine.Window;
import terrains.Terrain;

public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600.0f;
	
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private Window window;
	
	private Terrain[][] terrains;
	private Vector3f currentTerrainPoint;
	
	public MousePicker(Camera camera, Matrix4f projectionMatrix, Window window, Terrain[][] terrains) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
		this.window = window;
		currentRay = new Vector3f(0.0f, 0.0f, 0.0f);
		this.terrains = terrains;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay))
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		else
			currentTerrainPoint = null;
	}
	
	private Vector3f calculateMouseRay() {
		float mouseX = (float) window.getX();
		float mouseY = (float) window.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f inverseView = new Matrix4f(viewMatrix);
		inverseView.invert();
		Vector4f rayWorld = inverseView.transform(eyeCoords);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f inverseProjection = new Matrix4f(projectionMatrix);
		inverseProjection.invert();
		Vector4f eyeCoords = inverseProjection.transform(clipCoords);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1.0f, 0.0f);
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / window.getWidth() - 1.0f;
		float y = (2.0f * mouseY) / window.getHeight() - 1.0f;
		return new Vector2f(x, -y);
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f start = new Vector3f(camera.getPosition());
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		scaledRay.add(start);
		return scaledRay;
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + (finish - start) / 2.0f;
		if (count >= RECURSION_COUNT) {
			Vector3f endpoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endpoint.x, endpoint.z);
			if (terrain != null)
				return endpoint;
			else
				return null;
		}
		if (intersectionInRange(start, half, ray))
			return binarySearch(count + 1, start, half, ray);
		else
			return binarySearch(count + 1, half, finish, ray);
	}
	
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint))
			return true;
		else
			return false;
	}
	
	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.x, testPoint.z);
		float height = 0;
		if (terrain != null)
			height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
		if (testPoint.y < height)
			return true;
		else
			return false;
	}
	
	private Terrain getTerrain(float worldX, float worldZ) {
		int x = (int) (worldX / terrains[0][0].getSize());
		if (x < 0)
			x = 0;
		if (x > terrains.length - 1)
			x = terrains.length - 1;
		int z = (int) (worldZ / terrains[0][0].getSize());
		if (z < 0)
			z = 0;
		if (z > terrains.length - 1)
			z = terrains.length - 1;
		return terrains[x][z];
	}
}
