package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionKind;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.instrument.pojo.ConditionNode;
import no.nsd.qddt.domain.instrument.pojo.InstrumentNode;
import no.nsd.qddt.domain.instrument.pojo.Parameter;
import no.nsd.qddt.domain.treenode.TreeNode;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("instrumentNodeService")
public class InstrumentNodeServiceImpl implements InstrumentNodeService {

    private final InstrumentNodeRepository instrumentNodeRepository;

    @Autowired
    public InstrumentNodeServiceImpl(InstrumentNodeRepository instrumentNodeRepository) {
        this.instrumentNodeRepository = instrumentNodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return instrumentNodeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return instrumentNodeRepository.exists(uuid);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public <S extends InstrumentNode> S save(S instance) {
        System.out.println("InstrumentNode::save");
        return (S) postLoadProcessing(
            instrumentNodeRepository.saveAndFlush(
                prePersistProcessing( instance )));
    }



    @Override
    @Transactional(readOnly = true)
    public InstrumentNode<?> findOne(UUID uuid) {
        return instrumentNodeRepository.findById(uuid)
            .map( this::postLoadProcessing )
            .orElseThrow(
            () -> new ResourceNotFoundException(uuid, TreeNode.class));
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(UUID uuid) {
        instrumentNodeRepository.delete(uuid);
    }

    private <S extends InstrumentNode> S  prePersistProcessing(S instance) {
        assert  (instance != null);
//        if (instance instanceof QuestionConstruct)
//            QuestionConstruct qc = (QuestionConstruct)instance;
        instance.checkInNodes();
        System.out.println(instance.getName());
        if (instance.getElementKind() == ElementKind.SEQUENCE_CONSTRUCT){
            instance.addParameter( new Parameter(instance.getName(),"IN") );
        }
//        if (instance.children.size() > 0) {
//            foreach (child)
//            instance.getChildren().stream().forEach(this::prePersistProcessing);
//
//        }
        return instance;
    }


    protected InstrumentNode postLoadProcessing(InstrumentNode instance) {
        if (instance.getElementKind() == ElementKind.CONDITION_CONSTRUCT &&  instance.getElement() == null) {
            JsonReader reader = Json.createReader(new StringReader(instance.getName()));
            JsonObject jsonObject = reader.readObject();
            ConditionNode condNode = new ConditionNode();

            condNode.setId( UUID.fromString( jsonObject.getString("id")));
            condNode.setName( jsonObject.getString("name"));
            condNode.setConditionKind( ConditionKind.valueOf( jsonObject.getString("conditionKind")));
            condNode.setClassKind( jsonObject.getString("classKind"));
            condNode.setCondition( jsonObject.getString("condition"));

            instance.setElement( condNode );
        }

        return instance;
    }

}
