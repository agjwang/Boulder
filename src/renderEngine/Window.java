package renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

public class Window {
	
	private final String title;
	private int width;
	private int height;
	private boolean vSync;
	private boolean resized;
	private boolean cursorInWindow;
	private boolean isLeftClick;
	private boolean isRightClick;
	
	private double lastXPos = 0.0;
	private double currentXPos = 0.0;
	private double lastYPos = 0.0;
	private double currentYPos = 0.0;
	private double scrollYOffset = 0.0;
	private double dx = 0.0;
	private double dy = 0.0;
	
	private long windowHandle;
	private GLFWKeyCallback keyCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWErrorCallback errorCallback;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	
	public Window(String title, int width, int height, boolean vSync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
	}
	
	public void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if (!glfwInit())
			throw new IllegalStateException("UNABLE TO INITIALIZE GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowHandle == NULL)
			throw new RuntimeException("UNABLE TO CREATE GLFW WINDOW");
		
		glfwSetWindowSizeCallback(windowHandle, windowSizeCallback = new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long window, int width, int height) {
				Window.this.width = width;
				Window.this.height = height;
				Window.this.setResized(true);
			}
		});
		
		glfwSetKeyCallback(windowHandle, keyCallback = new GLFWKeyCallback() {
			
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(windowHandle, true);
			}
		});
		
		glfwSetCursorEnterCallback(windowHandle, cursorEnterCallback = new GLFWCursorEnterCallback() {
			
			@Override
			public void invoke(long window, boolean entered) {
				if (entered) {
					cursorInWindow = true;
				}
				else
					cursorInWindow = false;
				
			}
		});
		
		glfwSetCursorPosCallback(windowHandle, cursorPosCallback = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long window, double xpos, double ypos) {
				currentXPos = xpos;
				currentYPos = ypos;
			}
		});
		
		glfwSetMouseButtonCallback(windowHandle, mouseButtonCallback = new GLFWMouseButtonCallback() {
			
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS)
					isLeftClick = true;
				else
					isLeftClick = false;
				
				if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS)
					isRightClick = true;
				else
					isRightClick = false;
			}
		});
		
		glfwSetScrollCallback(windowHandle, scrollCallback = new GLFWScrollCallback() {
			
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollYOffset = yoffset;
			}
		});
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		glfwMakeContextCurrent(windowHandle);
		
		if (isVSync())
			glfwSwapInterval(1);
		
		glfwShowWindow(windowHandle);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void setClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}
	
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
	}
	
	public boolean windowShouldClose() {
		return glfwWindowShouldClose(windowHandle);
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isResized() {
		return resized;
	}
	
	public void setResized(boolean resized) {
		this.resized = resized;
	}
	
	public boolean isVSync() {
		return vSync;
	}
	
	public void setVSync(boolean vSync) {
		this.vSync = vSync;
	}
	
	public void update() {
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
	}
	
	public boolean isLeftClick() {
		return isLeftClick;
	}
	
	public boolean isRightClick() {
		return isRightClick;
	}
	
	public double getScrollY() {
		double yOffset = scrollYOffset;
		scrollYOffset = 0.0;
		return yOffset;
	}

	public double getDX() {
		return dx;
	}
	
	public double getDY() {
		return dy;
	}
	
	public double getX() {
		return currentXPos;
	}
	
	public double getY() {
		return currentYPos;
	}
	
	public void updateMousePos() {
		if (lastXPos > 0.0 && lastYPos > 0.0 && cursorInWindow) {
			dx = currentXPos - lastXPos;
			dy = currentYPos - lastYPos;
		} else {
			dx = 0.0;
			dy = 0.0;
		}
		
		lastXPos = currentXPos;
		lastYPos = currentYPos;
	}
}
