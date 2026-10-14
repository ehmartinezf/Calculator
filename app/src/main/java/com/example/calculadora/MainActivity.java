package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_CARACTERES = 12;

    private TextView pantalla;

    private String numeroActual = "0";
    private double primerNumero = 0;
    private String operador = "";
    private boolean nuevoNumero = true;

    private double memoria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        pantalla = findViewById(R.id.pantalla);

        View.OnClickListener listenerNumeros = v -> {
            Button boton = (Button) v;
            pulsarNumero(boton.getText().toString());
        };

        View.OnClickListener listenerResto = v -> {
            int id = v.getId();

            if (id == R.id.botonPunto) {
                pulsarPunto();
            } else if (id == R.id.botonSumar) {
                pulsarOperador("+");
            } else if (id == R.id.botonRestar) {
                pulsarOperador("-");
            } else if (id == R.id.botonMultiplicar) {
                pulsarOperador("*");
            } else if (id == R.id.botonDividir) {
                pulsarOperador("/");
            } else if (id == R.id.botonIgual) {
                pulsarIgual();
            } else if (id == R.id.botonBorrar) {
                pulsarClear();
            } else if (id == R.id.botonMC) {
                pulsarMC();
            } else if (id == R.id.botonMR) {
                pulsarMR();
            } else if (id == R.id.botonMMas) {
                pulsarMMas();
            } else if (id == R.id.botonMMenos) {
                pulsarMMenos();
            }
        };

        findViewById(R.id.boton0).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton1).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton2).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton3).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton4).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton5).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton6).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton7).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton8).setOnClickListener(listenerNumeros);
        findViewById(R.id.boton9).setOnClickListener(listenerNumeros);

        findViewById(R.id.botonPunto).setOnClickListener(listenerResto);
        findViewById(R.id.botonSumar).setOnClickListener(listenerResto);
        findViewById(R.id.botonRestar).setOnClickListener(listenerResto);
        findViewById(R.id.botonMultiplicar).setOnClickListener(listenerResto);
        findViewById(R.id.botonDividir).setOnClickListener(listenerResto);
        findViewById(R.id.botonIgual).setOnClickListener(listenerResto);
        findViewById(R.id.botonBorrar).setOnClickListener(listenerResto);
        findViewById(R.id.botonMC).setOnClickListener(listenerResto);
        findViewById(R.id.botonMR).setOnClickListener(listenerResto);
        findViewById(R.id.botonMMas).setOnClickListener(listenerResto);
        findViewById(R.id.botonMMenos).setOnClickListener(listenerResto);

        actualizarPantalla();
    }

    private void pulsarNumero(String cifra) {
        if (nuevoNumero || numeroActual.equals("0")) {
            numeroActual = cifra;
            nuevoNumero = false;
        } else if (numeroActual.length() < MAX_CARACTERES) {
            numeroActual += cifra;
        }
        actualizarPantalla();
    }

    private void pulsarPunto() {
        if (nuevoNumero) {
            numeroActual = "0.";
            nuevoNumero = false;
        } else if (!numeroActual.contains(".") && numeroActual.length() < MAX_CARACTERES) {
            numeroActual += ".";
        }
        actualizarPantalla();
    }

    private void pulsarOperador(String nuevoOperador) {
        try {
            if (!operador.isEmpty() && !nuevoNumero) {
                resolverOperacion();
            } else {
                primerNumero = obtenerNumeroActual();
            }
        } catch (ArithmeticException e) {
            mostrarError();
            return;
        }

        operador = nuevoOperador;
        nuevoNumero = true;
        actualizarPantalla();
    }

    private void pulsarIgual() {
        if (operador.isEmpty()) {
            return;
        }

        try {
            resolverOperacion();
        } catch (ArithmeticException e) {
            mostrarError();
            return;
        }

        operador = "";
        nuevoNumero = true;
        actualizarPantalla();
    }

    private void pulsarClear() {
        numeroActual = "0";
        primerNumero = 0;
        operador = "";
        nuevoNumero = true;
        actualizarPantalla();
    }

    private void pulsarMC() {
        memoria = 0;
    }

    private void pulsarMR() {
        numeroActual = formatearNumero(memoria);
        nuevoNumero = false;
        actualizarPantalla();
    }

    private void pulsarMMas() {
        memoria += obtenerNumeroActual();
    }

    private void pulsarMMenos() {
        memoria -= obtenerNumeroActual();
    }

    private void resolverOperacion() {
        double resultado = calcular(primerNumero, obtenerNumeroActual(), operador);
        primerNumero = resultado;
        numeroActual = formatearNumero(resultado);
    }

    private double calcular(double numero1, double numero2, String operador) {
        switch (operador) {
            case "+":
                return numero1 + numero2;
            case "-":
                return numero1 - numero2;
            case "*":
                return numero1 * numero2;
            case "/":
                if (numero2 == 0) {
                    throw new ArithmeticException("No se puede dividir entre cero");
                }
                return numero1 / numero2;
            default:
                throw new IllegalArgumentException("Operador desconocido: " + operador);
        }
    }

    private double obtenerNumeroActual() {
        try {
            return Double.parseDouble(numeroActual);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void actualizarPantalla() {
        pantalla.setText(numeroActual);
    }

    private void mostrarError() {
        numeroActual = "Error";
        primerNumero = 0;
        operador = "";
        nuevoNumero = true;
        actualizarPantalla();
    }

    private String formatearNumero(double numero) {
        if (numero == 0) {
            return "0";
        }
        DecimalFormat formato = new DecimalFormat("0.##########", new DecimalFormatSymbols(Locale.US));
        return formato.format(numero);
    }
}
