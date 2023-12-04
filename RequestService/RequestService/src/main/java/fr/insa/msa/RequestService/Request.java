package fr.insa.msa.RequestService;

public class Request {
	
	private int id;
	private String content;
	private int creatorId;
	private int volunteerId;
	
	public Request(int id, String content, int creator, int volunteer) {
		this.id = id;
		this.content = content;
		this.creatorId = creator;
		this.volunteerId = volunteer;
	}
	
	public Request(int id, String content, int creator) {
		super();
		this.id = id;
		this.content = content;
		this.creatorId = creator;
		this.volunteerId = -1;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCreator() {
		return creatorId;
	}

	public void setCreator(int creator) {
		this.creatorId = creator;
	}
	
	public int getVolunteer() {
		return this.volunteerId;
	}
	
	public void setVolunteer(int volunteer) {
		this.volunteerId = volunteer;
	}
}
