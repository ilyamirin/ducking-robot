package me.ilyamirin.little.hub.invasion.clients;

import com.google.protobuf.ByteString;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.Collection;
import java.util.List;
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

    private Client client;

    private CAFSClient(Client client1) {
        this.client = client1;
    }

    private CAFSClient() {
    }

    public static CAFSClient build() {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.getDeserializationConfig().addMixInAnnotations(CafsResponse.class, MixIn.class);
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
        //TODO [IK] check if we need to change this to true too
        provider.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(provider);
        config.getClasses().add(ProtobufMessageBodyWriter.class);
        config.getClasses().add(ProtobufMessageBodyReader.class);
        return new CAFSClient(Client.create(config));
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
                .resource("http://localhost:30001/parts/uploadParts");

        ClientResponse res = webResource
                .queryParam("sessionId", sessionId)
                .type("application/x-protobuf")
                .post(ClientResponse.class, request);

        /*        if (response.getStatus() != 200) {
         throw new RuntimeException("Failed : HTTP error code : "
         + response.getStatus());
         }

         /*        String output = response.getEntityInputStream()
         log.trace("Output from Server .... \n");
         log.trace(output);
         */
        log.trace("Finish uploading {} parts were successfully uploaded.", partUploads.size());
    }

    public void createNewFileVersion(FileVersion fileVersion, String sessionId) {
        log.info("Creating file {}({})", fileVersion.getFile().getPath(), fileVersion.getChunksCount());

        CreateFileVersionRequest request = new CreateFileVersionRequest();
        request.setSessionId(sessionId);
        request.setVersion(fileVersion);

        WebResource webResource = client
                .resource("http://localhost:30001/files/create");

        CafsResponse res = webResource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
                .put(CafsResponse.class, request);
        if (res.hasErrors()) {
            log.error("File version {} has not been created yet because: {}",
                    fileVersion, res.getErrorMessage());
        } else {
            log.info("Done creating file.");
        }
    }

    public List<FileVersionIdentifier> getBrokenVersions(String sessionId, String targetId) {
        WebResource webResource = client.resource("http://localhost:30001/brokenVersions/");

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
            log.info("{} broken versions have been found.",
                    response.getEntityFromResponse().getBrokenVersions().size());
            return response.getEntityFromResponse().getBrokenVersions();
        }
    }
}
