package View;

import Controler.Tela;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Auxiliar.Consts;

import javax.swing.ImageIcon; 
import java.awt.Graphics;     
import java.io.IOException;    
import Auxiliar.Desenho;       
import java.awt.FlowLayout;
import javax.swing.Box;

public class TelaIntroducao extends JFrame implements ActionListener {

    private JButton btnAvancar;
    private JPanel painelHistoriaControles;
    private int estado = 0; 

    private String[] historia = {
        "Em um mundo onde a antiga magia elemental fora extinta e esquecida,",
        " o nascimento de uma criança muda o curso da história.",
        "Aerya, uma jovem com o raro dom dos elementos,",
        "cresce confinada nas muralhas do castelo do reino de Dykevar, ",
        "uma nação dilacerada pela guerra.",
        "",
        "Criada como uma arma viva pela rainha Velmira, Aerya aprendeu apenas a ",
        "conjurar destruição. Mas, conforme seu poder cresce, sua magia começa a corromper, ",
        "ficando mais sombria, reflexo da própria prisão em que ela vive.",
        "Quando finalmente se rebela, Velmira antecipa a traição e esconde o poderoso grimório",
        "de Aerya nas profundezas do castelo amaldiçoado.",
        "",
        "Agora, enfraquecida Aerya embarca em uma ",
        "missão desesperada para recuperar seu poder.",
        "Cada sala, cada corredor e cada inimigo do castelo são preparos",
        "para o grande combate final com sua longa inimiga."
        
    };

    private ImageIcon fundoMagico;

    public TelaIntroducao() {
        setTitle("Aerya, a maga da Destruição");
        setSize(Consts.RES * Consts.CELL_SIDE, Consts.RES * Consts.CELL_SIDE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            fundoMagico = Desenho.carregarImagem("background_magic.png");
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagem de fundo: " + ex.getMessage());
            
        }
        
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fundoMagico != null) {
                    g.drawImage(fundoMagico.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 20, 50));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        painelPrincipal.setLayout(new BorderLayout());

        JPanel painelTitulo = new JPanel();
        painelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Aerya, a maga da Destruição", JLabel.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        lblTitulo.setForeground(new Color(255, 223, 0)); 
        painelTitulo.add(lblTitulo);
        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);

        painelHistoriaControles = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150)); 
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g); 
            }
        };
        painelHistoriaControles.setOpaque(false); 
        painelHistoriaControles.setLayout(new GridLayout(0, 1, 10, 10));
        
        atualizarPainel(); 
        
        JPanel painelCentralWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentralWrapper.setOpaque(false);
        painelCentralWrapper.add(painelHistoriaControles);
        painelPrincipal.add(painelCentralWrapper, BorderLayout.CENTER);


        
        JPanel painelInferior = new JPanel();
        painelInferior.setOpaque(false);
        btnAvancar = new JButton("Avançar");
        btnAvancar.setFont(new Font("Serif", Font.BOLD, 22));
        btnAvancar.setBackground(new Color(100, 50, 150)); 
        btnAvancar.setForeground(Color.WHITE);
        btnAvancar.setFocusPainted(false); 
        btnAvancar.addActionListener(this);
        painelInferior.add(btnAvancar);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    private void atualizarPainel() {
        painelHistoriaControles.removeAll(); 

        if (estado == 0) { 
            for (String linha : historia) {
                adicionarLabel(painelHistoriaControles, linha, new Color(220, 220, 255)); 
            }
        } else if (estado == 1) { 
            painelHistoriaControles.add(Box.createVerticalStrut(20));
            adicionarLabel(painelHistoriaControles, "COMANDOS DE JOGO:", new Color(255, 255, 100));
            painelHistoriaControles.add(Box.createVerticalStrut(15));

            adicionarLabel(painelHistoriaControles, "Movimentar CIMA: W", new Color(180, 255, 180));
            adicionarLabel(painelHistoriaControles, "Movimentar BAIXO: S", new Color(180, 255, 180));
            adicionarLabel(painelHistoriaControles, "Movimentar ESQUERDA: A", new Color(180, 255, 180));
            adicionarLabel(painelHistoriaControles, "Movimentar DIREITA: D", new Color(180, 255, 180));
            painelHistoriaControles.add(Box.createVerticalStrut(10));

            adicionarLabel(painelHistoriaControles, "Usar Gelo: Tecla I", new Color(150, 200, 255));
            adicionarLabel(painelHistoriaControles, "Usar Fogo: Tecla J", new Color(255, 180, 150));
            adicionarLabel(painelHistoriaControles, "Usar Água: Tecla K", new Color(150, 255, 255));
            adicionarLabel(painelHistoriaControles, "Usar Luz: Tecla L", new Color(255, 255, 150));
            painelHistoriaControles.add(Box.createVerticalStrut(10)); 

            adicionarLabel(painelHistoriaControles, "Salvar Jogo: Tecla 'P'", new Color(200, 200, 255)); 
            adicionarLabel(painelHistoriaControles, "Carregar Jogo: Tecla 'O'", new Color(200, 200, 255));
            adicionarLabel(painelHistoriaControles, "Próxima Fase: Tecla 'N'", new Color(200, 200, 255));
            adicionarLabel(painelHistoriaControles, "Reiniciar Fase: Tecla 'R'", new Color(200, 200, 255));
            painelHistoriaControles.add(Box.createVerticalStrut(20)); 

            adicionarLabel(painelHistoriaControles, "Objetivo: Encontre o Livro Ancestral!", Color.YELLOW);
            painelHistoriaControles.add(Box.createVerticalStrut(20));
        }

        painelHistoriaControles.revalidate();
        painelHistoriaControles.repaint();
    }

    private void adicionarLabel(JPanel painel, String texto, Color cor) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(cor);
        painel.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAvancar) {
            if (estado == 0) {
                estado = 1; 
                atualizarPainel();
                btnAvancar.setText("Começar Jornada"); 
            } else if (estado == 1) {
                this.dispose(); 
                Tela telaDeJogo = new Tela();
                telaDeJogo.setVisible(true);
                telaDeJogo.go();
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaIntroducao();
            }
        });
    }
}