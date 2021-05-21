package io.springbootquickstart.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

	@Autowired
	private TopicRepository repo;
	
	public List<Topic> getAllTopics() {
		ArrayList<Topic> topics = new ArrayList<>();
		repo.findAll().forEach(a -> topics.add(a));
		return topics;
	}
	
	public Topic getTopic(String id) {
		return repo.findById(id).get();
	}
	
	public void addTopic(Topic topic) {
		repo.save(topic);
	}

	public void addTopic(Topic topic, String id) {
		repo.save(topic);
	}

	public void deleteTopic(String id) {
		repo.deleteById(id);
	}
}
