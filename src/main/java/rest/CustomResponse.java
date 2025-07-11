package rest;

import java.util.List;

import io.restassured.response.Response;

public class CustomResponse {
	private final Response response;
	private final int statusCode;
	private final String statusLine;
	private final String responseBody;
	public Boolean leaveAssignLeave;
	public Boolean leavLeaveList;
	public Boolean leaveApplyLeave;
	public Boolean leaveMyLeave;
	public Boolean timeTmployeeTimesheet;
	public Boolean timeMyTimesheet;
	public int subUnitId;
	public String subUnitName;
	public int subUnitCount;
	public List<Integer> empIdList;
	public List<String> empNameList;
	public List<String> currencyList;
	public List<Integer> statusIdList;
	public List<String> statusNameList;

	/**
	 * Constructs a {@link CustomResponse} object using the provided RestAssured
	 * response.
	 *
	 * <p>
	 * This constructor initializes the response, extracts the HTTP status code and
	 * status line, and converts the response body to a string for easy access and
	 * debugging.
	 *
	 * @param response the RestAssured {@link Response} object returned from the API
	 *                 call
	 */
	public CustomResponse(Response response) {
		this.response = response;
		this.statusCode = response.getStatusCode();
		this.statusLine = response.getStatusLine();
		this.responseBody = response.getBody().asString();
	}

	/**
	 * Constructs a {@link CustomResponse} object using the provided response,
	 * status code, and status line.
	 *
	 * <p>
	 * This constructor allows manual assignment of the HTTP status code and status
	 * line while also extracting and storing the response body as a string for
	 * easier access.
	 *
	 * @param response   the RestAssured {@link Response} object returned from the
	 *                   API call
	 * @param statusCode the HTTP status code to be assigned to this response
	 * @param statusLine the HTTP status line to be assigned to this response
	 */
	public CustomResponse(Response response, int statusCode, String statusLine) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.responseBody = response.getBody().asString();
	}

	/**
	 * Constructs a {@link CustomResponse} object with response metadata and
	 * dashboard permission flags.
	 *
	 * <p>
	 * This constructor initializes the HTTP response details and extracts
	 * permission flags related to leave and timesheet functionalities. The response
	 * body is also stored as a string for further processing or logging.
	 *
	 * @param response              the RestAssured {@link Response} object returned
	 *                              from the API call
	 * @param statusCode            the HTTP status code of the response
	 * @param statusLine            the HTTP status line of the response
	 * @param leaveAssignLeave      flag indicating access to the 'Assign Leave'
	 *                              feature
	 * @param leavLeaveList         flag indicating access to the 'Leave List'
	 *                              feature
	 * @param leaveApplyLeave       flag indicating access to the 'Apply Leave'
	 *                              feature
	 * @param leaveMyLeave          flag indicating access to the 'My Leave' feature
	 * @param timeTmployeeTimesheet flag indicating access to the 'Employee
	 *                              Timesheet' feature
	 * @param timeMyTimesheet       flag indicating access to the 'My Timesheet'
	 *                              feature
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, Boolean leaveAssignLeave,
			Boolean leavLeaveList, Boolean leaveApplyLeave, Boolean leaveMyLeave, Boolean timeTmployeeTimesheet,
			boolean timeMyTimesheet) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.responseBody = response.getBody().asString();
		this.leaveAssignLeave = leaveAssignLeave;
		this.leavLeaveList = leavLeaveList;
		this.leaveApplyLeave = leaveApplyLeave;
		this.leaveMyLeave = leaveMyLeave;
		this.timeTmployeeTimesheet = timeTmployeeTimesheet;
		this.timeMyTimesheet = timeMyTimesheet;
	}

	/**
	 * Constructs a {@link CustomResponse} object with response metadata and
	 * employee subunit details.
	 *
	 * <p>
	 * This constructor initializes the HTTP response properties and stores
	 * information about the employee's subunit, including subunit ID, name, and the
	 * number of employees in that subunit. The full response body is also stored as
	 * a string.
	 *
	 * @param response     the RestAssured {@link Response} object returned from the
	 *                     API call
	 * @param statusCode   the HTTP status code of the response
	 * @param statusLine   the HTTP status line of the response
	 * @param subUnitId    the ID of the employee's subunit
	 * @param subUnitName  the name of the employee's subunit
	 * @param subUnitCount the number of employees in the subunit
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, int subUnitId, String subUnitName,
			int subUnitCount) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.responseBody = response.getBody().asString();
		this.subUnitCount = subUnitCount;
		this.subUnitId = subUnitId;
		this.subUnitName = subUnitName;
	}

	/**
	 * Constructs a {@link CustomResponse} object with response metadata, employee
	 * details, and currency information.
	 *
	 * <p>
	 * This constructor initializes the HTTP response properties and stores lists of
	 * employee IDs, employee names, and associated currency details extracted from
	 * the response. The full response body is also stored as a string.
	 *
	 * @param response        the RestAssured {@link Response} object returned from
	 *                        the API call
	 * @param statusCode      the HTTP status code of the response
	 * @param statusLine      the HTTP status line of the response
	 * @param idList          a list containing employee IDs extracted from the
	 *                        response
	 * @param nameList        a list containing employee names extracted from the
	 *                        response
	 * @param currencyDetails a list of strings containing currency names and IDs
	 *                        associated with the employee
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, List<Integer> idList,
			List<String> nameList, List<String> currencyDetails) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.responseBody = response.getBody().asString();
		this.empIdList = idList;
		this.empNameList = nameList;
		this.currencyList = currencyDetails;
	}

	/**
	 * Constructs a {@link CustomResponse} object with response metadata and
	 * employee status details.
	 *
	 * <p>
	 * This constructor initializes the HTTP response properties and stores lists of
	 * employee status IDs and names extracted from the response. The full response
	 * body is also captured as a string.
	 *
	 * @param response   the RestAssured {@link Response} object returned from the
	 *                   API call
	 * @param statusCode the HTTP status code of the response
	 * @param statusLine the HTTP status line of the response
	 * @param idList     a list containing employee status IDs extracted from the
	 *                   response
	 * @param nameList   a list containing employee status names extracted from the
	 *                   response
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, List<Integer> idList,
			List<String> nameList) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.responseBody = response.getBody().asString();
		this.statusIdList = idList;
		this.statusNameList = nameList;
	}

	// Getter for status code
	public int getStatusCode() {
		return statusCode;
	}

	// Getter for status line
	public String getStatusLine() {
		return statusLine;
	}

	// Getter for body as raw string
	public String getResponseBody() {
		return responseBody;
	}

	// Getter for full Response object (if needed)
	public Response getResponse() {
		return response;
	}
}
