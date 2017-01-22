package renderEngine;

import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;

public class MasterRenderer {
	
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader;
	private EntityRenderer renderer;
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(DisplayManager display, Loader loader) {
		shader = new StaticShader();
		terrainShader = new TerrainShader();
		
		enableCulling();
		
		entities = new HashMap<TexturedModel, List<Entity>>();
		
		createProjectionMatrix(display.getWindow().getWidth(), display.getWindow().getHeight());
		renderer = new EntityRenderer(shader, display, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Timer timer) {
		for (Terrain terrain : terrains)
			processTerrain(terrain);
		for (Entity entity : entities)
			processEntity(entity);
		render(lights, camera, timer);
	}
	
	public void render(List<Light> lights, Camera camera, Timer timer) {
		clear();
		
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
		
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		
		skyboxRenderer.render(camera, RED, GREEN, BLUE, timer);
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void clear() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1.0f);
	}
	
	public void cleanup() {
		terrainShader.cleanup();
		shader.cleanup();
	}
	
	private void createProjectionMatrix(int width, int height) {
		projectionMatrix = new Matrix4f();
		projectionMatrix.perspective(FOV, (float) width / height, NEAR_PLANE, FAR_PLANE);
	}
}
