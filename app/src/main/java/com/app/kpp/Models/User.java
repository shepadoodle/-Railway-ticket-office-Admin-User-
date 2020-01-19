package com.app.kpp.Models;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.annotation.Target;
import java.util.Objects;

public class User {
    private String name, email, pass, phone;

   public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User user = (User) o;
            return Objects.equals(email, user.email);
        }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int hashCode() {
        return Objects.hash(email);
    }
}

