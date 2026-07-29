package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class UIConstants {

    // ==========================================================
    // APPLICATION
    // ==========================================================

    public static final String APP_NAME = "SMART Student Financial Assistant";
    public static final String VERSION = "Version 1.0";

    // ==========================================================
    // WINDOW SIZE
    // ==========================================================

    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 700;

    public static final Dimension BUTTON_SIZE = new Dimension(170, 40);
    public static final Dimension LOGIN_BUTTON_SIZE = new Dimension(220, 45);
    public static final Dimension TEXTFIELD_SIZE = new Dimension(250, 35);

    // ==========================================================
    // COLORS
    // ==========================================================

    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color PANEL_COLOR = Color.WHITE;

    public static final Color PRIMARY_COLOR = new Color(31, 58, 95);
    public static final Color BUTTON_COLOR = new Color(46, 134, 222);

    public static final Color DELETE_BUTTON_COLOR = new Color(231, 76, 60);

    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    public static final Color WARNING_COLOR = new Color(243, 156, 18);

    public static final Color TEXT_COLOR = new Color(31, 41, 55);
    public static final Color SUBTEXT_COLOR = new Color(75, 85, 99);
    public static final Color DISABLED_COLOR = new Color(156, 163, 175);

    // ==========================================================
    // FONTS
    // ==========================================================

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);

    public static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 20);

    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 15);

    // ==========================================================
    // PRIVATE CONSTRUCTOR
    // ==========================================================

    private UIConstants() {
        // Prevent object creation
        // since this class only contains static constants, nobody should ever create an
        // object so making the constructor private prevents that
    }
}