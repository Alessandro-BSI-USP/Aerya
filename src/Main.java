import View.TelaIntroducao; // Importe a nova classe

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaIntroducao().setVisible(true); // Agora inicia a tela de introdução
            }
        });
    }
}
