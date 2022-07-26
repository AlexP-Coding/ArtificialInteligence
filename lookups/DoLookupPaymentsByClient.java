package woo.app.lookups;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.UnknownClientException;
import woo.app.exceptions.UnknownClientKeyException;

/**
 * Lookup payments by given client.
 */
public class DoLookupPaymentsByClient extends Command<Storefront> {

  private Input<String> _clientId;

  public DoLookupPaymentsByClient(Storefront storefront) {
    super(Label.PAID_BY_CLIENT, storefront);
    _clientId = _form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    _form.parse();

    try {
    	String payList = _receiver.lookupPaymentsByClient(_clientId.value());
    	_display.addLine(payList);
    	_display.display();
    }
    catch (UnknownClientException e) {
    	throw new UnknownClientKeyException(e.getKey());
    }
  }

}
