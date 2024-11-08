package onetomany.websockets;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpointConfig;

@EnableScheduling
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public WebSocketConfigurator webSocketConfigurator() {
        return new WebSocketConfigurator();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/matches-websocket")
                .setAllowedOrigins("*")  // For testing
                .withSockJS();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public static class WebSocketConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
        private static volatile BeanFactory context;

        @Override
        public <T> T getEndpointInstance(Class<T> endpoint) throws InstantiationException {
            return context.getBean(endpoint);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            WebSocketConfigurator.context = applicationContext;
        }
    }
}