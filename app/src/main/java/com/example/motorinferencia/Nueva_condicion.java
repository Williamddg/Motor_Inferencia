package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nueva_condicion extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_condicion);


        // spinner_regla traemos reglas y elejimos
        // spinner_y_o traemos si y u o
        // edit text txt_atributo tomamos atributo - variable
        // spinner_igualdad traemos equivalencia ( = < > )
        // edit txt txt_valor tomamos valor
        // button btn_agregar_condicion agrega condicion a regla elejida y muestra un toast
        // textview menu1 lanza actvity mainactivity
        // textview menu2 refresca activity
        // textview menu3 lanza acitvty resultados
        // textview menu4 lanza acitvty grafo
    }
}