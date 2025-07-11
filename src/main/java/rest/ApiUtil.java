package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the password from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>password</code>.
	 *
	 * @return the password as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	/**
	 * Sends a GET request to the specified login endpoint with a cookie and
	 * optional request body.
	 *
	 * <p>
	 * This method constructs and sends a GET request using RestAssured. It includes
	 * a cookie named <code>orangehrm</code> with the provided value and sets the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a
	 * request body map is provided, it is included in the request before the
	 * response is captured.
	 *
	 * @param endpoint    the API endpoint to send the GET request to (relative to
	 *                    the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         and status line
	 */
	public CustomResponse GetLogin(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		Response response = request.get(BASE_URL + endpoint); // ✅ Send the request
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();
		if (body != null) {
			request.body(body);
		}
		return new CustomResponse(response, statusCode, statusLine); // ✅ Fixed instantiation
	}

	/**
	 * Sends a GET request to the specified employee action summary endpoint with a
	 * cookie and optional request body.
	 *
	 * <p>
	 * This method constructs and sends a GET request using RestAssured. It sets the
	 * <code>Content-Type</code> header to <code>application/json</code> and
	 * includes a cookie named <code>orangehrm</code> with the given value. If a
	 * request body map is provided, it is added to the request before execution.
	 *
	 * @param endpoint    the API endpoint to retrieve employee action summary data
	 *                    (relative to the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         and status line
	 */
	public CustomResponse GetEmpActionSummary(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		Response response = request.get(BASE_URL + endpoint);
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		if (body != null) {
			request.body(body);
		}

		return new CustomResponse(response, statusCode, statusLine);
	}

	/**
	 * Sends a GET request to the specified dashboard shortcut endpoint with a
	 * cookie and optional request body, and extracts specific permission flags from
	 * the JSON response.
	 *
	 * <p>
	 * This method uses RestAssured to send a GET request to the given endpoint,
	 * setting the <code>Content-Type</code> header to <code>application/json</code>
	 * and including the <code>orangehrm</code> cookie. It parses the response JSON
	 * to extract permission-related boolean values such as leave and time
	 * management shortcuts.
	 *
	 * @param endpoint    the API endpoint to retrieve dashboard shortcut
	 *                    permissions (relative to the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, and extracted permission flags from the response body
	 */
	public CustomResponse GetDashboardShortcut(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		Response response = request.get(BASE_URL + endpoint);
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();
		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Boolean leaveAssignLeave = (Boolean) data.get("leave.assign_leave");
		Boolean leavLeaveList = (Boolean) data.get("leave.leave_list");
		Boolean leaveApplyLeave = (Boolean) data.get("leave.apply_leave");
		Boolean leaveMyLeave = (Boolean) data.get("leave.my_leave");
		Boolean timeEmployeeTimesheet = (Boolean) data.get("time.employee_timesheet");
		Boolean timeMyTimesheet = (Boolean) data.get("time.my_timesheet");

		return new CustomResponse(response, statusCode, statusLine, leaveAssignLeave, leavLeaveList, leaveApplyLeave,
				leaveMyLeave, timeEmployeeTimesheet, timeMyTimesheet);
	}

	/**
	 * Sends a GET request to the specified employee leave information endpoint with
	 * a cookie and optional request body.
	 *
	 * <p>
	 * This method uses RestAssured to construct and send a GET request to the given
	 * endpoint. It sets the <code>Content-Type</code> header to
	 * <code>application/json</code> and includes a cookie named
	 * <code>orangehrm</code> with the provided value. If a request body map is
	 * provided, it is attached to the request before sending.
	 *
	 * @param endpoint    the API endpoint to retrieve employee leave information
	 *                    (relative to the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         and status line
	 */
	public CustomResponse GetEmpLeaveInfo(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		Response response = request.get(BASE_URL + endpoint);
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		if (body != null) {
			request.body(body);
		}
		return new CustomResponse(response, statusCode, statusLine);
	}

	/**
	 * Sends a GET request to the specified employee subunit endpoint with a cookie
	 * and optional request body, and extracts subunit details from the response.
	 *
	 * <p>
	 * This method uses RestAssured to send a GET request to the provided endpoint.
	 * It sets the <code>Content-Type</code> header to <code>application/json</code>
	 * and includes a cookie named <code>orangehrm</code> with the given value.
	 * After receiving the response, it parses the JSON to extract the first
	 * subunit's ID, name, and associated employee count.
	 *
	 * @param endpoint    the API endpoint to retrieve employee subunit information
	 *                    (relative to the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, subunit ID, subunit name, and subunit employee count
	 */
	public CustomResponse GetEmpSubunit(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		Response response = request.get(BASE_URL + endpoint);
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();
		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> dataList = jsonPath.getList("data");
		Map<String, Object> firstItem = dataList.get(0);
		Map<String, Object> subunit = (Map<String, Object>) firstItem.get("subunit");

		int subUnitId = (int) subunit.get("id");
		String subUnitName = (String) subunit.get("name");
		int subUnitCount = (int) firstItem.get("count");

		if (body != null) {
			request.body(body);
		}
		return new CustomResponse(response, statusCode, statusLine, subUnitId, subUnitName, subUnitCount);
	}

	/**
	 * Sends a PUT request to update employee name information and extracts response
	 * data including ID, name, and associated currency details.
	 *
	 * <p>
	 * This method uses RestAssured to construct and send a PUT request to the
	 * specified endpoint. It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and sends the provided request body as JSON. After
	 * receiving the response, it extracts the employee ID, name, and a list of
	 * associated currency details from the response body.
	 *
	 * @param endpoint    the API endpoint to send the PUT request to (relative to
	 *                    the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        the request body containing employee name update data
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, lists of employee IDs, names, and currency details
	 */
	public CustomResponse PutEmpName(String endpoint, String cookieValue, Object body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(body);
		Response response = request.put(BASE_URL + endpoint);

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		// Store ID and name in lists (each with a single value)
		List<Integer> idList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();

		idList.add((Integer) data.get("id"));
		nameList.add((String) data.get("name"));

		// Extract currencies
		List<Map<String, Object>> currencies = (List<Map<String, Object>>) data.get("currencies");
		List<String> currencyDetails = new ArrayList<>();

		for (Map<String, Object> currency : currencies) {
			String currencyName = (String) currency.get("name");
			String currencyId = (String) currency.get("id");
			currencyDetails.add("Currency Name: " + currencyName + ", Currency ID: " + currencyId);
		}
		return new CustomResponse(response, statusCode, statusLine, idList, nameList, currencyDetails);
	}

	/**
	 * Sends a POST request to create a new employee status and extracts the
	 * resulting ID and name from the response.
	 *
	 * <p>
	 * This method uses RestAssured to send a POST request to the specified
	 * endpoint. It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and sends the given request body. After receiving the
	 * response, it parses the JSON to extract the employee status ID and name,
	 * storing them in separate lists.
	 *
	 * @param endpoint    the API endpoint to send the POST request to (relative to
	 *                    the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        the JSON-formatted request body containing the employee
	 *                    status details
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, and lists with the created employee status ID and name
	 */
	public CustomResponse PostEmpStatus(String endpoint, String cookieValue, String body) {
		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(body).post(BASE_URL + endpoint);

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		// Store ID and name in lists (each with a single value)
		List<Integer> idList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();

		idList.add((Integer) data.get("id"));
		nameList.add((String) data.get("name"));

		return new CustomResponse(response, statusCode, statusLine, idList, nameList);
	}

	/**
	 * Sends a PUT request to update an existing employee status and extracts the
	 * updated ID and name from the response.
	 *
	 * <p>
	 * This method uses RestAssured to send a PUT request to the specified endpoint.
	 * It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and sends the provided request body. After receiving
	 * the response, it extracts the updated employee status ID and name from the
	 * response data and stores them in lists. The method also logs the request body
	 * and raw response to the console for debugging purposes.
	 *
	 * @param endpoint    the API endpoint to send the PUT request to (relative to
	 *                    the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param requestBody the request body containing employee status update details
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, and lists with the updated employee status ID and name
	 */
	public CustomResponse PutEmpStatus(String endpoint, String cookieValue, Object requestBody) {
		System.out.println("Requestbody in apiutil: " + requestBody);

		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(requestBody).put(BASE_URL + endpoint);

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();
		System.out.println("Raw Response:\n" + response.getBody().asString());

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		// Store ID and name in lists (each with a single value)
		List<Integer> idList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();

		idList.add((Integer) data.get("id"));
		nameList.add((String) data.get("name"));

		return new CustomResponse(response, statusCode, statusLine, idList, nameList);
	}

	/**
	 * Sends a DELETE request to remove an employee record using the specified
	 * endpoint, cookie, and request body.
	 *
	 * <p>
	 * This method uses RestAssured to construct and send a DELETE request to the
	 * given endpoint. It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and sends the provided request body. The response's
	 * status code and status line are captured and returned.
	 *
	 * @param endpoint    the API endpoint to send the DELETE request to (relative
	 *                    to the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        the JSON-formatted request body specifying the employee to
	 *                    be deleted
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         and status line
	 */
	public CustomResponse DeleteEmp(String endpoint, String cookieValue, String body) {
		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(body).delete(BASE_URL + endpoint);

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		return new CustomResponse(response, statusCode, statusLine);
	}

	/**
	 * Sends a POST request to create a new employee name entry and extracts the
	 * resulting ID and name from the response.
	 *
	 * <p>
	 * This method uses RestAssured to send a POST request to the specified
	 * endpoint. It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and sends the given request body. After receiving the
	 * response, it parses the JSON to extract the newly created employee ID and
	 * name, storing them in separate lists.
	 *
	 * @param endpoint    the API endpoint to send the POST request to (relative to
	 *                    the base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        the JSON-formatted request body containing the employee
	 *                    name details
	 * @return a {@link CustomResponse} object containing the response, status code,
	 *         status line, and lists with the created employee ID and name
	 */
	public CustomResponse PostEmpName(String endpoint, String cookieValue, String body) {
		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(body).post(BASE_URL + endpoint);

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		// Store ID and name in lists (each with a single value)
		List<Integer> idList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();

		idList.add((Integer) data.get("id"));
		nameList.add((String) data.get("name"));

		return new CustomResponse(response, statusCode, statusLine, idList, nameList);
	}

}
