package com.mishchenkov.dto;

import com.mishchenkov.entity.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class UserRegistrationFormDTO implements Serializable {

    private static final long serialVersionUID = 7651820254767258687L;

    private String hidden;
    private String name;
    private String secondName;
    private String mail;
    private String firstPsw;
    private String secondPsw;
    private String isSendEmail;
    private String captcha;
    private byte[] file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFirstPsw() {
        return firstPsw;
    }

    public void setFirstPsw(String firstPsw) {
        this.firstPsw = firstPsw;
    }

    public String getSecondPsw() {
        return secondPsw;
    }

    public void setSecondPsw(String secondPsw) {
        this.secondPsw = secondPsw;
    }

    public boolean getIsSendEmail() {
        return "on".equals(isSendEmail);
    }

    public void setIsSendEmail(String isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public User toUser() {
        return User.getBuilder()
                .setName(name)
                .setSecondName(secondName)
                .setEmail(mail)
                .setPassword(firstPsw)
                .build();
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationFormDTO formDto = (UserRegistrationFormDTO) o;
        return hidden.equals(formDto.hidden)
                && name.equals(formDto.name)
                && secondName.equals(formDto.secondName)
                && mail.equals(formDto.mail)
                && firstPsw.equals(formDto.firstPsw)
                && secondPsw.equals(formDto.secondPsw)
                && isSendEmail.equals(formDto.isSendEmail)
                && captcha.equals(formDto.captcha)
                && Arrays.equals(file, formDto.file);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hidden, name, secondName, mail, firstPsw, secondPsw, isSendEmail, captcha);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }

    @Override
    public String toString() {
        return "UserRegistrationFormDto{" +
                "hidden='" + hidden + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", mail='" + mail + '\'' +
                ", firstPsw='" + firstPsw + '\'' +
                ", secondPsw='" + secondPsw + '\'' +
                ", isSendEmail='" + isSendEmail + '\'' +
                ", captcha='" + captcha + '\'' +
                ", file=" + Arrays.toString(file) +
                '}';
    }
}
