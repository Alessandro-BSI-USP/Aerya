package Modelo;

import java.io.Serializable;

public class Boss extends InimigoPerseguidor implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int pontosValor;

    private int vidaAtual; 
    private final int VIDA_MAXIMA = 15; 

    public Boss(String sNomeImagePNG) {
        super(sNomeImagePNG); 
        this.vidaAtual = VIDA_MAXIMA;
        this.bMortal = true; 
        this.bTransponivel = false;
        this.pontosValor = 500; 
        
    }

    public void receberDano() {
        if (vidaAtual > 0) {
            vidaAtual--;
            System.out.println("Boss atingido! Vida restante: " + vidaAtual);
        }
    }
    
    @Override
    public int getPontosValor() {
        return pontosValor;
    }

    public boolean estaDerrotado() {
        return vidaAtual <= 0;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public void setVidaAtual(int vidaAtual) {
        this.vidaAtual = vidaAtual;
    }

}
