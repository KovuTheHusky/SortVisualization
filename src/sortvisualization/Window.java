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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {
	
	private final static int MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private static boolean busy = false;

	HashMap<String, JMenuItem> items = new HashMap<String, JMenuItem>();
	
	public Window() throws HeadlessException {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("SortVisualization");
		
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem newArray = new JMenuItem("New");
		newArray.addActionListener(this);
		newArray.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, MODIFIER));
		file.add(newArray);
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(this);
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, MODIFIER));
		file.add(close);
		menu.add(file);
		JMenu edit = new JMenu("Edit");
		JMenuItem incSpeed = new JMenuItem("Increase Speed");
		incSpeed.addActionListener(this);
		incSpeed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, MODIFIER));
		incSpeed.setEnabled(false);
		edit.add(incSpeed);
		items.put("Increase Speed", incSpeed);
		JMenuItem decSpeed = new JMenuItem("Decrease Speed");
		decSpeed.addActionListener(this);
		decSpeed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, MODIFIER));
		edit.add(decSpeed);
		items.put("Decrease Speed", decSpeed);
		JMenuItem resetSpeed = new JMenuItem("Reset Speed");
		resetSpeed.addActionListener(this);
		resetSpeed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, MODIFIER));
		edit.add(resetSpeed);
		menu.add(edit);
		JMenu sort = new JMenu("Sort");
		JMenuItem selection = new JMenuItem("Selection");
		selection.addActionListener(this);
		selection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, MODIFIER));
		sort.add(selection);
		JMenuItem insertion = new JMenuItem("Insertion");
		insertion.addActionListener(this);
		insertion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, MODIFIER));
		sort.add(insertion);
		JMenuItem merge = new JMenuItem("Iterative Merge");
		merge.addActionListener(this);
		merge.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, MODIFIER));
		sort.add(merge);
		JMenuItem merge2 = new JMenuItem("Recursive Merge");
		merge2.addActionListener(this);
		merge2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, MODIFIER + Event.SHIFT_MASK));
		sort.add(merge2);
		JMenuItem bogo = new JMenuItem("Bogo");
		bogo.addActionListener(this);
		bogo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, MODIFIER));
		sort.add(bogo);
		menu.add(sort);
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About...");
		about.addActionListener(this);
		//about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, MODIFIER));
		items.put("About...", about);
		help.add(about);
		JMenuItem license = new JMenuItem("License...");
		license.addActionListener(this);
		//license.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, MODIFIER));
		items.put("License", license);
		help.add(license);
		menu.add(help);
		this.setJMenuBar(menu);
	}

	public Window(GraphicsConfiguration gc) {
		super(gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Window(String title) throws HeadlessException {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Window(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		switch(e.getActionCommand()) {
			case "Close":
				System.exit(0);
			case "About...":
				try {
					File compressed = new File(SortVisualization.class.getClassLoader().getResource("about.html").getFile());
					File image = new File(SortVisualization.class.getClassLoader().getResource("icon128.png").getFile());
					File temp = File.createTempFile("about", ".html");
					File tempImage = File.createTempFile("about", ".html");
					temp.deleteOnExit();
					tempImage.deleteOnExit();
					temp.delete();
					tempImage.delete();
					Files.copy(compressed.toPath(), temp.toPath());
					Files.copy(image.toPath(), tempImage.toPath());
					Desktop.getDesktop().browse(new URI("file://" + temp.getAbsolutePath()));
				} catch (IOException | URISyntaxException e2) {
					e2.printStackTrace();
				}
				break;
			case "License...":
				try {
					File compressed = new File(SortVisualization.class.getClassLoader().getResource("license.html").getFile());
					File temp = File.createTempFile("license", ".html");
					temp.deleteOnExit();
					temp.delete();
					Files.copy(compressed.toPath(), temp.toPath());
					Desktop.getDesktop().browse(new URI("file://" + temp.getAbsolutePath()));
				} catch (IOException | URISyntaxException e2) {
					e2.printStackTrace();
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
				new Thread(new Randomize(SortVisualization.getArray().length, 60, 540)).start();
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
		}
			
	}
	
	public static boolean isBusy() {
		return Window.busy;
	}
	
	public static void setBusy(boolean busy) {
		
		Window.busy = busy;
	}

}
