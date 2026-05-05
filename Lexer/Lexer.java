// O arquivo atual tem só o construtor. Adiciona os autômatos que faltam no construtor e o corpo completo da classe
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Token> tokens;
    private CharacterIterator code;
    private List<AFD> afds;

    public Lexer(String source) {
        tokens = new ArrayList<>();
        this.code = new StringCharacterIterator(source);
        afds = new ArrayList<>();

        // ORDEM IMPORTA: Arrow antes de OpArit (ambos começam com -)
        afds.add(new ArrowAFD());
        afds.add(new OpRel());
        afds.add(new OpArit());
        afds.add(new IdAFD());
        afds.add(new NumAFD());
        afds.add(new TextoAFD());
        afds.add(new Lexer());
    }

    private void skipWhitespace() {
        while (code.current() == ' '  ||
               code.current() == '\t' ||
               code.current() == '\r' ||
               code.current() == '\n') {
            code.next();
        }
    }

    private Token searchNextToken() {
        for (AFD afd : afds) {
            Token t = afd.evaluate(code);
            if (t != null) return t;
        }
        return null;
    }

    public List<Token> getTokens() {
        while (code.current() != CharacterIterator.DONE) {
            skipWhitespace();

            if (code.current() == CharacterIterator.DONE) break;

            Token t = searchNextToken();

            if (t == null) {
                throw new RuntimeException(
                    "[ERRO LÉXICO] Símbolo não reconhecido: '" + code.current() + "'"
                );
            }

            tokens.add(t);
        }

        tokens.add(new Token(TipoToken.EOF, "EOF"));
        return tokens;
    }
}