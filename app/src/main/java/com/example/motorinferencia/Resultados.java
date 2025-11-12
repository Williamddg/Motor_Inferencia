package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorinferencia.motor.BaseConocimiento;
import com.example.motorinferencia.motor.MotorInferencia;

import java.util.ArrayList;
import java.util.List;

public class Resultados extends AppCompatActivity {

    private ListView listaProcedimiento;
    private Button btnInferir;
    private ArrayAdapter<String> adaptador;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultados);

        // === Referencias UI ===
        listaProcedimiento = findViewById(R.id.lista_procedimiento);
        btnInferir = findViewById(R.id.btn_inferir);

        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        // === Base de conocimiento global ===
        BaseConocimiento base = BaseConocimiento.getInstancia();

        // === Adaptador con estilo oscuro ===
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setBackgroundColor(getResources().getColor(R.color.blanco));
                textView.setPadding(16, 12, 16, 12);
                return textView;
            }
        };
        listaProcedimiento.setAdapter(adaptador);

        // === Botón "Inferir" ===
        btnInferir.setOnClickListener(v -> {
            adaptador.clear();

            if (base.getReglas().isEmpty()) {
                Toast.makeText(this, "No hay reglas registradas.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear motor de inferencia
            MotorInferencia motor = new MotorInferencia(base);


            // Ejecutar inferencia
            List<String> resultados = motor.ejecutar();

            if (resultados.isEmpty()) {
                adaptador.add("No se generaron conclusiones.");
            } else {
                adaptador.addAll(resultados);
            }

            Toast.makeText(this, "Inferencia completada", Toast.LENGTH_SHORT).show();
        });

        // === Menú inferior ===
        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> recreate());
        menu4.setOnClickListener(v -> startActivity(new Intent(this, Grafo.class)));
    }
}
