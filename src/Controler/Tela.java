package Controler;

import Modelo.Personagem;
import Modelo.Hero;
import Modelo.Lanterna;
import Modelo.Power_Fire;
import Modelo.Tile; 

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.World;
import Modelo.GameState;
import Modelo.Livro;
import Modelo.Power_Ice;
import auxiliar.Posicao;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferStrategy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener, DropTargetListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private Tile[][] mapaBase = new Tile[Consts.MUNDO_ALTURA][Consts.MUNDO_LARGURA];
    private final ControleDeJogo cj = new ControleDeJogo(this);
    private Graphics g2; 
    private int cameraLinha;
    private int cameraColuna;
    
    private int levelAtual = 1;
    private final ArrayList<Personagem> personagens;
    
    private boolean gameWon = false;    
    private Livro livroDoJogo;
    private volatile boolean running = true;
    private Thread gameLoopThread;

    
    private Posicao posicaoTileProximaFase;
    
    private int pontuacaoTotal = 0;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                     Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
        
        this.createBufferStrategy(Consts.NUM_BUFFERED); 
        
        carregarLevel(levelAtual);
        go();
        
        new DropTarget(this, DnDConstants.ACTION_COPY, this, true); 
        personagens = new ArrayList<>();
        

    }
    public void iniciarJogo() {
        go(); 
    }
    
    public void carregarLevel(int n) {
        faseAtual = new ArrayList<>();
        mapaBase = new Tile[Consts.MUNDO_ALTURA][Consts.MUNDO_LARGURA];
        
        try {
            new World(Consts.PATH + n + "mapa.png", this); 
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cameraLinha = 35;
        cameraColuna = 0;
    }
    
    public void setPosicaoTileProximaFase(int linha, int coluna) {
        if (this.posicaoTileProximaFase == null) {
            this.posicaoTileProximaFase = new Posicao(linha, coluna);
        } else {
            this.posicaoTileProximaFase.setPosicao(linha, coluna);
        }
    }
    
    public ArrayList<Personagem> getPersonagens() {
        return personagens;
    }

    public ControleDeJogo getControleDeJogo() {
        return this.cj;
    }
    public void adicionarPontos(int pontos) {
        this.pontuacaoTotal += pontos;
        System.out.println("Pontos adicionados: " + pontos + ". Pontuação total: " + this.pontuacaoTotal);
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }


    public boolean isGameWon() {
        return gameWon;
    }
    public void saveGame(String fileName) {
        try {
            Hero currentHero = null;
            for (Personagem p : faseAtual) {
                if (p instanceof Hero) {
                    currentHero = (Hero) p;
                    break;
                }
            }

            if (currentHero == null) {
                System.err.println("Erro: Herói não encontrado para salvar o jogo!");
                return;
            }

            GameState gameState = new GameState(
                faseAtual,           
                mapaBase,            
                cameraLinha,        
                cameraColuna,        
                levelAtual,       
                currentHero.getPosicao(),
                currentHero.getDirecaoAtual(),
                Power_Fire.fogoAtivo 
            );
            
            try (FileOutputStream fileOut = new FileOutputStream(fileName); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(gameState);
            }
            System.out.println("Jogo salvo com sucesso em " + fileName);
        } catch (IOException i) {
            i.printStackTrace();
            System.err.println("Erro ao salvar o jogo: " + i.getMessage());
        }
    }

    public void loadGame(String fileName) {
        try {
            GameState gameState;
            try (FileInputStream fileIn = new FileInputStream(fileName); ObjectInputStream in = new ObjectInputStream(fileIn)) {
                gameState = (GameState) in.readObject();
            }
            System.out.println("Jogo carregado com sucesso de " + fileName);

            this.faseAtual.clear();
            this.faseAtual.addAll(gameState.getPersonagensDoNivel());

            this.mapaBase = gameState.getMapaDoNivel(); 
            this.cameraLinha = gameState.getCameraLinha();
            this.cameraColuna = gameState.getCameraColuna();
            this.levelAtual = gameState.getLevelAtual();

            
            Hero loadedHero = null;
            for (Personagem p : this.faseAtual) {
                if (p instanceof Hero) {
                    loadedHero = (Hero) p;
                    break;
                }
            }

            if (loadedHero != null) {
                this.setHero(loadedHero); 
                if (gameState.getHeroPosition() != null) {
                    loadedHero.getPosicao().setPosicao(gameState.getHeroPosition().getLinha(), gameState.getHeroPosition().getColuna());
                }
                if (gameState.getHeroDirection() != null) {
                    loadedHero.setDirecaoAtual(gameState.getHeroDirection());
                }
            } else {
                System.err.println("Erro: Herói não encontrado no estado carregado!");
            }
            
            Power_Fire.fogoAtivo = gameState.isPowerFireAtivo();
            Power_Ice.geloAtivo = false;

            for (Personagem p : faseAtual) {
                if (p.getsNomeImagePNG() != null && !p.getsNomeImagePNG().isEmpty()) {
                    try {
                        p.setarImage(p.getsNomeImagePNG());
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao recarregar imagem para personagem apos load: " + p.getClass().getSimpleName(), ex);
                    }
                }
            }

            this.setTitle("Level " + levelAtual + " -> Pos: " + (loadedHero != null ? loadedHero.getPosicao().getColuna() + ", " + loadedHero.getPosicao().getLinha() : "N/A"));
            this.repaint(); 
            System.out.println("Estado do jogo restaurado com sucesso.");

        } catch (ClassNotFoundException c) {
            System.err.println("Classe não encontrada durante o carregamento: " + c.getMessage());
            c.printStackTrace();
        } catch (IOException i) {
            System.err.println("Erro de E/S ao carregar o jogo: " + i.getMessage());
            i.printStackTrace();
        }
    }
    public void verificarVitoria() {
        if (hero == null || gameWon) {
            return;
        }

        for (Personagem p : personagens) { 
            if (p instanceof Livro) {
                if (hero.getPosicao().igual(p.getPosicao())) {
                    System.out.println("Herói pisou no livro! VOCÊ GANHOU O JOGO!");
                    gameWon = true; 
                    
                    break; 
                }
            }
        }
    }
    
    public void verificarPuzzles() {
        boolean todasLanternasAcesas = true;
        List<Lanterna> lanternasNoNivel = new ArrayList<>();

        for (Personagem p : faseAtual) {
            if (p instanceof Lanterna) {
                lanternasNoNivel.add((Lanterna) p);
                if (!((Lanterna) p).isAcesa()) {
                    todasLanternasAcesas = false; 
                }
            }
        }

        if (todasLanternasAcesas && !lanternasNoNivel.isEmpty()) { 
            System.out.println("Todas as Lanternas estão acesas! Porta deve abrir!");
            
            for (int r = 0; r < Consts.MUNDO_ALTURA; r++) {
                for (int c = 0; c < Consts.MUNDO_LARGURA; c++) {
                    Tile t = getTile(r, c); 
                    if (t != null && t.getNomeImagePNG().contains("porta.png") && !t.isTransponivel()) { 
                         try {
                            t.setTransponivel(true); 
                            t.setarImage("chao.png"); 
                            System.out.println("Porta na posição " + r + ", " + c + " aberta!");
                         } catch (IOException ex) {
                            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                         }
                    }
                }
            }
        }
    }
    
    @Override
    public void keyPressed (KeyEvent e){
        if(hero == null) return;
        
        Posicao proximaPosicao = new Posicao(hero.getPosicao().getLinha(), hero.getPosicao().getColuna());
        boolean tentouMover = false;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (hero.getDirecaoAtual() == Hero.Direcao.CIMA) {
                    proximaPosicao.moveUp();
                    tentouMover = true;
                } else {
                    hero.setDirecaoAtual(Hero.Direcao.CIMA);
                }
                break;
            case KeyEvent.VK_S:
                if (hero.getDirecaoAtual() == Hero.Direcao.BAIXO) {
                    proximaPosicao.moveDown();
                    tentouMover = true;
                } else {
                    hero.setDirecaoAtual(Hero.Direcao.BAIXO);
                }
                break;
            case KeyEvent.VK_A:
                if (hero.getDirecaoAtual() == Hero.Direcao.ESQUERDA) {
                    proximaPosicao.moveLeft();
                    tentouMover = true;
                } else {
                    hero.setDirecaoAtual(Hero.Direcao.ESQUERDA);
                }
                break;
            case KeyEvent.VK_D:
                if (hero.getDirecaoAtual() == Hero.Direcao.DIREITA) {
                    proximaPosicao.moveRight();
                    tentouMover = true;
                } else {
                    hero.setDirecaoAtual(Hero.Direcao.DIREITA);
                }
                break;
            case KeyEvent.VK_N:
                nextLevel();
                break;
            case KeyEvent.VK_R:
                resetLevel();
                break;
            case KeyEvent.VK_I:
                hero.usarIce();
                break;
            case KeyEvent.VK_J:
                hero.usarFire();
                break;
            case KeyEvent.VK_K:
                hero.usarWater();
                break;
            case KeyEvent.VK_L:
                hero.usarLight();
                break;
            case KeyEvent.VK_P: 
                saveGame("savegame.ser"); 
                break;
            case KeyEvent.VK_O: 
                loadGame("savegame.ser"); 
                break;
            default:
                break;
        }

        if (tentouMover) {
            boolean posicaoTileValida = ehPosicaoValida(proximaPosicao);
            boolean posicaoPersonagemValida = cj.ehPosicaoValida(faseAtual, proximaPosicao);

            if (posicaoTileValida && posicaoPersonagemValida) {
                hero.getPosicao().copia(proximaPosicao);
                for (Personagem p : faseAtual) {
                    if (p.getPosicao().igual(hero.getPosicao()) && p.isbMortal()) {
                        System.out.println("Herói pisou em um objeto mortal! Resetando fase...");
                        resetLevel();
                        break; 
                    }
                }
            }
        }
        
        this.atualizaCamera();
        this.setTitle("Level " + levelAtual + " -> Pos: " + hero.getPosicao().getColuna() + ", " + 
                      hero.getPosicao().getLinha() + " -> Direção: " + hero.getDirecaoAtual()); 
        this.repaint(); 
    }
    
    public void resetLevel(){
        Power_Fire.fogoAtivo = false;
        carregarLevel(levelAtual);
    }

    public void nextLevel() {
        if (levelAtual < Consts.TOTAL_LEVEIS) {
            levelAtual++;
            carregarLevel(levelAtual);
        } else {
            System.out.println("Parabéns! Você completou todos os níveis!");
        }
    }

    public void setTile(int linha, int coluna, Tile tile) {
        if (linha >= 0 && linha < mapaBase.length && 
            coluna >= 0 && coluna < mapaBase[0].length) {
            mapaBase[linha][coluna] = tile;
        }
    }

    public Tile getTile(int linha, int coluna) {
        if (linha >= 0 && linha < mapaBase.length &&
            coluna >= 0 && coluna < mapaBase[0].length) {
            return mapaBase[linha][coluna];
        }
        return null; 
    }

    public void setHero(Hero h) {
        this.hero = h;
    }
    public void setLivroDoJogo(Livro livro) {
        this.livroDoJogo = livro;
    }
    public void verificarCondicaoVitoria() {
        if (this.hero != null && this.livroDoJogo != null) {
            if (this.hero.getPosicao().igual(this.livroDoJogo.getPosicao())) {
                System.out.println("Parabéns! Você encontrou o livro e venceu o jogo!");
                pausarJogo();
                exibirTelaVitoria();
            }
        }
    }
    public void pausarJogo() {
        this.running = false;
        System.out.println("Fechando a tela do joguinho");

        if (gameLoopThread != null && gameLoopThread.isAlive()) {
            try {
                gameLoopThread.join(); // Aguarda a thread terminar antes de prosseguir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.dispose();
    }


    private void exibirTelaVitoria() {
        new View.TelaVitoria(this.getPontuacaoTotal()); 
    }
    public Hero getHero() {
        return this.hero;
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public int getLevelAtual(){
        return levelAtual;
    }
    
    public boolean ehPosicaoValida(Posicao p) {
        if (p.getLinha() < 0 || p.getColuna() < 0 ||
            p.getLinha() >= Consts.MUNDO_ALTURA || p.getColuna() >= Consts.MUNDO_LARGURA) {
            return false;
        }
        Tile t = getTile(p.getLinha(), p.getColuna());
        return t == null || t.isTransponivel();
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }
    public void verificarColisaoProximaFase() {
    if (this.hero != null && this.posicaoTileProximaFase != null) {
        if (this.hero.getPosicao().igual(this.posicaoTileProximaFase)) {
            System.out.println("Herói colidiu com o tile de próxima fase!");
            nextLevel(); 
        }
    }
}

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2; 
    }

    @Override
    public void paint(Graphics gOld) {
    if (!this.isDisplayable() || getBufferStrategy() == null) {
        return; 
    }

    BufferStrategy bs = getBufferStrategy();
    Graphics g = bs.getDrawGraphics();

    Graphics g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        Desenho.setGraphics(g2);

        if (!faseAtual.isEmpty()) {
            try {
                cj.processaTudo(faseAtual);
            } catch (IOException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha >= 0 && mapaLinha < Consts.MUNDO_ALTURA &&
                    mapaColuna >= 0 && mapaColuna < Consts.MUNDO_LARGURA) {
                    Tile tile = mapaBase[mapaLinha][mapaColuna];
                    if (tile != null) {
                        tile.desenhar(mapaLinha, mapaColuna);
                    }
                }
            }
        }

        if (!faseAtual.isEmpty()) {
            cj.desenhaOutrosPersonagens(faseAtual);
        }

        Hero h = cj.getHeroi(faseAtual);
        if (h != null) {
            h.autoDesenho();
        }

        verificarCondicaoVitoria();
        verificarColisaoProximaFase();
        
        g.dispose();
        g2.dispose();
        if (!bs.contentsLost()) {
            try {
                bs.show();
            } catch (IllegalStateException e) {
            }
        }

    }
    public void atualizaCamera() {
        if (hero == null) return; 

        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        int limiteSuperior = cameraLinha + Consts.MARGEM;
        int limiteInferior = cameraLinha + Consts.RES - Consts.MARGEM - 1;
        int limiteEsquerdo = cameraColuna + Consts.MARGEM;
        int limiteDireito = cameraColuna + Consts.RES - Consts.MARGEM - 1;

        if (linha < limiteSuperior) {
            cameraLinha = Math.max(0, cameraLinha - (limiteSuperior - linha));
        } else if (linha > limiteInferior) {
            cameraLinha = Math.min(Consts.MUNDO_ALTURA - Consts.RES, cameraLinha + (linha - limiteInferior));
        }

        if (coluna < limiteEsquerdo) {
            cameraColuna = Math.max(0, cameraColuna - (limiteEsquerdo - coluna));
        } else if (coluna > limiteDireito) {
            cameraColuna = Math.min(Consts.MUNDO_LARGURA - Consts.RES, cameraColuna + (coluna - limiteDireito));
        }
    }

    public void go() {
        gameLoopThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            double nsPerTick = 1_000_000_000.0 / 60.0;
            double delta = 0;

            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / nsPerTick;
                lastTime = now;

                while (delta >= 1) {
                    repaint();
                    delta--;
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoopThread.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX() - getInsets().left; 
        int mouseY = e.getY() - getInsets().top;   

        int mundoColuna = (mouseX / Consts.CELL_SIDE) + cameraColuna;
        int mundoLinha = (mouseY / Consts.CELL_SIDE) + cameraLinha;
        
        System.out.println("Clique do mouse em: Linha=" + mundoLinha + ", Coluna=" + mundoColuna);
    }

    public void mouseMoved(MouseEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Tela t = new Tela();
                t.setVisible(true);
            }
        });
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) { }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }

    @Override
    public void dragExit(DropTargetEvent dte) { }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY); 
        try {
            List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

            int mouseX = dtde.getLocation().x - getInsets().left; 
            int mouseY = dtde.getLocation().y - getInsets().top;   

            int mundoColuna = (mouseX / Consts.CELL_SIDE) + cameraColuna;
            int mundoLinha = (mouseY / Consts.CELL_SIDE) + cameraLinha;
            
            Posicao posicaoInvocacao = new Posicao(mundoLinha, mundoColuna);

            System.out.println("Arquivo solto em: " + mouseX + "," + mouseY + 
                               " -> Posicao no mundo: " + mundoLinha + "," + mundoColuna);

            for (File file : droppedFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
                    processarZipInimigo(file, posicaoInvocacao);
                } else {
                    System.out.println("O arquivo enviado não é um zip " + file.getName());
                }
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao processar arquivo arrastado", ex);
            System.err.println("Erro ao processar arquivo arrastado: " + ex.getMessage());
        } finally {
            dtde.dropComplete(true); 
        }
    }

    private void processarZipInimigo(File zipFile, Posicao posicaoInvocacao) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            Personagem personagemDesserializado = null;
            String personagemImageName = null;

            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    if (entry.getName().toLowerCase().endsWith(".ser")) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }
                        byte[] serializedData = baos.toByteArray();

                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData))) {
                            Object obj = ois.readObject();
                            // --- MUDANÇA: Verifica se é uma instância de Personagem ---
                            if (obj instanceof Personagem) {
                                personagemDesserializado = (Personagem) obj;
                                personagemImageName = personagemDesserializado.getsNomeImagePNG();
                                System.out.println("Personagem desserializado: " + personagemDesserializado.getClass().getSimpleName() +
                                                   " com imagem: " + personagemImageName);
                            } else {
                                System.err.println("Objeto no ZIP não é um Personagem: " + obj.getClass().getName() + " em " + entry.getName());
                            }
                            // --- FIM DA MUDANÇA ---
                        } catch (ClassNotFoundException e) {
                            System.err.println("Erro de classe não encontrada ao desserializar objeto " + entry.getName() + ": " + e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                               System.err.println("Erro de E/S ao desserializar objeto " + entry.getName() + ": " + e.getMessage());
                               e.printStackTrace();
                        }
                    }
                }
            }

            if (personagemDesserializado != null) {
                personagemDesserializado.setPosicao(posicaoInvocacao.getLinha(), posicaoInvocacao.getColuna());

                if (personagemImageName != null) {
                    try {
                        personagemDesserializado.setarImage(personagemImageName);
                    } catch (IOException e) {
                        System.err.println("Erro ao recarregar imagem para personagem desserializado '" + personagemImageName + "': " + e.getMessage());
                    }
                } else {
                    System.err.println("Nome da imagem não encontrado para personagem desserializado.");
                }

                this.addPersonagem(personagemDesserializado);
                System.out.println("Personagem '" + personagemDesserializado.getClass().getSimpleName() + "' invocado na posição: " +
                                   posicaoInvocacao.getLinha() + ", " + posicaoInvocacao.getColuna());
                this.repaint();
            } else {
                System.out.println("Nenhum personagem serializado válido encontrado no ZIP: " + zipFile.getName());
            }

        } catch (IOException e) {
            System.err.println("Erro de E/S ao processar ZIP '" + zipFile.getName() + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}