package Modelo;

import Auxiliar.Desenho;
import static Modelo.Hero.Direcao.BAIXO;
import static Modelo.Hero.Direcao.CIMA;
import static Modelo.Hero.Direcao.DIREITA;
import static Modelo.Hero.Direcao.ESQUERDA;
import java.io.Serializable;

public class Power_Fire extends Personagem implements Serializable {
    private Hero.Direcao direcao;
    private int tick;
    private int alcance;
    private final int delay;
    public static boolean fogoAtivo = false; 

    public Power_Fire(String sNomeImagePNG, Hero.Direcao direcao) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.direcao = direcao;
        this.tick = 0;
        this.alcance = 7;
        this.delay = 3;
        fogoAtivo = true; 
    }

    @Override
    public void autoDesenho() { 
       super.autoDesenho();
       tick++;

        boolean moveu = false;

        if (tick >= delay) {
            tick = 0;
            switch (direcao) {
                case CIMA -> moveu = this.moveUp();
                case BAIXO -> moveu = this.moveDown();
                case ESQUERDA -> moveu = this.moveLeft();
                case DIREITA -> moveu = this.moveRight();
            }

            alcance--;

            if (!moveu || !validaPosicao() || alcance <= 0) {
                Power_Fire.fogoAtivo = false;
                Desenho.acessoATelaDoJogo().removePersonagem(this);
            }
        }
    }
    public static void resetarFogoAtivo() {
        Power_Fire.fogoAtivo = false;
    }
}
