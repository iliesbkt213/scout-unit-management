package viewPackage;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ScoutAnimationPanel extends JPanel {

    private double flagWavePhase;
    private int campfireFlicker;
    private boolean running;
    private Thread animationThread;

    public ScoutAnimationPanel() {
        setBackground(new Color(255, 248, 220));
        flagWavePhase = 0.0;
        campfireFlicker = 0;
        running = false;
    }

    public synchronized void startAnimation() {
        if (running) {
            return;
        }
        running = true;
        animationThread = new Thread(this::animate, "ScoutAnimationThread");
        animationThread.setDaemon(true);
        animationThread.start();
    }

    public synchronized void stopAnimation() {
        running = false;
        if (animationThread != null) {
            animationThread.interrupt();
            animationThread = null;
        }
    }

    private void animate() {
        try {
            while (running) {
                flagWavePhase += 0.15;
                if (flagWavePhase > Math.PI * 2) {
                    flagWavePhase -= Math.PI * 2;
                }
                campfireFlicker = (int) (Math.random() * 6);
                repaint();
                Thread.sleep(60);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        drawSky(g2, width, height);
        drawGround(g2, width, height);
        drawFlag(g2, width, height);
        drawCampfire(g2, width, height);
        drawTent(g2, width, height);

        g2.dispose();
    }

    private void drawSky(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(180, 220, 255));
        g2.fill(new Rectangle2D.Double(0, 0, width, height * 0.65));
    }

    private void drawGround(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(120, 170, 90));
        g2.fill(new Rectangle2D.Double(0, height * 0.65, width, height * 0.35));
    }

    private void drawFlag(Graphics2D g2, int width, int height) {
        int poleX = (int) (width * 0.20);
        int poleTop = (int) (height * 0.20);
        int poleBottom = (int) (height * 0.65);
        g2.setColor(new Color(90, 60, 30));
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(poleX, poleTop, poleX, poleBottom);

        int flagWidth = (int) (width * 0.12);
        int flagHeight = (int) (height * 0.08);
        int[] xPoints = new int[flagWidth + 1];
        int[] yPoints = new int[flagWidth + 1];
        for (int i = 0; i <= flagWidth; i++) {
            xPoints[i] = poleX + i;
            yPoints[i] = poleTop + (int) (Math.sin((i / 12.0) + flagWavePhase) * 5);
        }
        g2.setColor(new Color(190, 30, 50));
        for (int i = 0; i < flagWidth; i++) {
            g2.fillRect(xPoints[i], yPoints[i], 1, flagHeight);
        }
    }

    private void drawCampfire(Graphics2D g2, int width, int height) {
        int centerX = (int) (width * 0.65);
        int centerY = (int) (height * 0.78);
        g2.setColor(new Color(70, 40, 20));
        g2.fillRect(centerX - 30, centerY + 8, 60, 6);
        for (int i = 0; i < 3; i++) {
            int flameHeight = 30 + campfireFlicker * 3 - i * 8;
            int flameWidth = 20 - i * 4;
            Color flameColor = i == 0 ? new Color(230, 80, 30)
                    : (i == 1 ? new Color(255, 160, 50) : new Color(255, 220, 80));
            g2.setColor(flameColor);
            g2.fill(new Ellipse2D.Double(
                    centerX - flameWidth / 2.0,
                    centerY - flameHeight,
                    flameWidth,
                    flameHeight));
        }
    }

    private void drawTent(Graphics2D g2, int width, int height) {
        int baseX = (int) (width * 0.40);
        int baseY = (int) (height * 0.78);
        int tentWidth = (int) (width * 0.10);
        int tentHeight = (int) (height * 0.12);

        int[] xPoints = {baseX, baseX + tentWidth / 2, baseX + tentWidth};
        int[] yPoints = {baseY, baseY - tentHeight, baseY};
        g2.setColor(new Color(150, 90, 50));
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.setColor(new Color(70, 40, 20));
        g2.drawPolygon(xPoints, yPoints, 3);
        g2.drawLine(baseX + tentWidth / 2, baseY - tentHeight,
                baseX + tentWidth / 2, baseY);
    }
}
