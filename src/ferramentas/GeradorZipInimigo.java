package ferramentas;

import Modelo.InimigoPatrulha;
import Modelo.InimigoPerseguidor;
import Modelo.InimigoBlindado; // Importa a nova classe InimigoBlindado
import Modelo.Personagem;
import Auxiliar.Consts; // Garante que Consts.PATH2 está corretamente configurado
import Modelo.Boss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeradorZipInimigo {
    public static void gerarZipParaInimigo(String nomeArquivoZip, Personagem inimigoParaSerializar, String nomeImagemBasePNG) {
        try {
            
            String arquivoSerTemp = "temp_inimigo.ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivoSerTemp))) {
                oos.writeObject(inimigoParaSerializar);
                System.out.println("Inimigo serializado para: " + arquivoSerTemp);
            }

            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(nomeArquivoZip))) {

                File serFile = new File(arquivoSerTemp);
                if (serFile.exists()) {
                    ZipEntry serEntry = new ZipEntry(serFile.getName());
                    zos.putNextEntry(serEntry);
                    Files.copy(serFile.toPath(), zos);
                    zos.closeEntry();
                    System.out.println("Arquivo .ser adicionado ao ZIP: " + serFile.getName());
                    serFile.delete(); 
                } else {
                    System.err.println("Erro: Arquivo .ser temporário não encontrado.");
                }

                List<String> nomesImagensParaIncluir = new ArrayList<>();
                String baseName = nomeImagemBasePNG.replace(".png", ""); 
                
                if (inimigoParaSerializar instanceof InimigoBlindado) {
                    nomesImagensParaIncluir.add(baseName + "_up.png");
                    nomesImagensParaIncluir.add(baseName + "_down.png");
                    nomesImagensParaIncluir.add(baseName + "_left.png");
                    nomesImagensParaIncluir.add(baseName + "_right.png");

                    String vulnerableBaseName = "notDarkKnight";
                    nomesImagensParaIncluir.add(vulnerableBaseName + "_up.png");
                    nomesImagensParaIncluir.add(vulnerableBaseName + "_down.png");
                    nomesImagensParaIncluir.add(vulnerableBaseName + "_left.png");
                    nomesImagensParaIncluir.add(vulnerableBaseName + "_right.png");
                    System.out.println("Imagens de Inimigo Blindado e vulnerabilidade definidas para inclusão.");
                }
                else if (inimigoParaSerializar instanceof Boss) {
                    nomesImagensParaIncluir.add(baseName + "_up.png");
                    nomesImagensParaIncluir.add(baseName + "_down.png");
                    nomesImagensParaIncluir.add(baseName + "_left.png");
                    nomesImagensParaIncluir.add(baseName + "_right.png");
                    System.out.println("Imagens de Boss definidas para inclusão.");
                }
                // Lógica para Inimigo Patrulha
                else if (inimigoParaSerializar instanceof InimigoPatrulha) {
                    nomesImagensParaIncluir.add(baseName + "_left.png");
                    nomesImagensParaIncluir.add(baseName + "_right.png");
                    System.out.println("Imagens de Inimigo Patrulha definidas para inclusão.");
                }
                else if (inimigoParaSerializar instanceof InimigoPerseguidor) {
                    nomesImagensParaIncluir.add(baseName + "_up.png");
                    nomesImagensParaIncluir.add(baseName + "_down.png");
                    nomesImagensParaIncluir.add(baseName + "_left.png");
                    nomesImagensParaIncluir.add(baseName + "_right.png");
                    System.out.println("Imagens de Inimigo Perseguidor definidas para inclusão.");
                }
                else {
                    nomesImagensParaIncluir.add(nomeImagemBasePNG); 
                    System.out.println("Imagem principal definida para inclusão.");
                }

                for (String nomeImagem : nomesImagensParaIncluir) {
                    String caminhoCompletoImagem = Consts.PATH2 + nomeImagem;
                    File imageFile = new File(caminhoCompletoImagem);
                    if (imageFile.exists()) {
                        ZipEntry imgEntry = new ZipEntry(imageFile.getName());
                        zos.putNextEntry(imgEntry);
                        Files.copy(imageFile.toPath(), zos);
                        zos.closeEntry();
                        System.out.println("Imagem adicionada ao ZIP: " + imageFile.getName());
                    } else {
                        System.err.println("AVISO: Imagem PNG '" + nomeImagem + "' não encontrada em " + caminhoCompletoImagem +
                                           ". Certifique-se de que todas as imagens direcionais estão na pasta 'imgs/'.");
                    }
                }
            }
            System.out.println("Arquivo ZIP criado com sucesso: " + nomeArquivoZip);

        } catch (IOException e) {
            System.err.println("Erro de E/S ao gerar ZIP: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    public static void main(String[] args) {
        InimigoPatrulha inimigoCaveira = new InimigoPatrulha("caveira.png");
        gerarZipParaInimigo("inimigo_caveira.zip", inimigoCaveira, "caveira.png");

        System.out.println("\n--- Gerando ZIP para Inimigo Perseguidor (ex: golen.png) ---");
        InimigoPerseguidor inimigoGolen = new InimigoPerseguidor("golen.png");
        gerarZipParaInimigo("inimigo_golen.zip", inimigoGolen, "golen.png");

        System.out.println("\n--- Gerando ZIP para Inimigo Blindado (ex: ArmoredKnight.png) ---");
        InimigoBlindado inimigoBlindado = new InimigoBlindado("darkNight.png");
        gerarZipParaInimigo("inimigo_darkKnight.zip", inimigoBlindado, "darkNight.png");
        
        System.out.println("\n--- Gerando ZIP para Boss (BossMonster) ---");
        Boss boss = new Boss("melenia.png");
        gerarZipParaInimigo("melenia_boss.zip", boss, "melenia.png");
    }
}
