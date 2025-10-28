package View;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.io.IOException;
import Auxiliar.Desenho;
import java.awt.FlowLayout; 

public class TelaVitoria extends JFrame {

    private ImageIcon fundoVitoria; 
  
    public TelaVitoria(int pontuacaoFinal) {
        setTitle("Vitória!");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            fundoVitoria = Desenho.carregarImagem("Aerya_a_maga.png");
        } catch (IOException ex) {
            System.err.println("Erro ao carregar imagem de fundo da vitória: " + ex.getMessage());
        }

        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fundoVitoria != null) {
                    g.drawImage(fundoVitoria.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(30, 30, 70));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        painelPrincipal.setLayout(new BorderLayout());

        if (pontuacaoFinal == 0) {
            JPanel painelConquista = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            painelConquista.setOpaque(false); 
            JLabel lblConquista = new JLabel("Conquista: Tá com medo de Bater?");
            lblConquista.setFont(new Font("Arial", Font.ITALIC, 14)); 
            lblConquista.setForeground(Color.RED);
            painelConquista.add(lblConquista);
            painelPrincipal.add(painelConquista, BorderLayout.NORTH);
        }

        JPanel painelConteudo = new JPanel();
        painelConteudo.setOpaque(false);
        painelConteudo.setLayout(new GridLayout(0, 1, 10, 10)); 

        
        painelConteudo.add(Box.createVerticalStrut(150));

        JLabel mensagemVitoria = new JLabel(
            "<html><center>AERYA RECUPEROU SEU PODER,<br>E PODE LUTAR CONTRA AS GARRAS DE VELMIRA!</center></html>",
            JLabel.CENTER
        );
        mensagemVitoria.setFont(new Font("Arial", Font.BOLD, 22));
        mensagemVitoria.setForeground(Color.YELLOW);
        painelConteudo.add(mensagemVitoria);

        painelConteudo.add(Box.createVerticalStrut(50));

        JLabel lblPontuacao = new JLabel("PONTUAÇÃO FINAL: " + pontuacaoFinal + " PONTOS", JLabel.CENTER);
        lblPontuacao.setFont(new Font("Arial", Font.BOLD, 40));
        lblPontuacao.setForeground(Color.WHITE);
        painelConteudo.add(lblPontuacao);

        
        painelConteudo.add(Box.createVerticalGlue()); 

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        add(painelPrincipal);

        setVisible(true);
    }

}