package com.codeski.sortvisualization;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SortVisualization {
    private static ArrayList<Image> icons;
    private static boolean muted = false;
    private static ArrayList<Window> windows = new ArrayList<Window>();

    public static void addWindow(Window w) {
        windows.add(w);
    }

    public static ArrayList<Image> getIconImages() {
        return icons;
    }

    public static int getWindowCount() {
        return windows.size();
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void main(String args[]) {
        int[] sizes = { 256, 128, 64, 48, 32, 24, 16 };
        icons = new ArrayList<Image>();
        for (int i : sizes)
            icons.add(new ImageIcon(SortVisualization.class.getClassLoader().getResource("icon" + i + ".png")).getImage());
        if (System.getProperty("os.name").equals("Mac OS X"))
            try {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        new Window();
    }

    public static void removeWindow(Window w) {
        windows.remove(w);
    }

    public static void setMuted(boolean muted) {
        SortVisualization.muted = muted;
    }
}
