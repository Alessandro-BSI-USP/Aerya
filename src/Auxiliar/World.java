package Auxiliar;

import Controler.Tela;
import Modelo.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import Modelo.Lava;

public class World {
    private Tela tela;

    public World(String path, Tela tela) throws IOException {
        this.tela = tela;
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    int pixelAtual = pixels[x + (y * map.getWidth())];
                    processPixel(pixelAtual, y, x);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o mapa: " + path);
            throw e;
        }
    }

    private void processPixel(int pixel, int y, int x) throws IOException {

        if (pixel == 0xFFFFFFFF) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
        } else if (pixel == 0xFF000000) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"wall.png", false));
        } else if (pixel == 0xFFFF0000) { 
            Hero h = new Hero("hero.png");
            h.setPosicao(y, x);
            tela.setHero(h);
            tela.addPersonagem(h);
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            tela.atualizaCamera();
        } else if (pixel == 0xFFFFF000) {
            Tile tile = new Tile("next_level.png", true); 
            tela.setTile(y, x, tile); 
            tela.setPosicaoTileProximaFase(y, x);
        } else if (pixel == 0xFF0022FF) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            Lanterna lanterna = new Lanterna("1lanterna_apagada.png", "1lanterna.png");
            lanterna.setPosicao(y, x); 
            tela.addPersonagem(lanterna);
        } else if (pixel == 0xFFFF00FE) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"cadeira.png", false)); 
            Porta porta = new Porta("1porta.png", "1porta_aberta.png", false); 
            porta.setPosicao(y, x);
            tela.addPersonagem(porta);
        } else if (pixel == 0xFFFFF001) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "cadeira.png", false));
            Porta portaFinal = new Porta("1porta.png", "next_level.png", true); 
            portaFinal.setPosicao(y, x);
            tela.addPersonagem(portaFinal);
        }else if (pixel == 0xFF808602) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            InimigoBlindado inimigoBlindado = new InimigoBlindado("darkNight.png"); 
            inimigoBlindado.setPosicao(y, x);
            tela.addPersonagem(inimigoBlindado);
        }else if (pixel == 0xFFFE82A6) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            Boss boss = new Boss("melenia.png"); 
            boss.setPosicao(y, x);
            tela.addPersonagem(boss);
        }
        
        //
        // SEGUNDA FASE
        //
        else if (pixel == 0xFF063A00) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"wall.png", false));
        } else if (pixel == 0xFF00E1CF) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"water.png", true));
        } else if (pixel == 0xFFF66202) {
            if (tela.getLevelAtual() == 2) {
                tela.setTile(y, x, new Tile(tela.getLevelAtual() + "water.png", true));
                Lava lava = new Lava("2lava.png");
                lava.setPosicao(y, x);
                tela.addPersonagem(lava);
            } else {
                tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            }
        } else if (pixel == 0xFF3D3F3E) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"ground.png", true));
        }
        
        //
        // TERCEIRA FASE
        //
        else if (pixel == 0xFF1D2F23) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"mapa1.png", false)); 
        } else if (pixel == 0xFF06001E) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"penumbra.png", false));
        } else if (pixel == 0xFF2B2050) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"parede.png", false));
        } else if (pixel == 0xFF010B2F) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"ground.png", true));
        } else if (pixel == 0xFF800080) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true)); 
            
            InimigoPatrulha inimigo = new InimigoPatrulha("caveira.png"); 
            inimigo.setPosicao(y, x);
            tela.addPersonagem(inimigo);
        }else if (pixel == 0xFF90F602) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "ground.png", true));
            // Apenas o nome da imagem, como o InimigoPatrulha
            InimigoPerseguidor perseguidor = new InimigoPerseguidor("golen.png"); 
            perseguidor.setPosicao(y, x);
            tela.addPersonagem(perseguidor);
        }
        
         
        //
        // QUARTA FASE 
        //
        else if (pixel == 0xFF595CA9) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"parede.png", false)); 
        } else if (pixel == 0xFF196F70) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"penumbra.png", false));
        } else if (pixel == 0xFF0C15FA) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"parede.png", false));
        } else if (pixel == 0xFF00F6DD) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"ground.png", true));
        } 
        
        //
        // QUINTA FASE 
        //
        else if (pixel == 0xFF060E32) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"buraco.png", false)); 
        }else if (pixel == 0xFF5B585F) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"ground.png", true)); 
        }else if (pixel == 0xFF1B0B28) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"parede.png", false)); 
        }
        
        // FASE FINAL  
        //
        else if (pixel == 0xFF2CF8FF) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"trono.png", false)); 
        }else if (pixel == 0xFF3B1616) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() + "laranja.png", true));
            Livro livro = new Livro(tela.getLevelAtual() + "livro.png"); 
            livro.setPosicao(y, x);
            tela.addPersonagem(livro); 
            tela.setLivroDoJogo(livro);
        }else if (pixel == 0xFF643190) {
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"roxo.png", true)); 
        }else if (pixel == 0xFFF65800) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"laranja.png", true)); 
        }else if (pixel == 0xFFF6C800) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"amarelo.png", true)); 
        }else if (pixel == 0xFFD9D99D) { 
            tela.setTile(y, x, new Tile(tela.getLevelAtual() +"preto.png", false)); 
        }
    }
}