package Modelo;

import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

import java.io.IOException;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Personagem> personagensDoNivel;
    private Tile[][] mapaDoNivel;
    private int cameraLinha;
    private int cameraColuna;
    private int levelAtual;
    private Posicao heroPosition; 
    private Hero.Direcao heroDirection; 
    private boolean powerFireAtivo; 

    public GameState(ArrayList<Personagem> personagens, Tile[][] mapa,
                     int camLinha, int camColuna, int currentLevel,
                     Posicao heroPos, Hero.Direcao heroDir,
                     boolean powerFireStatus) {
        
        this.personagensDoNivel = new ArrayList<>();
        for(Personagem p : personagens){
            this.personagensDoNivel.add(p);
        }
        
        this.mapaDoNivel = copyMap(mapa); 
        this.cameraLinha = camLinha;
        this.cameraColuna = camColuna;
        this.levelAtual = currentLevel;
        this.heroPosition = (heroPos != null) ? new Posicao(heroPos.getLinha(), heroPos.getColuna()) : null;
        this.heroDirection = heroDir;
        this.powerFireAtivo = powerFireStatus;
    }

    private Tile[][] copyMap(Tile[][] originalMap) {
        if (originalMap == null) return null;
        Tile[][] newMap = new Tile[originalMap.length][originalMap[0].length];
        for (int i = 0; i < originalMap.length; i++) {
            for (int j = 0; j < originalMap[i].length; j++) {
                if (originalMap[i][j] != null) {
                    try {
                        newMap[i][j] = new Tile(originalMap[i][j].getNomeImagePNG(), originalMap[i][j].isTransponivel());
                    } catch (IOException e) {
                        System.err.println("Erro ao copiar Tile para GameState: " + e.getMessage());
                        newMap[i][j] = null; 
                    }
                }
            }
        }
        return newMap;
    }

    public ArrayList<Personagem> getPersonagensDoNivel() {
        return personagensDoNivel;
    }

    public Tile[][] getMapaDoNivel() {
        return mapaDoNivel;
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public int getLevelAtual() {
        return levelAtual;
    }

    public Posicao getHeroPosition() {
        return heroPosition;
    }

    public Hero.Direcao getHeroDirection() {
        return heroDirection;
    }

    public boolean isPowerFireAtivo() {
        return powerFireAtivo;
    }
}