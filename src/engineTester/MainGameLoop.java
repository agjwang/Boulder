package engineTester;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Timer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;

public class MainGameLoop {

	public static final int TARGET_UPS = 60;
	public static final int TARGET_FPS = 60;
	
	public static void main(String[] args) {
		DisplayManager display = new DisplayManager();
		display.createDisplay();
		
		Loader loader = new Loader();
		Timer timer = new Timer();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadOBJModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadOBJModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel pine = new TexturedModel(OBJLoader.loadOBJModel("pine", loader), new ModelTexture(loader.loadTexture("pine")));
		
		grass.getTexture().setTransparency(true);
		grass.getTexture().setFakeLighting(true);
		flower.getTexture().setTransparency(true);
		flower.getTexture().setFakeLighting(true);
		fern.getTexture().setTransparency(true);
		fern.getTexture().setNumberOfRows(2);
		
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightMap");
		//Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap, "heightMap");
		//Terrain terrain3 = new Terrain(0, 1, loader, texturePack, blendMap, "heightMap");
		//Terrain terrain4 = new Terrain(1, 1, loader, texturePack, blendMap, "heightMap");
		
		Terrain[][] terrains = new Terrain[1][1];
		terrains[0][0] = terrain;
		//terrains[1][0] = terrain2;
		//terrains[0][1] = terrain3;
		//terrains[1][1] = terrain4;
		
		List<Terrain> terrainsList = new ArrayList<Terrain>();
		terrainsList.add(terrain);
		//terrainsList.add(terrain2);
		//terrainsList.add(terrain3);
		//terrainsList.add(terrain4);
		
		List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i = 0; i < 100; i++){
        	if (i % 10 == 0) {
        		float x = 0.0f;
        		float y = 0.0f;
        		float z = 0.0f;
        		while (y <= 5.0f) {
	        		x = random.nextFloat() * 150.0f;
	        		z = random.nextFloat() * 150.0f;
	        		int gridX = (int) (x / terrain.getSize());
	        		int gridZ = (int) (z / terrain.getSize());
	        		if (gridX < 0)
						gridX = 0;
					if (gridX > terrains.length - 1)
						gridX = terrains.length - 1;
					if (gridZ < 0)
						gridZ = 0;
					if (gridZ > terrains.length - 1)
						gridZ = terrains.length - 1;
	        		y = terrains[gridX][gridZ].getHeightOfTerrain(x, z);
        		}
        		entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360.0f, 0, 0.9f));
        	}
        	if (i % 10 == 0) {
        		/*
        		float x = random.nextFloat() * 150.0f;
        		float z = random.nextFloat() * 150.0f;
        		int gridX = (int) (x / terrain.getSize());
				int gridZ = (int) (z / terrain.getSize());
				if (gridX < 0)
					gridX = 0;
				if (gridX > terrains.length - 1)
					gridX = terrains.length - 1;
				if (gridZ < 0)
					gridZ = 0;
				if (gridZ > terrains.length - 1)
					gridZ = terrains.length - 1;
        		float y = terrains[gridX][gridZ].getHeightOfTerrain(x, z);
        		entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360.0f, 0, random.nextFloat() * 1.0f + 0.6f));
        		*/
        		float x = 0.0f;
        		float y = 0.0f;
        		float z = 0.0f;
        		while (y <= 5.0f) {
	        		x = random.nextFloat() * 150.0f;
	        		z = random.nextFloat() * 150.0f;
	        		int gridX = (int) (x / terrain.getSize());
	        		int gridZ = (int) (z / terrain.getSize());
	        		if (gridX < 0)
						gridX = 0;
					if (gridX > terrains.length - 1)
						gridX = terrains.length - 1;
					if (gridZ < 0)
						gridZ = 0;
					if (gridZ > terrains.length - 1)
						gridZ = terrains.length - 1;
	        		y = terrains[gridX][gridZ].getHeightOfTerrain(x, z);
        		}
        		entities.add(new Entity(pine, new Vector3f(x, y, z), 0, random.nextFloat() * 360.0f, 0, random.nextFloat() * 1.0f + 1.0f));        		
        		
        	}       
        }
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(1000.0f, 1000.0f, 1000.0f), new Vector3f(1.5f, 1.5f, 1.5f)));
		/*
		lights.add(new Light(new Vector3f(800f, 15.0f, 900.0f), new Vector3f(4.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(800f, 15.0f, 1000.0f), new Vector3f(0.0f, 4.0f, 4.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(800f, 15.0f, 1100.0f), new Vector3f(4.0f, 4.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		*/
		TexturedModel lamp = new TexturedModel(OBJLoader.loadOBJModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
		/*
		Entity lamp1 = new Entity(lamp, new Vector3f(800.0f, 0.0f, 900.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		lamp1.getModel().getTexture().setFakeLighting(true);
		Entity lamp2 = new Entity(lamp, new Vector3f(800.0f, 0.0f, 1000.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		lamp2.getModel().getTexture().setFakeLighting(true);
		Entity lamp3 = new Entity(lamp, new Vector3f(800.0f, 0.0f, 1100.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		lamp3.getModel().getTexture().setFakeLighting(true);
		
		entities.add(lamp1);
		entities.add(lamp2);
		entities.add(lamp3);
		*/
		
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadOBJModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerModel, new Vector3f(75.0f, 0.0f, 75.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		entities.add(player);
		
		Camera camera = new Camera(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		//guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		MasterRenderer renderer = new MasterRenderer(display, loader);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), display.getWindow(), terrains);
		
		float elapsedTime;
		float accumulator = 0.0f;
		float interval = 1.0f / TARGET_UPS;
		
		Entity lampEntity = new Entity(lamp, new Vector3f(800.0f, 0.0f, 800.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		lampEntity.getModel().getTexture().setFakeLighting(true);
		entities.add(lampEntity);
		Light lampLight = new Light(new Vector3f(800f, 15.0f, 800.0f), new Vector3f(4.0f, 4.0f, 4.0f), new Vector3f(1.0f, 0.01f, 0.002f));
		lights.add(lampLight);
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		List<WaterTile> waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(75, 75, 0));
		
		while (!display.isCloseRequested()) {
			
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			
			while (accumulator >= interval) {
				int gridX = (int) (player.getPosition().x / terrain.getSize());
				if (gridX < 0)
					gridX = 0;
				if (gridX > terrains.length - 1)
					gridX = terrains.length - 1;
				int gridZ = (int) (player.getPosition().z / terrain.getSize());
				if (gridZ < 0)
					gridZ = 0;
				if (gridZ > terrains.length - 1)
					gridZ = terrains.length - 1;
				camera.move(display.getWindow());
				player.move(display.getWindow(), terrains[gridX][gridZ]);
				picker.update();
				if (picker.getCurrentTerrainPoint() != null) {
					Vector3f currentPoint = picker.getCurrentTerrainPoint();
					lampEntity.setPosition(currentPoint);
					lampLight.setPosition(new Vector3f(currentPoint.x, currentPoint.y + 15.0f, currentPoint.z));
					
				}
				accumulator -= interval;
			}
			
			renderer.renderScene(entities, terrainsList, lights, camera, timer);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			
			display.updateDisplay();
		}
		
		waterShader.cleanup();
		guiRenderer.cleanup();
		renderer.cleanup();
		
		loader.cleanup();
	}
}
