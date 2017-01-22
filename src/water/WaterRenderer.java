package water;

import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;

public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;
	
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}
	
	public void render(List<WaterTile> water, Camera camera) {
		prepareRender(camera);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
					0.0f, 0.0f, 0.0f, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
	}
	
	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}
	
	private void setUpVAO(Loader loader) {
		float[] vertices = { -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
		quad = loader.loadToVAO(vertices, 2);
	}
}
