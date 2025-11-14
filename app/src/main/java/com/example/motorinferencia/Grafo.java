package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.motorinferencia.motor.MotorInferencia.InferenceStep;
import com.example.motorinferencia.motor.hecho;
import java.util.List;
import java.util.stream.Collectors;

public class Grafo extends AppCompatActivity {

    private LinearLayout contenedorGrafo;
    private Button btnGenerar;
    private List<InferenceStep> inferenceGraph;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafo);

        btnGenerar = findViewById(R.id.btn_generar);
        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        ScrollView scroll = findViewById(R.id.scrollContenido);
        contenedorGrafo = (LinearLayout) scroll.getChildAt(0);

        if (getIntent().hasExtra("inferenceGraph")) {
            inferenceGraph = (List<InferenceStep>) getIntent().getSerializableExtra("inferenceGraph");
        }

        btnGenerar.setOnClickListener(v -> {
            if (inferenceGraph != null && !inferenceGraph.isEmpty()) {
                generarVistaGrafo();
                Toast.makeText(this, "Grafo de inferencia generado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay un grafo de inferencia para mostrar", Toast.LENGTH_SHORT).show();
            }
        });

        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> startActivity(new Intent(this, Resultados.class)));
        menu4.setOnClickListener(v -> recreate());
    }

    private void generarVistaGrafo() {
        contenedorGrafo.removeAllViews();
        if (inferenceGraph == null || inferenceGraph.isEmpty()) return;

        int maxIteracion = inferenceGraph.stream().mapToInt(s -> s.iteracion).max().orElse(0);

        for (int i = 1; i <= maxIteracion; i++) {
            final int currentIteracion = i;
            List<InferenceStep> stepsInIteracion = inferenceGraph.stream()
                    .filter(s -> s.iteracion == currentIteracion)
                    .collect(Collectors.toList());

            if (stepsInIteracion.isEmpty()) continue;

            LinearLayout bloqueIteracion = new LinearLayout(this);
            bloqueIteracion.setOrientation(LinearLayout.VERTICAL);
            bloqueIteracion.setPadding(10, 10, 10, 10);
            LinearLayout.LayoutParams paramsIter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsIter.setMargins(0, 20, 0, 20);
            bloqueIteracion.setLayoutParams(paramsIter);
            bloqueIteracion.setBackgroundColor(Color.parseColor("#ECEFF1")); // Light grey

            TextView tituloIteracion = new TextView(this);
            tituloIteracion.setText("Iteración " + currentIteracion);
            tituloIteracion.setTextSize(22);
            tituloIteracion.setTextColor(Color.BLACK);
            tituloIteracion.setPadding(10, 10, 10, 20);
            bloqueIteracion.addView(tituloIteracion);

            for (InferenceStep step : stepsInIteracion) {
                LinearLayout bloquePaso = new LinearLayout(this);
                bloquePaso.setOrientation(LinearLayout.VERTICAL);
                bloquePaso.setGravity(Gravity.CENTER_HORIZONTAL);
                bloquePaso.setPadding(20, 20, 20, 20);
                bloquePaso.setBackgroundColor(Color.parseColor("#FFFFFF")); // White
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                bloquePaso.setLayoutParams(params);

                LinearLayout inputsLayout = new LinearLayout(this);
                inputsLayout.setOrientation(LinearLayout.VERTICAL); // Changed to VERTICAL
                inputsLayout.setGravity(Gravity.CENTER);

                for (hecho h : step.condicionesSatisfechas) {
                    TextView inputNode = new TextView(this);
                    inputNode.setText(h.getAtributo());
                    inputNode.setPadding(30, 20, 30, 20);
                    inputNode.setBackgroundColor(Color.parseColor("#B3E5FC")); // Light blue
                    inputNode.setTextColor(Color.BLACK);
                    inputNode.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams nodeParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    nodeParams.setMargins(10, 10, 10, 10);
                    inputNode.setLayoutParams(nodeParams);
                    inputsLayout.addView(inputNode);
                }
                bloquePaso.addView(inputsLayout);

                TextView reglaConFlecha = new TextView(this);
                reglaConFlecha.setText("↓ " + step.regla.getNombre() + " ↓");
                reglaConFlecha.setTextColor(Color.parseColor("#009688")); // Teal
                reglaConFlecha.setGravity(Gravity.CENTER);
                reglaConFlecha.setTextSize(16);
                reglaConFlecha.setPadding(0, 20, 0, 20);
                bloquePaso.addView(reglaConFlecha);

                TextView outputNode = new TextView(this);
                outputNode.setText(step.resultado.getAtributo());
                outputNode.setPadding(30, 20, 30, 20);
                outputNode.setBackgroundColor(Color.parseColor("#C8E6C9")); // Light green
                outputNode.setTextColor(Color.BLACK);
                outputNode.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams outputNodeParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                outputNodeParams.setMargins(10, 10, 10, 10);
                outputNode.setLayoutParams(outputNodeParams);
                bloquePaso.addView(outputNode);

                bloqueIteracion.addView(bloquePaso);
            }
            contenedorGrafo.addView(bloqueIteracion);
        }
    }
}
