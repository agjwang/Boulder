package skybox;

import org.joml.Matrix4f;

import entities.Camera;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import models.RawModel;
import renderEngine.Loader;
import renderEngine.Timer;

public class SkyboxRenderer {
	
	private static final float SIZE = 500.0f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" };
	private static String[] NIGHT_TEXTURE_FILES = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront" };
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	
	private float time = 0;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, float r, float g, float b, Timer timer) {
		shader.start();
		shader.loadViewMatrix(camera, timer);
		shader.loadFogColour(r, g, b);
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		bindTextures(timer);
		glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(Timer timer) {
		//time += timer.getTime() - timer.getLastLoopTime();
		//time %= 24.0f;
		
		time = 9.0f;
		
		int texture1;
		int texture2;
		float blendFactor;
		
		if (time >= 0.0f && time < 5.0f) {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0.0f) / (5.0f - 0.0f);
		} else if (time >- 5.0f && time < 8.0f) {
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5.0f) / (8.0f - 5.0f);
		} else if (time >= 8.0f && time < 21.0f) {
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 8.0f) / (21.0f - 8.0f);
		} else {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 21.0f) / (24.0f - 21.0f);
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
