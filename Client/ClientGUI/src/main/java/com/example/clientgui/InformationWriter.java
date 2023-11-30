package com.example.clientgui;

public interface InformationWriter {
    void ErrorLog(String header, String value);
    void WarningLog(String header, String value);
    void InformationLog(String header, String value);
}
