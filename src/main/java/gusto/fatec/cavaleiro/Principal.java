package gusto.fatec.cavaleiro;

import java.util.concurrent.Semaphore;

import gusto.fatec.cavaleiro.controller.ThreadCavaleiro;

public class Principal {
	public static void main(String[] args) {
		Semaphore semaforo = new Semaphore(1);

		for (int idCav = 0; idCav < 4; idCav++) {
			Thread threadCavaleiro = new ThreadCavaleiro(idCav + 1, semaforo);
			threadCavaleiro.start();
		}
	}

}
