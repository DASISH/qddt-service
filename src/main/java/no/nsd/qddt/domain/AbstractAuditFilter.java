package no.nsd.qddt.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Stig Norland
 */
public abstract class AbstractAuditFilter<N extends Number & Comparable<N>, T extends AbstractEntityAudit> {

    protected abstract Revision<N, T> postLoadProcessing(Revision<N, T> instance);

    protected Page<Revision<N, T>> getPageIncLatest(Revisions<N, T> revisions, Collection<AbstractEntityAudit.ChangeKind> filter, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        long totalsize = revisions.getContent().stream().filter(f -> !filter.contains(f.getEntity().getChangeKind())).count();
        return new PageImpl<>(
            Stream.concat(
                Stream.of(revisions.getLatestRevision())
                    .map(e -> {
                        e.getEntity().getVersion().setVersionLabel("Latest version");
                        return e;
                    }),
                revisions.reverse().getContent().stream()
                    .filter(f -> !filter.contains(f.getEntity().getChangeKind()))
            )
            .skip(skip)
            .limit(limit)
            .map(this::postLoadProcessing)
            .collect(Collectors.toList()), pageable,totalsize+1 );
    }

    protected Page<Revision<N, T>> getPage(Revisions<N, T> revisions, Collection<AbstractEntityAudit.ChangeKind> filter, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        long totalsize = revisions.getContent().stream().filter(f -> !filter.contains(f.getEntity().getChangeKind())).count();
        return new PageImpl<>(
            revisions.reverse().getContent().stream()
                .filter(f -> !filter.contains(f.getEntity().getChangeKind()))
                .skip(skip)
                .limit(limit)
                .map(this::postLoadProcessing)
                .collect(Collectors.toList()), pageable,totalsize);
    }

}

