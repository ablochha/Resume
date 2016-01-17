package com.team23.weather.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This Swing component can wrap a pre-existing {@link javax.swing.JPanel} and add a label on the top.
 *
 * @author Saquib Mian
 */
public class JComponentLabel extends JPanel {

    /**
     * the alignment of the label
     */
    public final float _alignment;

    /**
     * the contents of the label
     */
    private final String _label;

    /**
     * the component for the label
     * this is wrapped in a component so that it becomes an entire row
     */
    private JComponent _labelComponent;

    /**
     * the component that was wrapped
     */
    private final JComponent _component;

    /**
     * the font size of the label
     */
    private final Font _font;

    /**
     * Initializes the alignment of the label to the {@link com.team23.weather.gui.JComponentLabel#_alignment}
     * @param label the label of the component
     * @param component the {@link javax.swing.JPanel} to wrap
     */
    public JComponentLabel(String label, JComponent component) {
        this(label, component, Component.CENTER_ALIGNMENT);
    }

    /**
     * @param label the label of the component
     * @param component the {@link javax.swing.JPanel} to wrap
     * @param alignment the alignment of the label (e.g., {@link java.awt.Component#CENTER_ALIGNMENT}
     */
    public JComponentLabel(String label, JComponent component, float alignment ) {
        this(label, component, alignment, Fonts.Small);
    }

    /**
     * @param label the label of the component
     * @param component the {@link javax.swing.JPanel} to wrap
     * @param alignment the alignment of the label (e.g., {@link java.awt.Component#CENTER_ALIGNMENT}
     * @param font the font of the label
     */
    public JComponentLabel(String label, JComponent component, float alignment, Font font) {
        _label = label;
        _component = component;
        _alignment = alignment;
        _font = font;

        init();
    }

    private void init(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel(_label);
        label.setFont(_font);

        _labelComponent = new JPanel();
        _labelComponent.setLayout(new BoxLayout(_labelComponent, BoxLayout.PAGE_AXIS));
        _labelComponent.add(label);
        _labelComponent.setOpaque(true);
        _labelComponent.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(_font.getSize() * 1.5)));

        label.setAlignmentX(_alignment);
        _labelComponent.setAlignmentX(_alignment);
        _component.setAlignmentX(_alignment);

        this.add(_labelComponent);
        this.add(_component);
    }

    /**
     * Sets the background color of the label in this panel.
     * @param color the new background color
     */
    public void setLabelBackground( Color color ) {
        _labelComponent.setBackground(color);
    }
}
