package com.example.ExpensesTracked_Backend;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ExpensesTracked_Backend.service.CategoryRepository;
import com.example.ExpensesTracked_Backend.service.ExpensesRepository;
import com.example.ExpensesTracked_Backend.service.UserRepository;
import com.example.ExpensesTracked_Backend.service.imp.Category;
import com.example.ExpensesTracked_Backend.service.imp.Expenses;
import com.example.ExpensesTracked_Backend.service.imp.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * main controller class
 *
 */
@RestController    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	           // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@Autowired
	private ExpensesRepository expenseRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	/**
	 * adds new user to the user repositiory, sends error message if user already exists 
	 * @param n
	 * @return results
	 * @throws CloneNotSupportedException
	 */
	@PostMapping(path="/register") // Map ONLY GET Requests
	public @ResponseBody User addNewUser (@RequestBody User n) throws CloneNotSupportedException {
		ArrayList<User> s = (ArrayList<User>) userRepository.findAllByemail(n.getEmail());
		User result =(User) n.clone();
		if(s.size() > 1) {
			result.setError(true);
			result.setError_msg("You are already registered!");
			return result;
		} else {
			userRepository.save(n);
		}
		return result;
	}
	
	/**
	 * Method that returns all the users in the user repository
	 * @return userRepository.findAll()
	 */
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
	
	/**
	 * method that logs in the user, if email, password, or user is null throw servlet exception
	 * throws servlet exception if password is incorrect
	 * @param n
	 * @return user
	 * @throws ServletException
	 */
	@PostMapping(path="/login")
	public User login(@RequestBody User n){
		String jwtToken = "";
		User user = new User();
		if(n.getEmail() == null || n.getPassword() == null) {
			user.setError(true);
			user.setError_msg("Please Fill in both user and password");
			return user;
		}
		
		String email = n.getEmail();
		String password = n.getPassword();
		
		if (userRepository.getUserByemail(email)==null) {
			user.setError(true);
			user.setError_msg("Email is not associated with any accounts");
			return user;
		}
		user = userRepository.getUserByemail(email);
		
		String pwd = user.getPassword();
		
		if(!password.equals(pwd)) {
			user.setError(true);
			user.setError_msg("Email/passowrd combination does not match any records");
			return user;
		}
		
		jwtToken = Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
		user.setToken(jwtToken);
		
		return user;
	}
	/**
	 * The method returns all the expenses in the expenses repository
	 *  @return all expenses in repository
	 */
	@GetMapping(path="/expenses/all")
	public @ResponseBody Iterable<Expenses> getAllExpenses(){
		return expenseRepository.findAll();
	}
	/**
	 * Method to add a new expense in the expenses repository
	 * @param n
	 * @return String with the new expense
	 */
	@PostMapping(path="/expenses/add")
	public @ResponseBody Expenses addNewExpense(@RequestBody Expenses n) {
		Expenses result = new Expenses();
		if(n.getAmount() == " " | n.getCategory() == " " | n.getExpensesName() == " " | n.getToken() == null) {
			result.setError(true);
			result.setError_msg("One or more fields is empty");
			return result;
		}
		expenseRepository.save(n);
		result.setError(false);
		return result;
	}
	/**
	 * Method to add a new category to the category repository 
	 * and sends user message that new category was saved
	 * @param n
	 * @return String "saved"
	 */
	@PostMapping(path="/category/add")
	public @ResponseBody String addNewCategory(@RequestBody Category n) {
		categoryRepository.save(n);
		return "Saved";
	}
}