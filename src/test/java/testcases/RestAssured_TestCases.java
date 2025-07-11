package testcases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import rest.ApiUtil;
import testcases.TestCodeValidator;
import coreUtilities.utils.FileOperations;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.apache.poi.ss.usermodel.*;
import static org.testng.Assert.assertEquals;
import rest.CustomResponse;
import java.security.SecureRandom;
import io.restassured.http.ContentType;

import java.security.SecureRandom;
import io.restassured.http.ContentType;

@SuppressWarnings("unused")
public class RestAssured_TestCases {

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private int employeeStatus;
	private TestCodeValidator testCodeValidator;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	@Test(priority = 0, groups = { "PL1" }, description = "1. Login to the application using Selenium WebDriver\n"
			+ "2. Extract the cookie named 'orangehrm' after successful login\n"
			+ "3. Store the cookie value for subsequent API requests")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(9000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();
		testCodeValidator = new TestCodeValidator();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
	}

	@Test(priority = 1, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/auth/login' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK) and response is HTML page")
	public void GetLogin() throws IOException {
		System.out.println("Cookie is " + cookieValue);
		String endpoint = "/web/index.php/auth/login";
		CustomResponse customResponse = apiUtil.GetLogin(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLogin",
				List.of("given", "cookie", "get", "response"));

		// Fetch response from custom response object
		String responseBody = customResponse.getResponseBody(); // or .getResponse().asString()
		int statusCode = customResponse.getStatusCode();

		System.out.println("Status Code: " + statusCode);
		System.out.println("Response Body: " + responseBody);

		Assert.assertTrue(isImplementationCorrect, "GetLogin must be implemented using Rest Assured methods only!");
		assertEquals(statusCode, 200);

		// Additional HTML page assertions
		Assert.assertTrue(TestCodeValidator.GetLogin(customResponse),
				"The login page response body did not meet expected structure or content.");

	}

	@Test(priority = 2, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/dashboard/employees/action-summary' endpoint with a valid cookie\n"
					+ "2. Do not include any request body (null)\n"
					+ "3. Print the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetEmpActionSummary() throws IOException {
		String endpoint = "/web/index.php/api/v2/dashboard/employees/action-summary";
		CustomResponse customResponse = apiUtil.GetEmpActionSummary(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"GetEmpActionSummary", List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Status Line: " + customResponse.getStatusLine());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		Assert.assertTrue(isImplementationCorrect,
				"GetEmpActionSummary must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200.");
		Assert.assertTrue(TestCodeValidator.GetEmpActionSummary(customResponse),
				"Response must have non-null 'id' and 'group' fields in each data item.");

	}

	@Test(priority = 3, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/dashboard/shortcuts' endpoint with a valid cookie\n"
					+ "2. Do not provide any request body (null)\n"
					+ "3. Print the response status code and body for verification\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetDashboardShortcut() throws IOException {
		String endpoint = "/web/index.php/api/v2/dashboard/shortcuts";
		CustomResponse customResponse = apiUtil.GetDashboardShortcut(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"GetDashboardShortcut", List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Status Line: " + customResponse.getStatusLine());
		System.out.println("Response Body: " + customResponse.getResponseBody());
		JsonPath jsonPath = customResponse.getResponse().jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		// Safely extract values
		Boolean leaveAssignLeave = customResponse.leaveAssignLeave;
		Boolean leavLeaveList = customResponse.leavLeaveList;
		Boolean leaveApplyLeave = customResponse.leaveApplyLeave;
		Boolean leaveMyLeave = customResponse.leaveMyLeave;
		Boolean timeEmployeeTimesheet = customResponse.timeTmployeeTimesheet;
		Boolean timeMyTimesheet = customResponse.timeMyTimesheet;

		Assert.assertNotNull(leaveAssignLeave, "Missing: leave.assign_leave");
		Assert.assertNotNull(leavLeaveList, "Missing: leave.leave_list");
		Assert.assertNotNull(leaveApplyLeave, "Missing: leave.apply_leave");
		Assert.assertNotNull(leaveMyLeave, "Missing: leave.my_leave");
		Assert.assertNotNull(timeEmployeeTimesheet, "Missing: time.employee_timesheet");
		Assert.assertNotNull(timeMyTimesheet, "Missing: time.my_timesheet");

		Assert.assertTrue(isImplementationCorrect,
				"GetDashboardShortcut must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200.");
		Assert.assertTrue(TestCodeValidator.GetDashboardShortcut(customResponse),
				"All values in 'data' must be of boolean type.");

	}

	@Test(priority = 4, groups = {
			"PL1" }, description = "1. Generate the current date dynamically in 'yyyy-MM-dd' format\n"
					+ "2. Send a GET request to the '/web/index.php/api/v2/dashboard/employees/leaves?date={current_date}' endpoint with a valid cookie\n"
					+ "3. Do not include any request body (null)\n"
					+ "4. Log the date used, response status code, and response body\n"
					+ "5. Assert that the response status code is 200 (OK)")
	public void GetEmpLeaveInfo() throws IOException {
		// Dynamic current date in yyyy-MM-dd format
		String currdate = LocalDate.now().toString();
		String endpoint = "/web/index.php/api/v2/dashboard/employees/leaves?date=" + currdate;

		CustomResponse customResponse = apiUtil.GetEmpLeaveInfo(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpLeaveInfo",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Date Used: " + currdate);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Status Line: " + customResponse.getStatusLine());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		Assert.assertTrue(isImplementationCorrect,
				"GetEmpLeaveInfo must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200.");

	}

	@Test(priority = 5, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/dashboard/employees/subunit' endpoint with a valid cookie\n"
					+ "2. Do not provide any request body (null)\n"
					+ "3. Print the response status code and response body for validation\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetEmpSubunit() throws IOException {
		String endpoint = "/web/index.php/api/v2/dashboard/employees/subunit";
		CustomResponse customResponse = apiUtil.GetEmpSubunit(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpSubunit",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Status Line: " + customResponse.getStatusLine());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		Assert.assertNotNull(customResponse.subUnitName);
		Assert.assertNotNull(customResponse.subUnitId);
		Assert.assertNotNull(customResponse.subUnitCount);

		Assert.assertTrue(isImplementationCorrect,
				"GetEmpSubunit must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200.");
		Assert.assertTrue(TestCodeValidator.GetEmpSubunit(customResponse),
				"Subunit data must contain non-null 'id', 'name', and 'count' fields.");

	}

	@Test(priority = 6, groups = {
			"PL1" }, description = "Precondition: Retrieve the pay grade ID using getPayGradeid()\n"
					+ "1. Construct the endpoint '/web/index.php/api/v2/admin/pay-grades/{id}' dynamically using the retrieved ID\n"
					+ "2. Create a JSON request body with name as 'abcde'\n"
					+ "3. Send a PUT request with the constructed endpoint, valid cookie, and request body\n"
					+ "4. Print the response status code and body for verification\n"
					+ "5. Assert that the response status code is 200 (OK)")
	public void PutEmpName() throws Exception {
		int id = getPayGradeid();

		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PutEmpName");

		String requestBody = "{" + "\"name\": \"" + TestData.get("name") + "\"" + "}";
		String endpoint = "/web/index.php/api/v2/admin/pay-grades/" + id;
		String name = TestData.get("name");
		System.out.println(requestBody);
		System.out.println(endpoint);

		CustomResponse customResponse = apiUtil.PutEmpName(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutEmpName",
				List.of("given", "cookie", "put", "response"));

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Status Line: " + customResponse.getStatusLine());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		Assert.assertNotNull(customResponse.empIdList);
		Assert.assertNotNull(customResponse.empNameList);
		Assert.assertNotNull(customResponse.currencyList);

		Assert.assertTrue(isImplementationCorrect,
				"PutEmpName must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200.");
	}

	@Test(priority = 7, groups = {
			"PL1" }, description = "1. Generate a unique employment status name using the current timestamp\n"
					+ "2. Create a JSON request body with the generated name\n"
					+ "3. Send a POST request to the '/web/index.php/api/v2/admin/employment-statuses' endpoint with a valid cookie and request body\n"
					+ "4. Print the request body, response status code, and response body for debugging\n"
					+ "5. Assert that the response status code is 200, indicating successful creation")
	public void PostEmpStatus() throws Exception {

		// Step 1: Generate unique name
		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PostEmpStatus");

		String nameFromExcel = TestData.get("name");
		String finalName = "status_" + System.currentTimeMillis();

		String requestBody = "{" + "\"name\": \"" + finalName + "\"" + "}";

		// Step 2: Call the API method
		CustomResponse customResponse = apiUtil.PostEmpStatus("/web/index.php/api/v2/admin/employment-statuses",
				cookieValue, requestBody);

		// Step 3: Validate test method implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostEmpStatus",
				List.of("given", "cookie", "post", "response"));

		// Step 4: Logging
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		// Step 5: Extract values from response body
		JsonPath jsonPath = new JsonPath(customResponse.getResponseBody());
		employeeStatus = jsonPath.getInt("data.id");
		System.out.println("The employee status id: " + employeeStatus);
		String statusName = jsonPath.getString("data.name");

		// Step 6: Assertions
		Assert.assertTrue(isImplementationCorrect,
				"PostEmpStatus must be implemented using the Rest Assured methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for successful POST.");
		Assert.assertNotNull(customResponse.statusIdList);
		Assert.assertNotNull(customResponse.statusNameList);
		Assert.assertEquals(finalName, statusName, "The status name does not match the expected!");
		Assert.assertTrue(TestCodeValidator.PutEmpName(customResponse),
				"Response must contain non-null 'id' and non-empty 'name' in 'data'.");

	}

