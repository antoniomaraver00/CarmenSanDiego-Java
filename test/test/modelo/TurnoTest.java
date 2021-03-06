package test.modelo;

import junit.framework.Assert;
import modelo.Turno;
import modelo.excepciones.LadronNoPlaneoEscapeException;
import modelo.ladron.Ladron;
import modelo.ladron.ObjetoRobado;
import modelo.ladron.Valor;
import modelo.ladron.perfil.Cabello;
import modelo.ladron.perfil.Hobby;
import modelo.ladron.perfil.Perfil;
import modelo.ladron.perfil.Senia;
import modelo.ladron.perfil.Sexo;
import modelo.ladron.perfil.Vehiculo;
import modelo.mapa.Ciudad;
import modelo.mapa.Coordenada;
import modelo.mapa.Locacion;
import modelo.mapa.Local;
import modelo.mapa.Mapa;
import modelo.mapa.TipoLocal;
import modelo.policia.Velocidad;

import org.junit.Test;
import org.junit.Before;

public class TurnoTest {

	private int HORAS_TOTAL_JUEGO = 154;
	private String MENSAJE_NO_ESTA_LADRON = "Lo siento, nunca he visto a esa persona.";
	private Turno turno;
	private Velocidad velocidad;

	@Before
	public void setUp() {

		// Creo Coordenadas
		Coordenada ubicacion0 = new Coordenada(0, 0);
		Coordenada ubicacion1 = new Coordenada(5, 5);
		Coordenada ubicacion2 = new Coordenada(10, 10);
		Coordenada ubicacion3 = new Coordenada(15, 15);
		Coordenada ubicacion4 = new Coordenada(20, 20);

		// Creo Ciudades
		Ciudad ciudad0 = new Ciudad(ubicacion0);
		Ciudad ciudad1 = new Ciudad(ubicacion1);
		Ciudad ciudad2 = new Ciudad(ubicacion2);
		Ciudad ciudad3 = new Ciudad(ubicacion3);
		Ciudad ciudad4 = new Ciudad(ubicacion4);

		// Creo Locales
		Local local0 = new Local(TipoLocal.BIBLIOTECA);
		local0.setPista("Queria escalar el Monte Everest.");
		Local local1 = new Local(TipoLocal.BANCO);
		local1.setPista("Queria cambiar su dinero a yenes.");
		Local local2 = new Local(TipoLocal.BOLSA);
		ciudad0.agregarLocal(local0);
		ciudad1.agregarLocal(local1);
		ciudad2.agregarLocal(local2);

		// Creo Mapa
		Mapa mapa = new Mapa();
		mapa.agregarCiudad(ciudad0);
		mapa.agregarCiudad(ciudad1);
		mapa.agregarCiudad(ciudad2);
		mapa.agregarCiudad(ciudad3);
		mapa.agregarCiudad(ciudad4);

		// Creo ObjetoRobado
		ObjetoRobado objeto = new ObjetoRobado(Valor.COMUN);

		// Creo Ladron
		Perfil perfil = new Perfil("Carmen SanDiego", Sexo.FEMENINO, Cabello.ROJO, Senia.ANILLO, Vehiculo.LIMUSINA,
				Hobby.ALPINISMO);
		Ladron ladron = new Ladron(perfil);
		ladron.planearNuevoDestino(ciudad0);
		ladron.planearNuevoDestino(ciudad1);
		ladron.planearNuevoDestino(ciudad2);
		ladron.planearNuevoDestino(ciudad3);
		try { ladron.robarObjeto(objeto);
		} catch (LadronNoPlaneoEscapeException e) {
			Assert.fail();
		}

		// Creo Locacion
		Locacion locacionInicial = new Locacion(mapa, ciudad0, ladron);
		locacionInicial.agregarDestino(ciudad1);
		locacionInicial.agregarDestino(ciudad2);
		locacionInicial.agregarDestino(ciudad3);
		locacionInicial.agregarDestino(ciudad4);

		// Creo Turno
		Turno turno = new Turno(locacionInicial);
		this.turno = turno;

		// Creo Velocidad
		Velocidad velocidad = new Velocidad(700);
		this.velocidad = velocidad;

	}

	@Test
	public void deberianActualizarseBienLasHoras() {

		Assert.assertEquals(this.HORAS_TOTAL_JUEGO, this.turno.getHorasRestantes());
		this.turno.actualizar(3);

		Assert.assertEquals(this.HORAS_TOTAL_JUEGO - 3, this.turno.getHorasRestantes());
	}

