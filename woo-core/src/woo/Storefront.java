package woo;

import java.io.*;
import woo.exceptions.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Storefront: façade for the core classes.
 */
public class Storefront {

	/** Current filename. */
	private String _filename = "";

	/** The actual store. */
	private Store _store = new Store();

	private boolean _save = false;


	/**
	 * Constructor.
	 * 
	 * @return nothing
	 */
	public Storefront() {}
	
	/**
	 * Methods for save, load and import of files.
	 */

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws MissingFileAssociationException
	 */
	public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
		
		try {
			if (_save) {
				if (_filename.equals("")) {
					throw new MissingFileAssociationException();
				}
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));     
				out.writeObject(_store);
				out.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		} 
		_save = false;
	}

	/**
	 * @param filename
	 * @throws MissingFileAssociationException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
		_filename = filename;
		save();
	}

	/**
	 * @param filename
	 * @throws UnavailableFileException
	 */
	public void load(String filename) throws UnavailableFileException {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
			_store = (Store)in.readObject();
			in.close();
			_filename = filename;
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			throw new UnavailableFileException(filename);
		}

	}

	/**
	 * @param textfile
	 * @throws ImportFileException
	 */
	public void importFile(String textfile) throws ImportFileException {
		try {
			_store.importFile(textfile);
			_save = true;
		} catch (IOException | BadEntryException e) {
			throw new ImportFileException(textfile);
		}
	}

	/**
	 * Getters and setters.
	 */
	public boolean getSave(){
		return _save;
	}

	public String getFilename(){
		return _filename;
	}

	public void setFilename(String name){
		_filename = name;
	}

	/**
	 * Methods to get and advance date.
	 */
	public int getCurrentDate() {
		return _store.getCurrentDate();
	}

	public void advanceDate(int date) throws InvalidDateNumberException {
		_store.advanceDate(date);
		_save = true;
	}

	public String showGlobalBalance() {
		return _store.showGlobalBalance();
	}

	public void pay(int transactionId) throws UnknownTransactionException {
		_store.pay(transactionId);
		_save = true;
	}

	/**
	 * Products methods.
	 */
	public String showAllProducts(){
		return _store.showAllProducts();
	}

	public String lookupProductsUnderTopPrice(int price) {
		return _store.lookupProductsUnderTopPrice(price);
	}

	public String lookupPaymentsByClient(String clientId) throws UnknownClientException {
		return _store.lookupPaymentsByClient(clientId);
	}

	public void changePrice(String productId, int newPrice) throws UnknownProductException {
		_store.changePrice(productId, newPrice);
		_save = true;
	}

	public void registerProductBox(String productId, String supplierId, int price, int criticalValue,
	String serviceType) throws DuplicateProductException, UnknownSupplierException, InvalidServiceTypeException {

		_store.registerBox(productId, supplierId, price, criticalValue, 0, serviceType);
		_save = true;
	}

	public void registerProductContainer(String productId, String supplierId, int price,
	int criticalValue, String serviceType, String serviceLevel) throws DuplicateProductException,
	UnknownSupplierException, InvalidServiceTypeException, InvalidServiceLevelException {

		_store.registerContainer(productId, supplierId, price, criticalValue, 0, serviceType,
		serviceLevel);
		_save = true;
	}


	public void registerProductBook(String productId, String supplierId, int price, 
	int criticalValue, String title, String author, String isbn) 
	throws DuplicateProductException, UnknownSupplierException {
		
		_store.registerBook(productId, supplierId, price, criticalValue, 0, title, author, isbn);
		_save = true;
	}

	public boolean toggleProductNotifications(String idClient, String idProduct) throws UnknownClientException {
		boolean toggled = _store.toggleProductNotifications(idClient, idProduct);
		_save = true;
		return toggled;
	}

	/**
	 * Suppliers methods.
	 */
	public String showSuppliers(){
		return _store.showSuppliers();
	}

	public String showSupplierTransactions(String supplierId) throws UnknownSupplierException {
		return _store.showSupplierTransactions(supplierId);
	}

	public void registerSupplier(String id, String name, String address) throws
	DuplicateSupplierException {
		_store.registerSupplier(id, name, address);
		_save = true;
	}

	public boolean toggleTransactions(String id) throws UnknownSupplierException {
		boolean toggle = _store.toggleTransactions(id);
		_save = true;
		return toggle;
	}

	/**
	 * Clients methods.
	 */
	public String showAllClients(){
		return _store.showAllClients();
	}

	public String showClient(String id) throws UnknownClientException {
		return _store.showClient(id);
	}

	public String showClientTransactions(String clientId) throws UnknownClientException {
		String clientTransactions = _store.showClientTransactions(clientId);
		_save = true;
		return clientTransactions;
	}

	public void registerClient(String id, String name, String address) throws DuplicateClientException {
		_store.registerClient(id, name, address);
		_save = true;
	}


	/**
	 * Transactions methods.
	 */
	public String showTransaction(int transactionId) throws UnknownTransactionException {
		return _store.showTransaction(transactionId);
	}

	public void registerOrderTransaction(String supplierId, ArrayList <String> productKeys, ArrayList <Integer> amounts)
	throws UnknownSupplierException, BlacklistedSupplierException,
	UnknownProductException, IncompatibleSupplierKeyException	{
		_store.registerOrderTransaction(supplierId, productKeys, amounts);
		_save = true;
	}

	public void registerSaleTransaction(String clientId, int dueDate, String productId, int amount)
	throws UnknownClientException, UnknownProductException, UnderstockedProductException {
		_store.registerSaleTransaction(clientId, dueDate, productId, amount);
		_save = true;
	}


}
