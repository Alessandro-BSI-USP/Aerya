package Modelo;

import java.io.Serializable;

public class Livro extends Personagem implements Serializable {
    private static final long serialVersionUID = 1L;

    public Livro(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = false;     
    }

}