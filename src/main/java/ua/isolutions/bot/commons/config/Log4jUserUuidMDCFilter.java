package ua.isolutions.bot.commons.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Log4j2
public class Log4jUserUuidMDCFilter extends OncePerRequestFilter {

	public static final String MDC_USER_ID_TOKEN_KEY = "Log4jUserUuidMDCFilter.USER_ID";
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		try {
			try {
				JsonNode request = objectMapper.readTree(httpServletRequest.getInputStream());
				String currentUserId = getUserId(request);
				if (nonNull(currentUserId) && !currentUserId.isEmpty()) {
					MDC.put(MDC_USER_ID_TOKEN_KEY, currentUserId);
				}
			} catch (Exception e) {
				log.debug("Cannot obtain user id for the log", e);
			}
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} 	finally {
			MDC.remove(MDC_USER_ID_TOKEN_KEY);
		}
	}

	private String getUserId(JsonNode node) {
		JsonNode userId = node.get("user_id");
		if (nonNull(userId)) {
			return userId.asText();
		}

		JsonNode sender = node.get("sender");
		if (nonNull(sender)) {
			return sender.get("id").asText();
		}

		return node.get("user").get("id").asText();
	}
}
