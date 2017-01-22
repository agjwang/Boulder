package textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	
	
	public static int loadToTexture(String filename) {
		int textureID;
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		ByteBuffer data;
		
		data = stbi_load("res/" + filename + ".png", width, height, comp, 0);
		
		textureID = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		if (comp.get(0) == 3) {
			if ((width.get(0) & 3) != 0)
				glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width.get(0) & 1));
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, data);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		}
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		stbi_image_free(data);
		
		return textureID;
	}
	
	public static float[][] getPixelRGBData(String filename) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		ByteBuffer data;
		
		data = stbi_load("res/" + filename + ".png", width, height, comp, 0);
		
		float[][] pixelData = new float[width.get(0)][height.get(0)];
		
		int k = 0;
		
		if (comp.get(0) == 3) {
			for (int i = 0; i < width.get(0); i++) {
				for (int j = 0; j < height.get(0); j++) {
					pixelData[j][i] = (float) ((data.get(k * 3) & 0xFF) + (data.get(k * 3 + 1) & 0xFF) + (data.get(k * 3 + 2) & 0xFF));
					k++;
				}
			}
		} else {
			for (int i = 0; i < width.get(0); i++) {
				for (int j = 0; j < height.get(0); j++) {
					pixelData[j][i] = (float) ((data.get(k * 4) & 0xFF) + (data.get(k * 4 + 1) & 0xFF) + (data.get(k * 4 + 2) & 0xFF));
					k++;
				}
			}
		}
		
		stbi_image_free(data);
		return pixelData;
	}
	
	public static ByteBuffer getImageBuffer(String filename) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(new File("res/" + filename + ".png"));
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("COULD NOT LOAD " + filename);
			System.exit(-1);
		}
		
		return buffer;
	}
	
	public static int getImageHeight(String filename) {
		int height = 0;
		try {
			FileInputStream in = new FileInputStream(new File("res/" + filename +".png"));
			PNGDecoder decoder = new PNGDecoder(in);
			height = decoder.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("COULD NOT LOAD: " + filename);
			System.exit(-1);
		}
		return height;
	}
	
	public static int getImageWidth(String filename) {
		int width = 0;
		try {
			FileInputStream in = new FileInputStream(new File("res/" + filename +".png"));
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("COULD NOT LOAD: " + filename);
			System.exit(-1);
		}
		return width;
	}
}
