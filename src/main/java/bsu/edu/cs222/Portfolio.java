package bsu.edu.cs222.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Portfolio implements Serializable {
    private final Set<String> symbols = new LinkedHashSet<>();
    public boolean add(String sym){ return symbols.add(sym.toUpperCase()); }
    public boolean remove(String sym){ return symbols.remove(sym.toUpperCase()); }
    public Set<String> symbols(){ return symbols; }
}
