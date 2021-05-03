package com.mishchenkov.controller;

import com.mishchenkov.entity.Captcha;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.service.CaptchaService;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;

@WebServlet("/captcha.jpg")
public class CaptchaServicesController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(CaptchaServicesController.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info(req.getSession().getId());
        Captcha captcha = getCaptcha();

        addCaptchaToService(req, captcha);

        byte[] imageByteArray = putImageToByteArray(captcha.getBufferedImage());
        setResponseHeaders(resp, imageByteArray);
        sendData(resp, imageByteArray);
    }

    private void addCaptchaToService(HttpServletRequest req, Captcha captcha) {
        LocalTime createTime = LocalTime.now();
        String captchaValue = captcha.getCaptchaText();
        DataContainer<String,LocalTime> captchaContainer = new DataContainer<>(captchaValue, createTime);

        CaptchaService captchaService = getCaptchaService(req);
        captchaService.putCaptcha(req, captchaContainer);
    }

    private void sendData(HttpServletResponse resp, byte[] imageByteArray) throws IOException {
        OutputStream outputStream = resp.getOutputStream();
        outputStream.write(imageByteArray, 0, imageByteArray.length);
        outputStream.flush();
    }

    private void setResponseHeaders(HttpServletResponse resp, byte[] bytes) {
        resp.setHeader("Content-Type", "image/jpeg");
        resp.setHeader("Content-Length", bytes.length + "");
    }

    private byte[] putImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);

        return os.toByteArray();
    }

    private Captcha getCaptcha() {
        Captcha captcha = new Captcha();
        captcha.setBackground(Color.PINK);
        captcha.paintLines(5);
        captcha.paintText();
        captcha.paintLines(10);
        captcha.paintPoints(100);

        return captcha;
    }
}
