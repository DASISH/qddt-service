package no.nsd.qddt.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author Stig Norland
 */
@Embeddable
public class Version implements Comparable<Version> {

    private static final String versionFormat = "%1$s.%2$s %3$s";
    private Integer major=1;
    private Integer minor=0;
    private String versionLabel="";

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @Column(name ="rev")
    private Integer revision;

    @JsonIgnore
    @Transient
    private boolean isModified = false;

    @JsonIgnore
    @Transient
    private boolean isNew = false;

    public Version() {   }

    public Version(Integer major, Integer minor, Integer revision, String label) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.versionLabel = label;
    }

    @SuppressWarnings("SameParameterValue")
    public Version(boolean isNew) {
        this.isNew = isNew;
    }


    public Integer getMajor() {
        return major;
    }


    public void incMajor() {
        if (! isModified) {
            this.major++;
            this.minor = 0;
            isModified = true;
        }
    }

    public Integer getMinor() {
        return minor;
    }


    public void incMinor() {
        if (!isModified) {
            this.minor++;
            isModified = true;
        }
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    @JsonIgnore
    public boolean isNew() {
        return isNew;
    }

    @JsonIgnore
    public boolean isModified() {
        return isModified;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;

        Version version = (Version) o;

        if (major != null ? !major.equals(version.major) : version.major != null) return false;
        if (minor != null ? !minor.equals(version.minor) : version.minor != null) return false;
        return versionLabel != null ? versionLabel.equals(version.versionLabel) : version.versionLabel == null;

    }

    @Override
    public int hashCode() {
        int result = major != null ? major.hashCode() : 0;
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (versionLabel != null ? versionLabel.hashCode() : 0);
        return result;
    }

    @Override
    public String
    toString() {
        return  String.format(versionFormat, major, minor, versionLabel).trim();
    }

    @Override
    public int compareTo(Version o) {

        return this.getMajor().compareTo(o.getMajor()) + this.getMinor().compareTo(o.getMinor());

    }

    public String toDDIXml() {
        return "<Version>" + toString() +"</Version>";
    }
}
