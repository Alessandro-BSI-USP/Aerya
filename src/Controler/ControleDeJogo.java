package Controler;

import Modelo.Personagem;
import Modelo.Hero;
import Modelo.InimigoPatrulha;
import Modelo.InimigoPerseguidor;
import Modelo.Lanterna;
import Modelo.Lava;
import Modelo.LavaResfriada;
import Modelo.Porta;
import Modelo.Power_Fire;
import Modelo.Power_Water;
import Modelo.Power_Light;
import Modelo.Power_Ice;
import Auxiliar.Desenho;
import Modelo.Boss;
import Modelo.InimigoBlindado;
import auxiliar.Posicao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControleDeJogo {
    private final Tela tela;
    
    public ControleDeJogo(Tela tela) {
        this.tela = tela;
    }
    
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
            verificarColisoesComInimigos();
            
        }
    }
    
    public synchronized void processaTudo(ArrayList<Personagem> fase) throws IOException {
        ArrayList<Personagem> paraRemover = new ArrayList<>();
        ArrayList<Personagem> paraAdicionar = new ArrayList<>();

        List<Personagem> faseCopia = new ArrayList<>(fase);

        Hero hero = getHeroi(faseCopia);

        for (int i = 0; i < faseCopia.size(); i++) {
            Personagem pAtual = faseCopia.get(i);

            if (pAtual instanceof InimigoPatrulha) {
                InimigoPatrulha inimigo = (InimigoPatrulha) pAtual;

                inimigo.incrementaContador();
                if (!inimigo.estaNaHoraDeMover()) {
                    continue;
                }

                Posicao proximaPosicaoTentativa = new Posicao(inimigo.getPosicao().getLinha(), inimigo.getPosicao().getColuna());
                Hero.Direcao direcaoMovimento = inimigo.getDirecaoAtual(); 

                if (direcaoMovimento == Hero.Direcao.ESQUERDA) {
                    proximaPosicaoTentativa.moveLeft();
                } else { 
                    proximaPosicaoTentativa.moveRight();
                }

                boolean podeMoverParaTile = Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicaoTentativa);
                ArrayList<Personagem> outrosPersonagensParaValidar = new ArrayList<>(faseCopia);
                outrosPersonagensParaValidar.remove(inimigo);
                boolean podeMoverParaPersonagem = this.ehPosicaoValida(outrosPersonagensParaValidar, proximaPosicaoTentativa);

                if (podeMoverParaTile && podeMoverParaPersonagem) {
                    inimigo.getPosicao().copia(proximaPosicaoTentativa);
                    inimigo.setDirecaoAtual(direcaoMovimento);
                } else {
                    if (inimigo.getDirecaoAtual() == Hero.Direcao.ESQUERDA) {
                        inimigo.setDirecaoAtual(Hero.Direcao.DIREITA);
                    } else {
                        inimigo.setDirecaoAtual(Hero.Direcao.ESQUERDA);
                    }
                }
            }
            else if (pAtual instanceof InimigoPerseguidor && hero != null) {
                InimigoPerseguidor inimigo = (InimigoPerseguidor) pAtual;

                Posicao posHero = hero.getPosicao();
                Posicao posInimigo = inimigo.getPosicao();

                int deltaLinhaAbs = Math.abs(posHero.getLinha() - posInimigo.getLinha());
                int deltaColunaAbs = Math.abs(posHero.getColuna() - posInimigo.getColuna());

                int distancia = deltaLinhaAbs + deltaColunaAbs;

                if (distancia > inimigo.getRaioPercepcao()) {
                    inimigo.incrementaContador();
                    continue;
                }

                inimigo.incrementaContador();
                if (!inimigo.estaNaHoraDeMover()) {
                    continue;
                }

                Posicao proximaPosicaoTentativa = new Posicao(posInimigo.getLinha(), posInimigo.getColuna());

                int deltaLinha = posHero.getLinha() - posInimigo.getLinha();
                int deltaColuna = posHero.getColuna() - posInimigo.getColuna();

                boolean moveu = false;
                Hero.Direcao direcaoMovimento = inimigo.getDirecaoAtual();

                if (Math.abs(deltaLinha) >= Math.abs(deltaColuna)) {
                    if (deltaLinha > 0) {
                        proximaPosicaoTentativa.moveDown();
                        direcaoMovimento = Hero.Direcao.BAIXO;
                    } else if (deltaLinha < 0) {
                        proximaPosicaoTentativa.moveUp();
                        direcaoMovimento = Hero.Direcao.CIMA;
                    }

                    ArrayList<Personagem> outrosPersonagensParaValidar = new ArrayList<>(faseCopia);
                    outrosPersonagensParaValidar.remove(inimigo);
                    boolean podeMoverParaTile = Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicaoTentativa);
                    boolean podeMoverParaPersonagem = this.ehPosicaoValida(outrosPersonagensParaValidar, proximaPosicaoTentativa);

                    if (podeMoverParaTile && podeMoverParaPersonagem) {
                        inimigo.getPosicao().copia(proximaPosicaoTentativa);
                        moveu = true;
                        inimigo.setDirecaoAtual(direcaoMovimento);
                    } else {
                        proximaPosicaoTentativa.copia(posInimigo);
                        if (deltaColuna > 0) {
                            proximaPosicaoTentativa.moveRight();
                            direcaoMovimento = Hero.Direcao.DIREITA; 
                        } else if (deltaColuna < 0) {
                            proximaPosicaoTentativa.moveLeft();
                            direcaoMovimento = Hero.Direcao.ESQUERDA;
                        }

                        podeMoverParaTile = Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicaoTentativa);
                        podeMoverParaPersonagem = this.ehPosicaoValida(outrosPersonagensParaValidar, proximaPosicaoTentativa);
                        if (podeMoverParaTile && podeMoverParaPersonagem) {
                            inimigo.getPosicao().copia(proximaPosicaoTentativa);
                            moveu = true;
                            inimigo.setDirecaoAtual(direcaoMovimento);
                        }
                    }
                }

                if (!moveu) {
                    proximaPosicaoTentativa.copia(posInimigo);

                    if (deltaColuna > 0) {
                        proximaPosicaoTentativa.moveRight();
                        direcaoMovimento = Hero.Direcao.DIREITA; 
                    } else if (deltaColuna < 0) {
                        proximaPosicaoTentativa.moveLeft();
                        direcaoMovimento = Hero.Direcao.ESQUERDA; 
                    }

                    ArrayList<Personagem> outrosPersonagensParaValidar = new ArrayList<>(faseCopia);
                    outrosPersonagensParaValidar.remove(inimigo);
                    boolean podeMoverParaTile = Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicaoTentativa);
                    boolean podeMoverParaPersonagem = this.ehPosicaoValida(outrosPersonagensParaValidar, proximaPosicaoTentativa);

                    if (podeMoverParaTile && podeMoverParaPersonagem) {
                        inimigo.getPosicao().copia(proximaPosicaoTentativa);
                        moveu = true;
                        inimigo.setDirecaoAtual(direcaoMovimento);
                    } else if (!moveu) {
                        proximaPosicaoTentativa.copia(posInimigo);
                        if (deltaLinha > 0) {
                            proximaPosicaoTentativa.moveDown();
                            direcaoMovimento = Hero.Direcao.BAIXO;
                        } else if (deltaLinha < 0) {
                            proximaPosicaoTentativa.moveUp();
                            direcaoMovimento = Hero.Direcao.CIMA;
                        }

                        podeMoverParaTile = Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicaoTentativa);
                        podeMoverParaPersonagem = this.ehPosicaoValida(outrosPersonagensParaValidar, proximaPosicaoTentativa);
                        if (podeMoverParaTile && podeMoverParaPersonagem) {
                            inimigo.getPosicao().copia(proximaPosicaoTentativa);
                            moveu = true;
                            inimigo.setDirecaoAtual(direcaoMovimento);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < faseCopia.size(); i++) {
            Personagem pAtual = faseCopia.get(i);

            for (int j = i + 1; j < faseCopia.size(); j++) {
                Personagem outroPersonagem = faseCopia.get(j);

                if (paraRemover.contains(pAtual) || paraRemover.contains(outroPersonagem)) {
                    continue;
                }

                if (pAtual.getPosicao().estaNaMesmaPosicao(outroPersonagem.getPosicao())) {

                    // verifica a colisão do poder Power_Light com InimigoBlindado (para ativar a vulnerabilidade)
                    if ((pAtual instanceof Power_Light && outroPersonagem instanceof InimigoBlindado) ||
                        (pAtual instanceof InimigoBlindado && outroPersonagem instanceof Power_Light)) {
                        
                        InimigoBlindado inimigoBlindado = (pAtual instanceof InimigoBlindado) ? (InimigoBlindado) pAtual : (InimigoBlindado) outroPersonagem;
                        Personagem powerLight = (pAtual instanceof Power_Light) ? pAtual : outroPersonagem;
                        
                        if (!inimigoBlindado.isVulnerable()) {
                            inimigoBlindado.setVulnerable(true);
                            System.out.println("Inimigo Blindado atingido por Power_Light e se tornou VULNERAVEL!");
                        } 
                        paraRemover.add(powerLight);
                        continue;
                    }

                    if (((pAtual instanceof Power_Fire || pAtual instanceof Power_Water || pAtual instanceof Power_Ice) && outroPersonagem instanceof InimigoBlindado) ||
                        (pAtual instanceof InimigoBlindado && (outroPersonagem instanceof Power_Fire || outroPersonagem instanceof Power_Water || outroPersonagem instanceof Power_Ice))) {
                        
                        InimigoBlindado inimigoBlindado = (pAtual instanceof InimigoBlindado) ? (InimigoBlindado) pAtual : (InimigoBlindado) outroPersonagem;
                        Personagem poder = (pAtual instanceof InimigoBlindado) ? outroPersonagem : pAtual;

                        if (inimigoBlindado.isVulnerable()) {
                            tela.adicionarPontos(inimigoBlindado.getPontosValor());
                            paraRemover.add(inimigoBlindado);
                            paraRemover.add(poder);
                            if (poder instanceof Power_Fire) Power_Fire.resetarFogoAtivo();
                            continue;
                        } else {
                            System.out.println("Inimigo Imune a Ataque, use a Luz para remover proteção.");
                            paraRemover.add(poder);
                            if (poder instanceof Power_Fire) Power_Fire.resetarFogoAtivo();
                            continue;
                        }
                    }
                    
                    // Atacando o boss

                    if (((pAtual instanceof Power_Fire || pAtual instanceof Power_Water || pAtual instanceof Power_Light || pAtual instanceof Power_Ice) && outroPersonagem instanceof Boss) ||
                        (pAtual instanceof Boss && (outroPersonagem instanceof Power_Fire || outroPersonagem instanceof Power_Water || outroPersonagem instanceof Power_Light || outroPersonagem instanceof Power_Ice))) {

                        Boss boss = (pAtual instanceof Boss) ? (Boss) pAtual : (Boss) outroPersonagem;
                        Personagem poder = (pAtual instanceof Boss) ? outroPersonagem : pAtual;

                        boss.receberDano(); 
                        if (boss.estaDerrotado()) {
                            System.out.println("Boss derrotado!");
                            tela.adicionarPontos(boss.getPontosValor()); 
                            paraRemover.add(boss); 
                        }
                        paraRemover.add(poder); 
                        if (poder instanceof Power_Fire) Power_Fire.resetarFogoAtivo();
                        continue; 
                    }

                    // Power_fire
                    if ((pAtual instanceof Power_Fire && outroPersonagem instanceof InimigoPatrulha) ||
                                (pAtual instanceof InimigoPatrulha && outroPersonagem instanceof Power_Fire)) {
                        Personagem poder = (pAtual instanceof Power_Fire) ? pAtual : outroPersonagem;
                        InimigoPatrulha inimigo = (pAtual instanceof InimigoPatrulha) ? (InimigoPatrulha)pAtual : (InimigoPatrulha)outroPersonagem; 
                        tela.adicionarPontos(inimigo.getPontosValor()); 
                        paraRemover.add(inimigo);
                        paraRemover.add(poder);
                        Power_Fire.resetarFogoAtivo();
                        continue;
                    }
                    else if ((pAtual instanceof Power_Fire && outroPersonagem instanceof InimigoPerseguidor) ||
                            (pAtual instanceof InimigoPerseguidor && outroPersonagem instanceof Power_Fire)) {
                       if (!((pAtual instanceof InimigoPerseguidor && pAtual instanceof InimigoBlindado) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof InimigoBlindado) ||
                             (pAtual instanceof InimigoPerseguidor && pAtual instanceof Boss) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof Boss))) {
                           Personagem poder = (pAtual instanceof Power_Fire) ? pAtual : outroPersonagem;
                           InimigoPerseguidor inimigo = (pAtual instanceof InimigoPerseguidor) ? (InimigoPerseguidor)pAtual : (InimigoPerseguidor)outroPersonagem; 
                           tela.adicionarPontos(inimigo.getPontosValor()); 
                           paraRemover.add(inimigo);
                           paraRemover.add(poder);
                           Power_Fire.resetarFogoAtivo();
                           continue;
                       }
                   }

                    // Power_Fire com Lanterna
                    else if ((pAtual instanceof Power_Fire && outroPersonagem instanceof Lanterna) ||
                             (pAtual instanceof Lanterna && outroPersonagem instanceof Power_Fire)) {

                        Lanterna lanterna = (pAtual instanceof Lanterna) ? (Lanterna) pAtual : (Lanterna) outroPersonagem;
                        Personagem powerFire = (pAtual instanceof Power_Fire) ? pAtual : outroPersonagem;

                        if (!lanterna.isAcesa()) {
                            lanterna.acender();

                            int lanternasAcesasAtualmente = 0;
                            for (Personagem p : faseCopia) {
                                if (p instanceof Lanterna && ((Lanterna) p).isAcesa()) {
                                    lanternasAcesasAtualmente++;
                                }
                            }
                            System.out.println("Lanterna acesa! Total de lanternas acesas: " + lanternasAcesasAtualmente);

                            if (lanternasAcesasAtualmente == 4) {
                                System.out.println("4 Lanternas acesas! Abrindo portas normais!");
                                for (Personagem p : fase) {
                                    if (p instanceof Porta) {
                                        Porta porta = (Porta) p;
                                        if (!porta.isPortaFinal() && !porta.isAberto()) {
                                            porta.abrir();
                                            Desenho.acessoATelaDoJogo().setTile(porta.getPosicao().getLinha(),
                                                                                porta.getPosicao().getColuna(),
                                                                                new Modelo.Tile(Desenho.acessoATelaDoJogo().getLevelAtual() + "porta_aberta.png", true));
                                        }
                                    }
                                }
                            } else if (lanternasAcesasAtualmente == 6) {
                                System.out.println("6 Lanternas acesas! Abrindo a porta FINAL!");
                                for (Personagem p : fase) {
                                    if (p instanceof Porta) {
                                        Porta porta = (Porta) p;
                                        if (porta.isPortaFinal() && !porta.isAberto()) {
                                            porta.abrir();
                                            Desenho.acessoATelaDoJogo().setTile(porta.getPosicao().getLinha(),
                                                                                porta.getPosicao().getColuna(),
                                                                                new Modelo.Tile(Desenho.acessoATelaDoJogo().getLevelAtual() + "porta_final_aberta.png", true));
                                        }
                                    }
                                }
                            }
                        }
                        paraRemover.add(powerFire);
                        Power_Fire.resetarFogoAtivo();
                        continue;
                    }

                    // Colisao do Heroi com a Porta Aberta
                    if (hero != null) {
                        if ((pAtual == hero && outroPersonagem instanceof Porta && !((Porta)outroPersonagem).isPortaFinal() && ((Porta)outroPersonagem).isAberto()) ||
                            (outroPersonagem == hero && pAtual instanceof Porta && !((Porta)pAtual).isPortaFinal() && ((Porta)pAtual).isAberto())) {

                            System.out.println("Heroi passou por uma porta normal aberta! Resetando puzzle...");
                            resetarTodasLanternas(fase);
                            continue;
                        }
                        else if ((pAtual == hero && outroPersonagem instanceof Porta && ((Porta)outroPersonagem).isPortaFinal() && ((Porta)outroPersonagem).isAberto()) ||
                                 (outroPersonagem == hero && pAtual instanceof Porta && ((Porta)pAtual).isPortaFinal() && ((Porta)pAtual).isAberto())) {

                            System.out.println("Heroi passou pela porta FINAL aberta! Avancando de nivel...");
                            Desenho.acessoATelaDoJogo().nextLevel();
                            continue;
                        }
                    }

                    // Interassões mais simpels dos poderes
                    if ((pAtual instanceof Power_Water && outroPersonagem instanceof InimigoPatrulha) ||
                        (pAtual instanceof InimigoPatrulha && outroPersonagem instanceof Power_Water)) {

                        Personagem poder = (pAtual instanceof Power_Water) ? pAtual : outroPersonagem;
                        InimigoPatrulha inimigo = (pAtual instanceof InimigoPatrulha) ? (InimigoPatrulha)pAtual : (InimigoPatrulha)outroPersonagem;
                        tela.adicionarPontos(inimigo.getPontosValor()); 
                        paraRemover.add(inimigo);
                        paraRemover.add(poder); 
                        continue;
                    }
                    else if ((pAtual instanceof Power_Water && outroPersonagem instanceof InimigoPerseguidor) ||
                            (pAtual instanceof InimigoPerseguidor && outroPersonagem instanceof Power_Water)) {
                       if (!((pAtual instanceof InimigoPerseguidor && pAtual instanceof InimigoBlindado) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof InimigoBlindado) ||
                             (pAtual instanceof InimigoPerseguidor && pAtual instanceof Boss) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof Boss))) {
                           Personagem poder = (pAtual instanceof Power_Water) ? pAtual : outroPersonagem;
                           InimigoPerseguidor inimigo = (pAtual instanceof InimigoPerseguidor) ? (InimigoPerseguidor)pAtual : (InimigoPerseguidor)outroPersonagem; 
                           tela.adicionarPontos(inimigo.getPontosValor());
                           paraRemover.add(inimigo);
                           paraRemover.add(poder);
                           continue;
                       }
                   }

                    if ((pAtual instanceof Power_Light && outroPersonagem instanceof InimigoPatrulha) ||
                        (pAtual instanceof InimigoPatrulha && outroPersonagem instanceof Power_Light)) {

                        InimigoPatrulha inimigo = (pAtual instanceof InimigoPatrulha) ? (InimigoPatrulha)pAtual : (InimigoPatrulha)outroPersonagem; 
                        Personagem poder = (pAtual instanceof Power_Light) ? pAtual : outroPersonagem;

                        tela.adicionarPontos(inimigo.getPontosValor()); 
                        paraRemover.add(inimigo);
                        paraRemover.add(poder);
                        continue;
                    }
                    else if ((pAtual instanceof Power_Light && outroPersonagem instanceof InimigoPerseguidor) ||
                            (pAtual instanceof InimigoPerseguidor && outroPersonagem instanceof Power_Light)) {
                       if (!((pAtual instanceof InimigoPerseguidor && pAtual instanceof InimigoBlindado) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof InimigoBlindado) ||
                             (pAtual instanceof InimigoPerseguidor && pAtual instanceof Boss) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof Boss))) {
                           InimigoPerseguidor inimigo = (pAtual instanceof InimigoPerseguidor) ? (InimigoPerseguidor)pAtual : (InimigoPerseguidor)outroPersonagem; 
                           Personagem poder = (pAtual instanceof Power_Light) ? pAtual : outroPersonagem;
                           tela.adicionarPontos(inimigo.getPontosValor());
                           paraRemover.add(inimigo);
                           paraRemover.add(poder);
                           continue;
                       }
                   }

                    if ((pAtual instanceof Power_Ice && outroPersonagem instanceof InimigoPatrulha) ||
                        (pAtual instanceof InimigoPatrulha && outroPersonagem instanceof Power_Ice)) {

                        Personagem poder = (pAtual instanceof Power_Ice) ? pAtual : outroPersonagem;
                        InimigoPatrulha inimigo = (pAtual instanceof InimigoPatrulha) ? (InimigoPatrulha)pAtual : (InimigoPatrulha)outroPersonagem; 
                        tela.adicionarPontos(inimigo.getPontosValor()); 
                        paraRemover.add(inimigo);
                        paraRemover.add(poder); 
                        continue;
                    }
                    else if ((pAtual instanceof Power_Ice && outroPersonagem instanceof InimigoPerseguidor) ||
                            (pAtual instanceof InimigoPerseguidor && outroPersonagem instanceof Power_Ice)) {
                       if (!((pAtual instanceof InimigoPerseguidor && pAtual instanceof InimigoBlindado) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof InimigoBlindado) ||
                             (pAtual instanceof InimigoPerseguidor && pAtual instanceof Boss) ||
                             (outroPersonagem instanceof InimigoPerseguidor && outroPersonagem instanceof Boss))) {
                           Personagem poder = (pAtual instanceof Power_Ice) ? pAtual : outroPersonagem;
                           InimigoPerseguidor inimigo = (pAtual instanceof InimigoPerseguidor) ? (InimigoPerseguidor)pAtual : (InimigoPerseguidor)outroPersonagem;
                           tela.adicionarPontos(inimigo.getPontosValor()); 
                           paraRemover.add(inimigo);
                           paraRemover.add(poder); 
                           continue;
                       }
                   }

                    if ((pAtual instanceof Power_Water && outroPersonagem instanceof Lava) ||
                        (pAtual instanceof Lava && outroPersonagem instanceof Power_Water)) {

                        Lava lava = (pAtual instanceof Lava) ? (Lava) pAtual : (Lava) outroPersonagem;

                        if (!paraRemover.contains(lava)) {
                             paraRemover.add(lava);
                        }

                        LavaResfriada lavaResfriada = new LavaResfriada("2mapa.png");
                        lavaResfriada.setPosicao(lava.getPosicao().getLinha(), lava.getPosicao().getColuna());
                        paraAdicionar.add(lavaResfriada);

                        
                    }

                    // Colisao do Heroi com objetos mortais
                    if (hero != null) {
                        if (pAtual == hero && outroPersonagem.isbMortal()) {
                            System.out.println("HEROI MORREU! Tocou em um objeto mortal.");
                            Desenho.acessoATelaDoJogo().resetLevel();
                        } else if (outroPersonagem == hero && pAtual.isbMortal()) {
                            System.out.println("HEROI MORREU! Tocou em um objeto mortal.");
                            Desenho.acessoATelaDoJogo().resetLevel();
                        }
                    }
                }
            }
        }

        fase.removeAll(paraRemover);
        fase.addAll(paraAdicionar);
    }
    public void verificarColisoesComInimigos() {
        Hero heroi = Desenho.acessoATelaDoJogo().getHero(); 
        if (heroi == null || heroi.estaDerrotado()) {
            return; 
        }

        for (int i = 0; i < Desenho.acessoATelaDoJogo().getPersonagens().size(); i++) {
            Personagem p = Desenho.acessoATelaDoJogo().getPersonagens().get(i);

            if (p instanceof InimigoPatrulha || p instanceof InimigoPerseguidor || p instanceof InimigoBlindado || p instanceof Boss) {
                if (heroi.getPosicao().igual(p.getPosicao())) {
                    System.out.println("Herói colidiu com um inimigo! O herói vai morrer.");
                    heroi.receberDano(); 

                    if (heroi.estaDerrotado()) {
                        System.out.println("O Herói foi derrotado! Fim de jogo.");
                    } else {
                        heroi.voltaAUltimaPosicao(); 
                    }
                    break; 
                }
            }
        }
    }
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        for (Personagem pIesimoPersonagem : umaFase) {
            if (!pIesimoPersonagem.isbTransponivel() && pIesimoPersonagem.getPosicao().igual(p)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void desenhaOutrosPersonagens(ArrayList<Personagem> fase) {
        ArrayList<Personagem> copiaFase = new ArrayList<>(fase);
        for (Personagem p : copiaFase) {
            if (!(p instanceof Hero)) {
                p.autoDesenho();
            }
        }
    }
    
    public void resetarTodasLanternas(ArrayList<Personagem> fase) throws IOException {
        for (Personagem p : fase) {
            if (p instanceof Lanterna) {
                ((Lanterna) p).apagar(); 
            } else if (p instanceof Porta) { 
                Porta porta = (Porta) p;
                if (!porta.isPortaFinal() && porta.isAberto()) { 
                    porta.fechar(); 
                    Desenho.acessoATelaDoJogo().setTile(porta.getPosicao().getLinha(),
                                                        porta.getPosicao().getColuna(),
                                                        new Modelo.Tile(Desenho.acessoATelaDoJogo().getLevelAtual() + "cadeira.png", false));
                }
            }
        }
        System.out.println("Todas as lanternas foram apagadas e portas normais fechadas!");
    }

    public Hero getHeroi(List<Personagem> fase) {
        for (Personagem p : fase) {
            if (p instanceof Hero hero) {
                return hero;
            }
        }
        return null;
    }
}