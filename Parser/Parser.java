import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int posicaoAtual = 0;
    private Token tokenAtual;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokenAtual = tokens.get(0); // Inicia com o primeiro token
    }

    // Método principal: Verifica se o token é o esperado e avança
    private void consumir(TipoToken tipoEsperado) {
        if (tokenAtual.tipo == tipoEsperado) {
            posicaoAtual++;
            if (posicaoAtual < tokens.size()) {
                tokenAtual = tokens.get(posicaoAtual);
            }
        } else {
            // Se o programador digitou errado, o compilador para aqui!
            throw new RuntimeException("Erro Sintático: Esperava " + tipoEsperado + 
                                       ", mas encontrou " + tokenAtual.lexema);
        }
    }
}