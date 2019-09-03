package com.aplikasi.product.productfrontend.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import com.aplikasi.product.productfrontend.helper.EmailHelper;
import com.aplikasi.product.productfrontend.model.Product;
import com.aplikasi.product.productfrontend.service.RestProduct;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private RestProduct restProduct;
	
	EmailHelper emailHelper;

	@GetMapping("/")
	public String index() {
		return "redirect:/product/list-product";
	}

	@GetMapping("/product/list-product")
	public ModelMap dataProduct() {
		return new ModelMap().addAttribute("dataProduct", restProduct.dataProduct());
	}

	@PostMapping("/product/form-product")
	public String prosesForm(@ModelAttribute @Valid Product prod, BindingResult errors, SessionStatus sessionStatus) {
		LOGGER.info("Menyimpan data Product");

		if (errors.hasErrors()) {
			return "/product/form-product";
		}

		restProduct.saveProduct(prod);
		sessionStatus.setComplete();
		return "redirect:/product/list-product";
	}

	@GetMapping("/product/form-product")
	public ModelMap findById(@RequestParam(name = "id", required = false) String id) {
		Product prod = new Product();
		if (id != null) {
			prod = restProduct.findProductById(prod.getId());
			if (prod == null) {
				prod = new Product();
			}
		}
		return new ModelMap().addAttribute("product", prod);
	}

	@GetMapping("/sendEmail")
	public String sendEmail() throws AddressException, MessagingException, IOException {
		restProduct.sendEmail();
		return "redirect:/product/list-product";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("auth name: "+auth.getName()+" / "+auth);
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

}
