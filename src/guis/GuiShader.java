package guis;

import shaders.ShaderProgram;

import org.joml.Matrix4f;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.txt";
	
	private int location_transformationMatrix;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
}
