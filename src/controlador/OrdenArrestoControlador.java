package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controlador.*;
import vista.pantallas.*;
import modelo.Partida;
import modelo.ladron.*;
import modelo.policia.OrdenDeArresto;

public class OrdenArrestoControlador {
	private Partida modeloPartida;
	private JuegoVista vista;
	private PanelOrdenArresto panel;
	
	public OrdenArrestoControlador(Partida modeloPartida, JuegoVista vista) {
		this.modeloPartida = modeloPartida;
		this.vista = vista;
		this.panel = new PanelOrdenArresto();
		
		vista.getContentPane().removeAll();
		vista.add(panel);
		vista.getContentPane().validate();
		
		this.panel.addEmitirOrdenListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emitirOrden();
			}
		});
		
		this.panel.addVolverAPanelPartidaListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volverAPanelPartida();
			}
		});
		
	}
	
	public void emitirOrden() {
		Ladron ladronSeleccionado = null;
		String nombreLadron = this.panel.verLadronSeleccionado();
		for (Ladron ladron : modeloPartida.verOrdenDeArresto().getBaseDeLadrones()) {
			if ( nombreLadron == ladron.verNombre() ) {
				ladronSeleccionado = ladron;
			}
		}
		if ( modeloPartida.verOrdenDeArresto().arrestoEsValido(ladronSeleccionado) ) {
			modeloPartida.emitirOrden(ladronSeleccionado);
			new OrdenOKControlador(modeloPartida, vista);
		} else {
			new OrdenErrorControlador(modeloPartida, vista);
		}
	}
	
	public void volverAPanelPartida() {
		new PartidaControlador(modeloPartida, vista);
	}
	
}