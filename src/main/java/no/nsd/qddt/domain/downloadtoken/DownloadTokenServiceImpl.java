package no.nsd.qddt.domain.downloadtoken;

import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.ExpiredDownloadTokenException;
import no.nsd.qddt.exception.FileNotOwnedException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service("downloadTokenService")
class DownloadTokenServiceImpl implements DownloadTokenService {

    private DownloadTokenRepository downloadTokenRepository;

    private OtherMaterialService otherMaterialService;

    @Autowired
    public DownloadTokenServiceImpl (DownloadTokenRepository downloadTokenRepository,
                                     OtherMaterialService otherMaterialService) {
        this.downloadTokenRepository = downloadTokenRepository;
        this.otherMaterialService = otherMaterialService;
    }

    @Override
    @Transactional
    public DownloadToken getDownloadToken(OtherMaterial OtherMaterial, User user) {
//        if (!ArchivingPortalSecurityContext.isActiveUserAdmin()) {
            verifyUserOwnerShip(OtherMaterial, user);
//        }
        DownloadToken downloadToken = new DownloadToken(OtherMaterial);
        return save(downloadToken);
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadToken findByUUID(UUID uuid) {
        return downloadTokenRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, DownloadToken.class)
        );
    }

    @Override
    public Boolean verifyValidity(DownloadToken downloadToken) {
        LocalDateTime expirationDate = downloadToken.getModified().plusHours(1L);
        if (!LocalDateTime.now().isAfter(expirationDate)) {
            return true;
        }

        throw new ExpiredDownloadTokenException(downloadToken);
    }

    @Override
    public void deleteInBatch(Set<DownloadToken> downloadTokens) {
        downloadTokenRepository.deleteInBatch(downloadTokens);
    }


    @Transactional(readOnly = true)
    public List<DownloadToken> findAll() {
        return downloadTokenRepository.findAll();
    }

    @Override
    @Transactional
    public DownloadToken save(DownloadToken downloadToken) {
        return downloadTokenRepository.save(downloadToken);
    }


    @Transactional(readOnly = true)
    public void verifyUserOwnerShip(OtherMaterial otherMaterial, User user) {
        if (!user.getAgency().equals(otherMaterial.getAgency())) {
            throw new FileNotOwnedException(user, otherMaterial.getId());
        }
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(UUID uuid) {
        return false;
    }

    @Override
    public DownloadToken findOne(UUID uuid) {
        return downloadTokenRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, DownloadToken.class)
        );
    }

    @Override
    public List<DownloadToken> save(List<DownloadToken> instances) {
        return instances;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public void delete(List<DownloadToken> instances) {
        downloadTokenRepository.deleteInBatch(instances);
    }
}
