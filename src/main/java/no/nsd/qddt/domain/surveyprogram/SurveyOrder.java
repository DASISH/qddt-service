package no.nsd.qddt.domain.surveyprogram;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public class SurveyOrder {
    public UUID uuid;
    public Long index;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public SurveyOrder() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyOrder that = (SurveyOrder) o;

        if (uuid != null ? !uuid.equals( that.uuid ) : that.uuid != null) return false;
        return index != null ? index.equals( that.index ) : that.index == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (index != null ? index.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"SurveyOrder\", " +
            "\"id\":" + (uuid == null ? "null" : uuid) + ", " +
            "\"index\":" + (index == null ? "null" : "\"" + index + "\"") +
            "}";
    }


}

