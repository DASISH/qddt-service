package no.nsd.qddt.domain.downloadtoken;


import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface DownloadTokenRepository extends BaseRepository<DownloadToken, UUID> {

    Optional<DownloadToken> findByUuid(UUID uuid);

}
