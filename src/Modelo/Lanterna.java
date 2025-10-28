package Modelo;

import Auxiliar.Desenho;
import java.io.IOException;
import java.io.Serializable;

public class Lanterna extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean bAcesa; 
    private String nomeImagemAcesa;
    private String nomeImagemApagada;

    public Lanterna(String nomeImagemApagada, String nomeImagemAcesa) {
        super(nomeImagemApagada); 
        this.nomeImagemApagada = nomeImagemApagada;
        this.nomeImagemAcesa = nomeImagemAcesa;
        this.bAcesa = false;
        this.bMortal = false;
        this.bTransponivel = false; 
    }

    public boolean isAcesa() {
        return bAcesa;
    }

    public void acender() {
        if (!bAcesa) {
            this.bAcesa = true;
            try {
                this.iImage = Desenho.carregarImagem(nomeImagemAcesa);
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem da lanterna acesa: " + e.getMessage());
            }
            System.out.println("Lanterna acendeu!");
        }
    }

    public void apagar() {
        if (bAcesa) {
            this.bAcesa = false;
            try {
                this.iImage = Desenho.carregarImagem(nomeImagemApagada);
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem da lanterna apagada: " + e.getMessage());
            }
            System.out.println("Lanterna apagou!");
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}
