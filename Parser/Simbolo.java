package parser;
public class Simbolo {
    private final String nome;
    private final Tipo tipo;
    private final int escopo;
    private final int posDecl;
    private boolean inicializado;
    private boolean usado;

    // instanciando atributos de encapsulamento
    public Simbolo(String nome, Tipo tipo, int escopo, int posDecl, boolean inicializado) {
        this.nome = nome;
        this.tipo = tipo;
        this.escopo = escopo;
        this.posDecl = posDecl;
        this.inicializado = inicializado;
        this.usado = false;
    }

    //consult de regras
    public String getNome(){ return nome; }
    public Tipo getTipo(){ return tipo; }
    public int getEscopo(){ return escopo; }
    public int getPosDecl(){ return posDecl; }
    public boolean isInicializado(){ return inicializado; }
    public boolean isUsado(){ return usado; }

    // ancores de atualização, para quando houver leitura
    public void marcarInicializado(){ this.inicializado = true; }
    public void marcarUsado(){ this.usado = true; }

    @Override
    public String toString() {
        return String.format("Simbolo{%s : %s, escopo=%d, init=%s}",
                             nome, tipo, escopo, inicializado);
    }
}
