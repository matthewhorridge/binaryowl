package org.semanticweb.binaryowl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/10/2012
 */
public final class BinaryOWLVersion {

    private final int version;

    private static BinaryOWLVersion VERSION_1 = new BinaryOWLVersion(1);

    private static BinaryOWLVersion VERSION_2 = new BinaryOWLVersion(2);

    private BinaryOWLVersion(int version) {
        this.version = version;
    }

    public static BinaryOWLVersion getVersion(int version) {
        if(version == 1) {
            return VERSION_1;
        }
        else if(version == 2) {
            return VERSION_2;
        }
        else {
            return new BinaryOWLVersion(version);
        }
    }

    public int getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return "BinaryOWLVersion".hashCode() + version;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof BinaryOWLVersion)) {
            return false;
        }
        BinaryOWLVersion other = (BinaryOWLVersion) obj;
        return this.version == other.version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BinaryOWLVersion");
        sb.append("(");
        sb.append(version);
        sb.append(")");
        return sb.toString();
    }
    
    public String toHexString() {
        return Integer.toHexString(version);
    }
}
