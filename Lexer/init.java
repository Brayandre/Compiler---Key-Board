package lexer;

import java.io.*;
import java.util.List;

public class Init {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java init <arquivo.kb>");
            return;
        }

        // pega o conteudo do arquivo.kb

        String conteudo = new String(new FileInputStream(args[0]).readAllBytes());

        PrincipalLexer lexer = new PrincipalLexer(conteudo);

        try {
            List<Token> tokens = lexer.getTokens();
            System.out.println(" Tokens reconhecidos ")
            for (Token t : tokens) {
                System.out.println(t);
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}