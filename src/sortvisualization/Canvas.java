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
	private static final int TEXT_HEIGHT = 59;
	private static final int MIN = (int)(HEIGHT * 0.1);
	private static final int MAX = (int)((HEIGHT - TEXT_HEIGHT) * 0.9);
		
	private boolean running = false;
	private boolean dirty = true;

	private ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private ArrayList<Integer> toHighlight = new ArrayList<Integer>();
	
	private int fps;
	private int lastFps;
	private long lastFpsOut;

	public Canvas() {
		super();
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
		Timer timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(LayoutManager layout) {
		super(layout);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
	}

	public Canvas(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
	}

	public Canvas(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.setLocation(0, 0);
		this.setSize(WIDTH, HEIGHT);
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		Number[] array = SortVisualization.getArray();
		
//		HEIGHT = this.getHeight();
//		WIDTH = this.getWidth();
//		BAR_WIDTH = WIDTH / ARRAY_LENGTH - 1;
		
		if (this.dirty) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			for (int i = 0; i < array.length; ++i)
				array[i].dirty();
			this.dirty = false;
		}
				
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), 51);
		g.setColor(Color.BLACK);
		
		g.drawString("v0.2.0 FPS: " + (int)lastFps, 5, 15);
		g.drawString("Sorting algorithms: (s)election  (i)nsertion  (m)erge  recursive m(e)rge  (b)ogo", 5, 33);
		g.drawString("Miscellaneous: (n)ew  (f)lip  (c)heck  (1-9) pause length  (a)bout", 5, 51);
		
				++fps;
				long time = System.currentTimeMillis();
				if (time - 1000 > lastFpsOut) {
					lastFps = fps;
					fps = 0;
					lastFpsOut = time;
				}

				if (!running && !highlighted.isEmpty()) {
					g.setColor(Color.BLACK);
					for (int i : highlighted)
						g.fillRect(i * BAR_WIDTH + i + 1, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
					highlighted.clear();
				}

				toHighlight.clear();
				for (int i = 0; i < array.length; ++i) {
					if (array[i].isHighlighted()) {
						toHighlight.add(i);
					}

					if (array[i].isDirty()) {
						g.setColor(Color.WHITE);
						g.fillRect(i * BAR_WIDTH + i + 1, HEIGHT - MAX, BAR_WIDTH, MAX);
						g.setColor(Color.BLACK);
						g.fillRect(i * BAR_WIDTH + i + 1, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
					}
				}

				if (!toHighlight.isEmpty()) {
					if (!highlighted.isEmpty()) {
						for (int i = highlighted.size() - 1; i >= 0; --i) {
							if (!toHighlight.contains(highlighted.get(i))) {
								g.setColor(Color.BLACK);
								g.fillRect(highlighted.get(i) * BAR_WIDTH + highlighted.get(i) + 1, HEIGHT - array[highlighted.get(i)].getValue(), 
										BAR_WIDTH, array[highlighted.get(i)].getValue());
								highlighted.remove(i);
							}
						}
					}

					if (!toHighlight.equals(highlighted)) {
						for (int i : toHighlight) {
							if (!highlighted.contains(i)) {
								g.setColor(Color.BLUE);
								g.fillRect(i * BAR_WIDTH + i + 1, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
								highlighted.add(i);
							}
						}
					}
				}
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

}
