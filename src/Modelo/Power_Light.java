package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;

public class Power_Light extends Personagem implements Serializable{
    private Hero.Direcao direcao;
        
    public Power_Light(String sNomeImagePNG, Hero.Direcao direcao) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.direcao = direcao;
    }


    @Override
    public void autoDesenho() {
        super.autoDesenho();
        boolean moveu = false;

        switch (direcao) {
            case CIMA:
                moveu = this.moveUp();
                break;
            case BAIXO:
                moveu = this.moveDown();
                break;
            case ESQUERDA:
                moveu = this.moveLeft();
                break;
            case DIREITA:
                moveu = this.moveRight();
                break;
        }

        if (!moveu || !validaPosicao()) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        }
    }
    
}
