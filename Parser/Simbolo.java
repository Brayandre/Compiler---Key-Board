package parser;

/**
 * Representa um identificador declarado pela linguagem .kb.
 *
 * - nome:         lexema do ID (ex.: "x", "contador").
 * - tipo:         tipo semantico (INT, FLOAT, STRING, BOOL).
 * - escopo:       nivel de aninhamento onde foi declarado (0 = global do main).
 * - inicializado: true se ja recebeu valor (SETCAPS, insert(), atribuicao via SET, ou variavel do for).
 * - usado:        marca de uso para detectar variaveis declaradas e nunca lidas (warning).
 * - posDecl:      posicao do token no fluxo, util em mensagens de erro.
 */
public class Simbolo {
    private final String nome;
    private final Tipo tipo;
    private final int escopo;
    private final int posDecl;
    private boolean inicializado;
    private boolean usado;

    public Simbolo(String nome, Tipo tipo, int escopo, int posDecl, boolean inicializado) {
        this.nome = nome;
        this.tipo = tipo;
        this.escopo = escopo;
        this.posDecl = posDecl;
        this.inicializado = inicializado;
        this.usado = false;
    }

    public String getNome()          { return nome; }
    public Tipo   getTipo()           { return tipo; }
    public int    getEscopo()         { return escopo; }
    public int    getPosDecl()        { return posDecl; }
    public boolean isInicializado()   { return inicializado; }
    public boolean isUsado()          { return usado; }

    public void marcarInicializado()  { this.inicializado = true; }
    public void marcarUsado()         { this.usado = true; }

    @Override
    public String toString() {
        return String.format("Simbolo{%s : %s, escopo=%d, init=%s}",
                             nome, tipo, escopo, inicializado);
    }
}
