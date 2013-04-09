package me.ilyamirin.little.hub.invasion.clients;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import me.ilyamirin.little.hub.invasion.interaction.Proto;
import me.ilyamirin.little.hub.invasion.interaction.ProtobufMessageBodyReader;
import me.ilyamirin.little.hub.invasion.interaction.ProtobufMessageBodyWriter;
import me.ilyamirin.little.hub.invasion.interaction.cafs.*;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author ilamirin
 */
@Slf4j
public class CAFSClient {

    private Properties p;
    private Client client;

    private CAFSClient(Properties p, Client client) {
        this.p = p;
        this.client = client;
    }

    public static CAFSClient build(Properties p) {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.getDeserializationConfig().addMixInAnnotations(CafsResponse.class, MixIn.class);
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
        //TODO [IK] check if we need to change this to true too
        provider.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(provider);
        config.getClasses().add(ProtobufMessageBodyWriter.class);
        config.getClasses().add(ProtobufMessageBodyReader.class);
        return new CAFSClient(p, Client.create(config));
    }

    public void uploadChunks(Collection<FilePartUpload> partUploads, String sessionId) {
        log.trace("Start uploading: {} parts will be uploaded.", partUploads.size());

        Proto.UploadPartsRequest.Builder requestBuilder = Proto.UploadPartsRequest.newBuilder();
        for (FilePartUpload upload : partUploads) {
            Proto.UploadPartsRequest.FilePartUpload.Builder filePartUploadBuilder =
                    Proto.UploadPartsRequest.FilePartUpload.newBuilder();

            filePartUploadBuilder.setIndex(upload.getIndex());
            filePartUploadBuilder.setFileVersionKey(upload.getFileVersionKey());

            ByteString contentToUpload = ByteString.copyFrom(upload.getContent());
            filePartUploadBuilder.setContent(contentToUpload);

            Proto.UploadPartsRequest.FilePartUpload filePartUpload =
                    filePartUploadBuilder.build();

            requestBuilder.addFilePart(filePartUpload);
        }

        Proto.UploadPartsRequest request = requestBuilder.build();

        WebResource webResource = client
                .resource(p.getProperty("cafs.url").concat("parts/uploadParts"));

        CafsResponse res = webResource
                .queryParam("sessionId", sessionId)
                .type("application/x-protobuf")
                .post(CafsResponse.class, request);

        if (res.hasErrors()) {
            log.error("Somthing shit happened {}", res.getErrorMessage());
        } else {
            log.trace("Done chunk uploading file.");
        }

        log.trace("Finish uploading {} parts were successfully uploaded.", partUploads.size());
    }

    public boolean createNewFileVersion(FileVersion fileVersion, String sessionId) {
        log.info("Creating file {}({})", fileVersion.getFile().getPath(), fileVersion.getChunksCount());

        CreateFileVersionRequest request = new CreateFileVersionRequest();
        request.setSessionId(sessionId);
        request.setVersion(fileVersion);

        WebResource webResource = client
                .resource(p.getProperty("cafs.url").concat("files/create"));

        CafsResponse res = webResource
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(CafsResponse.class, request);
        if (res.hasErrors()) {
            log.error("File version {} has not been created yet because: {}",
                    fileVersion, res.getErrorMessage());
            return false;
        } else {
            log.trace("Done creating file.");
            return true;
        }
    }

    public List<FileVersionIdentifier> getBrokenVersions(String sessionId, String targetId) {
        WebResource webResource = client.resource(p.getProperty("cafs.url").concat("brokenVersions/"));

        log.info("Going to CAFS instance {} and asking him about broken versions list for target {} with sessionId {}",
                client, targetId, sessionId);

        CafsResponse<ListBrokenVersionsResponse> response = webResource
                .queryParam("targetId", targetId)
                .queryParam("sessionId", sessionId)
                .get(CafsResponse.class);

        if (response.hasErrors()) {
            log.error("Oops! {}", response.getErrorMessage());
            return null;
        } else {
            log.info("{} broken versions have been found.", response.getEntityFromResponse().getBrokenVersions().size());
            return response.getEntityFromResponse().getBrokenVersions();
        }
    }

    public boolean removeBrokenVersionsFromCafsRegistry(String sessionId, FileVersion fileVersion) {
        UnregisterBrokenVersionRequest request = new UnregisterBrokenVersionRequest();

        request.setSessionId(sessionId);
        request.setTargetId(fileVersion.getFile().getTargetId());
        request.setPath(fileVersion.getFile().getPath());
        request.setVersionId(fileVersion.getVersionId());

        WebResource webResource = client.resource(p.getProperty("cafs.url").concat("brokenVersions/"));

        CafsResponse response = webResource
                .path("unregister")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(CafsResponse.class, request);

        if (response.hasErrors()) {
            log.error("Oops! {}", response.getErrorMessage());
            return false;

        } else {
            log.info("Broken version of file version {} has benn remove successfully.", fileVersion);
            return true;

        }
    }

}
