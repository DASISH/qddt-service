package no.nsd.qddt.domain;

/**
 * ChangeKinds are the different ways an entity can be modified by the system/user.
 * First entry will always be CREATED.
 * NEW_REVISION used for taging a version as a release.
 * TYPO, can be used modify without breaking a release.
 * NEW_COPY_OF, used when someone reuses an existing Responsedomain, but want to modify it.
 * Every other version is a IN_DEVELOPMENT change.
 *
 * @author Stig Norland
 */
public enum ChangeKind {
    CREATED,
    NEW_REVISION,
    TYPO,
    NEW_COPY_OF,
    IN_DEVELOPMENT
}
