package com.gitegg.oauth.dto;

import lombok.Data;

/**
 * @author
 * @version V1.0
 * @Description: TODO
 * @date
 */
@Data
public class LoginResultDTO {

	private boolean success;

	private String message;

	private String targetUrl;
}
