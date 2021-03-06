package modelo.policia;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import modelo.LectorXML;
import modelo.ladron.Ladron;

import org.xml.sax.SAXException;

public class OrdenDeArresto {
	private List<Ladron> baseLadrones;
	private boolean ordenYaEmitida;
	private Ladron ladron;

	public OrdenDeArresto() {
		this.ordenYaEmitida = false;
		this.ladron = null;
		this.baseLadrones = new ArrayList<Ladron>();
	}

	public boolean arrestoEsValido(Ladron ladron) {
		if ( ladron == null ) return false;
		return ( baseLadrones.contains(ladron) && !this.ordenYaEmitida );
	}

	public void agregarLadronABaseDeDatos(Ladron ladron) {
		if (!arrestoEsValido(ladron)) {
			baseLadrones.add(ladron);
		}
	}

	public void emitirOrden(Ladron ladron) {
		if (arrestoEsValido(ladron)) {
			this.ladron = ladron;
			this.ordenYaEmitida = true;
		}
	}

	public List<Ladron> getBaseDeLadrones() {
		return this.baseLadrones;
	}
	
	public Ladron verLadron() {
		return this.ladron;
	}

	public void CargarBaseDeDatos() throws ParserConfigurationException, SAXException, IOException {
		this.baseLadrones = LectorXML.cargarLadrones();
	}
}