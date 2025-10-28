package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Hero extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient ImageIcon upImage, downImage, leftImage, rightImage;
    private String baseName;
    private Direcao direcaoAtual;

    private int waterPowerStage = 0;
    private int waterPowerCooldown = 0;
    private final int WATER_POWER_INTERVAL = 5;
    private Direcao waterPowerDirecaoInicial;
    private Posicao waterPowerPosInicial;
    
    private int vidaHeroi;
    private final int VIDA_MAXIMA_HEROI = 3; 
    
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.baseName = sNomeImagePNG.replace(".png", "");
        try {
            recarregarTodasImagensDoHeroi();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagens do Herói no construtor: " + ex.getMessage());
        }
        this.iImage = downImage;
        this.direcaoAtual = Direcao.BAIXO;
        this.bTransponivel = true;
        this.bMortal = false;
        
        this.vidaHeroi = VIDA_MAXIMA_HEROI; 
    }
    
    public void recarregarTodasImagensDoHeroi() throws IOException {
        this.upImage = Desenho.carregarImagem(baseName + "_up.png");
        this.downImage = Desenho.carregarImagem(baseName + "_down.png");
        this.leftImage = Desenho.carregarImagem(baseName + "_left.png");
        this.rightImage = Desenho.carregarImagem(baseName + "_right.png");
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); 

        recarregarTodasImagensDoHeroi();
        setDirecaoAtual(this.direcaoAtual); 
    } 



    public void voltaAUltimaPosicao() {
        this.pPosicao.volta();
    }

    @Override
    public boolean setPosicao(int linha, int coluna) {
        return this.pPosicao.setPosicao(linha, coluna);
    }

    @Override
    public boolean validaPosicao() {
        Modelo.Tile t = Desenho.acessoATelaDoJogo().getTile(pPosicao.getLinha(), pPosicao.getColuna());
        
        if (pPosicao.getLinha() < 0 || pPosicao.getLinha() >= Consts.MUNDO_ALTURA ||
            pPosicao.getColuna() < 0 || pPosicao.getColuna() >= Consts.MUNDO_LARGURA) {
            return false; 
        }
        
        if (t != null && !t.isTransponivel()) {
            return false; 
        }
        
        return true;
    }
    
    public Direcao getDirecaoAtual() {
        return direcaoAtual;
    }

    public void setDirecaoAtual(Direcao novaDirecao) {
        this.direcaoAtual = novaDirecao;
        switch (novaDirecao) {
            case CIMA: this.iImage = upImage; break;
            case BAIXO: this.iImage = downImage; break;
            case ESQUERDA: this.iImage = leftImage; break;
            case DIREITA: this.iImage = rightImage; break;
        }
    }

    @Override
    public boolean moveUp() {
        this.setDirecaoAtual(Direcao.CIMA);
        return true; 
    }

    @Override
    public boolean moveDown() {
        this.setDirecaoAtual(Direcao.BAIXO);
        return true;
    }

    @Override
    public boolean moveRight() {
        this.setDirecaoAtual(Direcao.DIREITA);
        return true;
    }

    @Override
    public boolean moveLeft() {
        this.setDirecaoAtual(Direcao.ESQUERDA);
        return true;
    }

    public void usarIce() {
        if (Power_Ice.geloAtivo) return; 

        int l = this.getPosicao().getLinha();
        int c = this.getPosicao().getColuna();
        String imagem = "ice.png";

        int[][] direcoes = {
            {-1,  0}, 
            { 1,  0}, 
            { 0, -1}, 
            { 0,  1},
            {-1, -1},  
            {-1,  1}, 
            { 1, -1}, 
            { 1,  1}  
        };

        for (int[] d : direcoes) {
            int novaLinha = l + d[0];
            int novaColuna = c + d[1];
            Posicao pos = new Posicao(novaLinha, novaColuna);

            if (Desenho.acessoATelaDoJogo().ehPosicaoValida(pos)) {
                Power_Ice gelo = new Power_Ice(imagem, novaLinha, novaColuna);
                Desenho.acessoATelaDoJogo().addPersonagem(gelo);
            }
        }
        Power_Ice.geloAtivo = true;
    }
    
    public void usarFire() {
        if (Power_Fire.fogoAtivo) return; 

        Posicao posInicialFogo = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());

        switch (direcaoAtual) { 
            case CIMA -> posInicialFogo.moveUp();
            case BAIXO -> posInicialFogo.moveDown();
            case ESQUERDA -> posInicialFogo.moveLeft();
            case DIREITA -> posInicialFogo.moveRight();
        }

        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(posInicialFogo)) { 
            Power_Fire f = new Power_Fire("fire.png", direcaoAtual); 
            f.setPosicao(posInicialFogo.getLinha(), posInicialFogo.getColuna());
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }

    public void usarWater() {
        if (waterPowerStage != 0) return; 

        waterPowerStage = 1;
        waterPowerCooldown = 0;
        waterPowerDirecaoInicial = this.direcaoAtual;
        waterPowerPosInicial = new Posicao(pPosicao.getLinha(), pPosicao.getColuna()); 
    }

    public void atualizarWaterPower() {
        if (waterPowerStage == 0) return;

        if (waterPowerCooldown > 0) {
            waterPowerCooldown--;
            return;
        }

        Posicao pos = new Posicao(waterPowerPosInicial.getLinha(), waterPowerPosInicial.getColuna());

        for (int i = 0; i < waterPowerStage; i++) {
            switch (waterPowerDirecaoInicial) {
                case CIMA -> pos.moveUp();
                case BAIXO -> pos.moveDown();
                case ESQUERDA -> pos.moveLeft();
                case DIREITA -> pos.moveRight();
            }
        }

        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(pos)) {
            Power_Water w = new Power_Water("water.png");
            boolean ok = w.setPosicao(pos.getLinha(), pos.getColuna());
            if (ok) {
                Desenho.acessoATelaDoJogo().addPersonagem(w);           
            }
        }

        waterPowerStage++;
        if (waterPowerStage > 4) {
            waterPowerStage = 0;
        } else {
            waterPowerCooldown = WATER_POWER_INTERVAL;
        }
    }

    public void usarLight() {
        Posicao posInicialLight = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());

        switch (direcaoAtual) { 
            case CIMA -> posInicialLight.moveUp();
            case BAIXO -> posInicialLight.moveDown();
            case ESQUERDA -> posInicialLight.moveLeft();
            case DIREITA -> posInicialLight.moveRight();
        }

        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(posInicialLight)) { 
            Power_Light e = new Power_Light("light.png", direcaoAtual);
            e.setPosicao(posInicialLight.getLinha(), posInicialLight.getColuna());
            Desenho.acessoATelaDoJogo().addPersonagem(e);
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        atualizarWaterPower();
    }
    
    public void receberDano() {
        if (this.vidaHeroi > 0) {
            this.vidaHeroi--;
            System.out.println("Herói atingido! Vida restante: " + this.vidaHeroi);
        }
    }

    public boolean estaDerrotado() {
        return this.vidaHeroi <= 0;
    }
    
    public int getVidaHeroi() {
        return vidaHeroi;
    }

    public void setVidaHeroi(int vidaHeroi) {
        this.vidaHeroi = vidaHeroi;
    }

    public enum Direcao {
        CIMA, BAIXO, ESQUERDA, DIREITA;
    }
}