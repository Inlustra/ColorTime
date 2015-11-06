package com.thenairn.colortime.painter;

import com.thenairn.colortime.painter.impl.GradientPainter;
import com.thenairn.colortime.painter.impl.InterpolateColorPainter;
import com.thenairn.colortime.painter.impl.SimpleColorPainter;

/**
 * Created by thomas on 06/11/15.
 */
public enum PainterType {

    Simple_Color(new SimpleColorPainter(), "Simple Color"),
    Gradient(new GradientPainter()),
    Interpolate_Color(new InterpolateColorPainter(), "Interpolate Color"),
    Interpolate_Color_Gradient(new InterpolateColorPainter(), "Interpolate Gradient");

    private final ColorPainter painter;
    private final String name;

    PainterType(ColorPainter painter) {
        this(painter, null);
    }

    PainterType(ColorPainter painter, String name) {
        this.painter = painter;
        this.name = name == null ? super.name() : name;
    }

    public ColorPainter getPainter() {
        return painter;
    }

    @Override
    public String toString() {
        return name;
    }
}
