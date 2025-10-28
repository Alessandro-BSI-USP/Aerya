package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;

public class Power_Ice extends Personagem implements Serializable {
    private int tempoRestante;
    public static boolean geloAtivo = false;

    public Power_Ice(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG);
        this.setPosicao(linha, coluna);
        this.bMortal = false;
        this.tempoRestante = 11;
        geloAtivo = true;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        tempoRestante--;
        if (tempoRestante <= 0) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            geloAtivo = false;
        }
    }
}
