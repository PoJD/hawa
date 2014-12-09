package cz.pojd.security.event;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import cz.pojd.homeautomation.model.Source;
import cz.pojd.homeautomation.model.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.model.rooms.Entry;
import cz.pojd.homeautomation.model.rooms.Room;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.homeautomation.model.spring.RoomSpecification;

public class SourceResolverImpl implements SourceResolver {

    private final Map<String, Source> sourceMap;

    @Inject
    public SourceResolverImpl(OutdoorDAO outdoorDAO, RoomsDAO roomsDAO) {
	sourceMap = new HashMap<String, Source>();
	sourceMap.put(outdoorDAO.getOutdoor().getId(), outdoorDAO.getOutdoor());
	for (RoomSpecification roomSpecification : RoomSpecification.values()) {
	    Room room = roomsDAO.getRoom(roomSpecification);
	    sourceMap.put(room.getId(), room);
	    for (Entry entry : room.getEntries()) {
		sourceMap.put(entry.getId(), entry);
	    }
	}
	for (Camera camera : Camera.values()) {
	    sourceMap.put(camera.getId(), camera);
	}
    }

    @Override
    public Source resolve(String name) {
	Source result = sourceMap.get(name);
	if (result != null) {
	    return result;
	} else {
	    return Camera.UNKNOWN;
	}
    }
}
