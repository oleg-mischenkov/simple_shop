package com.mishchenkov.entity;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Entities {

    private static final Logger LOGGER = Logger.getLogger(Entities.class);

    private Entities() {}

    public static <T extends Serializable> T copy(T entity) {
        T result;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(entity);

            ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            result = (T) objectInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warn(e);
            throw new IllegalStateException(e);
        }
        return result;
    }

}
