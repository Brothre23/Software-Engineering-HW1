/*
 * Controller for the phone book application
 *   No sophisticated error handling is implemented
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhoneBookController {

	private PhoneBookModel phonebookmodel;
	private PhoneBookView phonebookview;

	// The following are some commands that initiated by
	//   user's selection
	public static String START_COMMAND = "start";
	public static String QUIT_COMMAND = "quit";
	public static String ADD_COMMAND = "add";
	public static String SEARCH_COMMAND = "search";
	public static String UPDATE_COMMAND = "update";
	public static String DELETE_COMMAND = "delete";
	
	// A private field used to track user's input of person name
	private String name;
	
	/**
	 * Called by PhoneBookView to notify user has input
	 * @param userInput
	 */
	public void userHasInput(String userInput) throws Exception{
		String currentState = phonebookmodel.getState();

		if (userInput != null && userInput.length() != 0) {
			if (currentState.equals(PhoneBookModel.IDLE_STATE)) {
			    // 根據不同的指令進入不同的state
				if (userInput.equals(ADD_COMMAND)) {
					phonebookmodel.setState(PhoneBookModel.ADD_NAME_STATE);
				}
				else if (userInput.equals(SEARCH_COMMAND)) {
					phonebookmodel.setState(PhoneBookModel.SEARCH_STATE);
				}
				else if (userInput.equals(UPDATE_COMMAND)) {
					phonebookmodel.setState(PhoneBookModel.UPDATE_STATE);
				}
				else if (userInput.equals(DELETE_COMMAND)) {
					phonebookmodel.setState(PhoneBookModel.DELETE_STATE);
				}
				else if (userInput.equals(QUIT_COMMAND)) {
					phonebookmodel.updateDB();
					phonebookmodel.setState(PhoneBookModel.EXIT_STATE);
				}				
				else {
					phonebookmodel.setState(PhoneBookModel.ERROR_STATE);
				}
			}
			// 打完名子要進入打號碼的state
			else if (currentState.equals(PhoneBookModel.ADD_NAME_STATE)) {
				name = userInput;
				phonebookmodel.setState(PhoneBookModel.ADD_NUMBER_STATE);
			}
			// 進入打號碼的state然後創new entry
			else if (currentState.equals(PhoneBookModel.ADD_NUMBER_STATE)) {
				phonebookmodel.addAnEntry(name, userInput);
				phonebookmodel.setState(PhoneBookModel.IDLE_STATE);
			}
			// 準備更新電話號碼的state
			else if (currentState.equals(PhoneBookModel.UPDATE_STATE)){
				name = userInput;
				phonebookmodel.searchPhoneNumber(userInput);
				// 找不到要更新號碼的聯絡人
				if(phonebookmodel.getSearchResult() == null) {
					phonebookmodel.setState(PhoneBookModel.CANT_MODIFY_STATE);
				}
				else {
					phonebookmodel.setState(PhoneBookModel.UPDATE_NUMBER_STATE);
				}
			}
			// 要更新號碼的聯絡人確實存在（更新號碼同加入號碼）
			else if (currentState.equals(PhoneBookModel.UPDATE_NUMBER_STATE)){
				phonebookmodel.updatePhoneNumber(name, userInput);
				phonebookmodel.setState(PhoneBookModel.IDLE_STATE);
			}
			// 刪除號碼的state
			else if (currentState.equals(PhoneBookModel.DELETE_STATE)){
				name = userInput;
				phonebookmodel.searchPhoneNumber(userInput);
				if(phonebookmodel.getSearchResult() == null) {
					phonebookmodel.setState(PhoneBookModel.CANT_MODIFY_STATE);
				}
				else {
					phonebookmodel.deletePhoneNumber(name);
					phonebookmodel.setState(PhoneBookModel.IDLE_STATE);
				}
			}
			// 搜尋號碼的state
			else if (currentState.equals(PhoneBookModel.SEARCH_STATE)){
				phonebookmodel.searchPhoneNumber(userInput);
				phonebookmodel.setState(PhoneBookModel.SEARCH_RESULT_STATE);
			}
			// 重新開始的state
			else if (currentState.equals(PhoneBookModel.SEARCH_RESULT_STATE) ||
					 currentState.equals(PhoneBookModel.CANT_MODIFY_STATE)||
					 currentState.equals(PhoneBookModel.ERROR_STATE)) {
				if (userInput.equals(START_COMMAND)) {
					phonebookmodel.setState(PhoneBookModel.IDLE_STATE);
				}
				else if (userInput.equals(QUIT_COMMAND)) {
					phonebookmodel.updateDB();
					phonebookmodel.setState(PhoneBookModel.EXIT_STATE);
				}
				else {
					phonebookmodel.setState(PhoneBookModel.ERROR_STATE);
				}
			}					
		}
		else {
			phonebookmodel.setState(PhoneBookModel.ERROR_STATE);
		}		
	}
		
	/**
	 * start the application
	 */
	public void start() throws Exception{
		phonebookmodel.setState(PhoneBookModel.IDLE_STATE);
		while( !phonebookview.finish() ){
			phonebookview.getUserInput();
		}
	}

	/**
	 * constructor
	 */
	public PhoneBookController(String type) throws IOException{
		phonebookview = new PhoneBookView(this);
		if (type.equals("SQLiteModel")){
			phonebookmodel = new SQLiteModel(phonebookview);
		}
		else if (type.equals("TXTModel")){
			phonebookmodel = new TXTModel(phonebookview);
		}
		else {
			System.out.println("error type detected!");
			System.exit(1);
		}
	}
			
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.print("Input the model you want to create (TXTModel or SQLiteModel): ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String type = in.readLine();
		PhoneBookController controller = new PhoneBookController(type);
		controller.start();
	}		
}
