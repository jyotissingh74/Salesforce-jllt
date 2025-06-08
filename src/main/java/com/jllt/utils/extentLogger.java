package com.jllt.utils;

import com.aventstack.extentreports.Status;

public class extentLogger {
    public static void info(String message) {
        extentReportListener.getTest().log(Status.INFO, message);
    }

    public static void pass(String message) {
        extentReportListener.getTest().log(Status.PASS, message);
    }

    public static void fail(String message) {
        extentReportListener.getTest().log(Status.FAIL, message);
    }

    public static void warning(String message) {
        extentReportListener.getTest().log(Status.WARNING, message);
    }

    public static void skip(String message) {
        extentReportListener.getTest().log(Status.SKIP, message);
    }
}
