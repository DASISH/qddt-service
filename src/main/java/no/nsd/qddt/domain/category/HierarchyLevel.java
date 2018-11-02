package no.nsd.qddt.domain.category;

import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;

/**
 * @author Stig Norland
 */
public enum HierarchyLevel {
    ENTITY,
    GROUP_ENTITY;

    public static HierarchyLevel getEnum(String name) {
        if(IsNullOrTrimEmpty(name))
            return null;
//            throw new IllegalArgumentException("Enum cannot be null");
        for(HierarchyLevel v : values())
            if(name.equalsIgnoreCase(v.name())) return v;
        throw new IllegalArgumentException("Enum value not valid " + name);
    }

    }
