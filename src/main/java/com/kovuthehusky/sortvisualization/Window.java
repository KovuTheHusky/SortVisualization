package com.kovuthehusky.sortvisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {
    public static final int MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private final AudioEngine ae = new AudioEngine();
    private Number[] array = new Number[100];
    private final Canvas canvas;
    private boolean isStopped = true;
    private boolean isStopping = false;
    private int maximum = 1000;
    JMenuItem inc, dec, incVol, decVol;
    ArrayList<JMenuItem> items = new ArrayList<JMenuItem>();

    public Window() {
        super();
        // Get new numbers
        new Randomize(this).run();
        int max = 0;
        for (Number element : array)
            if (element.getValue() > max)
                max = element.getValue();
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
        file.add(jmi = new ModifierMenuItem("Open...", this, KeyEvent.VK_O));
        items.add(jmi);
        file.add(jmi = new ModifierMenuItem("Save...", this, KeyEvent.VK_S));
        items.add(jmi);
        file.add(new ModifierMenuItem("Close", this, KeyEvent.VK_W));
        if (!System.getProperty("os.name").equals("Mac OS X"))
            file.add(new ModifierMenuItem("Exit", this, KeyEvent.VK_F4, Event.ALT_MASK - MODIFIER));
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
        edit.add(jmi = new ModifierMenuItem("Increase Volume", this, KeyEvent.VK_UP));
        incVol = jmi;
        edit.add(jmi = new ModifierMenuItem("Decrease Volume", this, KeyEvent.VK_DOWN));
        decVol = jmi;
        edit.add(new ModifierMenuItem("Reset Volume", this));
        edit.add(new ModifierMenuItem("Custom Volume...", this));
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
        JMenu window = new JMenu("Window");
        window.add(new ModifierMenuItem("New Window", this));
        menu.add(window);
        JMenu help = new JMenu("Help");
        help.add(new ModifierMenuItem("About...", this));
        help.add(new ModifierMenuItem("License...", this));
        help.add(new ModifierMenuItem("Version...", this));
        menu.add(help);
        this.setJMenuBar(menu);
        // Add the canvas
        canvas = new Canvas(this);
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(canvas, BorderLayout.CENTER);
        // Pack the window and show it
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
                } else
                    System.exit(0);
                break;
            case "Exit":
                System.exit(0);
                break;
            case "About...":
                try {
                    Desktop.getDesktop().browse(new URI("https://kovuthehusky.com/projects#sortvisualization"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                break;
            case "License...":
                try {
                    File temp = File.createTempFile("license", ".html");
                    temp.deleteOnExit();
                    FileWriter fw = new FileWriter(temp);
                    InputStream in = this.getClass().getClassLoader().getResourceAsStream("license.html");
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
                JOptionPane.showMessageDialog(this, "SortVisualization @version@");
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
                int len2 = -1;
                while (len2 < 10 || len2 > 1000) {
                    String s = JOptionPane.showInputDialog(this, "How long should we pause for in milliseconds? (10-1000)");
                    if (s == null)
                        return;
                    else
                        len2 = Integer.parseInt(s);
                }
                ae.setLength(len2);
                if (ae.getLength() == 10) {
                    inc.setEnabled(false);
                    dec.setEnabled(true);
                } else if (ae.getLength() == 1000) {
                    inc.setEnabled(true);
                    dec.setEnabled(false);
                } else {
                    inc.setEnabled(true);
                    dec.setEnabled(true);
                }
                break;
            case "Highlight Color...":
                Color c = JColorChooser.showDialog(this, "Highlight Color", canvas.getColor());
                if (c != null)
                    canvas.setColor(c);
                break;
            case "Increase Volume":
                ae.setVolume(ae.getVolume() + 10);
                if (ae.getVolume() >= 100)
                    incVol.setEnabled(false);
                decVol.setEnabled(true);
                break;
            case "Decrease Volume":
                ae.setVolume(ae.getVolume() - 10);
                if (ae.getVolume() <= 0)
                    decVol.setEnabled(false);
                incVol.setEnabled(true);
                break;
            case "Reset Volume":
                ae.setVolume(50);
                incVol.setEnabled(true);
                decVol.setEnabled(true);
                break;
            case "Custom Volume...":
                int len3 = -1;
                while (len3 < 0 || len3 > 100) {
                    String s2 = JOptionPane.showInputDialog(this, "How loud should we play the sounds in percent? (0-100)");
                    if (s2 == null)
                        return;
                    else
                        len3 = Integer.parseInt(s2);
                }
                ae.setVolume(len3);
                if (ae.getVolume() == 0) {
                    incVol.setEnabled(false);
                    decVol.setEnabled(true);
                } else if (ae.getVolume() == 100) {
                    incVol.setEnabled(true);
                    decVol.setEnabled(false);
                } else {
                    incVol.setEnabled(true);
                    decVol.setEnabled(true);
                }
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
                this.start(new Randomize(this));
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
                this.start(new Randomize(this));
                break;
            case "Open...":
                JFileChooser jfc = new JFileChooser();
                Scanner scanner = null;
                if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        scanner = new Scanner(jfc.getSelectedFile());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    ArrayList<Number> nums = new ArrayList<Number>();
                    while (scanner.hasNextInt())
                        nums.add(new Number(scanner.nextInt(), ae));
                    this.setArray(nums.toArray(new Number[nums.size()]));
                }
                break;
            case "Save...":
                JFileChooser jfc2 = new JFileChooser();
                FileWriter fw = null;
                if (jfc2.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
                    try {
                        fw = new FileWriter(jfc2.getSelectedFile());
                        for (Number num : this.getArray())
                            fw.write(num.getValue() + " ");
                        fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                break;
            case "Selection Sort":
                this.start(new SelectionSort(this));
                break;
            case "Insertion Sort":
                this.start(new InsertionSort(this));
                break;
            case "Iterative Merge Sort":
                this.start(new MergeSort(this));
                break;
            case "Recursive Merge Sort":
                this.start(new RecursiveMergeSort(this));
                break;
            case "Bogosort":
                this.start(new BogoSort(this));
                break;
            case "Is Sorted?":
                this.start(new Runnable() {
                    @Override
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

    public Number[] getArray() {
        return array;
    }

    public AudioEngine getAudioEngine() {
        return ae;
    }

    public int getMaximum() {
        return maximum;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isStopping() {
        return isStopping;
    }

    public void setArray(Number[] array) {
        this.array = array;
    }

    public void start(final Runnable runnable) {
        new Thread(new Runnable() {
            @Override
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
}
