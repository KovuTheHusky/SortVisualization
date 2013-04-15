package sortvisualization;

import java.awt.Desktop;
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
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {
	
	private final static int MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private static boolean busy = false;

	private static HashMap<String, JMenuItem> items = new HashMap<String, JMenuItem>();
	
	public Window() throws HeadlessException {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("SortVisualization");
		this.setSize(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setLocationRelativeTo(null);
		this.setupMenuBar();
	}

	public Window(GraphicsConfiguration gc) {
		super(gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("SortVisualization");
		this.setSize(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setLocationRelativeTo(null);
		this.setupMenuBar();
	}

	public Window(String title) throws HeadlessException {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("SortVisualization");
		this.setSize(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setLocationRelativeTo(null);
		this.setupMenuBar();
	}

	public Window(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("SortVisualization");
		this.setSize(SortVisualization.getWidth(), SortVisualization.getHeight());
		this.setLocationRelativeTo(null);
		this.setupMenuBar();
	}
	
	private void setupMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		this.addMenuItem(file, "New", KeyEvent.VK_N);
		this.addMenuItem(file, "Close", KeyEvent.VK_W);
		bar.add(file);
		JMenu edit = new JMenu("Edit");
		this.addMenuItem(edit, "Increase Speed", KeyEvent.VK_EQUALS).setEnabled(false);
		this.addMenuItem(edit, "Decrease Speed", KeyEvent.VK_MINUS);
		this.addMenuItem(edit, "Reset Speed", KeyEvent.VK_0);
		bar.add(edit);
		JMenu sort = new JMenu("Sort");
		this.addMenuItem(sort, "Selection", KeyEvent.VK_S);
		this.addMenuItem(sort, "Insertion", KeyEvent.VK_I);
		this.addMenuItem(sort, "Iterative Merge", KeyEvent.VK_M);
		this.addMenuItem(sort, "Recursive Merge", KeyEvent.VK_M, Event.SHIFT_MASK);
		this.addMenuItem(sort, "Bogo", KeyEvent.VK_B);
		sort.addSeparator();
		this.addMenuItem(sort, "Is Sorted?");
		bar.add(sort);
		JMenu help = new JMenu("Help");
		this.addMenuItem(help, "About...");
		this.addMenuItem(help, "License...");
		bar.add(help);
		this.setJMenuBar(bar);
	}
	
	private JMenuItem addMenuItem(JMenu parent, String name) {
		JMenuItem jmi = new JMenuItem(name);
		jmi.addActionListener(this);
		parent.add(jmi);
		items.put(name, jmi);
		return jmi;
	}
	
	private JMenuItem addMenuItem(JMenu parent, String name, int key) {
		JMenuItem jmi = new JMenuItem(name);
		jmi.addActionListener(this);
		jmi.setAccelerator(KeyStroke.getKeyStroke(key, MODIFIER));
		parent.add(jmi);
		items.put(name, jmi);
		return jmi;
	}
	
	private JMenuItem addMenuItem(JMenu parent, String name, int key, int mod) {
		JMenuItem jmi = new JMenuItem(name);
		jmi.addActionListener(this);
		jmi.setAccelerator(KeyStroke.getKeyStroke(key, MODIFIER + mod));
		parent.add(jmi);
		items.put(name, jmi);
		return jmi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		switch(e.getActionCommand()) {
			case "Close":
				System.exit(0);
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
					while(scanner.hasNextLine())
						fw.write(scanner.nextLine());
					scanner.close();
					fw.close();
					Desktop.getDesktop().browse(new URI("file://" + temp.getAbsolutePath()));
				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
				break;
			case "Increase Speed":
				AudioEngine.setLength(AudioEngine.getLength() - 10);
				if (AudioEngine.getLength() <= 10)
					((JMenuItem)e.getSource()).setEnabled(false);
				items.get("Decrease Speed").setEnabled(true);
				break;
			case "Decrease Speed":
				AudioEngine.setLength(AudioEngine.getLength() + 10);
				if (AudioEngine.getLength() >= 1000)
					((JMenuItem)e.getSource()).setEnabled(false);
				items.get("Increase Speed").setEnabled(true);
				break;
			case "Reset Speed":
				AudioEngine.setLength(10);
				items.get("Increase Speed").setEnabled(true);
				items.get("Decrease Speed").setEnabled(true);
				if (AudioEngine.getLength() <= 10)
					items.get("Increase Speed").setEnabled(false);
				else if (AudioEngine.getLength() >= 1000)
					items.get("Decrease Speed").setEnabled(false);
				break;
		}
		
		if (Window.isBusy())
			return;
		
		switch(e.getActionCommand()) {
			case "New":
				new Thread(new Randomize(SortVisualization.getArray().length)).start();
				break;
			case "Selection":
				Window.setBusy(true);
				new Thread(new SelectionSort(SortVisualization.getArray())).start();
				break;
			case "Insertion":
				Window.setBusy(true);
				new Thread(new InsertionSort(SortVisualization.getArray())).start();
				break;
			case "Iterative Merge":
				Window.setBusy(true);
				new Thread(new MergeSort(SortVisualization.getArray())).start();
				break;
			case "Recursive Merge":
				Window.setBusy(true);
				new Thread(new RecursiveMergeSort(SortVisualization.getArray())).start();
				break;
			case "Bogo":
				Window.setBusy(true);
				new Thread(new BogoSort(SortVisualization.getArray())).start();
				break;
			case "Is Sorted?":
				Window.setBusy(true);
				new Thread(new Runnable() { public void run() {
					Number[] arr = SortVisualization.getArray();
					for (int i = 1; i < arr.length; ++i)
						if (arr[i - 1].gt(arr[i])) {
							Window.setBusy(false);
							break;
						}
					Window.setBusy(false);
				} }).start();
		}
			
	}
	
	public static boolean isBusy() {
		return Window.busy;
	}
	
	public static void setBusy(boolean busy) {
		items.get("New").setEnabled(!busy);
		items.get("Selection").setEnabled(!busy);
		items.get("Insertion").setEnabled(!busy);
		items.get("Iterative Merge").setEnabled(!busy);
		items.get("Recursive Merge").setEnabled(!busy);
		items.get("Bogo").setEnabled(!busy);
		items.get("Is Sorted?").setEnabled(!busy);
		Window.busy = busy;
	}

}
