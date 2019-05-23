package com.example.booker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

public class CargarPreferencias {
    Context mContext;

    public CargarPreferencias(Context context){
        this.mContext = context;
    }

    public String loadWelcomeName (){
        System.out.println("EN LOAD WELCOME NAMEEE");
        SharedPreferences sharedPreferences =  mContext.getSharedPreferences("credencial", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user","No encontramos tu nombre");
        System.out.println("OBTENIDO EN LOADWELCOMENAME " + user);
        return user;
    }
}
