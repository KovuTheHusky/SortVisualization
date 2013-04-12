package sortvisualization;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame implements KeyEventDispatcher {

	public Window() throws HeadlessException {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	public Window(GraphicsConfiguration gc) {
		super(gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	public Window(String title) throws HeadlessException {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	public Window(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			Thread t = new Thread(new Sort());
			t.start();
		}
		return true;
	}

}
