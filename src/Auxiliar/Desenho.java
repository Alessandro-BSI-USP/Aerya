package Auxiliar;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import Controler.Tela;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Desenho { 

    private static Tela jCenario;
    private static Graphics g; 

    public static void setCenario(Tela umJCenario) {
        jCenario = umJCenario;
    }

    public static Tela acessoATelaDoJogo() {
        return jCenario;
    }
    
    public static void setGraphics(Graphics graphics) {
        g = graphics;
    }

    public static void desenhar(ImageIcon iImage, int iColuna, int iLinha) {
        int telaX = (iColuna - jCenario.getCameraColuna()) * Consts.CELL_SIDE;
        int telaY = (iLinha - jCenario.getCameraLinha()) * Consts.CELL_SIDE;

        if (telaX >= -Consts.CELL_SIDE && telaX < Consts.RES * Consts.CELL_SIDE &&
            telaY >= -Consts.CELL_SIDE && telaY < Consts.RES * Consts.CELL_SIDE) {
            
            if (g != null) { 
                iImage.paintIcon(jCenario, g, telaX, telaY);
            } else {
                System.err.println("Erro: Graphics não setado em Desenho para desenhar.");
            }
        }
    }

    public static ImageIcon carregarImagem(String nomeImagem) throws IOException {
        try {
            URL imgURL = Desenho.class.getResource(Consts.PATH + nomeImagem);
            
            if (imgURL == null) {
                imgURL = Desenho.class.getResource(nomeImagem); 
                if (imgURL == null) {
                    throw new IOException("Imagem não encontrada: " + nomeImagem);
                }
            }
            
            Image img = new ImageIcon(imgURL).getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics graphicsBI = bi.createGraphics(); 
            graphicsBI.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null); 
            graphicsBI.dispose();
            return new ImageIcon(bi);

        } catch (IOException ex) {
            System.err.println("Erro ao carregar sprite: " + nomeImagem + " - " + ex.getMessage());
            throw ex;
        }
    }
}