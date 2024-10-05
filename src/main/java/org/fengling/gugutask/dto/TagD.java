package org.fengling.gugutask.dto;

import lombok.Data;

@Data
public class TagD {
    private Long Id;
    private String tagName;


    public TagD() {
    }


    public TagD(Long id, String tagName) {
        this.Id = id;
        this.tagName = tagName;
    }
}
