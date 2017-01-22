package textures;

public class ModelTexture {

	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	public boolean useFakeLighting() {
		return useFakeLighting;
	}

	public void setFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void setTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getTextureID() {
		return textureID;
	}
}
