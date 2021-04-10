package bo.game;

public enum NaziMember {
    HILTER("Hitler"),
    BORMANN("Bormann"),
    GOEBBELS("Goebbels"),
    GOERING("Goering"),
    HESS("Hess"),
    HIMMLER("Himmler")
    ;

    private String name;
    NaziMember(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
