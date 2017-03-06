package analizadorSintactico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LeerDocumento implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String nombreDeArchivo = AnalizadorSintactico.rutaDeArchivo.getText().trim();
		try {
			AnalizadorSintactico.resultadoAnalisisLexico.setText(AnalizadorSintactico.analizar(AnalizadorSintactico.leerArchivo(nombreDeArchivo)));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		AnalizadorSintactico.resultadoAnalisisSintactico.setText(AnalizadorSintactico.errores.toString());
	}

}