	@Test(priority = 9, groups = {
			"PL1" }, description = "Precondition: Retrieve the employment status ID using getemploymentstatusid()\n"
					+ "1. Construct the request body with the retrieved ID in the format: {\"ids\": [id]}\n"
					+ "2. Send a DELETE request to the '/web/index.php/api/v2/admin/employment-statuses' endpoint with a valid cookie and the request body\n"
					+ "3. Print the request body, response status code, and response body for debugging\n"
					+ "4. Assert that the response status code is 200, indicating the request was successful")
	public void DeleteEmp() throws IOException {
		// Use previously stored ID from PostEmpStatus
		CreateEmp();
		int employeeStatus = getemploymentstatusid();
		String requestBody = "{\"ids\": [" + employeeStatus + "]}";

		// Call updated API method
		CustomResponse customResponse = apiUtil.DeleteEmp("/web/index.php/api/v2/admin/employment-statuses",
				cookieValue, requestBody);

		// Validate implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "DeleteEmp",
				List.of("given", "cookie", "delete", "response"));

		// Logging
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		// Assertions
		Assert.assertTrue(isImplementationCorrect,
				"DeleteEmp must be implemented using the Rest Assured methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
	}

	@Test(priority = 8, dependsOnMethods = "PostEmpStatus", groups = {
			"PL1" }, description = "Precondition: Retrieve the employment status ID using getemploymentstatusid()\n"
					+ "1. Construct the endpoint '/web/index.php/api/v2/admin/employment-statuses/{id}' using the retrieved ID\n"
					+ "2. Create a JSON request body to update the employment status name to 'abcdef'\n"
					+ "3. Send a PUT request to the constructed endpoint with a valid cookie and the request body\n"
					+ "4. Print the request body, response status code, and response body for debugging\n"
					+ "5. Assert that the response status code is 200, indicating successful update")
	public void PutEmpStatus() throws Exception {
		// Use employeeStatus from earlier POST test
		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PutEmpStatus");

		String finalName = TestData.get("name") + UUID.randomUUID().toString().substring(0, 7);
		String requestBody = "{" + "\"name\": \"" + finalName + "\"" + "}";

		System.out.println(employeeStatus);
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses/" + employeeStatus + "";
		System.out.println("Request body: " + requestBody);
		System.out.println("This is the endpoint: " + endpoint);
		CustomResponse customResponse = apiUtil.PutEmpStatus(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutEmpStatus",
				List.of("given", "cookie", "put", "response"));

		// Logging
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		// Assertions
		Assert.assertNotNull(customResponse.statusIdList);
		Assert.assertNotNull(customResponse.statusNameList);
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
		Assert.assertTrue(isImplementationCorrect,
				"PutEmpStatus must be implemented using the Rest Assured methods only!");
	}

