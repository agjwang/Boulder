package terrains;

import org.joml.Vector2f;
import org.joml.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;
import toolbox.Maths;

public class Terrain {

	private static final float SIZE = 150.0f;
	private static final float MAX_HEIGHT = 40.0f;
	private static final float MAX_PIXEL_COLOUR = 256.0f + 256.0f + 256.0f;
	
	private int vertexCount;
	private float x;
	private float z;
	private float[][] heights;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}


	public TerrainTexture getBlendMap() {
		return blendMap;
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getZ() {
		return z;
	}


	public void setZ(float z) {
		this.z = z;
	}


	public RawModel getModel() {
		return model;
	}


	public void setModel(RawModel model) {
		this.model = model;
	}

	public float getSize() {
		return SIZE;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / (float) (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		if (gridX >= heights.length - 1 || gridX < 0 || gridZ >= heights.length - 1 || gridZ < 0)
			return 0.0f;
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= 1 - zCoord) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
	}
	
	private RawModel generateTerrain(Loader loader, String heightMap) {
		vertexCount = Texture.getImageHeight(heightMap);
		
		float[][] pixelData = Texture.getPixelRGBData(heightMap);
		heights = new float[Texture.getImageWidth(heightMap)][Texture.getImageHeight(heightMap)];
		
		int count = vertexCount * vertexCount;
		
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		
		int vertexPointer = 0;
		
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[vertexPointer * 3] = (float) j / (float) (vertexCount - 1) * SIZE;
				heights[j][i] = getHeightFromPixel(j, i, pixelData);
				vertices[vertexPointer * 3 + 1] = heights[j][i];
				vertices[vertexPointer * 3 + 2] = (float) i / (float) (vertexCount - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, pixelData);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / (float) (vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / (float) (vertexCount - 1);
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++) {
			for (int gx = 0; gx < vertexCount - 1; gx++) {
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int y, float[][] pixelData) {
		float heightL = getHeightFromPixel(x - 1, y, pixelData);
		float heightR = getHeightFromPixel(x + 1, y, pixelData);
		float heightD = getHeightFromPixel(x, y - 1, pixelData);
		float heightU = getHeightFromPixel(x, y + 1, pixelData);
		Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
		normal.normalize();
		return normal;
	}
	
	private float getHeightFromPixel(int x, int y, float[][] pixelData) {
		if (x < 0 || x >= vertexCount || y < 0 || y >= vertexCount)
			return 0.0f;
		
		float height = pixelData[x][y];
		height -= (MAX_PIXEL_COLOUR / 2.0f);
		height /= (MAX_PIXEL_COLOUR / 2.0f);
		height *= MAX_HEIGHT;
		return height;
	}
	
}
