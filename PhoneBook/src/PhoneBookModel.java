import java.util.Hashtable;

/*
 * Model data for the phone book application.  
 */

public abstract class PhoneBookModel {

	protected PhoneBookView phonebookview;

	// The following are various states captured by the model
	public static String ADD_NAME_STATE = "ADD_NAME";
	public static String ADD_NUMBER_STATE = "ADD_NUMBER";
	public static String SEARCH_STATE = "SEARCH";
	public static String IDLE_STATE = "IDLE";
	public static String SEARCH_RESULT_STATE = "SEARCH_RESULT";
	public static String ERROR_STATE = "ERROR";
	public static String EXIT_STATE = "EXIT";
	public static String UPDATE_STATE = "UPDATE";
    public static String UPDATE_NUMBER_STATE = "UPDATE_NUMBER";
	public static String CANT_MODIFY_STATE = "CANT_MODIFY";
	public static String DELETE_STATE = "DELETE";

	// Private fields used to track various model data
	protected String state = IDLE_STATE;
	protected String searchResult = null;
	protected Hashtable<String, String> phoneBook = null;

	/**
	 * set the state
	 * @param aState
	 */
	public void setState(String aState) throws Exception{
		state = aState;
		phonebookview.stateHasChanged(this, state);
	}
	
	/**
	 * add a phone entry
	 * @param name
	 * @param number
	 */
	public void addAnEntry(String name, String number) {
		phoneBook.put(name, number);
	}

	/**
	 * update a phone number
	 * @param name
	 * @param number
	 */
	public void updatePhoneNumber(String name, String number) { phoneBook.put(name, number); }

	/**
	 * delete a phone number
	 * @param name
	 */
	public void deletePhoneNumber(String name) { phoneBook.remove(name); }

	/**
	 * search the phone number and set the searchResult field
	 * @param name
	 */
	public void searchPhoneNumber(String name) {
		searchResult = phoneBook.get(name);
	}

	/**
	 * return the search result
	 */
	public String getSearchResult() {
		return searchResult;
	}

	/**
	 * get the state
	 */
	public String getState() {
		return state;
	}

	public void updateDB() throws Exception{}

	/**
	 * constructor
	 * @param view
	 */
	public PhoneBookModel(PhoneBookView view) {
		phonebookview = view;
		phoneBook = new Hashtable();
	}

	/**
	 * constructor
	 */
	public PhoneBookModel(){}
}