	@Test
	public void deberiaSaberseSiSeAcaboElTiempo() {
		Assert.assertEquals(true, this.turno.quedaTiempo());
		this.turno.actualizar(this.HORAS_TOTAL_JUEGO);

		Assert.assertEquals(0, this.turno.getHorasRestantes());
		Assert.assertEquals(false, this.turno.quedaTiempo());
	}

	@Test
	public void viajarDeberiaCambiarCiudadActual() {

		Ciudad destino = this.turno.getDestinos().get(0);
		try {
			this.turno.viajar(destino, this.velocidad);
		} catch (LadronNoPlaneoEscapeException e) {
			Assert.fail();
		}

		Assert.assertEquals(destino, this.turno.ciudadActual());
	}

	@Test
	public void interrogarDeberiaConsumir1HoraSiNuncaFueVisitado() {

		turno.setProbabilidadAtaqueNro(0,0);
		turno.setProbabilidadAtaqueNro(0,1);

		Local local0 = this.turno.getLocales().get(0);
		this.turno.interrogar(local0);
		Assert.assertEquals((this.HORAS_TOTAL_JUEGO - 1), this.turno.getHorasRestantes());
	}

	@Test
	public void interrogarDeberiaConsumir2HorasSiFueVisitado2Veces() {

		turno.setProbabilidadAtaqueNro(0,0);
		turno.setProbabilidadAtaqueNro(0,1);

		Local local0 = this.turno.getLocales().get(0);
		this.turno.interrogar(local0);
		this.turno.interrogar(local0);
		Assert.assertEquals((this.HORAS_TOTAL_JUEGO - 3), this.turno.getHorasRestantes());
	}

	@Test
	public void interrogarEnCiudadQuePasoLadronDeberiaDevolverRespuestaCorrecta() {

		// Ladron viaja de la ciudad0 a la ciudad1 al iniciarse el juego.
		Local local0 = this.turno.getLocales().get(0);
		Assert.assertEquals("Queria escalar el Monte Everest.", this.turno.interrogar(local0));

		// Viajo a pais por donde paso ladron
		Ciudad destinoConLadron = this.turno.getDestinos().get(0);

		try {
			this.turno.viajar(destinoConLadron, this.velocidad);
		} catch (LadronNoPlaneoEscapeException e) {
			Assert.fail();
		}

		Local local1 = this.turno.getLocales().get(0);
		Assert.assertEquals("Queria cambiar su dinero a yenes.", this.turno.interrogar(local1));
	}

	@Test
	public void interrogarEnCiudadSinLadronDeberiaDevolverRespuestaPorDefecto() {

		// Viajo a pais sin ladron
		Ciudad destinoSinLadron = this.turno.getDestinos().get(1);
		try {
			this.turno.viajar(destinoSinLadron, this.velocidad);
		} catch (LadronNoPlaneoEscapeException e) {
			Assert.fail();
		}

		Local local2 = this.turno.getLocales().get(0);

		Assert.assertEquals(this.MENSAJE_NO_ESTA_LADRON, this.turno.interrogar(local2));
	}

	@Test
	public void deberiaPerderse1HoraAlSerAcuchilladoPorPrimeraVez() {

		turno.setProbabilidadAtaqueNro(100,0);
		turno.setProbabilidadAtaqueNro(0,1);

		Local local0 = this.turno.getLocales().get(0);
		this.turno.interrogar(local0);
		Assert.assertEquals((this.HORAS_TOTAL_JUEGO - 1 - 1), this.turno.getHorasRestantes());
	}

	@Test
	public void deberianPerderse2HorasAlSerAcuchilladoPorSegundaVez() {

		turno.setProbabilidadAtaqueNro(100,0);
		turno.setProbabilidadAtaqueNro(0,1);

		Local local0 = this.turno.getLocales().get(0);
		this.turno.interrogar(local0); // 1 por interrogar + 1 por ser acuchillado.
		this.turno.interrogar(local0); // 2 por interrogar + 2 por ser acuchillado.
		Assert.assertEquals((this.HORAS_TOTAL_JUEGO - 3 - 3), this.turno.getHorasRestantes());
	}

	@Test
	public void deberianPerderse4HorasAlRecibirDisparo() {

		turno.setProbabilidadAtaqueNro(0, 0);
		turno.setProbabilidadAtaqueNro(100, 1);

		Local local0 = this.turno.getLocales().get(0);
		this.turno.interrogar(local0);
		Assert.assertEquals((this.HORAS_TOTAL_JUEGO - 1 - 4), this.turno.getHorasRestantes());
	}
}