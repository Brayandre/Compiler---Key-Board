import lexer.PrincipalLexer;
import lexer.Token;
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

        // Analisador lexico
        PrincipalLexer lexer = new PrincipalLexer(conteudo);
        List<Token> tokens = lexer.getTokens();

        // Tradução
        Tradutor tradutor = new Tradutor(tokens);
        String codigoGo = tradutor.traduzir();

        System.out.println(codigoGo);

        // Save com .go apos a tradução
        String saida = args[0].replace(".kb", ".go");
        try (PrintWriter pw = new PrintWriter(saida)) {
            pw.print(codigoGo);
        }
        System.err.println("[OK] Arquivo gerado: " + saida);
    }
}