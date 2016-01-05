package no.nsd.qddt.domain.version;

/**
 * http://semver.org/
 * MAJOR version when you make incompatible API changes,
 * MINOR version when you add functionality in a backwards-compatible manner, and
 * PATCH version when you make backwards-compatible bug fixes.
 * Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format
 *
 * @author Stig Norland
 */


public class SemVer{
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


    private String versionString;

    public int getMajor() {
        return Integer.getInteger(versionString.split(".")[VPos.major.ordinal()]);
    }

    public void setMajor(int major) {
        String[] version = versionString.split(".");
        versionString = String.format("{0}.{1}.{2}{4}",major,version[1],version[2],version[3].isEmpty()?"":"."+version[3]);
    }

    public int getMinor() {
        return Integer.getInteger(versionString.split(".")[VPos.minor.ordinal()]);
    }

    public void setMinor(int minor) {
        String[] version = versionString.split(".");
        versionString = String.format("{0}.{1}.{2}{4}",version[0],minor,version[2],version[3].isEmpty()?"":"."+version[3]);
    }

    public int getPatch() {
        return Integer.getInteger(versionString.split(".")[VPos.patch.ordinal()]);
    }

    public void setPatch(int patch) {
        String[] version = versionString.split(".");
        versionString = String.format("{0}.{1}.{2}{4}",version[0],version[1],patch,version[3].isEmpty()?"":"."+version[3]);
    }

    public String getVersionLabel() {
        return versionString.split(".")[VPos.patch.ordinal()];

    }

    public void setVersionLabel(String versionLabel) {
        String[] version = versionString.split(".");
        versionString = String.format("{0}.{1}.{2}{4}",version[0],version[1],version[2],versionLabel.isEmpty()?"":"."+versionLabel);
    }

    @Override
    public String toString() {
        return  "v" + versionString;
    }
}
