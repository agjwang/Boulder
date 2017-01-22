package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;

public class Maths {

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation)
			.rotateX((float) Math.toRadians(rx))
			.rotateY((float) Math.toRadians(ry))
			.rotateZ((float) Math.toRadians(rz))
			.scale(scale);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation.x, translation.y, 0.0f))
			.scale(new Vector3f(scale.x, scale.y, 1.0f));
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotateX((float) Math.toRadians(camera.getPitch()))
			.rotateY((float) Math.toRadians(camera.getYaw()))
			.rotateZ((float) Math.toRadians(camera.getRoll()))
			.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		
		return viewMatrix;
		
	}
}
