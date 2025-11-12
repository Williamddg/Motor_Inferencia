package com.example.motorinferencia;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorinferencia.motor.BaseConocimiento;
import com.example.motorinferencia.motor.Regla;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // === Referencias UI ===
        EditText txtAtributo = findViewById(R.id.txt_atributo);
        EditText txtValor = findViewById(R.id.txt_valor);
        Spinner spinnerIgualdad = findViewById(R.id.spinner_igualdad);
        Button btnAgregar = findViewById(R.id.btn_agregar_regla);

        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        // === Llenar Spinner de operadores ===
        List<String> operadores = Arrays.asList("=", ">", "<", ">=", "<=", "!=");

        // 🔹 Adaptador personalizado para texto oscuro
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operadores) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro)); // Texto seleccionado
                textView.setBackgroundColor(getResources().getColor(R.color.grisclaro)); // Fondo visible
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setBackgroundColor(getResources().getColor(R.color.blanco));
                return textView;
            }
        };
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIgualdad.setAdapter(adaptador);

        // === Base de conocimiento global ===
        BaseConocimiento base = BaseConocimiento.getInstancia();

        // === Agregar nueva regla ===
        btnAgregar.setOnClickListener(v -> {
            String atributo = txtAtributo.getText().toString().trim();
            String operador = spinnerIgualdad.getSelectedItem().toString();
            String valor = txtValor.getText().toString().trim();

            if (atributo.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear y guardar nueva regla
            Regla regla = new Regla(atributo);
            regla.setResultado(atributo + " " + operador + " " + valor);
            base.agregarRegla(regla);

            Toast.makeText(this, "Regla agregada con éxito", Toast.LENGTH_SHORT).show();
            txtAtributo.setText("");
            txtValor.setText("");
        });

        // === Menú inferior ===
        menu1.setOnClickListener(v -> recreate()); // recarga actual
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> startActivity(new Intent(this, Resultados.class)));
        menu4.setOnClickListener(v -> startActivity(new Intent(this, Grafo.class)));
    }
}
