package com.app.kpp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.app.kpp.Models.BlackList;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class FireBaseDatabaseHelperTest {
    private static final int TIMEOUT = 15; //Время ожидания ответа от FireBase

    private static CountDownLatch readCompletedSignal;


    @Test
    public void readBlacklistTest() throws InterruptedException {
        readCompletedSignal = new CountDownLatch(1);

        FireBaseDatabaseHelper helperBlack = new FireBaseDatabaseHelper();
        helperBlack.readBlacklist(this::getDataBlackListStatus);

        if (!readCompletedSignal.await(TIMEOUT, TimeUnit.SECONDS))
            throw new RuntimeException("getBlackListData not ready after  "+TIMEOUT+"  sec");
    }

    private void getDataBlackListStatus(List<BlackList> blackLists, List<String> keys) {
        printBlackListData(blackLists);
       // String s = "adqww";

       // assertEquals(blackLists.stream().map(e-> e.getEmail()),s);

        assertTrue(blackLists.stream().map(e -> e.getEmail()).anyMatch(s -> s.equals("ABC")));//adqww@
        //assertTrue(blackLists.stream().map(BlackList::getEmail).anyMatch(s -> s.equals("")));//admin@gmail.com

        readCompletedSignal.countDown();
    }

    private void printBlackListData(List<BlackList> blackLists) {
        System.out.println("================");
        for (BlackList blackList : blackLists) {
            System.out.println(blackList);
        }
        System.out.println("================");
    }
}