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
import androidx.appcompat.app.AppCompatActivity;
import com.example.motorinferencia.motor.BaseConocimiento;
import com.example.motorinferencia.motor.Condicion;
import com.example.motorinferencia.motor.Regla;
import com.example.motorinferencia.motor.hecho;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grafo extends AppCompatActivity {

    private LinearLayout contenedorGrafo;
    private Button btnGenerar;
    private Set<hecho> hechosFinales;

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

        if (getIntent().hasExtra("hechos")) {
            hechosFinales = (HashSet<hecho>) getIntent().getSerializableExtra("hechos");
        }

        BaseConocimiento base = BaseConocimiento.getInstancia();

        btnGenerar.setOnClickListener(v -> {
            contenedorGrafo.removeAllViews();
            generarVistaReglas(base.getReglas());
            if (hechosFinales != null && !hechosFinales.isEmpty()) {
                generarVistaHechos();
            }
            Toast.makeText(this, "Grafo generado", Toast.LENGTH_SHORT).show();
        });

        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> startActivity(new Intent(this, Resultados.class)));
        menu4.setOnClickListener(v -> recreate());
    }

    private void generarVistaReglas(List<Regla> reglas) {
        if (reglas.isEmpty()) {
            Toast.makeText(this, "No hay reglas para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Regla r : reglas) {
            LinearLayout bloqueRegla = new LinearLayout(this);
            bloqueRegla.setOrientation(LinearLayout.VERTICAL);
            bloqueRegla.setPadding(20, 20, 20, 20);
            bloqueRegla.setBackgroundColor(Color.parseColor("#E3F2FD"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 20);
            bloqueRegla.setLayoutParams(params);

            TextView nodoRegla = new TextView(this);
            nodoRegla.setText("Regla: " + r.getNombre());
            nodoRegla.setTextColor(Color.parseColor("#0D47A1"));
            nodoRegla.setTextSize(18);
            bloqueRegla.addView(nodoRegla);

            for (Condicion c : r.getCondiciones()) {
                TextView nodoCond = new TextView(this);
                nodoCond.setText("   └── " + c.toString());
                nodoCond.setTextColor(Color.parseColor("#1B5E20"));
                bloqueRegla.addView(nodoCond);
            }

            TextView resultado = new TextView(this);
            resultado.setText("      ↳ Resultado: " + r.getResultado());
            resultado.setTextColor(Color.parseColor("#E65100"));
            bloqueRegla.addView(resultado);

            contenedorGrafo.addView(bloqueRegla);
        }
    }

    private void generarVistaHechos() {
        TextView tituloHechos = new TextView(this);
        tituloHechos.setText("Hechos Finales (Conclusiones)");
        tituloHechos.setTextSize(20);
        tituloHechos.setTextColor(Color.BLACK);
        tituloHechos.setPadding(10, 30, 10, 10);
        contenedorGrafo.addView(tituloHechos);

        for (hecho h : hechosFinales) {
            LinearLayout bloqueHecho = new LinearLayout(this);
            bloqueHecho.setOrientation(LinearLayout.VERTICAL);
            bloqueHecho.setPadding(20, 10, 20, 10);
            bloqueHecho.setBackgroundColor(Color.parseColor("#FFF9C4")); // Amarillo claro
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 5);
            bloqueHecho.setLayoutParams(params);

            TextView nodoHecho = new TextView(this);
            nodoHecho.setText("▶ " + h.toString());
            nodoHecho.setTextColor(Color.parseColor("#F57F17")); // Ámbar
            nodoHecho.setTextSize(16);
            bloqueHecho.addView(nodoHecho);
            contenedorGrafo.addView(bloqueHecho);
        }
    }
}
