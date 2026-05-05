import java.io.*;
import java.util.List;

public class init {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java init <arquivo.kb>");
            return;
        }

        String conteudo = new String(new FileInputStream(args[0]).readAllBytes());

        Lexer lexer = new Lexer(conteudo);

        try {
            List<Token> tokens = lexer.getTokens();
            System.out.println("=== Tokens reconhecidos ===");
            for (Token t : tokens) {
                System.out.println(t);
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}