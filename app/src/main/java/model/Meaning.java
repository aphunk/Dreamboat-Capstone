package model;

public class Meaning {
    private String term;
    private String interpretation;

    public Meaning() {}

    public Meaning(String term, String meaning) {
        this.term = term;
        this.interpretation = interpretation;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public void setMeaning(String meaning) {
        this.interpretation = interpretation;
    }
}
