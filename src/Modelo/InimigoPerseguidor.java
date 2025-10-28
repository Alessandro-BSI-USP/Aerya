package Modelo;

import Auxiliar.Desenho;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon; 

public class InimigoPerseguidor extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int pontosValor;

    private int contadorMovimento;
    private int VELOCIDADE_INIMIGO_PERSEGUIDOR = 19;
    private final int raioPercepcao = 6;
    
    private transient ImageIcon upImage, downImage, leftImage, rightImage;
    private String baseName;
    private Hero.Direcao direcaoAtual;
    

    public InimigoPerseguidor(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.bTransponivel = false;
        this.contadorMovimento = 0;
        this.pontosValor = 50; 

        this.baseName = sNomeImagePNG.replace(".png", "");
        try {
            recarregarImagensDirecionais();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagens direcionais para Inimigo Perseguidor: " + ex.getMessage());
        }
        this.direcaoAtual = Hero.Direcao.BAIXO; 
        this.iImage = this.downImage;
    }

    void recarregarImagensDirecionais() throws IOException {
        this.upImage = Desenho.carregarImagem(baseName + "_up.png");
        this.downImage = Desenho.carregarImagem(baseName + "_down.png");
        this.leftImage = Desenho.carregarImagem(baseName + "_left.png");
        this.rightImage = Desenho.carregarImagem(baseName + "_right.png");
    }
    
    public int getRaioPercepcao() {
        return raioPercepcao;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); 
        recarregarImagensDirecionais(); 
        setDirecaoAtual(this.direcaoAtual);
    }

    public void incrementaContador() {
        contadorMovimento++;
    }
    

    public boolean estaNaHoraDeMover() {
        if (contadorMovimento >= VELOCIDADE_INIMIGO_PERSEGUIDOR) {
            contadorMovimento = 0;
            return true;
        }
        return false;
    }

    public void setDirecaoAtual(Hero.Direcao novaDirecao) {
        this.direcaoAtual = novaDirecao;
        switch (novaDirecao) {
            case CIMA: this.iImage = upImage; break;
            case BAIXO: this.iImage = downImage; break;
            case ESQUERDA: this.iImage = leftImage; break;
            case DIREITA: this.iImage = rightImage; break;
        }
    }
    
    public int getPontosValor() {
        return pontosValor;
    }

    public Hero.Direcao getDirecaoAtual() {
        return direcaoAtual;
    }

    @Override
        public void setsNomeImagePNG(String sNomeImagePNG) {
        super.setsNomeImagePNG(sNomeImagePNG); 
        this.baseName = sNomeImagePNG.replace(".png", ""); 
        try {
            recarregarImagensDirecionais(); 
        } catch (IOException e) {
            System.err.println("Erro ao mudar imagem base do inimigo para: " + sNomeImagePNG + " - " + e.getMessage());
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}