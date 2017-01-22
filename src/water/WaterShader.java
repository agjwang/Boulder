package water;

import org.joml.Matrix4f;

import entities.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/water/waterVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/water/waterFragmentShader.txt";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_modelMatrix;
	
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_modelMatrix = super.getUniformLocation("modelMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadModelMatrix(Matrix4f matrix) {
		super.loadMatrix(location_modelMatrix, matrix);
	}
}
