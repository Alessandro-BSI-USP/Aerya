package Modelo;

import Auxiliar.Desenho;
import java.io.IOException;
import java.io.Serializable;

public class Porta extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean bAberto; 
    private boolean isPortaFinal;

    private String nomeImagemFechada;
    private String nomeImagemAberta; 

    public Porta(String nomeImagemFechada, String nomeImagemAberta, boolean isPortaFinal) {
        super(nomeImagemFechada); 
        this.nomeImagemFechada = nomeImagemFechada;
        this.nomeImagemAberta = nomeImagemAberta;
        this.isPortaFinal = isPortaFinal;
        this.bTransponivel = false; 
        this.bMortal = false; 
        this.bAberto = false; 
    }

    public boolean isAberto() {
        return bAberto;
    }

    public boolean isPortaFinal() {
        return isPortaFinal;
    }

    public void abrir() {
        if (!bAberto) { 
            this.bTransponivel = true;
            this.bAberto = true;
            try {
                if (nomeImagemAberta != null && !nomeImagemAberta.isEmpty()) {
                    this.iImage = Desenho.carregarImagem(nomeImagemAberta);
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem da porta aberta: " + e.getMessage());
            }
            System.out.println("Porta " + (isPortaFinal ? "FINAL " : "") + "ABRIU!");
        }
    }

    public void fechar() {
        if (bAberto) { 
            this.bTransponivel = false; 
            this.bAberto = false;
            try {
                this.iImage = Desenho.carregarImagem(nomeImagemFechada); 
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem da porta fechada: " + e.getMessage());
            }
            System.out.println("Porta " + (isPortaFinal ? "FINAL " : "") + "FECHOU!");
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            if (bAberto) {
                this.iImage = Desenho.carregarImagem(nomeImagemAberta != null && !nomeImagemAberta.isEmpty() ? nomeImagemAberta : nomeImagemFechada);
            } else {
                this.iImage = Desenho.carregarImagem(nomeImagemFechada);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao recarregar imagem da porta após desserialização: " + ex.getMessage());
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}
