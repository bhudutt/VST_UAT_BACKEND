/**
 * 
 */
package com.hitech.dms.web.controller.email.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.email.EmailEntity;
import com.hitech.dms.web.service.email.EmailService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author santosh.kumar
 *
 */
@Component
public class Scheduler {
	private static final String API_BASE_URL = "http://localhost:8080/gateway/emailService/vst/email";

	@Autowired
	private EmailService emailService;

	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(fixedDelay = 10000, initialDelay = 10000)
	public void scheduleTask() {
		// String targetDate = "2023-12-18";
		String currentDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println("Current Date: " + currentDateAsString);
		try {
			List<EmailEntity> responseList = emailService.getEmailConfigDetails(currentDateAsString);
			System.out.println(responseList);

			if (responseList != null && !responseList.isEmpty()) {
				ExecutorService executorService = Executors.newFixedThreadPool(responseList.size());

				for (EmailEntity response : responseList) {
					executorService.submit(() -> processEmail(response));
				}

				// Shut down the executor service to release resources
				executorService.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processEmail(EmailEntity response) {
		try {
			String from = response.getFromMailId();
			String to = response.getToMailId();
			String cc = response.getCcMailId();
			String bcc = response.getBccMailId();
			String subject = response.getMailSubject();
			//String body = convertHtmlToPlainText(response.getMailBodyTxt());
			String body = response.getMailBodyTxt();
			
			MultipartFile[] files = null;
			String[] toAddresses = to != null ? getEmailAddressesArray(to) : null;
		    String[] ccArray = cc != null ? getEmailAddressesArray(cc) : null;
			//String[] bccArray = bcc != null ? getEmailAddressesArray(bcc): new String[] { "santoshthrsl123@gmail.com" };
			System.out.println("cc  "+cc);
			String[] bccArray = {"bhudutt.sharma@hitechroboticsystemz.com"};
			//String[] ccArray = {"santosh.kumar@hitechroboticsystemz.com"};
			//String[] bccArray = {"santosh.kumar@hitechroboticsystemz.com"};
			
			String responseData = emailService.sendMail(files,from, toAddresses, ccArray, bccArray, subject, body);

			//System.out.println("Response: " + responseData);

			if ("Mail sent successfully!".equalsIgnoreCase(responseData)) {
				String res = emailService.updateMailStatus(response.getMailItemId());
				System.out.println("Mail Status Update Response: " + res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] getEmailAddressesArray(String emailAddresses) {
		String[] addressesArray = emailAddresses.split(",");
		for (int i = 0; i < addressesArray.length; i++) {
			addressesArray[i] = addressesArray[i].trim();
		}
		return addressesArray;
	}

//	public String convertHtmlToPlainText(String html) {
//		Document document = Jsoup.parse(html);
//		Elements elements = document.select("p, a");
//		StringBuilder plainText = new StringBuilder();
//
//		for (Element element : elements) {
//			plainText.append(element.text()).append("\n");
//		}
//
//		return plainText.toString().trim();
//	}

	public String convertHtmlToPlainText(String input) {
		if (!input.contains("<")) {
			return input;
		}

		Document document = Jsoup.parse(input);
		Elements elements = document.select("p, a");
		StringBuilder plainText = new StringBuilder();

		for (Element element : elements) {
			plainText.append(element.text()).append("\n");
		}

		return plainText.toString().trim();
	}

}

//	@Scheduled(fixedDelay = 10000, initialDelay = 10000)
//	public void scheduleTask() {
//		String targetDate = "2023-12-18";
//		try {
//			List<EmailEntity> responseList = emailService.getEmailConfigDetails(targetDate);
//			System.out.println(responseList);
//			if (responseList != null && !responseList.isEmpty()) {
//				// EmailEntity responses = responseList.get(0);
//				for (EmailEntity response : responseList) {
//					String to = response.getToMailId();
//					String cc = response.getCcMailId();
//					String subject = response.getMailSubject();
//					String body = convertHtmlToPlainText(response.getMailBodyTxt());
//					//String body = response.getMailBodyTxt();
//
//					MultipartFile[] files = null;
//					String[] ccArray = cc != null ? cc.split(",") : null;
//					String[] bccArray = { "santosh.kumar@hitechroboticsystemz.com" };
//					String responseData = emailService.sendMail(files, to, ccArray, bccArray, subject, body);
//
////				HttpHeaders headers = new HttpHeaders();
////				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
////
////				MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
////				bodyMap.add("to", to);
////				bodyMap.add("cc", cc);
////				bodyMap.add("subject", subject);
////				bodyMap.add("body", body);
////
////				System.out.println("bodyMap::::::"+bodyMap);
////				if (files != null) {
////					for (MultipartFile file : files) {
////						ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
////							@Override
////							public String getFilename() {
////								return file.getOriginalFilename();
////							}
////						};
////						bodyMap.add("files", resource);
////					}
////				}
////
////				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
////
////				ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_BASE_URL + "/send",
////						requestEntity, String.class);
//
//					System.out.println("Response: " + responseData);
//					if (responseData.equalsIgnoreCase("Mail sent successfully!")) {
//					String res=emailService.updateMailStatus(response.getMailItemId());
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//    public static String convertHtmlToPlainText(String html) {
//        // Parse the HTML using JSoup
//        Document document = Jsoup.parse(html);
//
//        // Get the text content from all paragraphs and anchors
//        Elements elements = document.select("p, a");
//        StringBuilder plainText = new StringBuilder();
//
//        for (Element element : elements) {
//            plainText.append(element.text()).append("\n");
//        }
//
//        return plainText.toString().trim();
//    }
//
//}
