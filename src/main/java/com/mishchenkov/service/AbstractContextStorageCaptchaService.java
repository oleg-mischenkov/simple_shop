package com.mishchenkov.service;

import com.mishchenkov.entity.DataContainer;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractContextStorageCaptchaService implements CaptchaService {

    protected final Map<String, DataContainer<String, LocalTime>> storage;

    protected AbstractContextStorageCaptchaService(long timeoutSeconds) {
        storage = Collections.synchronizedMap(new LinkedHashMap<>());

        Thread thread = new Thread(new Demon(storage, timeoutSeconds));
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void putCaptcha(HttpServletRequest request, DataContainer<String, LocalTime> captchaContainer) {
        String fieldId = getFieldId(request);

        storage.remove(fieldId);
        storage.put(fieldId, captchaContainer);
    }

    @Override
    public DataContainer<String, LocalTime> deriveCaptcha(HttpServletRequest request) {
        String fieldId = getFieldId(request);

        Optional<DataContainer<String, LocalTime>> optionalContainer = Optional.ofNullable(storage.get(fieldId));
        return optionalContainer.orElse(new DataContainer<>("", null));
    }

    protected abstract String getFieldId(HttpServletRequest request);

    private class Demon implements Runnable {

        private final Logger logger = Logger.getLogger(Demon.class);

        private final Map<String, DataContainer<String, LocalTime>> storage;
        private final long timeoutSeconds;

        private Demon(Map<String, DataContainer<String, LocalTime>> storage, long timeoutSeconds) {
            this.storage = storage;
            this.timeoutSeconds = timeoutSeconds;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                logger.trace("main loop");
                if (storage.isEmpty()) {
                    logger.trace(String.format("sleep %d s.", timeoutSeconds));
                    sleep(timeoutSeconds);
                } else {
                    String valueId = storage.keySet().stream().findFirst().orElse("");
                    String value = obtainCaptchaValue(valueId);
                    LocalTime createTime = storage.get(valueId).getValue();
                    LocalTime currentTime = LocalTime.now();

                    if (currentTime.isAfter(createTime.plusSeconds(timeoutSeconds))) {
                        storage.remove(valueId);
                        logger.trace(String.format("%s | captcha = %S [removed]", valueId, value));
                    } else {
                        int pause = createTime.plusSeconds(timeoutSeconds).toSecondOfDay() - currentTime.toSecondOfDay();
                        sleep(pause);
                    }
                }
            }
        }

        private String obtainCaptchaValue(String valueId) {
            return Optional.ofNullable(storage.get(valueId)).orElse(new DataContainer<>("", null)).getName();
        }

        private void sleep(long seconds) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
                if (!Thread.interrupted()) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
