/**
 * Contains application specific utilities.
 * @author Dag Østgulen Heradstveit
 */
@AnyMetaDef(name= "OtherMaterialRef",idType = "UUID",metaType = "string",
    metaValues = {
        @MetaValue( value="T", targetEntity=TopicGroup.class ),
        @MetaValue( value="CC", targetEntity=ControlConstruct.class )
    }
)
package no.nsd.qddt.utils;

import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;