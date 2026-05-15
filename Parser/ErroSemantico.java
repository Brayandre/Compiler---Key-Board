package parser;

/**
 * Erro semantico coletado pelo AnalisadorSemantico.
 *
 * Diferente dos erros sintaticos (que sao RuntimeException disparados na hora),
 * os erros semanticos sao acumulados em uma lista para que o usuario veja
 * TODAS as falhas de uma execucao, e nao apenas a primeira.
 *
 * Para falhas catastroficas (ex.: programa sem computador_iniciar), o
 * analisador pode lancar uma RuntimeException convencional.
 */
public class ErroSemantico {

    /** Categoria do erro. Util para filtragem / testes. */
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

    public ErroSemantico(Categoria categoria, String mensagem, int posicao) {
        this.categoria = categoria;
        this.mensagem = mensagem;
        this.posicao = posicao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getPosicao() {
        return posicao;
    }

    @Override
    public String toString() {
        return "[ERRO SEMANTICO] " + categoria + " (pos " + posicao + "): " + mensagem;
    }
}
