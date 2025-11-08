package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Resultados extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultados);



        // button btn_inferir calcula y carga datos a lista
        // listview lista_procedimiento lsita para resultados y procedimiento
        // textview menu1 lanza actvity mainactivity
        // textview menu2 lanza actovity nueva_condicion
        // textview menu3 refescar activity
        // textview menu4 lanza acitvty grafo
    }
}