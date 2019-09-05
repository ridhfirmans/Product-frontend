package com.aplikasi.product.productfrontend.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aplikasi.product.productfrontend.helper.EmailHelper;
import com.aplikasi.product.productfrontend.model.Product;

@Service
public class RestProduct {

	@Value("${backendUrl}")
	private String backendUrl;
	@Value("${emailSender}")
	private String emailSender;
	@Value("${emailSenderPassword}")
	private String emailSenderPassword;
	
	private String pathProduct = "/product";

	private RestTemplate restTemplate = new RestTemplate();
	EmailHelper emailHelper = new EmailHelper();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProduct.class);

	public List<Product> dataProduct() {
		Product[] result = restTemplate.getForObject(backendUrl + pathProduct, Product[].class);
		return Arrays.asList(result);
	}

	public void saveProduct(Product product) {
		if (product.getId() == null) {
			restTemplate.postForLocation(backendUrl + pathProduct, product);
		}
	}

	public Product findProductById(Integer id) {
		LOGGER.info("ini idnya di restservice >> "+id);
		LOGGER.info("isinya paan: "+restTemplate.getForObject(backendUrl+pathProduct+"/"+id, Product.class));
		return restTemplate.getForObject(backendUrl + pathProduct + "/" + id, Product.class);
	}

	public void sendEmail() throws AddressException, MessagingException, IOException {
		Product[] result = restTemplate.getForObject(backendUrl + pathProduct, Product[].class);
		emailHelper.sendEmail(result,emailSender,emailSenderPassword);

	}
	
}
