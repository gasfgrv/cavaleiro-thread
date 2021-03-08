package gusto.fatec.cavaleiro.controller;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.text.MessageFormat.format;

public class ThreadCavaleiro extends Thread {

	private int portaEscolhida;

	private boolean tocha = true;
	private boolean pedra = true;

	private static final int PORTA_DA_SALVACAO = (int) (1 + (random() * (4 - 1)));

	private final int idCavaleiro;

	private final Semaphore semaforo;

	private int distancia = (int) (2 + (random() * (4 - 2)));

	private static final Logger LOGGER = Logger.getLogger(ThreadCavaleiro.class.getName());

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

		int distanciaTotal = 2000;

		while (distanciaPecorrida < distanciaTotal) {
			distanciaPecorrida += distancia;

			try {
				semaforo.acquire();

				if (distanciaPecorrida >= 500 && tocha) {
					pegouTocha();
				}

				if (distanciaPecorrida >= 1500 && pedra) {
					pegouPedra();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				semaforo.release();
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

			String logCavaleiroAndou = format("Cavaleiro: #{0} andou {1}m", idCavaleiro, distanciaPecorrida);
			LOGGER.info(logCavaleiroAndou);
			distanciaPecorrida++;
		}
	}

	private void pegouTocha() {
		tocha = false;
		String logCavaleiroPegouTocha = format("Cavaleiro: #{0} pegou a tocha", idCavaleiro);
		LOGGER.info(logCavaleiroPegouTocha);
		distancia = distancia + 2;
	}

	private void pegouPedra() {
		pedra = false;
		String logCavaleiroPegouPedra = format("Cavaleiro: #{0} pegou a pedra bilhante", idCavaleiro);
		LOGGER.info(logCavaleiroPegouPedra);
		distancia = distancia + 2;
	}

	private void escolhePorta() {
		try {
			semaforo.acquire();
			portaEscolhida++;

			String logCavaleiroEscolheuPorta = format("Cavaleiro: #{0} escolheu a porta #{1}", idCavaleiro, portaEscolhida);
			LOGGER.info(logCavaleiroEscolheuPorta);
			if (portaEscolhida == PORTA_DA_SALVACAO) {
				String logCavaleiroSalvou = format("Cavaleiro: #{0} se salvou", idCavaleiro);
				LOGGER.info(logCavaleiroSalvou);
			} else {
				String logCavaleiroMorreu = format("Cavaleiro: #{0} morreu", idCavaleiro);
				LOGGER.info(logCavaleiroMorreu);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			semaforo.release();
		}
	}

}
