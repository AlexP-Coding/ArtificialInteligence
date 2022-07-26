package woo;

import java.io.*;
import woo.exceptions.*;
import java.lang.*;
import java.lang.Math;
import java.util.regex.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Class Store implements a store.
 */
public class Store implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202009192006L;

	/** Store name .*/
	private String _name;
	
	/** Store clients. */
	private Map<String, Client> _clients = new TreeMap<String, Client>(String.CASE_INSENSITIVE_ORDER);

	/** Store products. */
	private Map<String, Product> _products = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
	
	/** Product suppliers. */
	private Map<String, Supplier> _suppliers = new TreeMap<String, Supplier>(String.CASE_INSENSITIVE_ORDER);
 
	// ! NEW 07-12
	private Map<Integer, Transaction> _transactions = new TreeMap<Integer, Transaction>();

	/** Current date. */
	private int _date;

	private int _nrTransactions;

	/**
	 * Constructor with a given name.
	 *
	 * @param name, store name
	 */
	public Store(String name){
		_name = name;
		_date = 0;
		_nrTransactions = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @return nothing
	 */
	public Store() {
		_date = 0;
		_nrTransactions = 0;
	}


	public enum ServiceType {  
		NORMAL, AIR, EXPRESS, PERSONAL
	}

	public enum ServiceLevel {
		B4, C4, C5, DL
	}

	public String showGlobalBalance() {
		double accounting = 0;
		double available = 0;

		for (int i = 0; i < _transactions.size(); i++) {
			Transaction t = _transactions.get(i);

			if (t.getDatePayed() >= 0) {
				available += t.getPriceToPay();
			}

			accounting += t.getPriceToPay();
		}

		int accountingBalance = (int) Math.round(accounting);
		int availableBalance = (int) Math.round(available);

		return "" + availableBalance + '\n' + accountingBalance;

	}


	/**
	 * Returns the current date.
	 *
	 * @return current date
	 */
	public int getCurrentDate(){
		return _date;
	}

	/**
	 * Advances the current date.
	 *
	 * @param date , number of days to advance
	 * @throws InvalidDateNumberException, if date is not a positive integer
	 */
	public void advanceDate(int date) throws InvalidDateNumberException {
		
		if (date <= 0) {
			throw new InvalidDateNumberException(date);
		}
		_date += date;
	}

 
	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException upon read failure
	 * @throws BadEntryException on invalid entry
	 */
	public void importFile(String txtfile) throws IOException, BadEntryException {
	 
		BufferedReader buffer = new BufferedReader(new FileReader(txtfile));
		String line;
		
		while ((line = buffer.readLine()) != null) {
			registerLine(line);
		}

		buffer.close();
	}



    /**
	 * Acts upon type of entity to be created and registers them according to the fields
	 * indicated.
	 *
	 * @param fields , list of Strings with entity tag and corresponding attributes
	 * @throws BadEntryException, if the there is an invalid entry field
	 * @throws IOException if there is an error spliting the fields
	 */
	private void registerLine(String line) throws IOException, BadEntryException {
		
		try {
			String[] fields = line.split("\\|");  

			Pattern patSupplier = Pattern.compile("^(SUPPLIER)");
			Pattern patClient = Pattern.compile("^(CLIENT)");
			Pattern patBox = Pattern.compile("^(BOX)");
			Pattern patContainer = Pattern.compile("^(CONTAINER)");
			Pattern patBook = Pattern.compile("^(BOOK)");

			if (patSupplier.matcher(fields[0]).matches()) {
				registerSupplier(fields); 
			}
			else if (patClient.matcher(fields[0]).matches()) {
				registerClient(fields);
			}
			else if (patBox.matcher(fields[0]).matches()) {
				registerBox(fields);
			}
			else if (patContainer.matcher(fields[0]).matches()) {
				registerContainer(fields);
			}
			else if (patBook.matcher(fields[0]).matches()) {
				registerBook(fields);
			}
			else {
				throw new BadEntryException(fields[0]);
			}
		}
		catch(DuplicateSupplierException | DuplicateClientException | DuplicateProductException e) {
			throw new BadEntryException(line, e);
		}
		catch(UnknownSupplierException | InvalidServiceTypeException | InvalidServiceLevelException e) {
			throw new BadEntryException(line, e);
		}
	}



	/**
	 * Toggles a supplier's transactions
	 * 
	 * @param id , the supplier's id
	 * @return toogle status of supplier
	 * @throws UnknownSupplierException, if the supplier key does not exist
	 */
	public boolean toggleTransactions(String id) throws UnknownSupplierException {

		Supplier s = _suppliers.get(id);

		if (s == null) {
			throw new UnknownSupplierException(id);
		}

		s.switchToggle();
		return s.getToggleStatus();
	}

	/**
	 * Return all suppliers.
	 * 
	 * @return String listing all suppliers
	 */
	public String showSuppliers(){
		String s = "";
		for(Map.Entry<String, Supplier> e : _suppliers.entrySet()){
			Supplier sup = e.getValue();
			s = s + sup.toString() + "\n";
		}
		return s;
	}
	
	/** Return all supplier transactions.
	 * 
	 * @param supplierId
	 * @return String listing all transactions from a supplier
	 * @throws UnknownSupplierException
	 */
	public String showSupplierTransactions(String supplierId) throws UnknownSupplierException {
		Supplier s = _suppliers.get(supplierId);

		if (s == null) {
			throw new UnknownSupplierException(supplierId);
		}

		ArrayList<OrderTransaction> orders = s.getOrders();
		String _allOrders = "";
		
		for (int i = 0; i < orders.size(); i++) {
			_allOrders += orders.get(i).toString();
		}

		return _allOrders;
	}
 
	/**
	 * Registers a supplier.
	 *
	 * @param id , the client's id
	 * @param name , the client's name
	 * @param address , the client's address
	 * @throws DuplicateSupplierException, if the supplier key is a duplicate
	 */
	public void registerSupplier(String id, String name, String address) throws DuplicateSupplierException {

		if (_suppliers.containsKey(id)) {
			throw new DuplicateSupplierException(id);
		}

		Supplier s = new Supplier(id, name, address);
		_suppliers.put(id.toUpperCase(), s);

	}

	/**
	 * Registers a supplier according to fields from file text line
	 *
	 * @param fields , list of Strings with supplier tag and corresponding attributes
	 * @throws BadEntryException, if the supplier's fields have an invalid entry
	 * @throws DuplicateSupplierException, if the supplier key is a duplicate
	 */
	private void registerSupplier(String[] fields) throws BadEntryException, DuplicateSupplierException {

		if (fields[0].equals("SUPPLIER")) {
			String id = fields[1];
			String name = fields[2];
			String address = fields[3]; 

			registerSupplier(id, name, address);
		}

		else {
			throw new BadEntryException(fields[0]);
		}

	}


	/**
	 * Return all clients.
	 *
	 * @return String listing all clients
	 */
	public String showAllClients(){
		String s = "";
		for(Map.Entry<String, Client> e : _clients.entrySet()) {
			Client c = e.getValue();
			s = s + c.toString() + "\n";
		}
		return s;
	}


	/**
	 * Return client's info from given id.
	 *
	 * @param id , the client's id
	 * @throws UnknownClientException if id does not belong to a client
	 * @return String listing client and their notifications
	 */
	public String showClient(String id) throws UnknownClientException {

		for (Map.Entry<String, Client> e : _clients.entrySet()) {
			Client c = e.getValue();
			if (c.getId().compareToIgnoreCase(id) == 0) {
				String s = "";
				s += c.toString() + "\n";
				for(int i = 0; i < c.getNotifs().size(); i++) {
					s += c.getNotifs().get(i).getType() + "|" + 
					c.getNotifs().get(i).getProductId() + "|" + 
					_products.get(c.getNotifs().get(i).getProductId()).getPrice() +
					"\n";
				}
				c.removeNotifs();
				return s;
			}
		}
		throw new UnknownClientException(id);
	}


	/**
	 * Registers a client.
	 *
	 * @param id , the client's id
	 * @param name , the client's name
	 * @param address , the client's address
	 * @throws DuplicateClientException if id already belongs to another client
	 */
	public void registerClient(String id, String name, String address) throws DuplicateClientException {
		
		if (_clients.containsKey(id)) {
			throw new DuplicateClientException(id);
		}

		Client c = new Client(id, name, address);
		_clients.put(id.toUpperCase(), c);
		for(Map.Entry<String, Product> e : _products.entrySet()){
			Product p = e.getValue();
			p.addObserver(c);
		}
	}


	/**
	 * Registers a client according to fields from file text line
	 *
	 * @param fields , list of Strings with client tag and corresponding attributes
	 * @throws BadEntryException, if the tag is invalid
	 * @throws DuplicateClientException, if the client key is a duplicate
	 */
	private void registerClient(String[] fields) throws BadEntryException, DuplicateClientException {

		if (fields[0].equals("CLIENT")) {
			String id = fields[1];
			String name = fields[2];
			String address = fields[3]; 

			registerClient(id, name, address);
		}

		else {
			throw new BadEntryException(fields[0]);
		}

	}


	/**
	 * Return all products.
	 *
	 * @return String listing all products
	 */
	public String showAllProducts(){
		String s = "";
		for(Map.Entry<String, Product> e : _products.entrySet()){
			Product p = e.getValue();
			s = s + p.toString() + "\n";
		}
		return s;
	}

    /**
	 * Lookup all products under a given price.
	 * 
	 * @param price
	 * @return String of products
	 */
	public String lookupProductsUnderTopPrice(int price) {
		String list = "";
		for (Map.Entry<String, Product> e : _products.entrySet()) {
			Product p = e.getValue();

			if (p.getPrice() < price) {
				list = list + p.toString() + "\n";
			}
		}
		return list;
	}
	
	/**
	 * Change the price on a product.
	 * 
	 * @param productId
	 * @param newPrice
	 * @throws UnknownProductException
	 */
	public void changePrice(String productId, int newPrice) throws UnknownProductException {
		Product p = _products.get(productId);

		if (p == null) {
			throw new UnknownProductException(productId);
		}
		else if (newPrice > 0) {
			p.setPrice(newPrice);
		}
	}

	/**
	 * Toggles a client's notifications on a certain product.
	 * 
	 * @return boolean on whether the notifications are active or not
	 */
	public boolean toggleProductNotifications(String idClient, String idProduct) throws UnknownClientException {
		for (Map.Entry<String, Client> e : _clients.entrySet()) {
			Client c = e.getValue();
			if (c.getId().compareToIgnoreCase(idClient) == 0) {
				boolean toggle;
				Product p = _products.get(idProduct);
				if(p.getClientList().contains(c)){
					p.removeObserver(c);
					toggle = false;
				}
				else{
					p.addObserver(c);
					toggle = true;
				}
				return toggle;
			}
		}
		throw new UnknownClientException(idClient);
	}

	/**
	 * Preliminar verification of the validity of the product to be registered.
	 *
	 * @param productId , id of product to be registered
	 * @param supplierId , id of the product's supplier
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @return _isValidPrice, to know if operation is valid
	 */
	private boolean verifyProduct(String productId, String supplierId, int price) throws DuplicateProductException, UnknownSupplierException {

		boolean _isValidPrice;

		if (_products.containsKey(productId)) {
			throw new DuplicateProductException(productId);
		}

		if (! (_suppliers.containsKey(supplierId))) {
			throw new UnknownSupplierException(supplierId);
		}

		if (price > 0)
			_isValidPrice = true;
		else
			_isValidPrice = false;

		return _isValidPrice;

	}

	/**
	 * Registers a box
	 *
	 * @param productId , the id of box to be registered
	 * @param supplierId , the id of the box's supplier
	 * @param price , the box's price
	 * @param criticalValue , the box's critical level
	 * @param amount , the box's stock available at the store
	 * @param serviceType , the box's serviceType
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @throws InvalidServiceTypeException, if the service type is invalid
	 * @return _willRegister, to tell if operation was valid
	 */
	public boolean registerBox( String productId, String supplierId, int price, 
	int criticalValue, int amount, String serviceType) throws DuplicateProductException, UnknownSupplierException, InvalidServiceTypeException {

		boolean _willRegister = verifyProduct(productId, supplierId, price);

		try {
			ServiceType.valueOf(serviceType);
		}
		catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidServiceTypeException(serviceType);
		}

		if (_willRegister == true) {
			ServiceType _serviceType = ServiceType.valueOf(serviceType);
			Product p = new Box(productId, supplierId, price, criticalValue, amount, _serviceType);
			_products.put(productId.toUpperCase(), p);
			for(Map.Entry<String, Client> e : _clients.entrySet()){
				Client c = e.getValue();
				p.addObserver(c);
			}
		} 

		return _willRegister;
	}


	/**
	 * Registers a box according to fields from file text line
	 *
	 * @param fields , list of Strings with product tag and corresponding attributes
	 * @throws BadEntryException if the product tag or price is invalid
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @throws InvalidServiceTypeException, if the service type is invalid
	 */
	public void registerBox(String[] fields) throws BadEntryException, DuplicateProductException, UnknownSupplierException, InvalidServiceTypeException {

		if (fields[0].equals("BOX")) {
	
			String productId = fields[1];
			String serviceType = fields[2];
			String supplierId = fields[3];
			int price = Integer.parseInt(fields[4]);
			int criticalValue = Integer.parseInt(fields[5]);
			int amount = Integer.parseInt(fields[6]);
			if (registerBox(productId, supplierId, price, criticalValue, amount, serviceType) == false) {
				throw new BadEntryException(fields[4]);
			}
		} 

		else {
			throw new BadEntryException(fields[0]);
		}
	}


	/**
	 * Registers a container.
	 *
	 * @param productId , the id of the container to be registered
	 * @param supplierId , the id of the container's supplier
	 * @param price , the container's price
	 * @param criticalValue , the container's critical level
	 * @param amount , the container's stock available at the store
	 * @param serviceType , the container's service type
	 * @param serviceLevel, the container's service level
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @throws InvalidServiceTypeException, if the service type is invalid
	 * @throws InvalidServiceLevelException, if the service level is invalid
	 * @return _willRegister, to tell if operation was valid
	 */
	public boolean registerContainer(String productId, String supplierId, int price, int criticalValue, int amount, String serviceType, String serviceLevel) 
	throws DuplicateProductException, UnknownSupplierException, InvalidServiceTypeException, InvalidServiceLevelException{

		boolean _willRegister = verifyProduct(productId, supplierId, price);

		try {
			ServiceType.valueOf(serviceType);
		}
		catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidServiceTypeException(serviceType);
		}

		try {
			ServiceLevel.valueOf(serviceLevel);
		}
		catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidServiceLevelException(serviceLevel);
		}

		if (_willRegister == true) {
			ServiceType _serviceType = ServiceType.valueOf(serviceType);
			ServiceLevel _serviceLevel = ServiceLevel.valueOf(serviceLevel);
			Product p = new Container(productId, supplierId, price, criticalValue, amount, _serviceType, _serviceLevel);
			_products.put(productId.toUpperCase(), p);
			for(Map.Entry<String, Client> e : _clients.entrySet()){
				Client c = e.getValue();
				p.addObserver(c);
			}
		}

		return _willRegister;
	}


	/**
	 * Registers a container according to fields from file text line
	 *
	 * @param fields , list of Strings with product tag and corresponding attributes
	 * @throws BadEntryException if the product tag is invalid
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @throws InvalidServiceTypeException, if the service type is invalid
	 * @throws InvalidServiceLevelException, if the service level is invalid
	 */
	public void registerContainer(String[] fields) throws BadEntryException, DuplicateProductException, UnknownSupplierException, InvalidServiceTypeException, InvalidServiceLevelException {

		if (fields[0].equals("CONTAINER")) {
		
			String productId = fields[1];
			String serviceType = fields[2];
			String serviceLevel = fields[3];
			String supplierId = fields[4];
			int price = Integer.parseInt(fields[5]);
			int criticalValue = Integer.parseInt(fields[6]);
			int amount = Integer.parseInt(fields[7]);

			if (registerContainer(productId, supplierId, price, criticalValue, amount, serviceType, serviceLevel) == false) {
				throw new BadEntryException(fields[5]);
			}
		} 

		else {
			throw new BadEntryException(fields[0]);
		}
	}



	/**
	 * Registers a book
	 *
	 * @param productId , the book's id
	 * @param supplierId , the supplier's id
	 * @param price , the product's price
	 * @param criticalValue , the book's critical level
	 * @param amount , the book's stock available at the store
	 * @param title , the book's title
	 * @param author , the book's author
	 * @param isbn , the book's isbn identification
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 * @return _willRegister, to tell if operation was valid   
	 */
	public boolean registerBook(String productId, String supplierId, int price, int criticalValue, 
	int amount, String title, String author, String isbn) throws DuplicateProductException, UnknownSupplierException {

		boolean _willRegister = verifyProduct(productId, supplierId, price);

		if (_willRegister == true) {
			Product p = new Book(productId, supplierId, price, criticalValue, amount, title, author, isbn);
			_products.put(productId.toUpperCase(), p);
			for(Map.Entry<String, Client> e : _clients.entrySet()){
				Client c = e.getValue();
				p.addObserver(c);
			}
		}

		return _willRegister;
	}


	/**
	 * Registers a book according to fields from file text line
	 *
	 * @param fields , list of Strings with product tag and corresponding attributes
	 * @throws BadEntryException if the product tag or product fields are invalid
	 * @throws DuplicateProductException, if the product key is a duplicate
	 * @throws UnknownSupplierException, if the supplier does not exist
	 */
	public void registerBook(String[] fields) throws BadEntryException, DuplicateProductException, UnknownSupplierException {

		if (fields[0].equals("BOOK")) {
		
			String productId = fields[1];
			String title = fields[2];
			String author = fields[3];
			String isbn = fields[4];
			String supplierId = fields[5];
			int price = Integer.parseInt(fields[6]);
			int criticalValue = Integer.parseInt(fields[7]);
			int amount = Integer.parseInt(fields[8]);

			if (registerBook(productId, supplierId, price, criticalValue, amount, title, author, isbn) == false) {
				throw new BadEntryException(fields[6]);
			}
		
		} 

		else {
			throw new BadEntryException(fields[0]);
		}
	}
	
	/** 
	 * Register an order.
	 * 
	 * @param supplierId
	 * @param productKeys
	 * @param amounts
	 * @throws UnknownSupplierException
	 * @throws BlacklistedSupplierException
	 * @throws UnknownProductException
	 * @throws IncompatibleSupplierKeyException
	 */
	public void registerOrderTransaction(String supplierId, ArrayList <String> productKeys,
	ArrayList <Integer> amounts) throws UnknownSupplierException, BlacklistedSupplierException,
	UnknownProductException, IncompatibleSupplierKeyException {

		boolean _willRegister = true;

		Supplier supplier = _suppliers.get(supplierId);

		if (supplier == null) {
			throw new UnknownSupplierException(supplierId);
		}

		if (supplier.getToggleStatus() == false) {
			throw new BlacklistedSupplierException(supplierId);
		}

		int cost = 0;
		ArrayList<String> _orderProducts = new ArrayList<String>();
		ArrayList<Integer> _orderAmounts = new ArrayList<Integer>();

		for (int i = 0; i < productKeys.size(); i++) {
			String productId = productKeys.get(i);
			Product p = _products.get(productId);
			if (p == null)
				throw new UnknownProductException(productId);

			if (p.getSupplierId().equals(supplierId) == false)
				throw new IncompatibleSupplierKeyException(supplierId, p.getProductId());

			if (amounts.get(i) <= 0) {
				_willRegister = false;
				break;
			}

			int productIndex = _orderProducts.indexOf(productId);

			if (productIndex >= 0) {
				int newAmount = _orderAmounts.get(productIndex) + amounts.get(i);
				_orderAmounts.add(productIndex, newAmount);
			}
			else {
				_orderProducts.add(p.getProductId());
				_orderAmounts.add(amounts.get(i));
			}

			cost += p.getPrice() * amounts.get(i);
		}	

		if (_willRegister) {
			OrderTransaction order = new OrderTransaction(supplierId, _nrTransactions, cost, _date, _orderProducts, _orderAmounts);
			_transactions.put(_nrTransactions, order);
			_nrTransactions++;
			supplier.addOrder(order);

			for (int i = 0; i < _orderProducts.size(); i++) {
				_products.get(_orderProducts.get(i)).addStock(amounts.get(i));
			}
		}
	}

	/**
	 * Register a new sale in the store.
	 * 
	 * @param clientId
	 * @param dueDate
	 * @param productId
	 * @param requested
	 * @throws UnknownClientException
	 * @throws UnknownProductException
	 * @throws UnderstockedProductException
	 */
	public void registerSaleTransaction(String clientId, int dueDate, String productId, int requested) 
	throws UnknownClientException, UnknownProductException, UnderstockedProductException {
		boolean _willRegister = true;

		Client client = _clients.get(clientId);
		if (client == null) {
			throw new UnknownClientException(clientId);
		}

		Product product = _products.get(productId);
		if (product == null) {
			throw new UnknownProductException(productId);
		}

		int amountAvailable = product.getAmount();
		if (amountAvailable < requested) {
			throw new UnderstockedProductException(product.getProductId(), requested, amountAvailable);
		}

		if (requested >= 0 && dueDate >= 0) {
			product.removeStock(requested);
			SaleTransaction sale = new SaleTransaction(_nrTransactions, client, product,
			requested, dueDate, _date);
			_transactions.put(_nrTransactions, sale);
			_nrTransactions++;
			client.addSale(sale);
		}
	}
	
	/**
	 * Return a trnasaction.
	 * 
	 * @param transactionId
	 * @return String showing the transaction
	 * @throws UnknownTransactionException
	 */
	public String showTransaction(int transactionId) throws UnknownTransactionException {
		Transaction t = _transactions.get(transactionId);

		if (t == null) {
			throw new UnknownTransactionException(transactionId);
		}

		return t.showTransaction(_date);
	}

	/**
	 * Lookup the payments of a given client.
	 * 
	 * @param clientId
	 * @return String with payments of a client.
	 * @throws UnknownClientException
	 */
	public String lookupPaymentsByClient(String clientId) throws UnknownClientException {
		Client client = _clients.get(clientId);

		if (client == null) {
			throw new UnknownClientException(clientId);
		}

		String listPayments = "";
		ArrayList<SaleTransaction> sales = client.getSales();

		for (int i = 0; i < sales.size(); i++) {
			SaleTransaction sale = sales.get(i);
			if (sale.getDatePayed() >= 0) {
				listPayments += sale.toString() + '\n';
			}
		}
		return listPayments;
	}

	/**
	 * Pay a sale.
	 * 
	 * @param transactionId
	 * @throws UnknownTransactionException
	 */
	public void pay(int transactionId) throws UnknownTransactionException {
		Transaction transaction = _transactions.get(transactionId);

		if (transaction == null) {
			throw new UnknownTransactionException(transactionId);
		}

		if (transaction.getDatePayed() < 0) {
			transaction.pay(_date);
		}
	}

	/**
	 * Show all transactions of a given client.
	 * 
	 * @param clientId
	 * @return String with client's transactions
	 * @throws UnknownClientException
	 */
	public String showClientTransactions(String clientId) throws UnknownClientException {
		Client client = _clients.get(clientId);

		if (client == null) {
			throw new UnknownClientException(clientId);
		}

		ArrayList<SaleTransaction> sales = client.getSales();

		String clientTransactions = "";

		for (int i = 0; i < sales.size(); i++) {
			clientTransactions += sales.get(i).toString() + '\n';
		}

		return clientTransactions;
	}
}
