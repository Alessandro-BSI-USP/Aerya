package auxiliar;

import java.io.Serializable;

public final class Posicao implements Serializable { 
    private static final long serialVersionUID = 1L;
    
    private int linha;
    private int coluna;
    private int linhaAnterior;
    private int colunaAnterior;

    public Posicao(int linha, int coluna) {
        this.linhaAnterior = -1; 
        this.colunaAnterior = -1;
        this.setPosicao(linha, coluna);
    }

    public boolean setPosicao(int linha, int coluna) {
        this.linhaAnterior = this.linha;
        this.colunaAnterior = this.coluna;

        if (linha < 0 || linha >= Auxiliar.Consts.MUNDO_ALTURA)
            return false;
        this.linha = linha;

        if (coluna < 0 || coluna >= Auxiliar.Consts.MUNDO_LARGURA)
            return false;
        this.coluna = coluna;

        return true;
    }

    public int getLinha() {
        return linha;
    }

    public boolean volta() {
        if (linhaAnterior == -1 || colunaAnterior == -1) {
            return false; 
        }
        this.linha = linhaAnterior;
        this.coluna = colunaAnterior;
        this.linhaAnterior = -1; 
        this.colunaAnterior = -1;
        return true;
    }

    public int getColuna() {
        return coluna;
    }

    public boolean igual(Posicao posicao) {
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public boolean estaNaMesmaPosicao(Posicao outraPosicao) {
        return this.igual(outraPosicao);
    }
    
    public boolean copia(Posicao posicao) {
        return this.setPosicao(posicao.getLinha(), posicao.getColuna());
    }

    public boolean moveUp() {
        return this.setPosicao(this.getLinha() - 1, this.getColuna());
    }

    public boolean moveDown() {
        return this.setPosicao(this.getLinha() + 1, this.getColuna());
    }

    public boolean moveRight() {
        return this.setPosicao(this.getLinha(), this.getColuna() + 1);
    }

    public boolean moveLeft() {
        return this.setPosicao(this.getLinha(), this.getColuna() - 1);
    }
    
}