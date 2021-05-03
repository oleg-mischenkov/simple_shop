package com.mishchenkov.service;

import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SessionCaptchaService implements CaptchaService {

    private final Logger logger = Logger.getLogger(this.getClass());

    private final Deque<HttpSession> deque;

    public SessionCaptchaService(long timeoutSeconds) {
        deque = new ConcurrentLinkedDeque<>();

        Thread thread = new Thread(new Demon(deque, timeoutSeconds));
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void putCaptcha(HttpServletRequest request, DataContainer<String, LocalTime> captchaContainer) {
        HttpSession session = request.getSession();
        session.setAttribute(AppConstant.SESSION_ITEM_CAPTCHA, captchaContainer);

        deque.removeAll(Arrays.asList(session));
        deque.add(session);
    }

    @Override
    public DataContainer<String, LocalTime> deriveCaptcha(HttpServletRequest request) {
        HttpSession session = request.getSession();
        DataContainer<String, LocalTime> result = (DataContainer<String, LocalTime>) session.getAttribute(AppConstant.SESSION_ITEM_CAPTCHA);

        if (result == null) return new DataContainer<>("", null);
        return result;
    }

    private class Demon implements Runnable {

        private final Deque<HttpSession> deque;
        private final long timeoutSeconds;

        private Demon(Deque<HttpSession> deque, long timeoutSeconds) {
            this.deque = deque;
            this.timeoutSeconds = timeoutSeconds;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {

                if (deque.isEmpty()) {
                    sleep(timeoutSeconds);
                } else {
                    HttpSession session = deque.getFirst();
                    LocalTime createTime = getCaptchaCreateTime(session);
                    LocalTime currentTime = LocalTime.now();

                    if (currentTime.isAfter( createTime.plusSeconds(timeoutSeconds) )) {
                        session.removeAttribute(AppConstant.SESSION_ITEM_CAPTCHA);
                        deque.removeFirst();
                    } else {
                        int pause = createTime.plusSeconds(timeoutSeconds).toSecondOfDay() - currentTime.toSecondOfDay();
                        sleep(pause);
                    }
                }
            }
        }

        private void sleep(long seconds) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                logger.warn(e);
                Thread.currentThread().interrupt();
            }
        }

        private LocalTime getCaptchaCreateTime(HttpSession session) {
            return ((DataContainer<String, LocalTime>) session.getAttribute(AppConstant.SESSION_ITEM_CAPTCHA)).getValue();
        }
    }

}
