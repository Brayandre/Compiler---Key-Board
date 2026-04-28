
public Lexer(String code) {
    tokens = new ArrayList<>();
    this.code = new StringCharacterIterator(code);
    afds = new ArrayList<>();
    
    afds.add(new OpRel());
    afds.add(new OpArit());

}
