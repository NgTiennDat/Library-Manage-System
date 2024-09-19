package com.datien.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionData implements Serializable {

    private String sessionId;
    private String userId;
    private String userName;
}
