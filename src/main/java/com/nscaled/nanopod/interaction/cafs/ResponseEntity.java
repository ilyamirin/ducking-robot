package com.nscaled.nanopod.interaction.cafs;

import lombok.ToString;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ListBrokenVersionsResponse.class, name = "listBrokenVersionsResponse")
})
@ToString
public abstract class ResponseEntity {
}
