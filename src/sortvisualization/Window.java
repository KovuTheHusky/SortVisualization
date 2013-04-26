package sortvisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {

	public static final int MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	private Number[] array = new Number[100];

	private int maximum = 1000;

	private boolean isStopping = false;
	private boolean isStopped = true;

	private Canvas canvas;
	private AudioEngine ae = new AudioEngine();

	ArrayList<JMenuItem> items = new ArrayList<JMenuItem>();
	JMenuItem inc, dec;

	public Window() throws HeadlessException {
		super();

		// Get new numbers
		new Randomize(this).run();

		int max = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i].getValue() > max)
				max = array[i].getValue();
		}
		maximum = max;

		// Set up the window
		SortVisualization.addWindow(this);
		this.setIconImages(SortVisualization.getIconImages());
		this.setTitle("SortVisualization");
		int minimumWidth = 2 * array.length + 1;
		this.setMinimumSize(new Dimension(minimumWidth, minimumWidth * 9 / 16));
		this.setResizable(true);

		// Set up the menu
		JMenuItem jmi;
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		file.add(jmi = new ModifierMenuItem("New", this, KeyEvent.VK_N));
		items.add(jmi);
		file.add(jmi = new ModifierMenuItem("New...", this, KeyEvent.VK_N, Event.SHIFT_MASK));
		items.add(jmi);
		file.add(new ModifierMenuItem("New Window", this));
		file.add(new ModifierMenuItem("Close", this, KeyEvent.VK_W));
		if (!System.getProperty("os.name").equals("Mac OS X"))
			file.add(new ModifierMenuItem("Exit", this, KeyEvent.VK_Q));
		menu.add(file);
		JMenu edit = new JMenu("Edit");
		edit.add(jmi = new ModifierMenuItem("Increase Speed", this, KeyEvent.VK_EQUALS)).setEnabled(false);
		inc = jmi;
		edit.add(jmi = new ModifierMenuItem("Decrease Speed", this, KeyEvent.VK_MINUS));
		dec = jmi;
		edit.add(new ModifierMenuItem("Reset Speed", this, KeyEvent.VK_0));
		edit.add(new ModifierMenuItem("Custom Speed...", this));
		edit.addSeparator();
		edit.add(new ModifierMenuItem("Highlight Color...", this));
		edit.addSeparator();
		JCheckBoxMenuItem jcbmi = new JCheckBoxMenuItem("Mute Sounds");
		jcbmi.addActionListener(this);
		edit.add(jcbmi);
		jcbmi = new JCheckBoxMenuItem("Mute All Sounds");
		jcbmi.addActionListener(this);
		edit.add(jcbmi);
		menu.add(edit);
		JMenu sort = new JMenu("Sort");
		sort.add(jmi = new ModifierMenuItem("Selection Sort", this, KeyEvent.VK_S));
		items.add(jmi);
		sort.add(jmi = new ModifierMenuItem("Insertion Sort", this, KeyEvent.VK_I));
		items.add(jmi);
		sort.add(jmi = new ModifierMenuItem("Iterative Merge Sort", this, KeyEvent.VK_M));
		items.add(jmi);
		sort.add(jmi = new ModifierMenuItem("Recursive Merge Sort", this, KeyEvent.VK_M, Event.SHIFT_MASK));
		items.add(jmi);
		sort.add(jmi = new ModifierMenuItem("Bogosort", this, KeyEvent.VK_B));
		items.add(jmi);
		sort.addSeparator();
		sort.add(jmi = new ModifierMenuItem("Is Sorted?", this));
		items.add(jmi);
		sort.add(jmi = new ModifierMenuItem("Stop Sorting", this, KeyEvent.VK_ESCAPE, -MODIFIER)).setEnabled(false);
		items.add(jmi);
		menu.add(sort);
		JMenu help = new JMenu("Help");
		help.add(new ModifierMenuItem("About...", this));
		help.add(new ModifierMenuItem("License...", this));
		help.add(new ModifierMenuItem("Version...", this));
		menu.add(help);
		this.setJMenuBar(menu);

		// Add the canvas
		this.canvas = new Canvas(this);
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(canvas, BorderLayout.CENTER);

		// Pack the window and show it
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public Window(GraphicsConfiguration gc) {
		throw new NotImplementedException();
	}

	public Window(String title) throws HeadlessException {
		throw new NotImplementedException();
	}

	public Window(String title, GraphicsConfiguration gc) {
		throw new NotImplementedException();
	}

	public boolean isStopping() {
		return this.isStopping;
	}

	public boolean isStopped() {
		return this.isStopped;
	}

	public int getMaximum() {
		return this.maximum;
	}

	public void start(final Runnable runnable) {
		new Thread(new Runnable() {
			public void run() {
				isStopped = false;
				for (JMenuItem jmi : items)
					jmi.setEnabled(!jmi.isEnabled());
				Thread thread;
				(thread = new Thread(runnable)).start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (JMenuItem jmi : items)
					jmi.setEnabled(!jmi.isEnabled());
				isStopped = true;
				isStopping = false;
			}
		}).start();
	}

	public Number[] getArray() {
		return this.array;
	}

	public void setArray(Number[] array) {
		this.array = array;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
			case "New Window":
				new Window();
				break;
			case "Close":
				if (SortVisualization.getWindowCount() > 1) {
					isStopping = true;
					SortVisualization.removeWindow(this);
					this.dispose();
				} else {
					System.exit(0);
				}
				break;
			case "Exit":
				System.exit(0);
				break;
			case "About...":
				try {
					Desktop.getDesktop().browse(new URI("http://codeski.com/sortvisualization"));
				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
				break;
			case "License...":
				try {
					File temp = File.createTempFile("license", ".html");
					temp.deleteOnExit();
					FileWriter fw = new FileWriter(temp);
					InputStream in = getClass().getClassLoader().getResourceAsStream("license.html");
					Scanner scanner = new Scanner(in);
					while (scanner.hasNextLine())
						fw.write(scanner.nextLine());
					scanner.close();
					fw.close();
					Desktop.getDesktop().browse(temp.toURI());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				break;
			case "Version...":
				JOptionPane.showMessageDialog(this, "SortVisualization Version 0.3.2α");
				break;
			case "Increase Speed":
				ae.setLength(ae.getLength() - 10);
				if (ae.getLength() <= 10)
					inc.setEnabled(false);
				dec.setEnabled(true);
				break;
			case "Decrease Speed":
				ae.setLength(ae.getLength() + 10);
				if (ae.getLength() >= 1000)
					dec.setEnabled(false);
				inc.setEnabled(true);
				break;
			case "Reset Speed":
				int len = 10;
				ae.setLength(len);
				inc.setEnabled(false);
				dec.setEnabled(true);
				break;
			case "Custom Speed...":
				int len2 = 0;
				while (len2 < 10 || len2 > 1000) {
					String s = JOptionPane.showInputDialog(this, "How long should we pause for in milliseconds? (10-1000)");
					if (s == null)
						return;
					else
						len2 = Integer.parseInt(s);
				}
				ae.setLength(len2);
				inc.setEnabled(true);
				dec.setEnabled(true);
				break;
			case "Highlight Color...":
				Color c = JColorChooser.showDialog(this, "Highlight Color", canvas.getColor());
				if (c != null)
					canvas.setColor(c);
				break;
			case "Mute Sounds":
				ae.setMuted(!ae.isMuted());
				break;
			case "Mute All Sounds":
				SortVisualization.setMuted(!SortVisualization.isMuted());
				break;
			case "Stop Sorting":
				isStopping = true;
				break;
		}

		if (!isStopped)
			return;

		switch (e.getActionCommand()) {
			case "New":
				start(new Randomize(this));
				break;
			case "New...":
				int len = 0;
				while (len < 1) {
					String s = JOptionPane.showInputDialog(this, "How many elements would you like?");
					if (s == null)
						return;
					else
						len = Integer.parseInt(s);
				}
				this.setArray(new Number[len]);
				start(new Randomize(this));
				break;
			case "Selection Sort":
				start(new SelectionSort(this));
				break;
			case "Insertion Sort":
				start(new InsertionSort(this));
				break;
			case "Iterative Merge Sort":
				start(new MergeSort(this));
				break;
			case "Recursive Merge Sort":
				start(new RecursiveMergeSort(this));
				break;
			case "Bogosort":
				start(new BogoSort(this));
				break;
			case "Is Sorted?":
				start(new Runnable() {
					public void run() {
						Number[] arr = array;
						for (int i = 1; i < arr.length; ++i)
							if (arr[i - 1].gt(arr[i])) {
								JOptionPane.showMessageDialog(null, "The array is not sorted.");
								return;
							}
						JOptionPane.showMessageDialog(null, "The array is sorted!");
					}
				});
		}

	}

	public AudioEngine getAudioEngine() {
		return ae;
	}

}
