package no.nsd.qddt.domain.controlconstruct.pojo;

/**
 * @author Stig Norland
 */
public enum ConditionKind {
    COMPUTATION_ITEM("ComputationItem","JavaScript"),
    IF_THEN_ELSE("IfThenElse","If Then Else"),
    LOOP("Loop","Loop (exactly the same as RepeatUntil)"),
    REPEAT_UNTIL("RepeatUntil","Repeat Until (exactly the same as Loop)"),
    REPEAT_WHILE("RepeatWhile","Repeat While (you could just as well use Loop)");

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
