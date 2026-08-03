package vista;

import io.CargadorTablero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import modelo.Celda;
import modelo.Estado;
import modelo.EstadoEnfermo;
import modelo.EstadoLatente;
import modelo.EstadoMuerto;
import modelo.EstadoVivo;
import modelo.Tablero;

public class VistaJuego extends JFrame {

    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private JPanel panelTablero;
    private JLabel estadoLabel;
    private JLabel generacionLabel;
    private JTextField filasField;
    private JTextField columnasField;
    private JTextField generacionesField;
    private JTextField delayField;
    private JButton botonPausarReanudar;
    private Timer timer;
    private int generacionActual = 0;
    private int maxGeneraciones = 0;
    private int delayMs = 500;
    private boolean simulacionActiva = false;
    private boolean simulacionPausada = false;

    public VistaJuego() {
        super("Juego de la Vida");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 700);
        setLocationRelativeTo(null);
        crearInterfaz();
    }

    public void iniciar() {
        setVisible(true);
    }

    private void crearInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelConfiguracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelConfiguracion.setBorder(BorderFactory.createTitledBorder("Configuración"));

        filasField = new JTextField(5);
        columnasField = new JTextField(5);
        generacionesField = new JTextField(5);
        delayField = new JTextField(5);

        filasField.setText("10");
        columnasField.setText("10");
        generacionesField.setText("0");
        delayField.setText("500");

        JButton botonArchivo = new JButton("Cargar desde archivo");
        JButton botonAleatorio = new JButton("Generar aleatorio");
        JButton botonIniciar = new JButton("Iniciar simulación");
        JButton botonPaso = new JButton("Siguiente paso");
        botonPausarReanudar = new JButton("Pausar");

        panelConfiguracion.add(new JLabel("Filas:"));
        panelConfiguracion.add(filasField);
        panelConfiguracion.add(new JLabel("Columnas:"));
        panelConfiguracion.add(columnasField);
        panelConfiguracion.add(new JLabel("Generaciones:"));
        panelConfiguracion.add(generacionesField);
        panelConfiguracion.add(new JLabel("Delay (ms):"));
        panelConfiguracion.add(delayField);
        panelConfiguracion.add(botonArchivo);
        panelConfiguracion.add(botonAleatorio);
        panelConfiguracion.add(botonIniciar);
        panelConfiguracion.add(botonPaso);
        panelConfiguracion.add(botonPausarReanudar);

        panelTablero = new JPanel();
        panelTablero.setBackground(Color.WHITE);
        panelTablero.setBorder(BorderFactory.createTitledBorder("Tablero"));

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createTitledBorder("Estado"));
        estadoLabel = new JLabel("Listo para iniciar la simulación.");
        estadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        estadoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        generacionLabel = new JLabel("Generación: 0");
        generacionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelEstado.add(estadoLabel, BorderLayout.CENTER);
        panelEstado.add(generacionLabel, BorderLayout.SOUTH);

        panelPrincipal.add(panelConfiguracion, BorderLayout.NORTH);
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);

        botonArchivo.addActionListener(e -> cargarDesdeArchivo());
        botonAleatorio.addActionListener(e -> configurarManual());
        botonIniciar.addActionListener(e -> iniciarBucle());
        botonPaso.addActionListener(e -> avanzarUnaGeneracion());
        botonPausarReanudar.addActionListener(e -> alternarPausaReanudar());

        getContentPane().add(panelPrincipal);
    }

    private void cargarDesdeArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = chooser.getSelectedFile();
                this.tablero = CargadorTablero.cargarDesdeArchivo(archivo.getAbsolutePath());
                renderizarTablero();
                estadoLabel.setText("Tablero cargado desde archivo: " + archivo.getName());
                generacionActual = 0;
                generacionLabel.setText("Generación: 0");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "No se encontró el archivo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void configurarManual() {
        try {
            int filas = Integer.parseInt(filasField.getText());
            int columnas = Integer.parseInt(columnasField.getText());

            if (filas <= 0 || columnas <= 0) {
                throw new IllegalArgumentException("Las filas y columnas deben ser mayores a 0.");
            }

            this.tablero = new Tablero(filas, columnas);

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Estado inicial;
                    double random = Math.random();
                    if (random < 0.25) {
                        inicial = new EstadoVivo();
                    } else if (random < 0.50) {
                        inicial = new EstadoMuerto();
                    } else if (random < 0.75) {
                        inicial = new EstadoEnfermo();
                    } else {
                        inicial = new EstadoLatente();
                    }
                    Celda cInicial = new Celda(inicial);
                    tablero.setCelda(i, j, cInicial);
                }
            }

            renderizarTablero();
            estadoLabel.setText("Tablero aleatorio generado.");
            generacionActual = 0;
            generacionLabel.setText("Generación: 0");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para filas y columnas.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarBucle() {
        if (tablero == null) {
            JOptionPane.showMessageDialog(this, "Primero debe crear o cargar un tablero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            maxGeneraciones = Integer.parseInt(generacionesField.getText());
            delayMs = Integer.parseInt(delayField.getText());
            if (delayMs < 0) {
                throw new IllegalArgumentException("El delay no puede ser negativo.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para generaciones y delay.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        detenerSimulacion();
        simulacionActiva = true;
        simulacionPausada = false;
        botonPausarReanudar.setText("Pausar");
        generacionActual = 0;
        generacionLabel.setText("Generación: 0");
        estadoLabel.setText("Simulación en ejecución...");
        renderizarTablero();

        timer = new Timer(delayMs, e -> {
            if (!simulacionActiva || simulacionPausada) {
                return;
            }

            if (maxGeneraciones > 0 && generacionActual >= maxGeneraciones) {
                detenerSimulacion();
                estadoLabel.setText("Simulación finalizada: límite de generaciones alcanzado.");
                return;
            }

            boolean huboCambios = tablero.avanzarGeneracion();
            if (!huboCambios) {
                detenerSimulacion();
                botonPausarReanudar.setText("Pausar");
                estadoLabel.setText("Simulación finalizada: el tablero se estabilizó.");
                return;
            }

            generacionActual++;
            generacionLabel.setText("Generación: " + generacionActual);
            renderizarTablero();
        });
        timer.start();
    }

    private void avanzarUnaGeneracion() {
        if (tablero == null) {
            JOptionPane.showMessageDialog(this, "Primero debe crear o cargar un tablero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean huboCambios = tablero.avanzarGeneracion();
        generacionActual++;
        generacionLabel.setText("Generación: " + generacionActual);
        renderizarTablero();

        if (!huboCambios) {
            estadoLabel.setText("Tablero estabilizado en la generación " + generacionActual + ".");
        } else {
            estadoLabel.setText("Se avanzó una generación.");
        }
    }

    private void alternarPausaReanudar() {
        if (!simulacionActiva || timer == null) {
            return;
        }

        if (simulacionPausada) {
            simulacionPausada = false;
            timer.start();
            botonPausarReanudar.setText("Pausar");
            estadoLabel.setText("Simulación reanudada.");
        } else {
            simulacionPausada = true;
            timer.stop();
            botonPausarReanudar.setText("Reanudar");
            estadoLabel.setText("Simulación pausada.");
        }
    }

    private void detenerSimulacion() {
        simulacionActiva = false;
        simulacionPausada = false;
        if (botonPausarReanudar != null) {
            botonPausarReanudar.setText("Pausar");
        }
        if (timer != null) {
            timer.stop();
        }
    }

    private void renderizarTablero() {
        if (tablero == null) {
            return;
        }

        panelTablero.removeAll();
        panelTablero.setLayout(new GridLayout(tablero.getFilas(), tablero.getColumnas(), 1, 1));

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                char representacion = tablero.getCelda(i, j).mostrar();
                JLabel celda = new JLabel(String.valueOf(representacion), SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setBackground(colorPorEstado(representacion));
                celda.setForeground(Color.BLACK);
                celda.setPreferredSize(new Dimension(25, 25));
                celda.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                celda.setFont(new Font("SansSerif", Font.BOLD, 12));
                panelTablero.add(celda);
            }
        }

        panelTablero.revalidate();
        panelTablero.repaint();
    }

    private Color colorPorEstado(char estado) {
        switch (estado) {
            case 'O':
                return new Color(50, 205, 50);
            case 'E':
                return new Color(255, 215, 0);
            case 'X':
                return new Color(255, 140, 0);
            default:
                return Color.BLACK;
        }
    }
}
