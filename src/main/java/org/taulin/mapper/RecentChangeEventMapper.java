package org.taulin.mapper;

import org.mapstruct.Mapper;
import org.taulin.model.Meta;
import org.taulin.model.MetaDTO;
import org.taulin.model.RecentChangeEvent;
import org.taulin.model.RecentChangeEventDTO;
import org.taulin.model.Revision;
import org.taulin.model.RevisionDTO;

@Mapper
public interface RecentChangeEventMapper {
    RecentChangeEvent recentChangeEventDtoToRecentChangeEvent(RecentChangeEventDTO recentChangeEventDTO);

    Meta metaDtoToMeta(MetaDTO metaDTO);

    Revision revisionDtoToRevision(RevisionDTO revisionDTO);
}
