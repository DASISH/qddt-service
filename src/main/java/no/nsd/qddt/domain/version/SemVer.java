package no.nsd.qddt.domain.version;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * http://semver.org/
 * MAJOR version when you make incompatible API changes,
 * MINOR version when you add functionality in a backwards-compatible manner, and
 * PATCH version when you make backwards-compatible bug fixes.
 * Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format
 *
 * @author Stig Norland
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SemVer {
    //    implements UserType {

//    @Override
//    public int[] sqlTypes() {
//        return new int[Types.NVARCHAR];
//    }
//
//    @Override
//    public Class returnedClass() {
//        return versionString.getClass();
//    }
//
//    @Override
//    public boolean equals(Object x, Object y) throws HibernateException {
//        return ObjectUtils.equals(x,y);
//    }
//
//    @Override
//    public int hashCode(Object x) throws HibernateException {
//        assert (x != null);
//        return x.hashCode();
//    }
//
//    @Override
//    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
//        String value = (String) StringType..nullSafeGet(rs, names[0]);
//        return ((value != null) ? new StringBuilder(value) : null);
//    }
//
//    @Override
//    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
//        String
//    }
//
//    @Override
//    public Object deepCopy(Object value) throws HibernateException {
//        return value;
//    }
//
//    @Override
//    public boolean isMutable() {
//        return true;
//    }
//
//    @Override
//    public Serializable disassemble(Object value) throws HibernateException {
//        return (Serializable)value;
//    }
//
//    @Override
//    public Object assemble(Serializable cached, Object owner) throws HibernateException {
//        return cached.);
//    }
//
//    @Override
//    public Object replace(Object original, Object target, Object owner) throws HibernateException {
//        return original;
//    }

    private enum VPos{
        major,
        minor,
        patch,
        label
    }

    private static final String searchPattern = "\\.|\\s";
    private static final String versionFormat = "%1$s.%2$s.%3$s %4$s";

    public SemVer(){
        versionString = "0.0.0 ";
    }

    public SemVer(String versionString) {
        this.versionString = versionString;
    }

    private String versionString;


    public void incMajor() {
        String[] version = versionString.split(searchPattern);
        Long inc = Long.decode(version[VPos.major.ordinal()]);
        version[VPos.major.ordinal()] = (++inc).toString();
        if (version.length == 4)
            versionString = String.format(versionFormat, version[0],version[1],version[2],version[3]);
        else
            versionString = String.format(versionFormat, version[0],version[1],version[2],"");

    }


    public void setMinor() {
        String[] version = versionString.split(searchPattern);
        Long inc = Long.decode(version[VPos.minor.ordinal()]);
        version[VPos.minor.ordinal()] = (++inc).toString();
        if (version.length == 4)
            versionString = String.format(versionFormat, version[0],version[1],version[2],version[3]);
        else
            versionString = String.format(versionFormat, version[0],version[1],version[2],"");
    }


    public void incPatch() {
        String[] version = versionString.split(searchPattern);
        Long inc = Long.decode(version[VPos.patch.ordinal()]);
        version[VPos.patch.ordinal()] = (++inc).toString();
        if (version.length == 4)
            versionString = String.format(versionFormat, version[0],version[1],version[2],version[3]);
        else
            versionString = String.format(versionFormat, version[0],version[1],version[2],"");
    }


    public void setVersionLabel(String versionLabel) {
        String[] version = versionString.split(searchPattern);
        Long inc = Long.decode(version[VPos.patch.ordinal()]);
        version[VPos.patch.ordinal()] = (++inc).toString();
        versionString = String.format(versionFormat, version[0],version[1],version[2],versionLabel);
    }

    @Override
    public String toString() {
        return  versionString;
    }
}
