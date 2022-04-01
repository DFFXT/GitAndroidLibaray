package com.fxffxt.recyclerviewtoptixed;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_GitAndroidLibaray);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater.from(this).setFactory(new LayoutInflater.Factory() {
            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return null;
            }
        });
    }
}