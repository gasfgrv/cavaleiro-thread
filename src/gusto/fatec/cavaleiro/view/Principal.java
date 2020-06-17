package gusto.fatec.cavaleiro.view;

import java.util.concurrent.Semaphore;

import gusto.fatec.cavaleiro.controller.ThreadCavaleiro;

public class Principal {
	public static void main(String[] args) {
		int permissoes = 1;
		
		Semaphore semaforo = new Semaphore(permissoes);

		for (int idCav = 0; idCav < 4; idCav++) {
			Thread threadCavaleiro = new ThreadCavaleiro(idCav + 1, semaforo);
			
			threadCavaleiro.start();
		}
	}

}
