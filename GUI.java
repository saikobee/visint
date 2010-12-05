import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GUI
extends JFrame
implements ActionListener {
    private JButton waveButton;
    private JButton wavierButton;

    public
    GUI() {
        setTitle("Volume Integral Visualizer");

        waveButton   = new JButton("Wave");
        wavierButton = new JButton("Wavier");

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(waveButton);
        cp.add(wavierButton);

        pack();

        waveButton  .addActionListener(this);
        wavierButton.addActionListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void
    main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }

    public void
    actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == waveButton) {
            onWaveButtonClicked();
        }
        else if (src == wavierButton) {
            onWavierButtonClicked();
        }
    }

    private void
    onWaveButtonClicked() {
        Visualizer.launchWith(new WaveFunc(), "cel");
    }

    private void
    onWavierButtonClicked() {
        Visualizer.launchWith(new WavierFunc(), null);
    }
}
