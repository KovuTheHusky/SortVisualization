package sortvisualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Canvas extends JPanel implements ActionListener {
	
	private static final int ARRAY_LENGTH = 100;
	private static int HEIGHT = 600;

	@SuppressWarnings("unused")
	private static int WIDTH = ARRAY_LENGTH > 200 ? ARRAY_LENGTH * 4 + 1 : 801;
	private static int BAR_WIDTH = WIDTH / ARRAY_LENGTH - 1;
	//private static final int TEXT_HEIGHT = 59;
	//private static final int MIN = (int)(HEIGHT * 0.1);
	//private static final int MAX = (int)((HEIGHT - TEXT_HEIGHT) * 0.9);
		
	private boolean running = false;

	private ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private ArrayList<Integer> toHighlight = new ArrayList<Integer>();
	
	private int fps;
	private int lastFps;
	private long lastFpsOut;
	
	private Timer timer;
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean getRunning() {
		return this.running;
	}

	public Canvas() {
		super();
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(LayoutManager layout) {
		super(layout);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
		timer = new Timer(16, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Number[] array = SortVisualization.getArray();
				
		g.setColor(Color.BLACK);
		
		g.drawString("FPS: " + (int)lastFps + " Speed: " + AudioEngine.getLength(), 5, 15);
		
				++fps;
				long time = System.currentTimeMillis();
				if (time - 1000 > lastFpsOut) {
					
					lastFps = fps;
					fps = 0;
					lastFpsOut = time;
				}

				if (!Window.isRunning() && !highlighted.isEmpty())
					highlighted.clear();

				toHighlight.clear();
				for (int i = 0; i < array.length; ++i)
					if (array[i].isHighlighted())
						toHighlight.add(i);

				if (!toHighlight.isEmpty()) {
					if (!highlighted.isEmpty()) {
						for (int i = highlighted.size() - 1; i >= 0; --i) {
							if (!toHighlight.contains(highlighted.get(i))) {
								highlighted.remove(i);
							}
						}
					}

					if (!toHighlight.equals(highlighted)) {
						for (int i : toHighlight) {
							
								highlighted.add(i);
							
						}
					}
				}
				
				for (int i = 0; i < array.length; ++i) {
					if (!highlighted.contains(i))
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.BLUE);
					g.fillRect(i * BAR_WIDTH + i + 1, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
				}
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}
	
}
