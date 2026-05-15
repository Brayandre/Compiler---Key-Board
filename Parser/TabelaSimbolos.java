package parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de simbolos com pilha de escopos.
 *
 * Cada escopo e um Map<nome, Simbolo>. A pilha cresce ao entrar em
 * { ... } de cmdSe / cmdWhile / cmdFor e encolhe ao sair.
 *
 * - declarar(...) insere no topo. Falha se ja existir no MESMO escopo.
 * - buscar(nome)  procura do topo ate a base (escopo mais externo).
 */
public class TabelaSimbolos {

    private final Deque<Map<String, Simbolo>> escopos = new ArrayDeque<>();

    public TabelaSimbolos() {
        // escopo global (nivel 0) -- corresponde ao corpo do main da .kb
        escopos.push(new HashMap<>());
    }

    /** Entra em um novo bloco/escopo aninhado. */
    public void abrirEscopo() {
        escopos.push(new HashMap<>());
    }

    /** Sai do bloco atual. Nao remove o escopo global. */
    public void fecharEscopo() {
        if (escopos.size() > 1) {
            escopos.pop();
        }
    }

    /** Nivel de aninhamento atual (0 = global). */
    public int nivelAtual() {
        return escopos.size() - 1;
    }

    /**
     * Tenta inserir um simbolo no escopo corrente.
     * Retorna true se inseriu, false se ja existia (redeclaracao).
     */
    public boolean declarar(Simbolo s) {
        Map<String, Simbolo> topo = escopos.peek();
        if (topo.containsKey(s.getNome())) {
            return false;
        }
        topo.put(s.getNome(), s);
        return true;
    }

    /** Busca o simbolo do escopo mais interno para o mais externo. Null se nao existir. */
    public Simbolo buscar(String nome) {
        for (Map<String, Simbolo> escopo : escopos) {
            Simbolo s = escopo.get(nome);
            if (s != null) return s;
        }
        return null;
    }

    /** Procura apenas no escopo corrente (uso para checar redeclaracao). */
    public Simbolo buscarLocal(String nome) {
        return escopos.peek().get(nome);
    }

    /** Itera apenas o escopo global (para gerar warnings de variaveis nao usadas). */
    public Iterable<Simbolo> escopoGlobal() {
        // o ultimo da pilha (base) e o global
        Map<String, Simbolo> global = null;
        for (Map<String, Simbolo> e : escopos) global = e;
        return global == null ? java.util.Collections.emptyList() : global.values();
    }
}
