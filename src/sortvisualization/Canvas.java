package sortvisualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Canvas extends JPanel implements ActionListener {
	

	private static int BAR_WIDTH = SortVisualization.getWidth() / SortVisualization.getArray().length - 1;
		
	private ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private ArrayList<Integer> toHighlight = new ArrayList<Integer>();
	
	private int fps;
	private int lastFps;
	private long lastFpsOut;
	
	private Timer timer;

	public Canvas() {
		super();
		Dimension d = new Dimension(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setPreferredSize(d);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(LayoutManager layout) {
		super(layout);
		Dimension d = new Dimension(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setPreferredSize(d);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		Dimension d = new Dimension(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setPreferredSize(d);
		timer = new Timer(16, this);
		timer.start();
	}

	public Canvas(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		Dimension d = new Dimension(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setPreferredSize(d);
		timer = new Timer(16, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Number[] array = SortVisualization.getArray();
				
		g.setColor(Color.BLACK);
		
		g.setFont(Font.decode("arial-plain-10"));
		
		g.drawString(lastFps + "", 5, 12);
		
				++fps;
				long time = System.currentTimeMillis();
				if (time - 1000 > lastFpsOut) {
					
					lastFps = fps;
					fps = 0;
					lastFpsOut = time;
				}

				if (!Window.isBusy() && !highlighted.isEmpty())
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
					g.fillRect(i * BAR_WIDTH + i + 1, SortVisualization.getHeight() - array[i].getValue(), BAR_WIDTH, array[i].getValue());
				}
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}
	
}
