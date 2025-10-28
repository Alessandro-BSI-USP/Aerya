package Modelo;

import javax.swing.ImageIcon;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import Auxiliar.Desenho; 
import Auxiliar.Consts; 

public class Tile implements Serializable { 
    private static final long serialVersionUID = 1L; 

    private transient ImageIcon imagem; 
    private boolean transponivel;
    private String nomeImagemPNG;

    public Tile(String nomeImagem, boolean transponivel) throws IOException {
        this.nomeImagemPNG = nomeImagem; 
        this.transponivel = transponivel;
        carregarImagemInterna(); 
    }

    private void carregarImagemInterna() throws IOException {
        URL imgURL = getClass().getResource(Consts.PATH + nomeImagemPNG);
        if (imgURL == null) {
            imgURL = getClass().getResource(nomeImagemPNG);
            if (imgURL == null) {
                throw new IOException("Imagem não encontrada: " + Consts.PATH + nomeImagemPNG);
            }
        }
        this.imagem = new ImageIcon(imgURL);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        carregarImagemInterna(); 
    }

    public boolean isTransponivel() {
        return transponivel;
    }

    public void setTransponivel(boolean transponivel) {
        this.transponivel = transponivel;
    }

    public String getNomeImagePNG() {
        return nomeImagemPNG;
    }

    public void setarImage(String novoNomeImagemPNG) throws IOException {
        this.nomeImagemPNG = novoNomeImagemPNG;
        carregarImagemInterna();
    }
    
    public void desenhar(int linha, int coluna) {
        if (this.imagem != null) {
            Desenho.desenhar(this.imagem, coluna, linha); 
        } else {
            System.err.println("Erro: Imagem do Tile '" + nomeImagemPNG + "' não carregada para desenho.");
        }
    }
}