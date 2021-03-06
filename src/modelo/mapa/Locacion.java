package modelo.mapa;

import java.util.List;
import java.util.ArrayList;

import modelo.excepciones.LadronNoPlaneoEscapeException;
import modelo.ladron.Ladron;

public class Locacion {

	private Mapa mapa;
	private Ladron ladron;
	private Ciudad ciudadActual;
	private List<Ciudad> ciudadesDestino;
	private static int CANTIDAD_DESTINOS = 4;

	public Locacion(Mapa mapa, Ciudad ciudadActual, Ladron ladron) {
		this.mapa = mapa;
		this.ladron = ladron;
		this.ciudadActual = ciudadActual;
		this.ciudadesDestino = new ArrayList<Ciudad>();
	}
	
	public boolean tieneDestino(Ciudad ciudad){
		return (this.ciudadesDestino.contains(ciudad));
	}

	private void generarNuevosDestinos() {		
		this.ciudadesDestino.clear();
		this.agregarDestino(this.ladron.ciudadActual());

		for (int i = 0; i < (CANTIDAD_DESTINOS-1); i++) {
			Ciudad destino = this.mapa.elegirCiudadAlAzar();
			while ( this.ciudadesDestino.contains(destino) ) {
				destino = this.mapa.elegirCiudadAlAzar();
			}
			this.agregarDestino(destino);
		}
	}

	public void viajar(Ciudad destino) throws LadronNoPlaneoEscapeException {

		Ciudad ciudadAnterior = this.ciudadActual;
		this.ciudadActual = destino;

		// Hago escapar al ladron si el policia lo alcanza.
		if (estaLadron()) {
			if ( this.ladron.escapar() ) {
				this.generarNuevosDestinos();
			} else {
				//Si ladron no pudo escapar es porque estoy en su escondite.
				//No puedo llamar a generarNuevosDestinos() pues figuraria dos veces el escondite.
				this.ciudadesDestino.clear();
				this.agregarDestino(ciudadAnterior);
				for ( int i=0; i < (CANTIDAD_DESTINOS-1); i++ ) {
					Ciudad nuevoDestino = this.mapa.elegirCiudadAlAzar();
					while (this.ciudadesDestino.contains(nuevoDestino)) {
						nuevoDestino = this.mapa.elegirCiudadAlAzar();
					}
					this.agregarDestino(nuevoDestino);
				}
			}
		} else {
			this.ciudadesDestino.remove(destino);
			this.agregarDestino(ciudadAnterior);
		}
	}

	public String interrogar(Local local) {
		if (pasoLadronRecientemente() || estaLadron()) {
			return local.responder();
		}
		local.visitar();
		return "Lo siento, nunca he visto a esa persona.";
	}

	public void agregarDestino(Ciudad destino) {
		this.ciudadesDestino.add(destino);
	}

	public Ciudad ciudadActual() {
		return this.ciudadActual;
	}

	public boolean pasoLadronRecientemente() {
		if (this.ladron.ciudadAnterior() == null)
			return false;
		return (this.ladron.ciudadAnterior().equals(this.ciudadActual));
	}

	public boolean estaLadron() {
		return (this.ladron.ciudadActual().equals(this.ciudadActual));
	}

	public List<Local> getLocales() {
		return this.ciudadActual.getLocales();
	}

	public List<Ciudad> getDestinos() {
		return this.ciudadesDestino;
	}

	public Ciudad verDestinoNro(int nro) {
		return ciudadesDestino.get(nro - 1);
	}

	public Ladron getLadron() {
		return this.ladron;
	}

}