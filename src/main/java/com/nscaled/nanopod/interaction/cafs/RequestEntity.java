package com.nscaled.nanopod.interaction.cafs;

import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;

@XmlTransient
@Data
public abstract class RequestEntity {

    private String sessionId;
}
