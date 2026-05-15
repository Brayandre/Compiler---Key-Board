package parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolos {

    // resgata os escopos gerados
    private final Deque<Map<String, Simbolo>> escopos = new ArrayDeque<>();

    public TabelaSimbolos() {
        // escopo global
        escopos.push(new HashMap<>());
    }

    // chamada de escopo por bloco
    public void abrirEscopo() {

        escopos.push(new HashMap<>());
    }

    // return de escopo por bloco
    public void fecharEscopo() {
        if (escopos.size() > 1) {
            escopos.pop();
        }
    }

    public int nivelAtual() {
        return escopos.size() - 1;
    }

    // busca add um simbolo, n poder haver mais q 1, senao gera o erro semantico
    public boolean declarar(Simbolo s) {
        Map<String, Simbolo> topo = escopos.peek();
        if (topo.containsKey(s.getNome())) {
            return false;
        }
        topo.put(s.getNome(), s);
        return true;
    }

    public Simbolo buscar(String nome) {
        for (Map<String, Simbolo> escopo : escopos) {
            Simbolo s = escopo.get(nome);
            if (s != null)
                return s;
        }
        return null;
    }

    // Procura a variável no bloco
    public Simbolo buscarLocal(String nome) {
        return escopos.peek().get(nome);
    }

    // lista todos os simbolos
    public Iterable<Simbolo> escopoGlobal() {
        // o ultimo da pilha (base) e o global
        Map<String, Simbolo> global = null;
        for (Map<String, Simbolo> e : escopos)
            global = e;
        return global == null ? java.util.Collections.emptyList() : global.values();
    }
}
