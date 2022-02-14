package com.example.pfc.app;

import android.app.Application;
import android.content.Context;

public class contextoApp extends Application {
    private static contextoApp instancia;
    public static contextoApp getInstance() {return instancia;}
    public static Context getContext() {return instancia;}
    @Override public void onCreate() {instancia=this; super.onCreate();}
}
