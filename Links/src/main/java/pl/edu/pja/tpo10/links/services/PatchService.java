package pl.edu.pja.tpo10.links.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.stereotype.Service;
import pl.edu.pja.tpo10.links.exceptions.WrongPasswordException;
import pl.edu.pja.tpo10.links.models.Link;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class PatchService
{
    private final ObjectMapper objectMapper;

    public PatchService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public boolean hasCorrectFields(JsonMergePatch patch) throws JsonPatchException
    {
        List<String> correctFields = List.of("name", "targetUrl", "password");
        JsonNode patchNode = getNode(patch);
        List<String> l = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(patchNode.fields(), Spliterator.ORDERED), false)
                .filter(field -> !field.getValue().isNull())
                .map(Map.Entry::getKey).toList();
        System.out.println(l);
        return  StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(patchNode.fields(), Spliterator.ORDERED), false)
                .filter(field -> !field.getValue().isNull())
                .map(Map.Entry::getKey)
                .allMatch(correctFields::contains);
    }

    public Optional<String> getName(JsonMergePatch patch) throws JsonPatchException
    {
        JsonNode patchNode = getNode(patch);
        return Optional.ofNullable(patchNode.get("name")).map(JsonNode::asText);
    }

    public Optional<String> getPassword(JsonMergePatch patch) throws JsonPatchException
    {
        JsonNode patchNode = getNode(patch);
        return Optional.ofNullable(patchNode.get("password")).map(JsonNode::asText);
    }

    public Link applyPatch(Link link, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException, WrongPasswordException, IllegalArgumentException
    {
        return objectMapper.treeToValue(patch.apply(objectMapper.valueToTree(link)), Link.class);
    }



    private JsonNode getNode(JsonMergePatch patch) throws JsonPatchException
    {
        return patch.apply(objectMapper.valueToTree(new Link()));
    }
}
