package com.example.motorinferencia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.motorinferencia.motor.BaseConocimiento;
import com.example.motorinferencia.motor.Condicion;
import com.example.motorinferencia.motor.MotorInferencia;
import com.example.motorinferencia.motor.Regla;
import com.example.motorinferencia.motor.hecho;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Resultados extends AppCompatActivity {

    private ListView listaProcedimiento;
    private Button btnInferir;
    private ArrayAdapter<String> adaptador;
    private MotorInferencia motor;
    private List<hecho> hechosIniciales = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        listaProcedimiento = findViewById(R.id.lista_procedimiento);
        btnInferir = findViewById(R.id.btn_inferir);
        TextView menu1 = findViewById(R.id.menu1);
        TextView menu2 = findViewById(R.id.menu2);
        TextView menu3 = findViewById(R.id.menu3);
        TextView menu4 = findViewById(R.id.menu4);

        BaseConocimiento base = BaseConocimiento.getInstancia();
        motor = new MotorInferencia(base);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.negro));
                return textView;
            }
        };
        listaProcedimiento.setAdapter(adaptador);

        btnInferir.setOnClickListener(v -> {
            if (base.getReglas().isEmpty()) {
                Toast.makeText(this, "No hay reglas registradas.", Toast.LENGTH_SHORT).show();
                return;
            }
            iniciarProcesoDeInferencia();
        });

        menu1.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        menu2.setOnClickListener(v -> startActivity(new Intent(this, Nueva_condicion.class)));
        menu3.setOnClickListener(v -> recreate());
        menu4.setOnClickListener(v -> {
            Intent intent = new Intent(this, Grafo.class);
            if (motor != null && motor.getInferenceGraph() != null) {
                intent.putExtra("inferenceGraph", (Serializable) motor.getInferenceGraph());
            }
            startActivity(intent);
        });
    }

    private void iniciarProcesoDeInferencia() {
        adaptador.clear();
        hechosIniciales.clear();
        BaseConocimiento base = BaseConocimiento.getInstancia();
        Set<Condicion> condicionesUnicas = new LinkedHashSet<>();
        for (Regla regla : base.getReglas()) {
            condicionesUnicas.addAll(regla.getCondiciones());
        }

        if (condicionesUnicas.isEmpty()) {
            ejecutarInferencia();
        } else {
            preguntarCerteza(new ArrayList<>(condicionesUnicas), 0);
        }
    }

    private void preguntarCerteza(List<Condicion> condiciones, int index) {
        if (index >= condiciones.size()) {
            ejecutarInferencia();
            return;
        }

        Condicion condicion = condiciones.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nivel de Certeza");
        builder.setMessage("¿Qué tan cierto es que: " + condicion.toString() + "?\n\n" +
                "-1.0: Totalmente Falso\n" +
                "-0.8: Casi Falso\n" +
                "-0.6: Probablemente Falso\n" +
                "-0.4: Algo Falso\n" +
                "-0.2: Ligeramente Falso\n" +
                " 0.0: No se sabe (Neutro)\n" +
                " 0.2: Ligeramente Verdadero\n" +
                " 0.4: Algo Verdadero\n" +
                " 0.6: Probablemente Verdadero\n" +
                " 0.8: Casi Verdadero\n" +
                " 1.0: Totalmente Verdadero");
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final TextView tvCerteza = new TextView(this);
        tvCerteza.setText("Certeza: 0.0");
        tvCerteza.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvCerteza.setTextSize(18);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(20);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double certeza = (progress - 10) / 10.0;
                tvCerteza.setText("Certeza: " + String.format("%.1f", certeza));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        layout.addView(tvCerteza);
        layout.addView(seekBar);
        builder.setView(layout);

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            double certeza = (seekBar.getProgress() - 10) / 10.0;
            hechosIniciales.add(new hecho(condicion.getAtributo(), condicion.getValor(), certeza));
            preguntarCerteza(condiciones, index + 1);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void ejecutarInferencia() {
        motor = new MotorInferencia(BaseConocimiento.getInstancia());
        for (hecho h : hechosIniciales) {
            motor.agregarHecho(h);
        }

        List<String> resultados = motor.ejecutar();
        if (resultados.isEmpty()) {
            adaptador.add("No se generaron conclusiones.");
        } else {
            adaptador.addAll(resultados);
        }
        Toast.makeText(this, "Inferencia completada", Toast.LENGTH_SHORT).show();
    }
}
