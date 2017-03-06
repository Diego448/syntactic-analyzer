package analizadorSintactico;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class AnalizadorSintactico {

	public enum TablaDeSimbolos {IDENTIFICADOR, CONSTANTE, TIPO_DE_DATO, OPERADOR_ARITMETICO, OPERADOR_DE_COMPARACION, OPERADOR_DE_ASIGNACION, METODO, EXPRESION, DELIMITADOR};
	static LeerDocumento leerDocumento = new LeerDocumento();
	static JFrame ventanaPrincipal = new JFrame("Analizador léxico");
	static JTextField rutaDeArchivo = new JTextField();
	static JTextArea resultadoAnalisisLexico = new JTextArea();
	static JTextArea resultadoAnalisisSintactico = new JTextArea();
	static JButton abrirArchivo = new JButton("Abrir");
	static JButton analizar = new JButton("Analizar");
	static JLabel etiquetaRutaDeArchivo = new JLabel("Nombre de archivo: ");
	public static final int almacenamientoDeInstruccion = 10;
	static StringBuilder errores = new StringBuilder();
	
	public static void inicializarVentana() {
		ventanaPrincipal.setSize(300, 550);
		ventanaPrincipal.setLayout(new FlowLayout());	
		rutaDeArchivo.setPreferredSize(new Dimension(200, 30));
		abrirArchivo.setPreferredSize(new Dimension(70, 30));
		analizar.addActionListener(leerDocumento);
		analizar.setPreferredSize(new Dimension(100, 40));
		resultadoAnalisisLexico.setPreferredSize(new Dimension(280, 200));
		resultadoAnalisisSintactico.setPreferredSize(new Dimension(280, 200));
		ventanaPrincipal.setResizable(false);
		ventanaPrincipal.getContentPane().add(etiquetaRutaDeArchivo);
		ventanaPrincipal.getContentPane().add(rutaDeArchivo);		
		//ventanaPrincipal.getContentPane().add(abrirArchivo);
		ventanaPrincipal.getContentPane().add(analizar);
		ventanaPrincipal.getContentPane().add(resultadoAnalisisLexico);
		ventanaPrincipal.getContentPane().add(resultadoAnalisisSintactico);
		ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventanaPrincipal.setVisible(true);
	}
	
	public static String leerArchivo(String archivo) throws IOException {
		BufferedReader entrada = null;
		String instruccion = null;
		StringBuilder codigo = new StringBuilder();
		try {
			entrada = new BufferedReader(new FileReader(archivo));
			while((instruccion = entrada.readLine()) != null) {
				codigo.append(instruccion + " ");
			}
		} catch(IOException ex){
			System.out.println("Archivo no encontrado");
			resultadoAnalisisLexico.setText("Archivo no encontrado");
		} finally {
			entrada.close();
		}
		return codigo.toString();
	}
	
	public static String analizar(String instruccion) {
		String simbolos[] = new String[7];
		StringBuilder resultado = new StringBuilder();
		
		TablaDeSimbolos instrucciones[][] = new TablaDeSimbolos[almacenamientoDeInstruccion][almacenamientoDeInstruccion];
		int indiceInstruccion = 0;
		int indiceSimbolo = 0;
		simbolos = instruccion.split("\\s");
		for(int i = 0; i < simbolos.length; i++) {
			//System.out.println(simbolos[i]);
			switch(simbolos[i]) {
				case "entero": case "fraccionario":
					System.out.println("Tipo de dato" + ", " + simbolos[i]);
					resultado.append("Tipo de dato" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.TIPO_DE_DATO;
					indiceSimbolo++;
					break;
				case "+": case "-": case "*": case "/":
					System.out.println("Operador aritmético" + ", " + simbolos[i]);
					resultado.append("Operador aritmético" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.OPERADOR_ARITMETICO;
					indiceSimbolo++;
					break;
				case "<": case "=<": case "==": case "=>": case ">":
					System.out.println("Operador de comparación" + ", " + simbolos[i]);
					resultado.append("Operador de comparación" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.OPERADOR_DE_COMPARACION;
					indiceSimbolo++;
					break;
				case "=":
					System.out.println("Operador de asiganación" + ", " + simbolos[i]);
					resultado.append("Operador de asiganación" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.OPERADOR_DE_ASIGNACION;
					indiceSimbolo++;
					break;
				case "imprimir":
					System.out.println("Método" + ", " + simbolos[i]);
					resultado.append("Método" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.METODO;
					indiceSimbolo++;
					break;
					
				case "#":
					System.out.println("Delimitador" + ", " + simbolos[i]);
					resultado.append("Delimitador" + ", " + simbolos[i] + "\n");
					instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.DELIMITADOR;
					indiceSimbolo = 0;
					indiceInstruccion++;
					break;
				default:
					if(simbolos[i].matches("\\d+")) {
						System.out.println("Constante" + ", " + simbolos[i]);
						resultado.append("Constante" + ", " + simbolos[i] + "\n");
						instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.CONSTANTE;
						indiceSimbolo++;
					} else if(simbolos[i].matches("\\D\\w*")){
						System.out.println("Identificador" + ", " + simbolos[i]);
						resultado.append("Identificador" + ", " + simbolos[i] + "\n");
						instrucciones[indiceInstruccion][indiceSimbolo] = TablaDeSimbolos.IDENTIFICADOR;
						indiceSimbolo++;
					} else {
						System.out.println("Desconocido" + ", " + simbolos[i]);
						resultado.append("Desconocido" + ", " + simbolos[i] + "\n");
					}
					break;
			}
		}
		analisisSintactico(instrucciones);
		return resultado.toString();
	}
	
	public static void analisisSintactico(TablaDeSimbolos instrucciones[][]) {
		
		
		for(int instruccionActual = 0; instruccionActual < almacenamientoDeInstruccion; instruccionActual++) {
			if(instrucciones[instruccionActual][0] == null)
				break;
			int simboloActual = 0;
			switch(instrucciones[instruccionActual][simboloActual]) {
				case TIPO_DE_DATO:
					simboloActual++;
					if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.IDENTIFICADOR) {
						simboloActual++;
						if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.OPERADOR_DE_ASIGNACION) {
							simboloActual++;
							if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.CONSTANTE) {
								simboloActual++;
								if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
									System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
									errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
								}
							} else if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.EXPRESION) {
								simboloActual++;
								if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
									System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
									errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
								}
							} else {
								System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
								errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
							}
						} else if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR){
							System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
							errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
						}
					} else {
						System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
						errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
					}
				break;
				
				case IDENTIFICADOR:
					simboloActual++;
					if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.OPERADOR_DE_ASIGNACION) {
						simboloActual++;
						if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.CONSTANTE) {
							simboloActual++;
							if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
								System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
								errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
							}
						} else if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.IDENTIFICADOR) {
							simboloActual++;
							if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
								System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
								errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
							}
						} else {
							System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
							errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
						}
					} else {
						System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
						errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
					}
				break;
				
				case METODO:
					simboloActual++;
					if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.CONSTANTE) {
						simboloActual++;
						if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
							System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
							errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
						}
					} else if(instrucciones[instruccionActual][simboloActual] == TablaDeSimbolos.IDENTIFICADOR) {
						simboloActual++;
						if(instrucciones[instruccionActual][simboloActual] != TablaDeSimbolos.DELIMITADOR) {
							System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
							errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
						}
					} else {
						System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
						errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
					}
				break;
				
				default:
					System.out.println("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido");
					errores.append("Error de sintaxis en linea " + instruccionActual + ". Simbolo " + simboloActual + " no válido \n");
				break;	
			}
			/*for(int simboloActual = 0; simboloActual < almacenamientoDeInstruccion; simboloActual++) {
				if(instrucciones[instruccionActual][simboloActual] == null)
					break;
				System.out.println(instrucciones[instruccionActual][simboloActual].toString());
				
			}*/
		}
	}
	
	public static void main(String[] args) throws IOException {
		inicializarVentana();
		//System.out.println(leerArchivo("Prueba.txt"));
		//analizar(leerArchivo("Prueba.txt"));
	}

}
