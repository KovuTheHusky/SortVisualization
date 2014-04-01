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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class Canvas extends JPanel implements ActionListener {
	private Color color = Color.BLUE;
	private int fps;
	private final ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private int lastFps;
	private long lastFpsOut;
	private Timer timer;
	private final ArrayList<Integer> toHighlight = new ArrayList<Integer>();
	private Window window;

	public Canvas() {
		throw new NotImplementedException();
	}

	public Canvas(boolean isDoubleBuffered) {
		throw new NotImplementedException();
	}

	public Canvas(LayoutManager layout) {
		throw new NotImplementedException();
	}

	public Canvas(LayoutManager layout, boolean isDoubleBuffered) {
		throw new NotImplementedException();
	}

	public Canvas(Window window) {
		super();
		this.window = window;
		int len = window.getArray().length;
		int width = len * 8 + len + 2;
		if (width % len == 0)
			++width;
		this.setPreferredSize(new Dimension(width, width * 9 / 16));
		timer = new Timer(16, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

	public Color getColor() {
		return color;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = this.getWidth();
		int height = this.getHeight();
		int bar_width = (width - 1) / window.getArray().length - 1;
		int extra = (width - ((window.getArray().length - 1) * bar_width + window.getArray().length - 1 + 1 + bar_width)) / 2;
		Number[] array = window.getArray();
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
		if (window.isStopped() && !highlighted.isEmpty())
			highlighted.clear();
		toHighlight.clear();
		for (int i = 0; i < array.length; ++i)
			if (array[i].isHighlighted())
				toHighlight.add(i);
		if (!toHighlight.isEmpty()) {
			if (!highlighted.isEmpty())
				for (int i = highlighted.size() - 1; i >= 0; --i)
					if (!toHighlight.contains(highlighted.get(i)))
						highlighted.remove(i);
			if (!toHighlight.equals(highlighted))
				for (int i : toHighlight)
					highlighted.add(i);
		}
		for (int i = 0; i < array.length; ++i) {
			if (!highlighted.contains(i))
				g.setColor(Color.BLACK);
			else
				g.setColor(color);
			g.fillRect(extra + i * bar_width + i + 1, height - array[i].getValue() * height / (window.getMaximum() + 100), bar_width, array[i].getValue() * height / (window.getMaximum() + 100));
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
