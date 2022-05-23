package gusto.fatec.cavaleiro.controller;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.text.MessageFormat.format;

public class ThreadCavaleiro extends Thread {
    private static final int PORTA_DA_SALVACAO = new Random().nextInt(4) + 1;
    private static final Logger LOGGER = Logger.getLogger(ThreadCavaleiro.class.getName());
    private int distancia;
    private final int idCavaleiro;
    private final Semaphore semaforo;
    private int portaEscolhida;
    private boolean tocha;
    private boolean pedra;

    public ThreadCavaleiro(int idCavaleiro, Semaphore semaforo) {
        this.idCavaleiro = idCavaleiro;
        this.semaforo = semaforo;

        this.tocha = true;
        this.pedra = true;

        Random random = new Random();
        this.distancia = random.nextInt(3) + 2;
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

                pegouTocha(distanciaPecorrida);
                pegouPedra(distanciaPecorrida);

                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                semaforo.release();
            }

            String logCavaleiroAndou = MessageFormat.format(
                    "Cavaleiro: #{0} andou {1}m",
                    idCavaleiro,
                    distanciaPecorrida
            );
            LOGGER.info(logCavaleiroAndou);

            distanciaPecorrida++;
        }
    }

    private void pegouTocha(int distanciaPecorrida) {
        if (distanciaPecorrida >= 500 && tocha) {
            tocha = false;

            String logCavaleiroPegouTocha = format("Cavaleiro: #{0} pegou a tocha", idCavaleiro);
            LOGGER.info(logCavaleiroPegouTocha);

            distancia += 2;
        }
    }

    private void pegouPedra(int distanciaPecorrida) {
        if (distanciaPecorrida >= 1500 && pedra) {
            pedra = false;

            String logCavaleiroPegouPedra = format("Cavaleiro: #{0} pegou a pedra bilhante", idCavaleiro);
            LOGGER.info(logCavaleiroPegouPedra);

            distancia += 2;
        }
    }

    private void escolhePorta() {
        try {
            semaforo.acquire();

            portaEscolhida++;

            String logCavaleiroEscolheuPorta = format("Cavaleiro: #{0} escolheu a porta #{1}", idCavaleiro, portaEscolhida);
            LOGGER.info(logCavaleiroEscolheuPorta);

            String logCavaleiro = portaEscolhida == PORTA_DA_SALVACAO
                    ? MessageFormat.format("Cavaleiro: #{0} se salvou", idCavaleiro)
                    : MessageFormat.format("Cavaleiro: #{0} morreu", idCavaleiro);
            LOGGER.info(logCavaleiro);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

}
