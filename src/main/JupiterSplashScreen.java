package main;

import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class JupiterSplashScreen extends JWindow {

    private int duration;

    public JupiterSplashScreen(int d) {
        duration = d;
    }

    public void showSplash() {
        ImageBackgroundPanel content = new ImageBackgroundPanel();
        this.setContentPane(content);

        int width = 390;
        int height = 215;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screen = tk.getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JLabel title = new JLabel("JUPITER: CSSE232 Assembler and Runtime Simulator", JLabel.CENTER);
        title.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        title.setForeground(Color.white);
        content.add(title, BorderLayout.NORTH);

        setVisible(true);
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
        	System.out.println(e);
        }
        setVisible(false);
    }
    
    class ImageBackgroundPanel extends JPanel {
        Image image;
        public ImageBackgroundPanel() {
            try {
                image = new ImageIcon(Toolkit.getDefaultToolkit()
                		.getImage(this.getClass().getResource("/jupiter.jpg"))).getImage();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null)
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}

