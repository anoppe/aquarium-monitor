package nl.noppe.auke.aquarium.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MetricsController {

	private SimpMessageSendingOperations messageTemplate;
	
	@Autowired
	public void setMessageTemplate(SimpMessageSendingOperations messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	
	@MessageMapping("/hello")
    public void greeting(String message) throws Exception {
        Thread.sleep(3000); // simulated delay
        messageTemplate.convertAndSend("/queue/greetings", message);
    }
	
}
