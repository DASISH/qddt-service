package no.nsd.qddt.domain.controlconstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructSequenceJson extends ConstructJson {

    private List<ConstructJson> children = new ArrayList<>();

    private String indexRationale;

    private String label;

    private String description;

    ConstructSequenceJson(ControlConstruct construct) {
        super(construct);
        indexRationale = construct.getIndexRationale();
        label = construct.getLabel();
        description = construct.getDescription();
        children = construct.getChildren().stream().map(cc->{
            switch (cc.getControlConstructKind()) {
                case QUESTION_CONSTRUCT:
                    return new ConstructQuestionJson(cc);
                case STATEMENT_CONSTRUCT:
                    return new ConstructConditionJson(cc);
                case CONDITION_CONSTRUCT:
                    return new ConstructConditionJson(cc);
                case SEQUENCE_CONSTRUCT:
                    return new ConstructSequenceJson(cc);
                default:
                    return new ConstructJson(cc);
            }
        }).collect(Collectors.toList());
    }

    public List<ConstructJson> getChildren() {
        return children;
    }

    public String getIndexRationale() {
        return indexRationale;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
