package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorinferencia.motor.BaseConocimiento;
import com.example.motorinferencia.motor.Condicion;
import com.example.motorinferencia.motor.Regla;

import java.util.List;

public class Grafo extends AppCompatActivity {

    private LinearLayout contenedorGrafo;
    private Button btnGenerar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grafo);

        // === Referencias UI ===
        btnGenerar = findViewById(R.id.btn_generar);
        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        // ✅ Obtén el LinearLayout dentro del ScrollView correctamente
        ScrollView scroll = findViewById(R.id.scrollContenido);
        contenedorGrafo = (LinearLayout) scroll.getChildAt(0);

        if (contenedorGrafo == null) {
            contenedorGrafo = new LinearLayout(this);
            contenedorGrafo.setOrientation(LinearLayout.VERTICAL);
        }

        BaseConocimiento base = BaseConocimiento.getInstancia();

        // === Acción del botón "Generar" ===
        btnGenerar.setOnClickListener(v -> {
            contenedorGrafo.removeAllViews();

            List<Regla> reglas = base.getReglas();
            if (reglas.isEmpty()) {
                Toast.makeText(this, "No hay reglas para mostrar en el grafo", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Regla r : reglas) {
                // 🔹 Contenedor visual de cada regla
                LinearLayout bloqueRegla = new LinearLayout(this);
                bloqueRegla.setOrientation(LinearLayout.VERTICAL);
                bloqueRegla.setPadding(20, 20, 20, 20);
                bloqueRegla.setBackgroundColor(Color.parseColor("#E3F2FD")); // azul muy claro
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 20);
                bloqueRegla.setLayoutParams(params);

                // 🟦 Nodo principal (Regla)
                TextView nodoRegla = new TextView(this);
                nodoRegla.setText("Regla: " + r.getNombre());
                nodoRegla.setTextColor(Color.parseColor("#0D47A1")); // azul oscuro
                nodoRegla.setTextSize(18);
                nodoRegla.setPadding(10, 10, 10, 10);
                bloqueRegla.addView(nodoRegla);

                // 🟩 Condiciones
                if (r.getCondiciones().isEmpty()) {
                    TextView sinCond = new TextView(this);
                    sinCond.setText("   └── (Sin condiciones)");
                    sinCond.setTextColor(Color.parseColor("#555555")); // gris medio
                    bloqueRegla.addView(sinCond);
                } else {
                    for (Condicion c : r.getCondiciones()) {
                        TextView nodoCond = new TextView(this);
                        nodoCond.setText("   └── " + c.toString());
                        nodoCond.setTextColor(Color.parseColor("#1B5E20")); // verde oscuro
                        bloqueRegla.addView(nodoCond);
                    }
                }

                // 🟧 Resultado final
                TextView resultado = new TextView(this);
                resultado.setText("      ↳ Resultado: " + r.getResultado());
                resultado.setTextColor(Color.parseColor("#E65100")); // naranja fuerte
                resultado.setPadding(0, 10, 0, 10);
                bloqueRegla.addView(resultado);

                contenedorGrafo.addView(bloqueRegla);
            }

            Toast.makeText(this, "Grafo generado correctamente", Toast.LENGTH_SHORT).show();
        });

        // === Menú inferior ===
        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> startActivity(new Intent(this, Resultados.class)));
        menu4.setOnClickListener(v -> recreate());
    }
}
