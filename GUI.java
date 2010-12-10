import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import javax.swing.UIManager.*;

/**
 * This class controls the GUI launcher.
 * @author Brian Mock
 */
public class GUI
extends JFrame
implements ActionListener {
    private JButton   waveButton;
    private JButton wavierButton;
    private JButton saddleButton;

    private JButton defaultShaderButton;
    private JButton   phongShaderButton;
    private JButton     celShaderButton;

    private WaveFunc   wave   = new WaveFunc();
    private WavierFunc wavier = new WavierFunc();
    private SaddleFunc saddle = new SaddleFunc();
    
    private String theShader;

    private String theTitle = "Visual Integrator by Brian Mock";

    // Method provided by Oracle.com Java tutorial
    private void
    attemptNimbusStyle() {
        try {
            for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) {
        }
    }

    public
    GUI() {
        attemptNimbusStyle();
        setTitle(theTitle);

        waveButton          = new JButton(wave.toString());
        wavierButton        = new JButton(wavier.toString());
        saddleButton        = new JButton(saddle.toString());
        //=========================================|
        defaultShaderButton = new JButton("Plain" );
        phongShaderButton   = new JButton("Pretty");
        celShaderButton     = new JButton("Cel"   );

        theShader = null;
        disableShaderButton(defaultShaderButton);

        waveButton          .setActionCommand("set_func_wave"   );
        wavierButton        .setActionCommand("set_func_wavier" );
        saddleButton        .setActionCommand("set_func_saddle" );
        //=======================================================|
        defaultShaderButton .setActionCommand("set_shader_none" );
        phongShaderButton   .setActionCommand("set_shader_phong");
        celShaderButton     .setActionCommand("set_shader_cel"  );

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());

        //cp.add(new JLabel("Shader:"));
        //=========================|
        //cp.add(defaultShaderButton);
        //cp.add(phongShaderButton  );
        //cp.add(celShaderButton    );
        //=========================|
        //cp.add(makeVSep()         );
        //cp.add(makeVSep()         );
        //=========================|
        cp.add(new JLabel("Function:"));
        //=========================|
        cp.add(waveButton         );
        cp.add(wavierButton       );
        cp.add(saddleButton       );

        pack();

        defaultShaderButton .addActionListener(this);
        phongShaderButton   .addActionListener(this);
        celShaderButton     .addActionListener(this);
        //==========================================|
        waveButton          .addActionListener(this);
        wavierButton        .addActionListener(this);
        saddleButton        .addActionListener(this);

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

    /** Make a vertical separator. */
    public JSeparator
    makeVSep() {
        return new JSeparator(JSeparator.VERTICAL);
    }

    /** Handle actions being performed. */
    public void
    actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("set_func_wave")) {
            Visualizer.launchWith(wave, theShader);
        }
        else if (cmd.equals("set_func_wavier")) {
            Visualizer.launchWith(wavier, theShader);
        }
        else if (cmd.equals("set_func_saddle")) {
            Visualizer.launchWith(saddle, theShader);
        }
        else if (cmd.equals("set_shader_none")) {
            useShaderButton(src, null);
        }
        else if (cmd.equals("set_shader_phong")) {
            useShaderButton(src, "phong");
        }
        else if (cmd.equals("set_shader_cel")) {
            useShaderButton(src, "cel");
        }
    }

    private void
    useShaderButton(Object button, String shaderName) {
        JButton cButton = (JButton) button;
        enableShaderButtons();
        disableShaderButton(cButton);
        theShader = shaderName;
    }

    private void
    enableShaderButtons() {
        JButton[] buttons = {
            celShaderButton,
            phongShaderButton,
            defaultShaderButton
        };

        for (JButton button: buttons) {
            button.setEnabled(true);
        }
    }

    private void
    disableShaderButton(JButton button) {
        button.setEnabled(false);
    }
}
