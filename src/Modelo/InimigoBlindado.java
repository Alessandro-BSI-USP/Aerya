package Modelo;

import Auxiliar.Desenho; 
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class InimigoBlindado extends InimigoPerseguidor implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int pontosValor;

    private boolean isVulnerable;

    private transient ImageIcon vulnerableUpImage, vulnerableDownImage, vulnerableLeftImage, vulnerableRightImage;
    private String vulnerableBaseName; 

    public InimigoBlindado(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.pontosValor = 100; 
        this.isVulnerable = false; 
        this.vulnerableBaseName = "notDarkKnight";

        try {
            recarregarImagensVulneraveis();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagens de vulnerabilidade para Inimigo Blindado: " + ex.getMessage());
        }
    }

    private void recarregarImagensVulneraveis() throws IOException {
        this.vulnerableUpImage = Desenho.carregarImagem(vulnerableBaseName + "_up.png");
        this.vulnerableDownImage = Desenho.carregarImagem(vulnerableBaseName + "_down.png");
        this.vulnerableLeftImage = Desenho.carregarImagem(vulnerableBaseName + "_left.png");
        this.vulnerableRightImage = Desenho.carregarImagem(vulnerableBaseName + "_right.png");
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); 
        super.recarregarImagensDirecionais(); 
        recarregarImagensVulneraveis(); 

        setVulnerable(this.isVulnerable); 
    }

    public void setVulnerable(boolean vulnerable) {
        this.isVulnerable = vulnerable;
        if (vulnerable) {
            System.out.println("Inimigo Blindado se tornou VULNERAVEL!");
            setDirecaoAtual(this.getDirecaoAtual());
        } else {
            setDirecaoAtual(this.getDirecaoAtual()); 
        }
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }
    
    @Override
    public int getPontosValor() {
        return pontosValor;
    }

    @Override
    public void setDirecaoAtual(Hero.Direcao novaDirecao) {
        super.setDirecaoAtual(novaDirecao);

        if (this.isVulnerable) {
            switch (novaDirecao) {
                case CIMA: this.iImage = vulnerableUpImage; break;
                case BAIXO: this.iImage = vulnerableDownImage; break;
                case ESQUERDA: this.iImage = vulnerableLeftImage; break;
                case DIREITA: this.iImage = vulnerableRightImage; break;
            }
        } else {
        }
    }

}
