package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;

public class Power_Water extends Personagem implements Serializable {
    private int vidaFrames;
    private static final int DURACAO = 15;

    public Power_Water(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = false;
        this.vidaFrames = DURACAO; 
    }

    public int getVidaFrames() {
        return vidaFrames;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();

        vidaFrames--; 

        
        if (vidaFrames <= 0) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            
        }
    }
}