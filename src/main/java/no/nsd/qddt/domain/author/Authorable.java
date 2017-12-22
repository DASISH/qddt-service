package no.nsd.qddt.domain.author;

import java.util.Set;

/**
 * @author Stig Norland
 */
public interface Authorable {
    /**
     * Add a {@link Author} to a {@link Set} of authors.
     * @param user added author.
     */
    void addAuthor(Author user);

    /**
     * Get all authors attached to this entity as a {@link Set}
     */
    Set<Author> getAuthors();

    /**
     * Set the {@link Set} of {@link Author} for the entity.
     * @param authors populated set of authors.
     */
    void setAuthors(Set<Author> authors);
}
