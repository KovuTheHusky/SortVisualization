package sortvisualization;

import java.awt.Component;
import java.awt.Event;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {
	
	private final static int MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private static boolean running = false;

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
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(this);
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, MODIFIER));
		file.add(about);
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
			case "New":
				new Thread(new Randomize(SortVisualization.getArray().length, 60, 540)).start();
				break;
			case "Close":
				System.exit(0);
			case "About":
				new Thread(new Runnable() { public void run() {
					JOptionPane.showMessageDialog((Component)Window.getWindows()[0],
							"SortVisualization is a project designed" + System.lineSeparator() +
							"for educational purposes. It was made by" + System.lineSeparator() + 
							"Kevin Breslin who is a student at" + System.lineSeparator() + 
							"Suffolk County Community College in" + System.lineSeparator() + 
							"Selden, New York. Version 0.2.0.",
							"About", JOptionPane.INFORMATION_MESSAGE); } }).start();
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
				
			case "Selection":
				Window.running = true;
				new Thread(new SelectionSort(SortVisualization.getArray())).start();
				break;
			case "Insertion":
				Window.running = true;
				new Thread(new InsertionSort(SortVisualization.getArray())).start();
				break;
			case "Iterative Merge":
				Window.running = true;
				new Thread(new MergeSort(SortVisualization.getArray())).start();
				break;
			case "Recursive Merge":
				Window.running = true;
				new Thread(new RecursiveMergeSort(SortVisualization.getArray())).start();
				break;
			case "Bogo":
				Window.running = true;
				new Thread(new BogoSort(SortVisualization.getArray())).start();
				break;
		}
			
	}
	
	public static boolean isRunning() {
		return Window.running;
	}
	
	public static void setRunning(boolean running) {
		Window.running = running;
	}

}
