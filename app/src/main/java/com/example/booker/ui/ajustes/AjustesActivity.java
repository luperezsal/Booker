package com.example.booker.ui.ajustes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.booker.R;


import java.util.List;


public class AjustesActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        setupActionBar();
        Fragment fragment = new AjustesFragment();
        System.out.println("Creando ajustes activity");
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new AjustesFragment()).commit();
    }

    public void setupActionBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ajustes);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}