package com.app.kpp.Models;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

public class BlackList {
    private String email, reasonForBan;

    public BlackList(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReasonForBan() {
        return reasonForBan;
    }

    public void setReasonForBan(String reasonForBan) {
        this.reasonForBan = reasonForBan;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        BlackList user = (BlackList) o;
        return Objects.equals(email, user.email);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "BlackList{" +
                "email='" + email + '\'' +
                '}';
    }
}

