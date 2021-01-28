package ua.isolutions.bot.commons.config;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MultiReadServletRequest extends javax.servlet.http.HttpServletRequestWrapper {

	private byte[] body;

	public MultiReadServletRequest(HttpServletRequest request) {
		super(request);
		try {
			body = StreamUtils.copyToByteArray(request.getInputStream());
		} catch (IOException ex) {
			body = new byte[0];
		}
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
	}

	@Override
	public ServletInputStream getInputStream() {
		return new ServletInputStream() {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

			@Override
			public int read() {
				return inputStream.read();
			}
		};
	}
}

