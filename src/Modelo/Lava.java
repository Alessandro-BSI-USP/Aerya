package Modelo;

import java.io.Serializable;

public class Lava extends Personagem implements Serializable {

    public Lava(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = true;
    }

    
    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}