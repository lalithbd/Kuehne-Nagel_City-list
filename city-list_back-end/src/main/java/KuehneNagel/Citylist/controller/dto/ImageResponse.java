package KuehneNagel.Citylist.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponse {

    private Long id;
    private String name;
    private String description;
    private byte[] data;
}
