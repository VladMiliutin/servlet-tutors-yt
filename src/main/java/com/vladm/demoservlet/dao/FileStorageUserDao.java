package com.vladm.demoservlet.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladm.demoservlet.model.User;
import com.vladm.demoservlet.utils.ObjectMapperInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileStorageUserDao implements UserDao {

    private final static String PATH_TO_FOLDER = "/home/vladm/IdeaProjects/demo-servlet/data";

    private final static Map<String, User> LOCAL_USER_MAP = new HashMap<>();

    private static FileStorageUserDao INSTANCE;
    private final ObjectMapper mapper;

    public FileStorageUserDao(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        sync();
        User persistedUser = LOCAL_USER_MAP.put(user.getId(), user);
        sync();
        return persistedUser;
    }

    @Override
    public List<User> findAll() {
        sync();
        return new ArrayList<>(LOCAL_USER_MAP.values());
    }

    @Override
    public Optional<User> findOne(String id) {
        sync();
        return Optional.ofNullable(LOCAL_USER_MAP.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        sync();
        return LOCAL_USER_MAP.values()
                .stream()
                .filter(usr -> usr.getName().equals(name))
                .findFirst();

    }

    @Override
    public Optional<User> findByEmail(String email) {
        sync();
        return LOCAL_USER_MAP.values()
                .stream()
                .filter(usr -> usr.getEmail().equals(email))
                .findFirst();

    }

    @Override
    public User update(User user) {
       return save(user);
    }

    @Override
    public void delete(String id) {
        sync();
        LOCAL_USER_MAP.remove(id);
        sync();
    }

    @Override
    public boolean userExists(User user) {
        sync();
        return LOCAL_USER_MAP.values()
                .stream()
                .anyMatch(usr -> usr.getEmail().equals(user.getEmail()) || usr.getName().equals(user.getName()));

    }

    public void sync() {
        try {
            File file = readFile();
            if(LOCAL_USER_MAP.isEmpty()) {
                Map<String, User> FILE_USER_MAP = mapper.readValue(file, new TypeReference<Map<String, User>>() {});
                LOCAL_USER_MAP.putAll(FILE_USER_MAP);
            } else {
                JsonNode jsonNode = mapper.valueToTree(LOCAL_USER_MAP);
                FileUtils.write(file, jsonNode.toPrettyString());
            }
        } catch (IOException e) {
            System.out.println("Users file corrupted");
            throw new RuntimeException(e);
        }
    }

    private static File readFile() throws IOException {
        File file = FileUtils.getFile(PATH_TO_FOLDER + "/users.json");

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileUtils.write(file, "{}");
            file = FileUtils.getFile(PATH_TO_FOLDER + "/users.json");
        }

        return file;
    }

    public static FileStorageUserDao getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FileStorageUserDao(ObjectMapperInstance.getInstance());
        }

        return INSTANCE;
    }
}
