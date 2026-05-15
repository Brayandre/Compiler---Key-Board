import lexer.PrincipalLexer;
import lexer.Token;
import parser.AnalisadorSemantico;
import parser.ErroSemantico;
import parser.Tradutor;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java Main <arquivo.kb>");
            return;
        }

        String conteudo = new String(new FileInputStream(args[0]).readAllBytes());

        PrincipalLexer lexer = new PrincipalLexer(conteudo);
        List<Token> tokens = lexer.getTokens();
        AnalisadorSemantico sem = new AnalisadorSemantico(tokens);
        List<ErroSemantico> erros = sem.analisar();
        List<ErroSemantico> warns = sem.getWarnings();

        //verificação de erro semantico
        for (ErroSemantico w : warns) {
            System.out.println("[WARN] " + w.getCategoria() + ": " + w.getMensagem());
        }
        if (!erros.isEmpty()) {
            System.err.println("Encontrados " + erros.size() + " erro(s) semantico(s):");
            for (ErroSemantico e : erros) {
                System.err.println("  - " + e);
            }
            System.err.println("[ABORTADO] Traducao nao sera executada.");
            return;
        }

        //traducao para go
        Tradutor tradutor = new Tradutor(tokens);
        String codigoGo = tradutor.traduzir();
        System.out.println(codigoGo);

        // 4) salvando arquivo go
        String saida = args[0].replace(".kb", ".go");
        try (PrintWriter pw = new PrintWriter(saida)) {
            pw.print(codigoGo);
        }
        System.err.println("[OK] Arquivo gerado: " + saida);
    }
}