	@Test(priority = 10, groups = {
			"PL1" }, description = "1. Generate a unique job category name using a random UUID substring\n"
					+ "2. Create a JSON request body with the generated name\n"
					+ "3. Send a POST request to the '/web/index.php/api/v2/admin/job-categories' endpoint with a valid cookie and the request body\n"
					+ "4. Print the request body, response status code, and response body for debugging\n"
					+ "5. Assert that the response status code is 200, indicating successful creation of the job category")
	public void PostEmpName() throws Exception {
		// Step 1: Generate unique name
		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "postEmpStatus");

		String nameFromExcel = TestData.get("name");
		String finalName = "name_" + UUID.randomUUID().toString().substring(0, 5);

		String requestBody = "{" + "\"name\": \"" + finalName + "\"" + "}";

		// Step 2: Call updated API method
		CustomResponse customResponse = apiUtil.PostEmpName("/web/index.php/api/v2/admin/job-categories", cookieValue,
				requestBody);

		// Step 3: Validate structure of method
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostEmpName",
				List.of("given", "cookie", "post", "response"));

		// Step 4: Print debug info
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		// Step 5: Assert response
		Assert.assertTrue(isImplementationCorrect,
				"PostEmpName must be implemented using the Rest Assured methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");

		// Optional: Assert returned name
		JsonPath jsonPath = new JsonPath(customResponse.getResponseBody());
		String returnedName = jsonPath.getString("data.name");
		Assert.assertNotNull(customResponse.statusIdList);
		Assert.assertNotNull(customResponse.statusNameList);
		Assert.assertEquals(returnedName, finalName, "Returned job category name does not match the request.");
		Assert.assertTrue(TestCodeValidator.PostEmpName(customResponse),
				"Response must include non-null 'id' and non-empty 'name' in 'data'.");

	}

	/*------------Helper Methods------------*/

	public String generateRandomString(int length) {

		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom RANDOM = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = RANDOM.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}

		return sb.toString();
	}

	public void CreateEmp() {
		String name = generateRandomString(8);

		// Create JSON body as a string
		String requestBody = "{ \"name\": \"" + name + "\" }";

		// Send POST request with body and cookie
		Response response = RestAssured.given().cookie("orangehrm", cookieValue).contentType(ContentType.JSON)
				.body(requestBody)
				.post("https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/employment-statuses");

		// Log response
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody().asString());
	}

	public int getPayGradeid() {
		String endpoint = "/web/index.php/api/v2/admin/pay-grades?limit=50&offset=0";
		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}
	}

	public int getemploymentstatusid() {
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=50&offset=0";

		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}

	}

//	public int getFirstEmploymentStatus() {
//		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=50&offset=0";
//
//		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);
//
//		if (response.statusCode() == 200) {
//			int firstId = response.jsonPath().getInt("data[0].id");
//			System.out.println("First Job Title ID: " + firstId);
//			return firstId;
//		} else {
//			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
//			return -1;
//		}
//
//	}
//
//	public int getFirstJobTitleId() {
//		String endpoint = "/web/index.php/api/v2/admin/job-titles?limit=50&offset=0&sortField=jt.jobTitleName&sortOrder=ASC";
//		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);
//
//		if (response.statusCode() == 200) {
//			int firstId = response.jsonPath().getInt("data[0].id");
//			System.out.println("First Job Title ID: " + firstId);
//			return firstId;
//		} else {
//			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
//			return -1;
//		}
//
//	}
}
