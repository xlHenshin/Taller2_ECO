package com.example.trabajo2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView pregunta, puntaje, contTiempo;
    private EditText respuesta;
    private Button ok, repetir;
    private Pregunta p;
    private int puntuar, tiempo;
    private String arregloPreguntas[];
    private boolean textPressed;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntuar = 0;
        tiempo = 30;
        textPressed = false;

        contTiempo = findViewById(R.id.tiempo);
        pregunta = findViewById(R.id.pregunta);
        respuesta = findViewById(R.id.respuesta);
        ok = findViewById(R.id.ok);
        repetir = findViewById(R.id.repetir);
        puntaje = findViewById(R.id.puntaje);

        repetir.setVisibility(View.GONE);
        nuevaPregunta();

        contTiempo.setText("" + tiempo);
        contadorTiempo();
        puntaje.setText("" + puntuar);

        ok.setOnClickListener(
                v -> {
                    responder();
                }
        );

        repetir.setOnClickListener(

                v -> {
                    reiniciar();
                    nuevaPregunta();
                }
        );

        pregunta.setOnTouchListener(

                (view, event) -> {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            textPressed = true;
                            new Thread(

                                    () -> {

                                        for (int i = 0; i<20;i++){

                                            try {
                                                Thread.sleep(75);
                                                if (textPressed==false){
                                                    return;
                                                }

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        runOnUiThread(

                                                ()->{
                                                    nuevaPregunta();
                                                    respuesta.setText("");
                                                }

                                        );

                                    }



                            ).start();

                            break;

                        case MotionEvent.ACTION_UP:

                            textPressed=false;
                            break;
                    }

                    return true;
                }

        );

    }

    public void responder() {

        String res = respuesta.getText().toString();
        int resInt = Integer.parseInt(res);
        int resCorrecta = p.getRespuesta();

        if (resInt == resCorrecta) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            puntuar = puntuar + 5;
            puntaje.setText("" + puntuar);
            respuesta.setText("");
        } else {
            Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show();
            puntuar = puntuar - 4;
        }
        nuevaPregunta();
    }

    public void nuevaPregunta() {

        p = new Pregunta();
        pregunta.setText(p.getPregunta());
    }

    public void contadorTiempo() {

        new Thread(

                () -> {
                    while (tiempo > 0) {
                        tiempo = tiempo - 1;
                        runOnUiThread(

                                () -> {
                                    contTiempo.setText("" + tiempo);
                                }

                        );

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(

                            () -> {
                                repetir.setVisibility(View.VISIBLE);
                                respuesta.setVisibility(View.GONE);
                                ok.setVisibility(View.GONE);
                            }

                    );

                }

        ).start();
    }

    public void reiniciar() {

        tiempo = 30;
        puntuar = 0;
        puntaje.setText("" + puntuar);
        repetir.setVisibility(View.GONE);
        respuesta.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);
        contTiempo.setText("" + tiempo);
        contadorTiempo();
        respuesta.setText("");
        //nuevaPregunta();

    }

}