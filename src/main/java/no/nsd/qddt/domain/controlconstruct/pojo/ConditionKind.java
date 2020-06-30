package no.nsd.qddt.domain.controlconstruct.pojo;

/**
 * @author Stig Norland
 */
public enum ConditionKind {
    COMPUTATION_ITEM("ComputationItem","JavaScript"),
    IF_THEN_ELSE("IfThenElse","If Then Else"),
//    LOOP("ForI","For i = X do X += STEP while i <= X "),
    LOOP("ForEach","For each SOURCES do SEQUENCE"),
    REPEAT_UNTIL("RepeatUntil","Repeat SEQUENCE Until CONDITION"),
    REPEAT_WHILE("RepeatWhile","Repeat SEQUENCE While CONDITION");

    ConditionKind(String name, String description){
        this.name = name;
        this.description = description;
    }

    private final String name;

    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ConditionKind getEnum(String name) {
        if(name == null)
            throw new IllegalArgumentException();
        for(ConditionKind v : values())
            if(name.equalsIgnoreCase(v.getName())) return v;
        throw new IllegalArgumentException();
    }
}
