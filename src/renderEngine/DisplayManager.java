package renderEngine;

public class DisplayManager {
	
	private static final String TITLE = "OPENGL_TEST";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private boolean vSync = true;

	private Window window;
	
	public void createDisplay() {
		window = new Window(TITLE, WIDTH, HEIGHT, vSync);
		window.init();
	}
	
	public void updateDisplay() {
		window.update();
	}
	
	public boolean isCloseRequested() {
		return window.windowShouldClose();
	}
	
	public Window getWindow() {
		return window;
	}
}
