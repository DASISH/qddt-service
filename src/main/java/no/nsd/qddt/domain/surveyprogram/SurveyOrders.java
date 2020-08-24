package no.nsd.qddt.domain.surveyprogram;

import java.util.Arrays;
import java.util.List;

public class SurveyOrders {
    private List<SurveyOrder> content;

    public SurveyOrders() {
    }

    public List<SurveyOrder> getContent() {
        return content;
    }

    public void setContent(List<SurveyOrder> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyOrders surveyOrders = (SurveyOrders) o;

        return content != null ? content.equals( surveyOrders.content ) : surveyOrders.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"SurveyOrders\", " +
            "\"content\":" + (content == null ? "null" : Arrays.toString( content.toArray() )) +
            "}";
    }


}
