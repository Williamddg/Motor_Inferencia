package com.example.motorinferencia;

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
import com.example.motorinferencia.motor.Condicion;
import com.example.motorinferencia.motor.Regla;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nueva_condicion extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_condicion);

        // === Referencias a UI ===
        Spinner spinnerRegla = findViewById(R.id.spinner_regla);
        Spinner spinnerYO = findViewById(R.id.spinner_y_o);
        Spinner spinnerIgualdad = findViewById(R.id.spinner_igualdad);
        EditText txtAtributo = findViewById(R.id.txt_atributo);
        EditText txtValor = findViewById(R.id.txt_valor);
        Button btnAgregarCondicion = findViewById(R.id.btn_agregar_condicion);

        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        // === Base de conocimiento compartida ===
        BaseConocimiento base = BaseConocimiento.getInstancia();

        // === Llenar Spinner de reglas ===
        List<String> nombresReglas = new ArrayList<>();
        for (Regla r : base.getReglas()) {
            nombresReglas.add(r.getNombre());
        }

        if (nombresReglas.isEmpty()) {
            nombresReglas.add("No hay reglas disponibles");
        }

        // 🔹 Adaptador personalizado con texto oscuro
        ArrayAdapter<String> adaptadorReglas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombresReglas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setBackgroundColor(getResources().getColor(R.color.grisclaro));
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
        adaptadorReglas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegla.setAdapter(adaptadorReglas);

        // === Spinner de operadores ===
        List<String> operadores = Arrays.asList("=", ">", "<", ">=", "<=", "!=");
        ArrayAdapter<String> adaptadorOps = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operadores) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setBackgroundColor(getResources().getColor(R.color.grisclaro));
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
        adaptadorOps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIgualdad.setAdapter(adaptadorOps);

        // === Spinner de Y / O ===
        List<String> logicos = Arrays.asList("Y", "O");
        ArrayAdapter<String> adaptadorLogico = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, logicos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setBackgroundColor(getResources().getColor(R.color.grisclaro));
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
        adaptadorLogico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYO.setAdapter(adaptadorLogico);

        // === Acción botón agregar condición ===
        btnAgregarCondicion.setOnClickListener(v -> {
            if (base.getReglas().isEmpty()) {
                Toast.makeText(this, "Primero crea una regla en la pantalla principal", Toast.LENGTH_LONG).show();
                return;
            }

            String nombreRegla = spinnerRegla.getSelectedItem().toString();
            String atributo = txtAtributo.getText().toString().trim();
            String operador = spinnerIgualdad.getSelectedItem().toString();
            String valor = txtValor.getText().toString().trim();

            if (atributo.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Regla reglaSeleccionada = base.buscarRegla(nombreRegla);
            if (reglaSeleccionada == null) {
                Toast.makeText(this, "Regla no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            Condicion nuevaCond = new Condicion(atributo, operador, valor);
            reglaSeleccionada.agregarCondicion(nuevaCond);

            Toast.makeText(this, "Condición agregada a la regla " + nombreRegla, Toast.LENGTH_SHORT).show();

            txtAtributo.setText("");
            txtValor.setText("");
        });

        // === Menú inferior ===
        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> recreate());
        menu3.setOnClickListener(v -> startActivity(new Intent(this, Resultados.class)));
        menu4.setOnClickListener(v -> startActivity(new Intent(this, Grafo.class)));
    }
}
