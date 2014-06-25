package carmen.vista;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JuegoVista extends JFrame{
	private static int MAX_ALTURA = 500;
	private static int MAX_ANCHURA = 400;
	
	PanelFondo fondoMenu = new PanelFondo("fondo.png");
	PanelCarga carga = new PanelCarga();
	PanelNuevaPartida pnlNuevaPartida = new PanelNuevaPartida();
	BotonNuevoJuego btnNuevoJuego = new BotonNuevoJuego();
	BotonSalirJuego btnSalirJuego = new BotonSalirJuego();
	
	public static void main(String[] args){
		new JuegoVista();
	}
	
	public JuegoVista(){
		super("Carmen SanDiego - 75.07 FIUBA");
		setSize(MAX_ALTURA,MAX_ANCHURA);
		setResizable(true);
		setVisible(true);
		getContentPane().add(fondoMenu);
		fondoMenu.add(btnNuevoJuego);
		fondoMenu.add(btnSalirJuego);
		
		btnNuevoJuego.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fondoMenu.esconder();
				getContentPane().add(carga);
				getContentPane().add(pnlNuevaPartida);
				//hacer que la carga se mueva con la lectura de los archivos
				getContentPane().remove(carga);
//				fondoMenu.setVisible(true);
			}
		});
		
	}
}
