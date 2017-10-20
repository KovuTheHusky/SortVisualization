package com.kovuthehusky.sortvisualization;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class ModifierMenuItem extends JMenuItem {
    public ModifierMenuItem() {
        super();
    }

    public ModifierMenuItem(Action a) {
        super(a);
    }

    public ModifierMenuItem(Icon icon) {
        super(icon);
    }

    public ModifierMenuItem(String text) {
        super(text);
    }

    public ModifierMenuItem(String text, ActionListener listener) {
        super(text);
        this.addActionListener(listener);
    }

    public ModifierMenuItem(String text, ActionListener listener, int key) {
        super(text);
        this.setAccelerator(KeyStroke.getKeyStroke(key, Window.MODIFIER));
        this.addActionListener(listener);
    }

    public ModifierMenuItem(String text, ActionListener listener, int key, int mod) {
        super(text);
        this.setAccelerator(KeyStroke.getKeyStroke(key, Window.MODIFIER + mod));
        this.addActionListener(listener);
    }

    public ModifierMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    public ModifierMenuItem(String text, int mnemonic) {
        super(text, mnemonic);
    }
}
