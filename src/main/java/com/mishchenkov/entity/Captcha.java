package com.mishchenkov.entity;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import static java.awt.RenderingHints.*;

public class Captcha {

    private final BufferedImage bufferedImage;
    private final Graphics2D graphics;
    private final AffineTransform transform;
    private String captchaText;
    private final Color[] colors = {
            Color.RED, Color.BLUE, Color.DARK_GRAY, Color.MAGENTA,
            new Color(140, 94, 95) , new Color(162, 140, 17), new Color(51, 137, 14)
    };
    private int colorIndex;

    private static final int FONT_SIZE = 30;
    private static final int TEXT_SHIFT = 10;

    public Captcha() {
        this(getRandomByRange(11111, 99999) + "");
    }

    public Captcha(String captchaText) {
        this(TEXT_SHIFT * 2 + (captchaText.length() * FONT_SIZE),FONT_SIZE * 2);
        this.captchaText = captchaText;
    }

    public Captcha(int width, int height) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
        transform = graphics.getTransform();
    }

    public void setBackground(Color color) {
        int width = getImgWidth();
        int height = getImgHeight();

        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
    }

    public void paintText() {
        Font font = new Font("Dialog", Font.BOLD, FONT_SIZE);
        graphics.setFont(font);

        for (int i = 0; i < captchaText.length(); i++) {
            char ch = captchaText.charAt(i);

            int randomYShift = getRandomByRange(0, 20) - 10;
            int x = TEXT_SHIFT + FONT_SIZE * i;
            int y = (bufferedImage.getHeight() / 2) + (FONT_SIZE / 2) + randomYShift;

            int form = 10;
            int to = 150;
            double rotateIndex = (double) getRandomByRange(form, to) / 1000;

            graphics.setColor(getNextColor());
            graphics.rotate(rotateIndex, x + getImgWidth() / 2., y);
            graphics.drawString(ch + "", x, y);

        }
    }

    public void paintPoints(int count) {
        graphics.setTransform(transform);

        for (int i = 0; i <= count; i++) {
            int x = getRandomByRange(0, getImgWidth());
            int y = getRandomByRange(0, getImgHeight());

            Color color = getNextColor();

            graphics.setColor(color);
            graphics.drawOval(x, y, 1, 1);
        }
    }

    public void paintLines(int count) {
        graphics.setTransform(transform);

        for (int i = 0; i <= count; i++) {
            int x = getRandomByRange(0, getImgWidth());
            int y = getRandomByRange(0, getImgHeight());
            int x2 = getRandomByRange(0, getImgWidth());
            int y2 = getRandomByRange(0, getImgHeight());

            Color color = getNextColor();

            graphics.setColor(color);
            graphics.drawLine(x, y, x2, y2);
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getCaptchaText() {
        return captchaText;
    }

    private Color getNextColor() {
        colorIndex = colorIndex % colors.length;
        return colors[colorIndex++];
    }

    private int getImgWidth() {
        return bufferedImage.getWidth();
    }

    private int getImgHeight() {
        return bufferedImage.getHeight();
    }

    private static int getRandomByRange(int from, int to) {
        Random random = new SecureRandom();
        return from + random.nextInt(to - from);
    }

}
