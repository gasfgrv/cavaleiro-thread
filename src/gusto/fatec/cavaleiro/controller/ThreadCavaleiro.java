package gusto.fatec.cavaleiro.controller;

import java.util.concurrent.Semaphore;

public class ThreadCavaleiro extends Thread {

	private final int distanciaTotal = 2000;

	private static int portaEscolhida;

	private static boolean tocha = true;
	private static boolean pedra = true;

	private static int portaDaSalvacao = (int) (1 + (Math.random() * (4 - 1)));

	private int idCavaleiro;

	private Semaphore semaforo;

	private int distancia = (int) (2 + (Math.random() * (4 - 2)));

	public ThreadCavaleiro(int idCavaleiro, Semaphore semaforo) {
		this.idCavaleiro = idCavaleiro;
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		cavaleiroAndando();
		escolhePorta();
	}

	private void cavaleiroAndando() {
		int distanciaPecorrida = 0;

		while (distanciaPecorrida < distanciaTotal) {
			distanciaPecorrida += distancia;

			try {
				semaforo.acquire();

				if (distanciaPecorrida >= 500 && tocha == true) {
					pegouTocha();
				}

				if (distanciaPecorrida >= 1500 && pedra == true) {
					pegouPedra();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} finally {
				semaforo.release();
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Cavaleiro: #" + idCavaleiro + " andou " + distanciaPecorrida + "m");
			distanciaPecorrida++;
		}
	}

	private void pegouTocha() {
		tocha = false;
		System.out.println("Cavaleiro: #" + idCavaleiro + " pegou a tocha");
		distancia = distancia + 2;
	}

	private void pegouPedra() {
		pedra = false;
		System.out.println("Cavaleiro: #" + idCavaleiro + " pegou a pedra bilhante");
		distancia = distancia + 2;
	}

	private void escolhePorta() {
		try {
			semaforo.acquire();
			portaEscolhida++;

			System.out.println("Cavaleiro: #" + idCavaleiro + " escolheu a porta #" + portaEscolhida);
			if (portaEscolhida == portaDaSalvacao) {
				System.out.println("Cavaleiro: #" + idCavaleiro + " se salvou");
			} else {
				System.out.println("Cavaleiro: #" + idCavaleiro + " morreu");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
	}

}
