package Modelo;

import Auxiliar.Desenho;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon; 

public class InimigoPatrulha extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int pontosValor;

    private int contadorMovimento;
    private int VELOCIDADE_INIMIGO = 5;

    private Hero.Direcao direcaoAtual;
    private String sNomeImagePNG;

    private transient ImageIcon leftImage, rightImage;
    private String baseName; 

    public InimigoPatrulha(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.bTransponivel = false;
        this.direcaoAtual = Hero.Direcao.ESQUERDA; 
        this.sNomeImagePNG = sNomeImagePNG; 
        this.pontosValor = 20; 

        this.baseName = sNomeImagePNG.replace(".png", "");
        try {
            recarregarImagensDirecionais();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagens direcionais para Inimigo Patrulha: " + ex.getMessage());
        }
        this.iImage = this.leftImage; 

        this.contadorMovimento = 0;
    }

    

    private void recarregarImagensDirecionais() throws IOException {
        this.leftImage = Desenho.carregarImagem(baseName + "_left.png");
        this.rightImage = Desenho.carregarImagem(baseName + "_right.png");
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        recarregarImagensDirecionais(); 
        setDirecaoAtual(this.direcaoAtual);
    }

    public void setDirecaoAtual(Hero.Direcao direcao) {
        this.direcaoAtual = direcao;
        switch (direcao) {
            case ESQUERDA:
                this.iImage = leftImage;
                break;
            case DIREITA:
                this.iImage = rightImage;
                break;
            default: 
                this.iImage = leftImage; 
                break;
        }
    }
    
    public int getPontosValor() {
        return pontosValor;
    }

    public Hero.Direcao getDirecaoAtual() {
        return direcaoAtual;
    }

    @Override 
    public String getsNomeImagePNG() {
        return sNomeImagePNG;
    }

    @Override
        public void setsNomeImagePNG(String sNomeImagePNG) {
        this.sNomeImagePNG = sNomeImagePNG;
        this.baseName = sNomeImagePNG.replace(".png", ""); 
        try {
            recarregarImagensDirecionais(); 
        } catch (IOException e) {
            System.err.println("Erro ao mudar imagem base do inimigo para: " + sNomeImagePNG + " - " + e.getMessage());
        }
    }

    public void incrementaContador() {
        contadorMovimento++;
    }

    public boolean estaNaHoraDeMover() {
        if (contadorMovimento >= VELOCIDADE_INIMIGO) {
            contadorMovimento = 0;
            return true;
        }
        return false;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}