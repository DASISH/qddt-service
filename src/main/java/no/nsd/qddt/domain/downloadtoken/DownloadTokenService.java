package no.nsd.qddt.domain.downloadtoken;



import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.user.User;

import java.util.Set;
import java.util.UUID;

public interface DownloadTokenService extends BaseService<DownloadToken, UUID> {

    DownloadToken getDownloadToken(OtherMaterial otherMaterial, User user);

    DownloadToken findByUUID(UUID uuid);

    Boolean verifyValidity(DownloadToken downloadToken);

    void deleteInBatch(Set<DownloadToken> downloadTokens);

    void verifyUserOwnerShip(OtherMaterial otherMaterial, User user);

}
