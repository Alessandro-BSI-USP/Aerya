package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
public abstract class Personagem implements Serializable {

    private static final long serialVersionUID = 1L; 

    protected transient ImageIcon iImage;
    protected Posicao pPosicao;
    protected boolean bTransponivel; 
    protected boolean bMortal;     
    protected String sNomeImagePNG; 

    public boolean isbMortal() {
        return bMortal;
    }

    protected Personagem(String sNomeImagePNG) {
        this.sNomeImagePNG = sNomeImagePNG; 
        this.bTransponivel = true; 
        this.bMortal = false; 
        this.pPosicao = new Posicao(0, 0); 

        try {
            this.iImage = Desenho.carregarImagem(sNomeImagePNG);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagem para Personagem " + sNomeImagePNG + ": " + ex.getMessage());
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.setColor(java.awt.Color.RED);
            g.fillRect(0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE);
            g.dispose();
            this.iImage = new ImageIcon(bi);
        }
    }

    public Posicao getPosicao() {
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }
    
    public void setsNomeImagePNG(String sNomeImagePNG) {
        this.sNomeImagePNG = sNomeImagePNG;
        try {
            this.iImage = new ImageIcon(new java.io.File(Auxiliar.Consts.PATH + sNomeImagePNG).toURI().toURL());
        } catch (IOException ex) {
            System.out.println("Erro ao recarregar imagem em Personagem.setsNomeImagePNG: " + ex.getMessage());
        }
    }

    public void setbMortal(boolean bMortal) {
        this.bMortal = bMortal;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho(){
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());      
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    public boolean moveUp() {
        return this.pPosicao.moveUp();
    }

    public boolean moveDown() {
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        return this.pPosicao.moveLeft();
    }

    protected boolean validaPosicao() {
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.pPosicao)) {
            this.pPosicao.volta();
            return false;
        }
        return true;
    }

    public void setarImage(String sNomeImagePNG) throws IOException {
        this.sNomeImagePNG = sNomeImagePNG;
        this.iImage = Desenho.carregarImagem(sNomeImagePNG);
    }
    
    public String getsNomeImagePNG() {
        return sNomeImagePNG;
    }
}