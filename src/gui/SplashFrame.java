package gui;

import util.UIConstants;

import javax.swing.*;
import java.awt.*;

public class SplashFrame extends JFrame {

    private JPanel mainPanel; // object of JPannel

    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel loadingLabel;
    private JLabel versionLabel;

    private JProgressBar progressBar;

    private Timer timer;
    private int progress = 0;

    public SplashFrame() {

        initializeComponents();
        layoutComponents();
        registerListeners();

        setVisible(true);
    }

    private void initializeComponents() {

        // Frame
        setTitle(UIConstants.APP_NAME);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main Panel - an object of jpannel
        mainPanel = new JPanel();
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Labels
        titleLabel = new JLabel("SMART Student Financial Assistant");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        subtitleLabel = new JLabel("Manage Your Money Smarter");
        subtitleLabel.setFont(UIConstants.NORMAL_FONT);
        subtitleLabel.setForeground(UIConstants.SUBTEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(UIConstants.NORMAL_FONT);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        versionLabel = new JLabel(UIConstants.VERSION);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(UIConstants.DISABLED_COLOR);
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    private void layoutComponents() {

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar.setMaximumSize(new Dimension(350, 30));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(loadingLabel);
        centerPanel.add(Box.createVerticalGlue());

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(versionLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registerListeners() {

        timer = new Timer(40, e -> {

            progress++;

            progressBar.setValue(progress);

            if (progress < 30)
                loadingLabel.setText("Loading...");

            else if (progress < 70)
                loadingLabel.setText("Initializing...");

            else
                loadingLabel.setText("Almost Ready...");

            if (progress >= 100) {

                timer.stop();

                dispose();

                new LoginFrame();
            }

        });

        timer.start();
    }
}
