package models;

public class EventOrganizer extends User{
	private String events_created;

	public String getEvents_created() {
		return events_created;
	}

	public void setEvents_created(String events_created) {
		this.events_created = events_created;
	}

	public EventOrganizer() {
		super();
	}
	
}
