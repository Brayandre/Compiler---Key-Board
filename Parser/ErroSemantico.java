package parser;

public class ErroSemantico {

    // categoria de obrigatoriedade de cada tipo
    public enum Categoria {
        DECLARACAO_DUPLICADA,
        ID_NAO_DECLARADO,
        TIPO_INCOMPATIVEL,
        OPERACAO_INVALIDA,
        USO_ANTES_DE_INICIALIZAR,
        VARIAVEL_DECLARADA_NAO_USADA,
        OUTRO
    }

    private final Categoria categoria;
    private final String mensagem;
    private final int posicao;

    //instancia variaveis
    public ErroSemantico(Categoria categoria, String mensagem, int posicao) {
        this.categoria = categoria;
        this.mensagem  = mensagem;
        this.posicao   = posicao;
    }

    //return para debug
    public Categoria getCategoria() { return categoria; }
    public String getMensagem() { return mensagem; }
    public int getPosicao() { return posicao; }

    @Override
    public String toString() {
        return "ERRO SEMANTICO :" + categoria + " (pos " + posicao + "): " + mensagem;
    }
}
