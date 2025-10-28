package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;

public class LavaResfriada extends Personagem implements Serializable {
    private int vidaFrames; 
    private static final int DURACAO_RESFRIADA = 100; 

    public LavaResfriada(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true; 
        this.bMortal = false;     
        this.vidaFrames = DURACAO_RESFRIADA;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        vidaFrames--;
        if (vidaFrames <= 0) {
            
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            
            Lava lavaOriginal = new Lava("2lava.png"); 
            lavaOriginal.setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
            Desenho.acessoATelaDoJogo().addPersonagem(lavaOriginal);
        }
    }
}