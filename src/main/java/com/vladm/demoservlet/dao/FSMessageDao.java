package com.vladm.demoservlet.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladm.demoservlet.model.Message;
import com.vladm.demoservlet.utils.ObjectMapperInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FSMessageDao implements MessageDao{

    private final static String PATH_TO_FILE = "/home/vladm/IdeaProjects/demo-servlet/data/messages.json";
    
    private final Map<String, Message> messages = new HashMap<>();
    
    private final ObjectMapper mapper;
    
    private static FSMessageDao INSTANCE;

    public FSMessageDao(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Message save(Message message) {
        sync();
        Message persisted = messages.put(message.getId(), message);
        sync();
        return persisted;
    }

    @Override
    public List<Message> findAll() {
        sync();
        return new ArrayList<>(messages.values());
    }

    @Override
    public Optional<Message> findOne(String id) {
        sync();
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findByUserId(String id) {
        sync();
        return messages.values().stream()
                .filter(msg -> msg.getUserId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByIds(List<String> ids) {
        sync();
        return messages.values().stream()
                .filter(msg -> ids.contains(msg.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Message update(Message message) {
        return save(message);
    }

    @Override
    public void delete(String id) {
        sync();
        messages.remove(id);
        sync();
    }


    public void sync() {
        try {
            File file = readFile();
            if(messages.isEmpty()) {
                Map<String, Message> fileMessageMap = mapper.readValue(file, new TypeReference<>() {
                });
                messages.putAll(fileMessageMap);
            } else {
                JsonNode jsonNode = mapper.valueToTree(messages);
                FileUtils.write(file, jsonNode.toPrettyString());
            }
        } catch (IOException e) {
            System.out.println("Message file corrupted");
            throw new RuntimeException(e);
        }
    }

    private static File readFile() throws IOException {
        File file = FileUtils.getFile(PATH_TO_FILE);

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileUtils.write(file, "{}");
            file = FileUtils.getFile(PATH_TO_FILE);
        }

        return file;
    }

    public static FSMessageDao getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FSMessageDao(ObjectMapperInstance.getInstance());
        }

        return INSTANCE;
    }
}
