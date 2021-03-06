package test.modelo;

import modelo.ladron.Itinerario;
import modelo.mapa.Ciudad;
import modelo.mapa.Coordenada;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class ItinerarioTest {

	private Itinerario itinerario;
	private Ciudad ciudad0;
	private Ciudad ciudad1;

	@Before
	public void setUp() {
		this.itinerario = new Itinerario();

		Coordenada ubicacion0 = new Coordenada(0, 0);
		this.ciudad0 = new Ciudad(ubicacion0);
		Coordenada ubicacion1 = new Coordenada(10, 10);
		this.ciudad1 = new Ciudad(ubicacion1);
	}

	@Test
	public void testItinerarioSeCreaOK() {

		Assert.assertNotNull(this.itinerario);
		Assert.assertEquals(false, this.itinerario.tieneDestinos());
	}

	@Test
	public void testItinerarioAgregaUnaCiudad() {

		this.itinerario.agregarAlRecorrido(ciudad0);
		Assert.assertEquals(true, this.itinerario.tieneDestinos());
		Assert.assertEquals(ciudad0, this.itinerario.ciudadNro(0));
		this.itinerario.agregarAlRecorrido(ciudad1);
		Assert.assertEquals(true, this.itinerario.tieneDestinos());
		Assert.assertEquals(ciudad1, this.itinerario.ciudadNro(1));
	}
}