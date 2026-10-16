package com.jadicaped;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.runelite.client.ui.PluginPanel;

public class PostDeathSummaryPanel extends PluginPanel {
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 420;
    private static final float TITLE_FONT_SIZE = 13f;

    private final JLabel titleLabel = new JLabel("Post-Death Sidebar Summary");
    private final JTextArea summaryArea = new JTextArea();
    private final JButton exportButton = new JButton("Export to Text");

    public PostDeathSummaryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setOpaque(false);
        summaryArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        summaryArea.setText(PostDeathSummary.empty().toPlainText());

        exportButton.addActionListener(event -> exportSummaryToText());
        exportButton.setFocusPainted(false);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        add(exportButton, BorderLayout.SOUTH);
    }

    public void setSummary(PostDeathSummary summary) {
        final PostDeathSummary resolved = summary == null ? PostDeathSummary.empty() : summary;
        summaryArea.setText(resolved.toPlainText());
    }

    public void setNoAttemptYet() {
        summaryArea.setText(PostDeathSummary.empty().toPlainText());
    }

    private void exportSummaryToText() {
        final String text = summaryArea.getText();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }
}
